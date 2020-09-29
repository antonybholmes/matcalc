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

import org.jebtk.modern.spinner.ModernCompactSpinner;

/**
 * The Class ScaleSpinner is a specialized spinner for controlling plot scale
 * values.
 */
public class ScaleSpinner extends ModernCompactSpinner {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant STEPS. */
  private static final double[] STEPS = { 0.1, 0.25, 0.5, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
      19, 20 };

  /**
   * Instantiates a new scale spinner.
   *
   * @param value the value
   */
  public ScaleSpinner(double value) {
    super(value, STEPS);
  }
}
