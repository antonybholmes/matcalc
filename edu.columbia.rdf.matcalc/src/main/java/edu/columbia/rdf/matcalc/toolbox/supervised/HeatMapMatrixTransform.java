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

import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.DifferentialExpressionPlotMatrixTransform;

/**
 * Creates a ttest plot of a matrix. Attempts to reuse existing plot if it still
 * exists.
 * 
 * @author Antony Holmes
 *
 */
public class HeatMapMatrixTransform extends DifferentialExpressionPlotMatrixTransform {

  /**
   * Instantiates a new t test plot matrix transform.
   *
   * @param parent           the parent
   * @param inputMatrix      the input matrix
   * @param groups           the groups
   * @param comparisonGroups the comparison groups
   * @param rowGroups        the row groups
   * @param countGroups      the count groups
   * @param history          the history
   * @param Props            the properties
   */

  public HeatMapMatrixTransform(ModernRibbonWindow parent, DataFrame inputMatrix, XYSeriesModel groups,
      XYSeriesGroup comparisonGroups, XYSeriesModel rowGroups, CountGroups countGroups, List<String> history,
      Props properties) {
    super(parent, "Create Supervised Classification Plot", inputMatrix, groups, comparisonGroups, rowGroups,
        countGroups, history, properties);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.PlotMatrixTransform#createWindow()
   */
  @Override
  public SupervisedPlotWindow createWindow() {
    return new SupervisedPlotWindow((ModernRibbonWindow) mParent, mMatrix, mGroups, mComparisonGroups, mRowGroups,
        mCountGroups, mHistory, mProps);
  }
}
