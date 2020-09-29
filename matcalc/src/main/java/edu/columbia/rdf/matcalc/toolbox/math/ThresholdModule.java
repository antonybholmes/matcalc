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
package edu.columbia.rdf.matcalc.toolbox.math;

import java.text.ParseException;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.math.ui.matrix.MatrixTransforms;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.input.ModernInputDialog;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Row name.
 *
 * @author Antony Holmes
 */
public class ThresholdModule extends Module implements ModernClickListener {
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
    return "Shift";
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

    ModernPopupMenu2 popup = new ModernPopupMenu2();

    popup.addMenuItem(new ModernTwoLineMenuItem("Min", "Ensure each cell has a minimum value.",
        AssetService.getInstance().loadIcon("min", 32)));
    popup.addMenuItem(new ModernTwoLineMenuItem("Min/Max", "Threshold all values between min and max.",
        AssetService.getInstance().loadIcon("min_max", 32)));
    popup.addMenuItem(new ModernTwoLineMenuItem("Min Shift", "Shift all values to be >= 0.",
        AssetService.getInstance().loadIcon("min_shift", 32)));

    // The default behaviour is to do a log2 transform.
    RibbonLargeDropDownButton2 button = new RibbonLargeDropDownButton2("Threshold", popup);
    button.setChangeText(false);
    button.setToolTip("Threshold", "Threshold functions.");
    mWindow.getRibbon().getToolbar("Formulas").getSection("Functions").add(button);
    button.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    DataFrame m = mWindow.getCurrentMatrix();

    if (e.getMessage().equals("Min")) {
      mWindow.history().addToHistory("Minimum Threshold", MatrixTransforms.minThreshold(mWindow, m, 1));
    } else if (e.getMessage().equals("Min/Max")) {
      mWindow.history().addToHistory("Minimum Threshold", MatrixTransforms.minMaxThreshold(mWindow, m, 1, 10000)); // addFlowItem(new
                                                                                                                   // MinMaxMatrixTransform(this,
                                                                                                                   // getCurrentMatrix(),
                                                                                                                   // 1,
                                                                                                                   // 10000));
    } else if (e.getMessage().equals("Min Shift")) {
      minShift(); // mWindow.history().addToHistory("Min Shift",
                  // MatrixTransforms.subtract(mWindow, m, 1));
    } else if (e.getMessage().equals("Median Shift")) {
      mWindow.history().addToHistory("Median Shift", MatrixOperations.divide(m, MatrixOperations.median(m))); // .collapseMaxMedian(m,
                                                                                                              // rowAnnotation)MatrixTransforms.medianShift(mWindow,
                                                                                                              // m));
    } else {

    }
  }

  /**
   * Min shift.
   *
   * @throws ParseException the parse exception
   */
  private void minShift() {
    DataFrame m = mWindow.getCurrentMatrix();

    double min = MatrixOperations.min(m);

    System.err.println("min " + min);

    ModernInputDialog dialog = new ModernInputDialog(mWindow, "Minimum", "Minimum Expression", "1");

    dialog.setVisible(true);

    if (dialog.isCancelled()) {
      return;
    }

    double add = Double.parseDouble(dialog.getText());

    DataFrame ret = MatrixOperations.add(MatrixOperations.subtract(m, min), add);

    mWindow.history().addToHistory("Minimum Threshold", ret);
  }
}
