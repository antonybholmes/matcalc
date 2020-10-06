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
package edu.columbia.rdf.matcalc.toolbox.supervised;

import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.DifferentialExpressionPlotWindow;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public class SupervisedPlotWindow extends DifferentialExpressionPlotWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new t test plot window.
   *
   * @param window           the window
   * @param matrix           the matrix
   * @param groups           the groups
   * @param comparisonGroups the comparison groups
   * @param rowGroups        the row groups
   * @param countGroups      the count groups
   * @param history          the history
   * @param Props            the properties
   */
  public SupervisedPlotWindow(ModernRibbonWindow window, DataFrame matrix, XYSeriesModel groups,
      XYSeriesGroup comparisonGroups, XYSeriesModel rowGroups, CountGroups countGroups, List<String> history,
      Props properties) {
    super(window, "Supervised Classification", matrix, groups, comparisonGroups, rowGroups, countGroups, history,
        properties);
  }
}
