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

import org.jebtk.graphplot.figure.props.TickMarkProps;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class TickColorPlotControl.
 */
public class TickColorPlotControl extends HBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check visible.
   */
  private CheckBox mCheckVisible;

  /**
   * The member color button.
   */
  private ColorSwatchButton mColorButton;

  /**
   * The member tick.
   */
  private TickMarkProps mTick;

  /**
   * Instantiates a new tick color plot control.
   *
   * @param parent the parent
   * @param name   the name
   * @param tick   the tick
   */
  public TickColorPlotControl(ModernWindow parent, String name, TickMarkProps tick) {

    mTick = tick;

    mCheckVisible = new ModernCheckBox(name);
    mCheckVisible.setSelected(tick.getLineStyle().getVisible());

    mColorButton = new ColorSwatchButton(parent, tick.getLineStyle().getColor());

    add(mCheckVisible);
    add(Box.createHorizontalGlue());
    add(mColorButton);
    // setBorder(ModernWidget.BORDER);

    mCheckVisible.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mTick.getLineStyle().setVisible(mCheckVisible.isSelected());
      }
    });

    mColorButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mTick.getLineStyle().setColor(getSelectedColor());
      }
    });
  }

  /**
   * Checks if is selected.
   *
   * @return true, if is selected
   */
  public boolean isSelected() {
    return mCheckVisible.isSelected();
  }

  /**
   * Gets the selected color.
   *
   * @return the selected color
   */
  public Color getSelectedColor() {
    return isSelected() ? mColorButton.getSelectedColor() : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
   * event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    mTick.getLineStyle().setColor(getSelectedColor());
  }

}
