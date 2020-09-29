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

import org.jebtk.graphplot.figure.props.TickProps;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class TicksInsidePlotControl.
 */
public class TicksInsidePlotControl extends HBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check box.
   */
  private CheckBox mCheckBox = new ModernCheckBox("Draw Inside");

  /**
   * The member tick.
   */
  private TickProps mTick;

  /**
   * Instantiates a new ticks inside plot control.
   *
   * @param parent the parent
   * @param tick   the tick
   */
  public TicksInsidePlotControl(ModernWindow parent, TickProps tick) {
    mTick = tick;

    mCheckBox.setSelected(mTick.getDrawInside());

    add(mCheckBox);

    mCheckBox.addClickListener(this);
  }

  /**
   * Checks if is selected.
   *
   * @return true, if is selected
   */
  public boolean isSelected() {
    return mCheckBox.isSelected();
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
    mTick.setDrawInside(mCheckBox.isSelected());
  }

}
