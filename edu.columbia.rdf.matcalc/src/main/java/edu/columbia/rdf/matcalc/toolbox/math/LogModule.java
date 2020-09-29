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
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.help.ModernMenuHelpItem;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.RibbonLargeOptionalDropDownButton2;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class LogModule.
 */
public class LogModule extends Module implements ModernClickListener {

  /** The Constant ICON. */
  private static final ModernIcon ICON = AssetService.getInstance().loadIcon("log", 24);

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
    return "Log";
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

    /*
     * RibbonLargeButton button = new RibbonLargeButton("Log 2",
     * UIService.getInstance().loadIcon("log", 32), "Log 2",
     * "Log 2 transform the expression values in the matrix.");
     * button.addClickListener(this);
     * mWindow.getRibbon().getToolbar("Transform").getSection("Log").add(button) ;
     * 
     * button = new RibbonLargeButton("Log 10",
     * UIService.getInstance().loadIcon("log", 32), "Log 10",
     * "Log 10 transform the expression values in the matrix.");
     * button.addClickListener(this);
     * mWindow.getRibbon().getToolbar("Transform").getSection("Log").add(button) ;
     * 
     * button = new RibbonLargeButton("Ln", UIService.getInstance().loadIcon("log",
     * 32), "Ln",
     * "Log e (natural log) transform the expression values in the matrix.");
     * button.addClickListener(this);
     * mWindow.getRibbon().getToolbar("Transform").getSection("Log").add(button) ;
     */

    ModernPopupMenu2 popup = new ModernPopupMenu2();

    popup.addMenuItem(new ModernTwoLineMenuItem("Log 2", "Log 2 tranform.", ICON));
    popup.addMenuItem(new ModernTwoLineMenuItem("Log 10", "Log 10 tranform.", ICON));
    popup.addMenuItem(new ModernTwoLineMenuItem("Ln", "Natural log tranform.", ICON));

    // popup.addMenuItem(new ModernMenuSeparator());

    popup.addMenuItem(new ModernMenuHelpItem("Help with log transforming a matrix...", "matcalc.modules.math.log.url")
        .setTextOffset(48));

    // The default behaviour is to do a log2 transform.
    RibbonLargeOptionalDropDownButton2 button = new RibbonLargeOptionalDropDownButton2("Log(m)", popup);
    button.setToolTip("Log", "Log transform a matrix.");

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
    if (e.getMessage().equals("Log 10")) {
      mWindow.history().addToHistory("log10", "log10", log10(mWindow, mWindow.getCurrentMatrix(), 1));
    } else if (e.getMessage().equals("Ln")) {
      mWindow.history().addToHistory("ln", "ln", ln(mWindow, mWindow.getCurrentMatrix(), 1));
    } else {
      mWindow.history().addToHistory("log2", "log2", log2(mWindow, mWindow.getCurrentMatrix(), 1));
    }
  }

  /**
   * Log2.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param min    the min
   * @return the annotation matrix
   */
  public static DataFrame log2(ModernWindow parent, DataFrame matrix, double min) {
    LogDialog dialog = new LogDialog(parent, min, 2, false);

    dialog.setVisible(true);

    return log(parent, matrix, dialog);
  }

  /**
   * Log10.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param min    the min
   * @return the annotation matrix
   */
  public static DataFrame log10(ModernWindow parent, DataFrame matrix, double min) {
    LogDialog dialog = new LogDialog(parent, min, 10, false);

    dialog.setVisible(true);

    return log(parent, matrix, dialog);
  }

  /**
   * Ln.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param min    the min
   * @return the annotation matrix
   */
  public static DataFrame ln(ModernWindow parent, DataFrame matrix, double min) {
    LogDialog dialog = new LogDialog(parent, min, 2, true);

    dialog.setVisible(true);

    return log(parent, matrix, dialog);
  }

  /**
   * Log.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param dialog the dialog
   * @return the annotation matrix
   */
  public static DataFrame log(ModernWindow parent, DataFrame matrix, LogDialog dialog) {

    if (dialog.getStatus() == ModernDialogStatus.OK) {
      DataFrame m;

      if (dialog.getAdd()) {
        m = MatrixOperations.add(matrix, dialog.getAddAmount());
      } else if (dialog.getMin()) {
        m = MatrixOperations.min(matrix, dialog.getMinAmount());
      } else {
        m = matrix;
      }

      if (dialog.getNatural()) {
        return MatrixOperations.ln(m);
      } else {
        return MatrixOperations.log(m, dialog.getBase());
      }
    } else {
      return null;
    }
  }
}
