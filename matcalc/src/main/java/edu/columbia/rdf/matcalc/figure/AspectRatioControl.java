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

import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.AspectRatio;
import org.jebtk.modern.UI;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;

/**
 * The class AspectRatioControl.
 */
public class AspectRatioControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member aspect field.
   */
  private ModernCompactSpinner mXField = new ModernCompactSpinner(1, 100, 100, "%");

  /** The m Y field. */
  private ModernCompactSpinner mYField = new ModernCompactSpinner(1, 100, 100, "%");

  /**
   * Instantiates a new aspect ratio control.
   */
  public AspectRatioControl() {
    this(new AspectRatio());
  }

  /**
   * Instantiates a new aspect ratio control.
   *
   * @param aspectRatio the aspect ratio
   */
  public AspectRatioControl(AspectRatio aspectRatio) {

    add(new HExpandBox("X scale", mXField));
    add(UI.createVGap(5));
    add(new HExpandBox("Y scale", mYField));

    mXField.setValue((int) (aspectRatio.getX() * 100));
    mYField.setValue((int) (aspectRatio.getY() * 100));

    setAlignmentY(TOP_ALIGNMENT);
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addChangeListener(ChangeListener l) {
    mXField.addChangeListener(l);
    mYField.addChangeListener(l);
  }

  /**
   * Gets the aspect ratio.
   *
   * @return the aspect ratio
   */
  public AspectRatio getpectRatio() {
    return new AspectRatio(mXField.getIntValue() / 100.0, mYField.getIntValue() / 100.0);
  }

}
