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

import org.jebtk.core.Props;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.UI;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class IntensityControl.
 */
public class TreeControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member slider.
   */
  private ModernCompactSpinner mSlider = new ModernCompactSpinner(1, 1000, 200);

  /** The m check visible. */
  private ColoredPlotControl mCheckVisible;

  /** The m properties. */
  private Props mProps;

  /** The m prefix. */
  private String mPrefix;

  /**
   * The member spinner.
   *
   * @param parent the parent
   * @param Props  the properties
   * @param prefix the prefix
   */
  // private ModernCompactSpinner mSpinner =
  // new ModernCompactSpinner(1, 10, 3);

  public TreeControl(ModernWindow parent, Props properties, String prefix) {
    mPrefix = prefix;
    mProps = properties;

    mSlider.setValue(properties.getInt(prefix + ".width"));

    mCheckVisible = new ColoredPlotControl(parent, "Show", properties.getColor(prefix + ".color"),
        properties.getBool(prefix + ".visible"));

    add(mCheckVisible);
    add(UI.createVGap(5));

    Box box = VBox.create();
    box.add(new HExpandBox("Width", mSlider));

    box.add(new CheckControl(parent, "Color leaves by group", properties, prefix + ".leaf.color"));

    box.setBorder(BorderService.getInstance().createLeftBorder(10));

    add(box);
    // setBorder(ModernWidget.BORDER);

    mSlider.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mProps.set(mPrefix + ".width", mSlider.getIntValue());
      }
    });

    mCheckVisible.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mProps.set(mPrefix + ".visible", mCheckVisible.isSelected());
        mProps.set(mPrefix + ".color", mCheckVisible.getSelectedColor());
      }
    });
  }
}
