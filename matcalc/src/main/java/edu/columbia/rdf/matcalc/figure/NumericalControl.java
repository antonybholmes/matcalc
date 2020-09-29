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

import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;

/**
 * The class NumericalControl.
 */
public class NumericalControl extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The field.
   */
  private ModernCompactSpinner field;

  /**
   * Instantiates a new numerical control.
   *
   * @param name  the name
   * @param min   the min
   * @param max   the max
   * @param value the value
   */
  public NumericalControl(String name, int min, int max, int value) {
    field = new ModernCompactSpinner(min, max, value);

    add(new ModernAutoSizeLabel(name));
    add(Box.createHorizontalGlue());
    add(field);
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addChangeListener(ChangeListener l) {
    field.addChangeListener(l);
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public int getValue() {
    return field.getIntValue();
  }

}
