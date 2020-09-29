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
package edu.columbia.rdf.matcalc.toolbox.core.collapse;

import java.text.ParseException;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Collapse a file based on a repeated column. The selected column will be
 * reduced to a list of unique values whilst all other columns will be turned
 * into semi-colon separated lists of values.
 *
 * @author Antony Holmes
 *
 */
public class CollapseModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mCollapseButton = new RibbonLargeButton(
      AssetService.getInstance().loadIcon("collapse", 24));

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Collapse";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    mCollapseButton.setToolTip("Collapse", "Collapse rows on a column.");

    window.getRibbon().getToolbar("Data").getSection("Tools").add(mCollapseButton);

    mCollapseButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    try {
      collapse();
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Match.
   *
   * @throws ParseException the parse exception
   */
  private void collapse() throws ParseException {
    DataFrame m = mWindow.getCurrentMatrix();

    CollapseDialog dialog = new CollapseDialog(mWindow, m, mWindow.getGroups());

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    collapse(m, dialog.getCollapseName(), dialog.getGroup1(), dialog.getGroup2(), dialog.getCollapseType(), mWindow);

    // mWindow.history().addToHistory("Duplicate rows", m);
    // mWindow.history().addToHistory("Collapse rows", mcollapsed);
  }

  /**
   * Collapse the rows in a matrix.
   *
   * @param m                         the m
   * @param rowAnnotationCollapseName the row annotation collapse name
   * @param group1                    the group1
   * @param group2                    the group2
   * @param type                      the type
   * @param window                    the window
   * @return the annotation matrix
   * @throws ParseException the parse exception
   */
  public static DataFrame collapse(DataFrame m, String rowAnnotationCollapseName, MatrixGroup group1,
      MatrixGroup group2, CollapseType type, MainMatCalcWindow window) throws ParseException {

    DataFrame ret = null;

    switch (type) {
    case MAX:
      ret = MatrixOperations.collapseMax(m, rowAnnotationCollapseName); // new
                                                                        // CollapseMaxMatrixView(m,
                                                                        // rowAnnotationCollapseName);
      break;
    case MIN:
      ret = MatrixOperations.collapseMin(m, rowAnnotationCollapseName); // new
                                                                        // CollapseMinMatrixView(m,
                                                                        // rowAnnotationCollapseName);
      break;
    case MAX_STDEV:
      ret = MatrixOperations.collapseMaxStdDev(m, rowAnnotationCollapseName); // new
                                                                              // CollapseMaxStdDevMatrixView(m,
                                                                              // rowAnnotationCollapseName);
      break;
    case MAX_MEAN:
      ret = MatrixOperations.addRowMeans(m);
      window.history().addToHistory("Add Mean", ret);
      ret = MatrixOperations.collapseMaxMean(m, rowAnnotationCollapseName); // new
                                                                            // CollapseMaxMeanMatrixView(m,
                                                                            // rowAnnotationCollapseName);
      break;
    case MAX_MEDIAN:
      ret = MatrixOperations.addRowMedians(m);
      window.history().addToHistory("Add Median", ret);
      ret = MatrixOperations.collapseMaxMedian(ret, rowAnnotationCollapseName); // return
                                                                                // new
                                                                                // CollapseMaxMedianMatrixView(m,
                                                                                // rowAnnotationCollapseName);
      break;
    case MAX_TSTAT:
      ret = MatrixOperations.addTStat(m, group1, group2);
      window.history().addToHistory("Add T-Stats", ret);
      ret = MatrixOperations.collapseMaxTStat(ret, rowAnnotationCollapseName); // return
                                                                               // new
                                                                               // CollapseTTestMatrixView(m,
                                                                               // rowAnnotationCollapseName,
                                                                               // group1,
                                                                               // group2);
      break;
    case MAX_IQR:
      ret = MatrixOperations.addIQR(m);
      window.history().addToHistory("Add IQR", ret);
      ret = MatrixOperations.collapseMaxIQR(ret, rowAnnotationCollapseName); // return
                                                                             // new
                                                                             // CollapseTTestMatrixView(m,
                                                                             // rowAnnotationCollapseName,
                                                                             // group1,
                                                                             // group2);
      break;
    case MAX_QUART_COEFF_DISP:
      ret = MatrixOperations.addQuartCoeffDisp(m);
      window.history().addToHistory("Add QuartCoeffDisp", ret);
      ret = MatrixOperations.collapseMaxQuartCoeffDisp(ret, rowAnnotationCollapseName); // return new
                                                                                        // CollapseTTestMatrixView(m,
                                                                                        // rowAnnotationCollapseName,
                                                                                        // group1, group2);
      break;
    default:
      ret = m;
      break;
    }

    window.history().addToHistory("Collapse rows", ret);

    return ret;
  }
}
