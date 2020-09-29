/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.matcalc.toolbox.stats;

import org.apache.commons.math3.distribution.FDistribution;
import org.jebtk.core.Mathematics;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class OneWayAnovaModule.
 */
public class OneWayAnovaModule extends Module implements ModernClickListener {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "One-way ANOVA";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mParent = window;

    RibbonLargeButton button = new RibbonLargeButton("One-way ANOVA",
        AssetService.getInstance().loadIcon("calculator", 24), "One-way ANOVA", "One-way analysis of variance.");
    button.addClickListener(this);

    mParent.getRibbon().getToolbar("Statistics").getSection("Statistics").add(button);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    anova();
  }

  /**
   * Anova.
   */
  private void anova() {
    DataFrame m = mParent.getCurrentMatrix();

    int rows = m.getRows();
    int cols = m.getCols();

    double y;

    // Count the total number of observations
    int numObs = 0;

    for (int c = 0; c < cols; ++c) {
      numObs += Matrix.countNumericalRows(m, c);
    }

    //
    // Treatments
    //

    int treatDegFree = cols - 1;

    double treatSS = 0;

    double treatSS1 = 0;

    for (int c = 0; c < cols; ++c) {
      int rowCount = Matrix.countNumericalRows(m, c);

      double s1 = 0;

      for (int r = 0; r < rows; ++r) {
        y = m.getValue(r, c);

        if (Mathematics.isInvalidNumber(y)) {
          break;
        }

        s1 += y;
      }

      s1 *= s1;
      s1 /= rowCount;

      treatSS1 += s1;
    }

    double treatSS2 = 0;

    for (int c = 0; c < cols; ++c) {
      for (int r = 0; r < rows; ++r) {
        y = m.getValue(r, c);

        if (Mathematics.isInvalidNumber(y)) {
          break;
        }

        treatSS2 += y;
      }
    }

    treatSS2 *= treatSS2;
    treatSS2 /= numObs;

    treatSS = treatSS1 - treatSS2;

    double treatMS = treatSS / treatDegFree;

    //
    // Error
    //

    int errDegFree = numObs - cols;

    double errSS = 0;

    double errSS1 = 0;

    for (int c = 0; c < cols; ++c) {
      for (int r = 0; r < rows; ++r) {
        y = m.getValue(r, c);

        if (Mathematics.isInvalidNumber(y)) {
          break;
        }

        errSS1 += y * y;
      }
    }

    errSS = errSS1;

    for (int c = 0; c < cols; ++c) {
      int rowCount = Matrix.countNumericalRows(m, c);

      double e = 0;

      for (int r = 0; r < rows; ++r) {
        y = m.getValue(r, c);

        if (Mathematics.isInvalidNumber(y)) {
          break;
        }

        e += y;
      }

      e *= e;

      e /= rowCount;

      errSS -= e;
    }

    double errMS = errSS / errDegFree;

    //
    // Total
    //

    int totalDegFree = numObs - 1;

    double totalSS = errSS1 - treatSS2;

    double f = treatMS / errMS;

    FDistribution dist = new FDistribution(treatDegFree, errDegFree);

    double p = 1.0 - dist.cumulativeProbability(f);

    System.err.println("treat " + treatSS + " " + treatDegFree + "  " + treatMS + " " + f + " " + p);
    System.err.println("err " + errSS + " " + errDegFree + "  " + errMS);
    System.err.println("total " + totalSS + " " + totalDegFree);

    DataFrame ret = DataFrame.createNumericalMatrix(3, 5);

    ret.setColumnName(0, "SS");
    ret.setColumnName(1, "df");
    ret.setColumnName(2, "MS");
    ret.setColumnName(3, "F");
    ret.setColumnName(4, "P>F");

    ret.getIndex().setAnnotation("Source", 0, "Columns");
    ret.getIndex().setAnnotation("Source", 1, "Error");
    ret.getIndex().setAnnotation("Source", 2, "Total");

    ret.set(0, 0, treatSS);
    ret.set(0, 1, treatDegFree);
    ret.set(0, 2, treatMS);
    ret.set(0, 3, f);
    ret.set(0, 4, p);

    ret.set(1, 0, errSS);
    ret.set(1, 1, errDegFree);
    ret.set(1, 2, errMS);

    ret.set(2, 0, totalSS);
    ret.set(2, 1, totalDegFree);

    mParent.history().addToHistory("ANOVA", ret);
  }

}
