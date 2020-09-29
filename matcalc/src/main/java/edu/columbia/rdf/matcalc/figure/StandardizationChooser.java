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
package edu.columbia.rdf.matcalc.figure;

import javax.swing.BoxLayout;

import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;

/**
 * The class StandardizationChooser.
 */
public class StandardizationChooser extends ModernWidget {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member matrix standardize check.
   */
  private ModernTwoStateWidget mMatrixStandardizeCheck = new ModernRadioButton("Matrix");

  /**
   * The member row standardize check.
   */
  private ModernTwoStateWidget mRowStandardizeCheck = new ModernRadioButton("Row");

  /**
   * The member column standardize check.
   */
  private ModernTwoStateWidget mColumnStandardizeCheck = new ModernRadioButton("Column");

  /**
   * The member none check.
   */
  private ModernTwoStateWidget mNoneCheck = new ModernRadioButton("None");

  /**
   * Instantiates a new standardization chooser.
   */
  public StandardizationChooser() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    add(mMatrixStandardizeCheck);
    add(mRowStandardizeCheck);
    add(mColumnStandardizeCheck);
    add(mNoneCheck);
    add(ModernPanel.createVGap());

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mMatrixStandardizeCheck);
    group.add(mRowStandardizeCheck);
    group.add(mColumnStandardizeCheck);
    group.add(mNoneCheck);

    mRowStandardizeCheck.setSelected(true);
  }

  /**
   * Gets the standardization method.
   *
   * @return the standardization method
   */
  public ColorNormalizationType getStandardizationMethod() {
    if (mMatrixStandardizeCheck.isSelected()) {
      return ColorNormalizationType.ZSCORE_MATRIX;
    } else if (mColumnStandardizeCheck.isSelected()) {
      return ColorNormalizationType.ZSCORE_COLUMN;
    } else if (mRowStandardizeCheck.isSelected()) {
      return ColorNormalizationType.ZSCORE_ROW;
    } else {
      return ColorNormalizationType.NONE;
    }
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mMatrixStandardizeCheck.addClickListener(l);
    mRowStandardizeCheck.addClickListener(l);
    mColumnStandardizeCheck.addClickListener(l);
    mNoneCheck.addClickListener(l);
  }
}
