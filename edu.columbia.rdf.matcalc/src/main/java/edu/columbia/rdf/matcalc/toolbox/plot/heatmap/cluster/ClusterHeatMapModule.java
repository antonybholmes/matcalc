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

import org.jebtk.graphplot.PlotFactory;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.cluster.DistanceMetric;
import org.jebtk.math.cluster.HierarchicalClustering;
import org.jebtk.math.cluster.Linkage;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.icons.Cluster32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class ClusterHeatMapModule.
 */
public class ClusterHeatMapModule extends Module implements ModernClickListener {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Cluster Heat Map";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mParent = window;

    RibbonLargeButton button = new RibbonLargeButton("Cluster Heat Map",
        AssetService.getInstance().loadIcon(Cluster32VectorIcon.class, 24), "Heat Map", "Generate a cluster heat map.");
    button.addClickListener(this);

    // button.setEnabled(false);

    mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
  }

  /**
   * Creates the.
   */
  private void create() {
    HierarchicalClusteringDialog dialog = new HierarchicalClusteringDialog(mParent);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    DistanceMetric distanceMetric = dialog.getDistanceMetric();

    Linkage linkage = dialog.getLinkage();

    DataFrame m = mParent.getCurrentMatrix();

    boolean plot = dialog.getCreatePlot();

    cluster(m, distanceMetric, linkage, dialog.clusterRows(), dialog.clusterColumns(), dialog.optimalLeafOrder(), plot);
  }

  /**
   * Cluster.
   *
   * @param m                the m
   * @param distanceMetric   the distance metric
   * @param linkage          the linkage
   * @param clusterRows      the cluster rows
   * @param clusterColumns   the cluster columns
   * @param optimalLeafOrder the optimal leaf order
   * @param showHeatmap      the show heatmap
   */
  public void cluster(DataFrame m, DistanceMetric distanceMetric, Linkage linkage, boolean clusterRows,
      boolean clusterColumns, boolean optimalLeafOrder, boolean showHeatmap) {

    if (m == null) {
      return;
    }

    Cluster rowCluster = null;
    Cluster columnCluster = null;

    if (clusterRows) {
      rowCluster = HierarchicalClustering.rowCluster(m, linkage, distanceMetric, optimalLeafOrder);
    }

    if (clusterColumns) {
      columnCluster = HierarchicalClustering.columnCluster(m, linkage, distanceMetric, optimalLeafOrder);
    }

    if (rowCluster == null && columnCluster == null) {
      return;
    }

    // XYSeriesGroup allSeries = mParent.getGroups();

    /*
     * for (int c = 0; c < m.getColumnCount(); ++c) { XYSeries series = new
     * XYSeries(m.getColumnName(c));
     * 
     * allSeries.add(series); }
     */

    Figure figure = Figure.createFigure(); // window.getFigure();

    SubFigure subFigure = figure.currentSubFigure();

    PlotFactory.createClusterHeatMap(m, subFigure, mParent.getGroups(), rowCluster, columnCluster);

    Graph2dWindow window = new Graph2dWindow(mParent, figure);

    window.setVisible(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    create();
  }

}
