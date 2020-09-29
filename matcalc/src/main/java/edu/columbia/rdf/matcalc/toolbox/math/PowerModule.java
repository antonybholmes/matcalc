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

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class LogModule.
 */
public class PowerModule extends Module implements ModernClickListener {

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
    return "Power";
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

    popup.addMenuItem(new ModernTwoLineMenuItem("m^x", "Compute the xth power of each cell.",
        AssetService.getInstance().loadIcon("mx", 24)));
    popup.addMenuItem(new ModernTwoLineMenuItem("x^m", "Compute x to the power of each cell.",
        AssetService.getInstance().loadIcon("xm", 24)));
    popup.addMenuItem(new ModernTwoLineMenuItem("e^m", "Compute the exponent of each cell.",
        AssetService.getInstance().loadIcon("em", 24)));

    // The default behaviour is to do a log2 transform.
    RibbonLargeDropDownButton2 button = new RibbonLargeDropDownButton2(AssetService.getInstance().loadIcon("xy", 24),
        popup);
    button.setChangeText(false);
    button.setToolTip("Power", "Power functions.");
    mWindow.getRibbon().getToolbar("Formulas").getSection("Functions").add(button);
    button.addClickListener(this);
  }

  /**
   * Power.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param base   the base
   * @return the annotation matrix
   */
  public static DataFrame power(ModernWindow parent, DataFrame matrix, int base) {
    PowerDialog dialog = new PowerDialog(parent, base);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.OK) {
      return MatrixOperations.power(matrix, dialog.getBase());
    } else {
      return null;
    }
  }

  public static DataFrame power(ModernWindow parent, int base, DataFrame matrix) {
    PowerDialog dialog = new PowerDialog(parent, base);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.OK) {
      return MatrixOperations.power(dialog.getBase(), matrix);
    } else {
      return null;
    }
  }

  public static DataFrame em(DataFrame matrix) {
    return MatrixOperations.em(matrix);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getMessage().equals("m^x")) {
      mWindow.history().addToHistory("m^x", power(mWindow, mWindow.getCurrentMatrix(), 2));
    } else if (e.getMessage().equals("x^m")) {
      mWindow.history().addToHistory("x^m", power(mWindow, 2, mWindow.getCurrentMatrix()));
    } else {
      mWindow.history().addToHistory("e^m", em(mWindow.getCurrentMatrix()));
    }
  }
}
