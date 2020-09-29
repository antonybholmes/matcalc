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

import java.awt.Color;

import javax.swing.Box;

import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton2;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class ColoredPlotControl.
 */
public class ColoredPlotControl extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check box.
   */
  private ModernTwoStateWidget mCheckBox;

  /**
   * The member color button.
   */
  private ColorSwatchButton2 mColorButton;

  /**
   * Instantiates a new colored plot control.
   *
   * @param parent the parent
   * @param name   the name
   * @param color  the color
   */
  public ColoredPlotControl(ModernWindow parent, String name, Color color) {
    this(parent, name, color, true);
  }

  /**
   * Instantiates a new colored plot control.
   *
   * @param parent   the parent
   * @param name     the name
   * @param color    the color
   * @param selected the selected
   */
  public ColoredPlotControl(ModernWindow parent, String name, Color color, boolean selected) {
    mCheckBox = new ModernCheckSwitch(name);
    mCheckBox.setSelected(selected);

    mColorButton = new ColorSwatchButton2(parent, color);

    add(mCheckBox);
    add(Box.createHorizontalGlue());
    add(mColorButton);

    setAlignmentY(TOP_ALIGNMENT);
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mCheckBox.addClickListener(l);
    mColorButton.addClickListener(l);
  }

  /**
   * Checks if is selected.
   *
   * @return true, if is selected
   */
  public boolean isSelected() {
    return mCheckBox.isSelected();
  }

  /**
   * Gets the selected color.
   *
   * @return the selected color
   */
  public Color getSelectedColor() {
    return isSelected() ? mColorButton.getSelectedColor() : null;
  }

}
