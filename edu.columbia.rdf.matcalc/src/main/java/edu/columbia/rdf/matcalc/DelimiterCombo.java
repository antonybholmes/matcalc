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

import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox2;

/**
 * Defines a set of delimiters for parsing files.
 */
public class DelimiterCombo extends ModernComboBox2 {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new delimiter combo.
   */
  public DelimiterCombo() {
    addMenuItem("Tab");
    addMenuItem("Space");
    addMenuItem(",");
    addMenuItem(";");

    setSelectedIndex(0);

    UI.setSize(this, 80);
  }

  /**
   * Gets the delimiter.
   *
   * @return the delimiter
   */
  public String getDelimiter() {
    switch (getSelectedIndex()) {
    case 1:
      return " ";
    case 2:
      return ",";
    case 3:
      return ";";
    default:
      return TextUtils.TAB_DELIMITER;
    }
  }

  /**
   * Set the default delimiter.
   *
   * @param delimiter the new delimiter
   */
  public void setDelimiter(String delimiter) {
    if (delimiter.equals(" ")) {
      setSelectedIndex(1);
    } else if (delimiter.equals(",")) {
      setSelectedIndex(2);
    } else if (delimiter.equals(";")) {
      setSelectedIndex(3);
    } else {
      setSelectedIndex(0);
    }
  }
}
