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

import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelPosition;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;

/**
 * The class RowLabelPositionControl.
 */
public class RowLabelPositionControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member left check.
   */
  private ModernTwoStateWidget mLeftCheck;

  /**
   * The member right check.
   */
  private ModernTwoStateWidget mRightCheck;

  /**
   * Instantiates a new row label position control.
   *
   * @param rowLabelPosition the row label position
   */
  public RowLabelPositionControl(RowLabelPosition rowLabelPosition) {
    mLeftCheck = new ModernRadioButton("Left");
    add(mLeftCheck);
    // add(ModernPanel.createVGap());
    mRightCheck = new ModernRadioButton("Right");
    add(mRightCheck);

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mLeftCheck);
    group.add(mRightCheck);

    if (rowLabelPosition == RowLabelPosition.LEFT) {
      mLeftCheck.setSelected(true);
    } else {
      mRightCheck.setSelected(true);
    }
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mLeftCheck.addClickListener(l);
    mRightCheck.addClickListener(l);
  }

  /**
   * Gets the position.
   *
   * @return the position
   */
  public RowLabelPosition getPosition() {
    return mLeftCheck.isSelected() ? RowLabelPosition.LEFT : RowLabelPosition.RIGHT;
  }

}
