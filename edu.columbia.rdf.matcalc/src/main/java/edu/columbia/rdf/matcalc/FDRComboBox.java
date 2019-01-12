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

import org.jebtk.math.statistics.FDRType;
import org.jebtk.modern.combobox.ModernComboBox;

/**
 * For choosing an FDR method.
 *
 * @author Antony Holmes
 */
public class FDRComboBox extends ModernComboBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new FDR combo box.
   */
  public FDRComboBox() {
    addMenuItem("None");
    addMenuItem("Bonferroni");
    addMenuItem("Benjamini-Hochberg");
  }

  /**
   * Gets the FDR type.
   *
   * @return the FDR type
   */
  public FDRType getFDRType() {
    switch (getSelectedIndex()) {
    case 1:
      return FDRType.BONFERRONI;
    case 2:
      return FDRType.BENJAMINI_HOCHBERG;
    default:
      return FDRType.NONE;
    }
  }
}
