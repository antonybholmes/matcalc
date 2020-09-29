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
package edu.columbia.rdf.matcalc;

import java.awt.Dimension;

import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowService;

/**
 * The class MatCalcWindowCombo.
 */
public class MatCalcWindowCombo extends ModernComboBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant SIZE.
   */
  private static final Dimension SIZE = new Dimension(400, ModernWidget.WIDGET_HEIGHT);

  /**
   * Instantiates a new mat calc window combo.
   */
  public MatCalcWindowCombo() {
    for (ModernWindow window : WindowService.getInstance()) {
      if (!(window instanceof MainMatCalcWindow)) {
        continue;
      }

      addMenuItem(window.getSubTitle());
    }

    UI.setSize(this, SIZE);
  }
}
