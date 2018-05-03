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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap;

import org.jebtk.core.model.DoubleModel;

/**
 * The Class ScaleModel.
 */
public class ScaleModel extends DoubleModel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  // private double mMax;

  /**
   * Instantiates a new scale model.
   */
  public ScaleModel() {
    this(3);
  }

  /**
   * Instantiates a new scale model.
   *
   * @param v the v
   */
  public ScaleModel(double v) {
    update(v);

    // mMax = max;
  }

  /*
   * @Override public void update(double v) { super.update(Mathematics.bound(v,
   * -mMax, mMax)); }
   */

  // public double getBaseline() {
  // return mMax;
  // }

  /**
   * Gets the scale.
   *
   * @return the scale
   */
  public double getScale() {
    double v = mItem;

    if (mItem > 0) {
      ++v;
    } else if (v < 0) {
      v = 1.0 / (1.0 - v);
    } else {
      v = 1.0;
    }

    return v;
  }
}
