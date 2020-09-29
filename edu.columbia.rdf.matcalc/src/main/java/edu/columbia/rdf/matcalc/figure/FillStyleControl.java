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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.props.FillPattern;
import org.jebtk.graphplot.figure.props.FillProps;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.graph2d.FillPatternButton;

/**
 * The class FillControl.
 */
public class FillStyleControl extends HBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member color button.
   */
  private ColorSwatchButton mColorButton;

  /**
   * The member fill.
   */
  private FillProps mFillStyle;

  /**
   * The member check box.
   */
  private CheckBox mCheckBox;

  /**
   * The member pattern button.
   */
  private FillPatternButton mPatternButton;

  /**
   * The class PatternEvents.
   */
  private class PatternEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
     * event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      FillPattern pattern;

      if (e.getMessage().equals("Cross")) {
        pattern = FillPattern.CROSS_HATCH;
      } else if (e.getMessage().equals("Back")) {
        pattern = FillPattern.BACK_HATCH;
      } else if (e.getMessage().equals("Forward")) {
        pattern = FillPattern.FORWARD_HATCH;
      } else if (e.getMessage().equals("Vert")) {
        pattern = FillPattern.VERT_HATCH;
      } else if (e.getMessage().equals("Hoz")) {
        pattern = FillPattern.HOZ_HATCH;
      } else {
        pattern = FillPattern.SOLID;
      }

      mFillStyle.setPattern(pattern);

      // setColors();
    }
  }

  /**
   * Instantiates a new fill control.
   *
   * @param parent the parent
   * @param fill   the fill
   */
  public FillStyleControl(ModernWindow parent, FillProps fill) {
    this(parent, "Fill", fill);
  }

  /**
   * Instantiates a new fill control.
   *
   * @param parent the parent
   * @param name   the name
   * @param fill   the fill
   */
  public FillStyleControl(ModernWindow parent, String name, FillProps fill) {
    mFillStyle = fill;

    mCheckBox = new ModernCheckSwitch(name);
    mCheckBox.setSelected(fill.getVisible());

    mColorButton = new ColorSwatchButton(parent, fill.getColor());

    mPatternButton = new FillPatternButton(fill.getPattern());

    add(mCheckBox);
    add(Box.createHorizontalGlue());
    add(ModernPanel.createHGap());
    add(mColorButton);
    add(ModernPanel.createHGap());
    add(mPatternButton);

    mCheckBox.addClickListener(this);
    mColorButton.addClickListener(this);
    mPatternButton.addClickListener(new PatternEvents());

    mFillStyle.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mColorButton.setSelectedColor(mFillStyle.getColor());
      }
    });
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
    return mColorButton.getSelectedColor();
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
    mFillStyle.updateColor(getSelectedColor());
    mFillStyle.setVisible(isSelected());
  }

}
