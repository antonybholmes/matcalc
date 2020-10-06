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

import java.io.IOException;
import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.cluster.DistanceMatrix;
import org.jebtk.math.cluster.DistanceMetric;
import org.jebtk.math.cluster.HierarchicalClustering;
import org.jebtk.math.cluster.Linkage;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.icons.Cluster32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.Module;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ClusterProps;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.cluster.HierarchicalClusteringDialog;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.LegacyHeatMapModule;

/**
 * Legacy cluster module using older heatmap module.
 *
 * @author Antony Holmes
 */
public class LegacyClusterModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mClusterButton = new RibbonLargeButton("Cluster",
      new Raster32Icon(new Cluster32VectorIcon()), "Cluster", "Cluster rows and columns.");

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Cluster";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    window.getRibbon().getToolbar("Plot").getSection("Plot").add(mClusterButton);

    mClusterButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    try {
      cluster();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Cluster.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void cluster() throws IOException {
    DataFrame m = mWindow.getCurrentMatrix();

    if (m == null) {
      return;
    }

    Props props = new ClusterProps();

    LegacyHeatMapModule.scaleLargeMatrixImage(m, props);

    if (mWindow.getHistoryPanel().getItemCount() == 0) {
      return;
    }

    HierarchicalClusteringDialog dialog = new HierarchicalClusteringDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    DistanceMetric distanceMetric = dialog.getDistanceMetric();

    Linkage linkage = dialog.getLinkage();

    props.set("plot.heatmap.visible", dialog.getShowHeatMap());

    cluster(m, distanceMetric, linkage, dialog.clusterRows(), dialog.clusterColumns(), dialog.optimalLeafOrder(),
        props);
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
   * @param Props            the properties
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void cluster(DataFrame m, DistanceMetric distanceMetric, Linkage linkage, boolean clusterRows,
      boolean clusterColumns, boolean optimalLeafOrder, Props properties) throws IOException {

    cluster(mWindow, m, distanceMetric, linkage, clusterRows, clusterColumns, optimalLeafOrder, properties);

    /*
     * if (m == null) { return; }
     * 
     * Matrix im = m.getInnerMatrix();
     * 
     * Cluster rowCluster = null; Cluster columnCluster = null;
     * 
     * 
     * if (clusterRows) { rowCluster = HierarchicalClustering.rowCluster(im,
     * linkage, distanceMetric, optimalLeafOrder); }
     * 
     * if (clusterColumns) { columnCluster =
     * HierarchicalClustering.columnCluster(im, linkage, distanceMetric,
     * optimalLeafOrder); }
     * 
     * if (rowCluster == null && columnCluster == null) { return; }
     * 
     * List<Integer> rowOrder; List<Integer> columnOrder;
     * 
     * if (rowCluster != null) { rowOrder = Cluster.getLeafOrderedIds(rowCluster); }
     * else { rowOrder = Mathematics.sequence(0, im.getRowCount() - 1); }
     * 
     * if (columnCluster != null) { columnOrder =
     * Cluster.getLeafOrderedIds(columnCluster); } else { columnOrder =
     * Mathematics.sequence(0, im.getColumnCount() - 1); }
     * 
     * DataFrame m2 = AnnotatableMatrix.copyInnerRows(m, rowOrder);
     * 
     * DataFrame m3 = AnnotatableMatrix.copyInnerColumns(m2, columnOrder);
     * 
     * mWindow.history().addToHistory("Cluster ordered matrix", m3);
     * 
     * // So we can plot each row using a color //RowStandardizeMatrixView
     * normalizedMatrix = new RowStandardizeMatrixView(m);
     * 
     * 
     * //System.err.println("cluster " + clusters.size());
     * 
     * //previewPanel.addPreview(new PreviewTablePanel("Collapsed " + fileCounter,
     * false, collapsedFile));
     * 
     * System.err.println("Cluster " + clusterRows + " " + clusterColumns);
     * 
     * List<String> history = mWindow.getTransformationHistory();
     * 
     * mWindow.history().addToHistory(new ClusterPlotMatrixTransform(mWindow, m3,
     * XYSeriesModel.create(mWindow.getGroups()),
     * XYSeriesModel.create(mWindow.getRowGroups()), rowCluster, columnCluster,
     * CountGroups.defaultGroup(m), history, properties));
     */
  }

  public static DistanceMatrix createDistanceMatrix(Matrix m, DistanceMetric d) {
    int c = m.getCols();

    DistanceMatrix distance = new DistanceMatrix(c); // DoubleMatrix(s, s);

    for (int i = 0; i < c; ++i) {
      for (int j = 0; j < c; ++j) {
        distance.set(i, j, (double) d.columnDistance(m, i, j));
      }
    }

    return distance;
  }

  /**
   * Cluster.
   *
   * @param window           the window
   * @param m                the m
   * @param distanceMetric   the distance metric
   * @param linkage          the linkage
   * @param clusterRows      the cluster rows
   * @param clusterColumns   the cluster columns
   * @param optimalLeafOrder the optimal leaf order
   * @param Props            the properties
   * @return the annotation matrix
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static DataFrame cluster(MainMatCalcWindow window, DataFrame m, DistanceMetric distanceMetric, Linkage linkage,
      boolean clusterRows, boolean clusterColumns, boolean optimalLeafOrder, Props properties) throws IOException {

    if (m == null) {
      return null;
    }

    Cluster rowCluster = null;
    Cluster columnCluster = null;

    if (clusterRows) {
      rowCluster = HierarchicalClustering.rowCluster(m, linkage, distanceMetric, optimalLeafOrder);
    }

    if (clusterColumns) {
      // Create the distance matrix
      columnCluster = HierarchicalClustering.columnCluster(m, linkage, distanceMetric, optimalLeafOrder);

      // Provide distance matrix to user
      DataFrame df = new DataFrame(createDistanceMatrix(m, distanceMetric));
      df.setRowNames(m.getColumnNames());
      df.setColumnNames(m.getColumnNames());

      window.history().addToHistory("Distance matrix", df);
    }

    if (rowCluster == null && columnCluster == null) {
      return m;
    }

    DataFrame m2;

    if (rowCluster != null) {
      List<Integer> rowOrder = Cluster.getLeafOrderedIds(rowCluster);

      m2 = DataFrame.copyRows(m, rowOrder);
    } else {
      // rowOrder = Mathematics.sequence(0, m.getRowCount() - 1);
      m2 = m;
    }

    DataFrame m3;

    if (columnCluster != null) {
      List<Integer> columnOrder = Cluster.getLeafOrderedIds(columnCluster);

      m3 = DataFrame.copyInnerColumns(m2, columnOrder);
    } else {
      // columnOrder = Mathematics.sequence(0, m.getColumnCount() - 1);
      m3 = m2;
    }

    window.history().addToHistory("Cluster ordered matrix", m3);

    // So we can plot each row using a color
    // RowStandardizeMatrixView normalizedMatrix = new
    // RowStandardizeMatrixView(m);

    // System.err.println("cluster " + clusters.size());

    // previewPanel.addPreview(new PreviewTablePanel("Collapsed " + fileCounter,
    // false, collapsedFile));

    /*
     * ClusterPlotWindow window = new ClusterPlotWindow(m, groups, rowCluster,
     * columnCluster);
     * 
     * window.setVisible(true);
     */

    List<String> history = window.getTransformationHistory();

    // for (int i = 0; i < m3.getColumnCount(); ++i) {
    // System.err.println("c " + i + " " + m3.getColumnName(i));
    // }

    window.history()
        .addToHistory(new ClusterPlotMatrixTransform(window, m3, XYSeriesModel.create(window.getGroups()),
            XYSeriesModel.create(window.getRowGroups()), rowCluster, columnCluster, CountGroups.defaultGroup(m3),
            history, properties));

    return m3;
  }
}
