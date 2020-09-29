/**
normalize() * Copyright 2016 Antony Holmes
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

import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class ZScoreModule.
 */
public class ZScoreModule extends Module implements ModernClickListener {

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
    return "Z-score";
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

    popup.addMenuItem(
        new ModernTwoLineMenuItem("Matrix", "Z-score matrix.", AssetService.getInstance().loadIcon("z_score", 32)));
    popup.addMenuItem(
        new ModernTwoLineMenuItem("Row", "Row z-score.", AssetService.getInstance().loadIcon("z_score", 32)));

    // The default behaviour is to do a log2 transform.
    RibbonLargeDropDownButton2 button = new RibbonLargeDropDownButton2("Z-score", popup);
    button.setChangeText(false);
    button.setToolTip("Z-score", "Z-score functions.");
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
    if (e.getMessage().equals("Matrix")) {
      mWindow.history().addToHistory("z-score", "z-score", MatrixOperations.zscore(mWindow.getCurrentMatrix())); // new
                                                                                                                 // ZScoreMatrixTransform(this,
                                                                                                                 // getCurrentMatrix()));
    } else if (e.getMessage().equals("Row")) {
      mWindow.history().addToHistory("Row z-score", "Row z-score",
          MatrixOperations.rowZscore(mWindow.getCurrentMatrix())); // addFlowItem(new
                                                                   // ZScoreRowsMatrixTransform(this,
                                                                   // getCurrentMatrix()));
    } else {
      // do nothing
    }
  }
}
