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
import javax.swing.BoxLayout;

import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.AspectRatio;
import org.jebtk.graphplot.figure.heatmap.legacy.HeatMapSettings;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class HeatMapControl.
 */
public class HeatMapControl extends Box {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check show.
   */
  private CheckBox mCheckShow = new ModernCheckBox("Show", true);

  /**
   * The member grid element.
   */
  private ColoredPlotControl mGridElement;

  /**
   * The member border element.
   */
  private ColoredPlotControl mBorderElement;

  /**
   * The member aspect ratio element.
   */
  private AspectRatioControl mAspectRatioElement;

  /**
   * Instantiates a new heat map control.
   *
   * @param parent the parent
   * @param show   the show
   */
  public HeatMapControl(ModernWindow parent, boolean show) {
    super(BoxLayout.PAGE_AXIS);

    mCheckShow.setSelected(show);

    add(mCheckShow);

    add(ModernPanel.createVGap());

    mGridElement = new ColoredPlotControl(parent, "Grid", Color.BLACK);
    add(mGridElement);

    add(ModernPanel.createVGap());

    mBorderElement = new ColoredPlotControl(parent, "Border", Color.BLACK);
    add(mBorderElement);

    add(ModernPanel.createVGap());

    mAspectRatioElement = new AspectRatioControl();
    add(mAspectRatioElement);
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mCheckShow.addClickListener(l);
    mBorderElement.addClickListener(l);
    mGridElement.addClickListener(l);
  }

  /**
   * Adds the change listener.
   *
   * @param l the l
   */
  public void addChangeListener(ChangeListener l) {
    mAspectRatioElement.addChangeListener(l);
  }

  /**
   * Show heat map.
   *
   * @return true, if successful
   */
  public boolean showHeatMap() {
    return mCheckShow.isSelected();
  }

  /**
   * Gets the grid color.
   *
   * @return the grid color
   */
  public Color getGridColor() {
    return mGridElement.getSelectedColor();
  }

  /**
   * Gets the border color.
   *
   * @return the border color
   */
  public Color getBorderColor() {
    return mBorderElement.getSelectedColor();
  }

  /**
   * Gets the properties.
   *
   * @return the properties
   */
  public HeatMapSettings getProperties() {
    HeatMapSettings props = new HeatMapSettings();
    props.show = showHeatMap();
    props.aspectRatio = getpectRatio();
    props.gridColor = getGridColor();
    props.borderColor = getBorderColor();

    return props;
  }

  /**
   * Gets the aspect ratio.
   *
   * @return the aspect ratio
   */
  public AspectRatio getpectRatio() {
    return mAspectRatioElement.getpectRatio();
  }
}
