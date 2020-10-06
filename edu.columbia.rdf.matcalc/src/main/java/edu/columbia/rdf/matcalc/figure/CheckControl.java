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

import org.jebtk.core.Props;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class IntensityControl.
 */
public class CheckControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m check. */
  private CheckBox mCheck;

  /** The m properties. */
  private Props mProps;

  /** The m setting. */
  private String mSetting;

  /**
   * The member spinner.
   *
   * @param parent  the parent
   * @param name    the name
   * @param Props   the properties
   * @param setting the setting
   */
  // private ModernCompactSpinner mSpinner =
  // new ModernCompactSpinner(1, 10, 3);

  public CheckControl(ModernWindow parent, String name, Props properties, String setting) {
    mSetting = setting;
    mProps = properties;

    mCheck = new ModernCheckSwitch(name);

    mCheck.setSelected(properties.getBool(setting));

    add(UI.createVGap(5));
    add(mCheck);
    add(UI.createVGap(5));

    mCheck.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mProps.set(mSetting, mCheck.isSelected());
      }
    });
  }
}
