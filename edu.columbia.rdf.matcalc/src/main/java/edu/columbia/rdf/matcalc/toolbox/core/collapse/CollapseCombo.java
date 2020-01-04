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

import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;

/**
 * The class CollapseCombo.
 */
public class CollapseCombo extends ModernComboBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new collapse combo.
   */
  public CollapseCombo() {
    addMenuItem("None");
    addMenuItem("Max");
    addMenuItem("Min");
    addMenuItem("Max Std Dev");
    addMenuItem("Max Mean");
    addMenuItem("Max Median");
    addMenuItem("Max T-statistic");
    addMenuItem("Max IQR");
    addMenuItem("Max QuartCoeffDisp");

    setSelectedIndex(0);

    UI.setSize(this, ModernWidget.LARGE_SIZE);
  }
}
