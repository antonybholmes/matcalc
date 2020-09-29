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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.props.LegendProps;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;

/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes
 *
 */
public class LegendRibbonSection extends RibbonSection {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member legend.
   */
  private LegendProps mLegend;

  /**
   * The show legend button.
   */
  private CheckBox mShowButton = new ModernCheckSwitch("Show");

  /** The m show border button. */
  private CheckBox mShowBorderButton = new ModernCheckSwitch("Border");

  /** The m show background button. */
  private CheckBox mShowBackgroundButton = new ModernCheckSwitch("Background");

  private CheckBox mInsideButton = new ModernCheckSwitch("Inside");

  /**
   * The legend position picker.
   */
  private LegendPositionPicker legendPositionPicker;

  /**
   * Instantiates a new legend ribbon section.
   *
   * @param ribbon the ribbon
   * @param legend the legend
   */
  public LegendRibbonSection(Ribbon ribbon, LegendProps legend) {
    super(ribbon, "Legend");

    mLegend = legend;

    add(mShowButton, mShowBorderButton, mShowBackgroundButton, mInsideButton);
    add(ModernPanel.createHGap());
    addSeparator();
    // add(ModernPanel.createHGap());
    legendPositionPicker = new LegendPositionPicker(legend);
    add(legendPositionPicker);

    mShowButton.setSelected(legend.getVisible());
    mShowBorderButton.setSelected(legend.getStyle().getLineStyle().getVisible());
    mShowBackgroundButton.setSelected(legend.getStyle().getFillStyle().getVisible());
    mInsideButton.setSelected(legend.isInside());

    mShowButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mLegend.setVisible(mShowButton.isSelected());
      }
    });

    mShowBorderButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mLegend.getStyle().getLineStyle().setVisible(mShowBorderButton.isSelected());
      }
    });

    mShowBackgroundButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mLegend.getStyle().getFillStyle().setVisible(mShowBackgroundButton.isSelected());
      }
    });

    mInsideButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mLegend.setInside(mInsideButton.isSelected());
      }
    });

    mLegend.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mShowButton.setSelected(mLegend.getVisible());
        mShowBorderButton.setSelected(mLegend.getStyle().getLineStyle().getVisible());
        mShowBackgroundButton.setSelected(mLegend.getStyle().getFillStyle().getVisible());
      }
    });
  }
}
