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

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.props.LegendProps;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;

/**
 * The class LineStyleControl.
 */
public class LegendPositionControl extends HBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check box.
   */
  private LegendPositionCombo mCombo = new LegendPositionCombo();

  /** The m legend. */
  private LegendProps mLegend;

  /**
   * Instantiates a new line style control.
   *
   * @param legend the legend
   */
  public LegendPositionControl(LegendProps legend) {
    mLegend = legend;

    add(new ModernAutoSizeLabel("Position"));
    add(Box.createHorizontalGlue());
    add(mCombo);

    mCombo.addClickListener(this);

    legend.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        mCombo.setPosition(mLegend.getPosition());
      }
    });
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
    mLegend.setPosition(mCombo.getPosition());
  }

}
