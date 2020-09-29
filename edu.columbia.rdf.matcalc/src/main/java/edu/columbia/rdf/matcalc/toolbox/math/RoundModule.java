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
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class LogModule.
 */
public class RoundModule extends Module implements ModernClickListener {

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
    return "Round";
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

    // The default behaviour is to do a log2 transform.
    RibbonLargeButton button = new RibbonLargeButton("Round", AssetService.getInstance().loadIcon("xy", 24));
    button.setToolTip("Round", "Round values");
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
  public static DataFrame round(ModernWindow parent, DataFrame matrix) {
    return MatrixOperations.round(matrix);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    mWindow.history().addToHistory("Round", round(mWindow, mWindow.getCurrentMatrix()));
  }
}
