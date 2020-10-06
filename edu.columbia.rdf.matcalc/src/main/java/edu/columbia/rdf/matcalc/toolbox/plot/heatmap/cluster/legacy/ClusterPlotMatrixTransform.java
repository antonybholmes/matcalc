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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap.cluster.legacy;

import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.HeatMapPlotMatrixTransform;

/**
 * Create a cluster plot from a matrix.
 *
 * @author Antony Holmes
 */
public class ClusterPlotMatrixTransform extends HeatMapPlotMatrixTransform {

  /**
   * The member row cluster.
   */
  private Cluster mRowCluster;

  /**
   * The member column cluster.
   */
  private Cluster mColumnCluster;

  /**
   * Instantiates a new cluster plot matrix transform.
   *
   * @param parent        the parent
   * @param matrix        the input matrix
   * @param groups        the groups
   * @param rowGroups     the row groups
   * @param rowCluster    the row cluster
   * @param columnCluster the column cluster
   * @param countGroups   the count groups
   * @param history       the history
   * @param Props         the properties
   */
  public ClusterPlotMatrixTransform(ModernRibbonWindow parent, DataFrame matrix, XYSeriesModel groups,
      XYSeriesModel rowGroups, Cluster rowCluster, Cluster columnCluster, CountGroups countGroups, List<String> history,
      Props properties) {
    super(parent, "Create Hierarchical Cluster Plot", matrix, groups, rowGroups, countGroups, history, properties);

    mRowCluster = rowCluster;
    mColumnCluster = columnCluster;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.PlotMatrixTransform#createWindow()
   */
  @Override
  public ClusterPlotWindow createWindow() {
    return new ClusterPlotWindow((ModernRibbonWindow) mParent, mMatrix, mGroups, mRowGroups, mRowCluster,
        mColumnCluster, mCountGroups, mHistory, mProps);
  }
}
