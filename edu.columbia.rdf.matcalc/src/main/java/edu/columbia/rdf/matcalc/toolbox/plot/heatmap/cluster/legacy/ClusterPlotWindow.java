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
import java.util.concurrent.atomic.AtomicInteger;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.FormatPlotPane;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.HeatMapWindow;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public class ClusterPlotWindow extends HeatMapWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant NEXT_ID.
   */
  private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

  /**
   * The member id.
   */
  private final int mId = NEXT_ID.getAndIncrement();

  /** The m row cluster. */
  private Cluster mRowCluster;

  /** The m column cluster. */
  private Cluster mColumnCluster;

  /**
   * Instantiates a new cluster plot window.
   *
   * @param window        the window
   * @param matrix        the matrix
   * @param groups        the groups
   * @param rowGroups     the row groups
   * @param rowCluster    the row cluster
   * @param columnCluster the column cluster
   * @param countGroups   the count groups
   * @param history       the history
   * @param Props         the properties
   */
  public ClusterPlotWindow(ModernWindow window, DataFrame matrix, XYSeriesModel groups, XYSeriesModel rowGroups,
      Cluster rowCluster, Cluster columnCluster, CountGroups countGroups, List<String> history, Props properties) {
    super(window, matrix, groups, rowGroups, countGroups, history, properties);

    mRowCluster = rowCluster;
    mColumnCluster = columnCluster;

    setTitle("Clustergram " + mId + " - " + getAppInfo().getName());

    setFormatPane(createFormatPane());
  }

  /**
   * Creates the format pane.
   *
   * @return the format plot pane
   */
  public FormatPlotPane createFormatPane() {
    return new ClusterPanel(this, mMatrix, mGroups, mRowGroups, mRowCluster, mColumnCluster, mZoomModel, mColorMapModel,
        mColorModel, mScaleModel, tabsPane().tabs(), mCountGroups, mHistory, mProps);
  }
}
