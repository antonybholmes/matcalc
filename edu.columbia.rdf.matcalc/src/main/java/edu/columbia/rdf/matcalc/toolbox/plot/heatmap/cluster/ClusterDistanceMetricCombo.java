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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap.cluster;

import org.jebtk.math.cluster.DistanceMetric;
import org.jebtk.math.cluster.EuclideanDistanceMetric;
import org.jebtk.math.cluster.ManhattanDistanceMetric;
import org.jebtk.math.cluster.MaximumDistanceMetric;
import org.jebtk.math.cluster.PearsonDistanceMetric;
import org.jebtk.modern.combobox.ModernComboBox;

/**
 * The Class ClusterDistanceMetricCombo.
 */
public class ClusterDistanceMetricCombo extends ModernComboBox {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new cluster distance metric combo.
   */
  public ClusterDistanceMetricCombo() {
    addMenuItem("Euclidean");
    addMenuItem("Manhattan");
    addMenuItem("Maximum");
    addMenuItem("Pearson");
  }

  /**
   * Gets the distance metric.
   *
   * @return the distance metric
   */
  public DistanceMetric getDistanceMetric() {
    DistanceMetric distanceMetric;

    switch (getSelectedIndex()) {
    case 0:
      distanceMetric = new EuclideanDistanceMetric();
      break;
    case 1:
      distanceMetric = new ManhattanDistanceMetric();
      break;
    case 2:
      distanceMetric = new MaximumDistanceMetric();
      break;
    default:
      distanceMetric = new PearsonDistanceMetric();
      break;
    }

    return distanceMetric;
  }
}
