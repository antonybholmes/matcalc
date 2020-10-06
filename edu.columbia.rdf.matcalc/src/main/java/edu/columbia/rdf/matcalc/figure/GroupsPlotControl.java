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

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProps;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class GroupsPlotControl.
 */
public class GroupsPlotControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The check box.
   */
  // private CheckBox mCheckShow = new ModernCheckBox("Labels");

  /**
   * The check color.
   */
  private ModernTwoStateWidget mCheckColor = new ModernCheckSwitch("Show", true);

  /**
   * The check grid.
   */
  private ModernTwoStateWidget mCheckGrid = new ModernCheckSwitch("Grid", false);

  /**
   * The check border.
   */
  // private CheckBox mGheckBorder = new ModernCheckBox("Border", true);

  /**
   * The label color button.
   */
  // private ColorSwatchButton mLabelColorButton;

  /**
   * The border color button.
   */
  private ColorSwatchButton mBorderColorButton;

  /**
   * The grid color button.
   */
  private ColorSwatchButton mGridColorButton;

  /**
   * Instantiates a new groups plot control.
   *
   * @param parent the parent
   * @param color  the color
   * @param Props  the properties
   */
  public GroupsPlotControl(ModernWindow parent, Color color, Props properties) {

    /*
     * if (labelOption) { mLabelColorButton = new ColorSwatchButton(parent, color);
     * 
     * add(new HExpandBox(mCheckShow, mLabelColorButton));
     * 
     * add(ModernPanel.createVGap()); }
     */

    add(mCheckColor);

    add(UI.createVGap(5));

    // Box box = VBox.create();

    mGridColorButton = new ColorSwatchButton(parent, color);
    add(new HExpandBox(mCheckGrid, mGridColorButton));

    // mBorderColorButton = new ColorSwatchButton(parent, color);
    // box.add(new HExpandBox(mGheckBorder, mBorderColorButton));
    // box.setBorder(BorderService.getInstance().createLeftBorder(10));

    // add(box);
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    // mCheckShow.addClickListener(l);
    // mLabelColorButton.addClickListener(l);
    mCheckColor.addClickListener(l);
    // mGheckBorder.addClickListener(l);
    // mBorderColorButton.addClickListener(l);
    mCheckGrid.addClickListener(l);
    mGridColorButton.addClickListener(l);
  }

  /**
   * Checks if is selected.
   *
   * @return true, if is selected
   */
  // public boolean isSelected() {
  // return mCheckShow.isSelected();
  // }

  /**
   * Gets the color.
   *
   * @return the color
   */
  // public Color getColor() {
  // return mLabelColorButton.getSelectedColor();
  // }

  /**
   * Gets the show colors.
   *
   * @return the show colors
   */
  public boolean getShowColors() {
    return mCheckColor.isSelected();
  }

  /**
   * Gets the properties.
   *
   * @return the properties
   */
  public GroupProps getProperties() {
    GroupProps props = new GroupProps();
    // properties.show = isSelected();
    // properties.color = getColor();
    props.showColors = getShowColors();
    // properties.showBorder = getShowBorder();
    // properties.borderColor = getBorderColor();
    props.showGrid = getShowGrid();
    props.gridColor = getGridColor();

    return props;
  }

  /**
   * Gets the show grid.
   *
   * @return the show grid
   */
  private boolean getShowGrid() {
    return mCheckGrid.isSelected();
  }

  /**
   * Gets the grid color.
   *
   * @return the grid color
   */
  public Color getGridColor() {
    return mGridColorButton.getSelectedColor();
  }

  /**
   * Gets the show border.
   *
   * @return the show border
   */
  // private boolean getShowBorder() {
  // return mGheckBorder.isSelected();
  // }

  /**
   * Gets the border color.
   *
   * @return the border color
   */
  public Color getBorderColor() {
    return mBorderColorButton.getSelectedColor();
  }
}
