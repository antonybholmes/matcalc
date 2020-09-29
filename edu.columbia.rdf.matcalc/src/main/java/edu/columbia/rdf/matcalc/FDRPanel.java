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
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;

/**
 * For choosing an FDR method.
 *
 * @author Antony Holmes
 */
public class FDRPanel extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The none button.
   */
  private ModernRadioButton noneButton = new ModernRadioButton("None");

  /**
   * The bonferroni button.
   */
  private ModernRadioButton bonferroniButton = new ModernRadioButton("Bonferroni");

  /**
   * The bh button.
   */
  private ModernRadioButton bhButton = new ModernRadioButton("Benjamini-Hochberg");

  /**
   * The member max p field.
   */
  private ModernCompactSpinner mMaxPField = new ModernCompactSpinner(0, 1, 0.05, 0.01);

  /**
   * The member check p.
   */
  private ModernTwoStateWidget mCheckP = new ModernCheckSwitch("Maximum p-value");

  /**
   * Instantiates a new FDR panel.
   */
  public FDRPanel() {
    add(noneButton);
    add(bonferroniButton);
    add(bhButton);

    add(UI.createVGap(10));

    UI.setSize(mCheckP, 150, ModernWidget.WIDGET_HEIGHT);

    add(new HExpandBox(mCheckP, mMaxPField));

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(noneButton);
    group.add(bonferroniButton);
    group.add(bhButton);

    noneButton.setSelected(true);
  }

  /**
   * Gets the FDR type.
   *
   * @return the FDR type
   */
  public FDRType getFDRType() {
    if (bhButton.isSelected()) {
      return FDRType.BENJAMINI_HOCHBERG;
    } else if (bonferroniButton.isSelected()) {
      return FDRType.BONFERRONI;
    } else {
      return FDRType.NONE;
    }
  }

  /**
   * Gets the max p.
   *
   * @return the max p
   */
  public double getMaxP() {
    if (mCheckP.isSelected()) {
      return Double.parseDouble(mMaxPField.getText());
    } else {
      // No filtering
      return 1;
    }
  }
}
