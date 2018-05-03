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

import javax.swing.Box;

import org.jebtk.math.cluster.AverageLinkage;
import org.jebtk.math.cluster.CompleteLinkage;
import org.jebtk.math.cluster.DistanceMetric;
import org.jebtk.math.cluster.Linkage;
import org.jebtk.math.cluster.SingleLinkage;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.figure.PlotConstants;

/**
 * The class HierarchicalClusteringDialog.
 */
public class HierarchicalClusteringDialog extends ModernDialogHelpWindow
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member distance combo.
   */
  private ClusterDistanceMetricCombo mDistanceCombo = new ClusterDistanceMetricCombo();

  /**
   * The member linkage combo.
   */
  private ModernComboBox mLinkageCombo = new ModernComboBox();

  /**
   * The cluster columns check.
   */
  private CheckBox mCheckClusterColumns = new ModernCheckSwitch(
      "Cluster columns", true);

  /**
   * The cluster rows check.
   */
  private CheckBox mCheckClusterRows = new ModernCheckSwitch("Cluster rows");

  private CheckBox mCheckShowHeatMap = new ModernCheckSwitch("Show heat map",
      true);

  /** The m check optimal leaf order. */
  private CheckBox mCheckOptimalLeafOrder = new ModernCheckSwitch(
      "Optimal leaf order", true);

  /**
   * The member check plot.
   */
  private CheckBox mCheckPlot = new ModernCheckSwitch(PlotConstants.MENU_PLOT,
      true);

  /**
   * Instantiates a new hierarchical clustering dialog.
   *
   * @param parent the parent
   */
  public HierarchicalClusteringDialog(ModernWindow parent) {
    super(parent, "matcalc.cluster.help.url");

    setTitle("Cluster");

    setup();

    createUi();
  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    mLinkageCombo.addMenuItem("Average");
    mLinkageCombo.addMenuItem("Complete");
    mLinkageCombo.addMenuItem("Single");
    mLinkageCombo.setSelectedIndex(0);

    mDistanceCombo.setSelectedIndex(3);

    setSize(480, 360);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    Box box = Box.createVerticalBox();

    box.add(new HExpandBox("Distance", mDistanceCombo));
    box.add(ModernPanel.createVGap());
    box.add(new HExpandBox("Linkage", mLinkageCombo));
    box.add(ModernPanel.createVGap());
    box.add(mCheckClusterRows);
    box.add(ModernPanel.createVGap());
    box.add(mCheckClusterColumns);
    box.add(ModernPanel.createVGap());
    box.add(mCheckPlot);
    box.add(ModernPanel.createVGap());
    box.add(mCheckShowHeatMap);
    box.add(ModernPanel.createVGap());
    box.add(mCheckOptimalLeafOrder);

    // box.setBorder(ModernWidget.LARGE_BORDER);

    setCard(box);
  }

  /**
   * Gets the distance metric.
   *
   * @return the distance metric
   */
  public DistanceMetric getDistanceMetric() {
    return mDistanceCombo.getDistanceMetric();
  }

  /**
   * Gets the linkage.
   *
   * @return the linkage
   */
  public Linkage getLinkage() {

    Linkage linkage;

    switch (mLinkageCombo.getSelectedIndex()) {
    case 1:
      linkage = new CompleteLinkage();
      break;
    case 2:
      linkage = new SingleLinkage();
      break;
    default:
      linkage = new AverageLinkage();
      break;
    }

    return linkage;
  }

  /**
   * Cluster rows.
   *
   * @return true, if successful
   */
  public boolean clusterRows() {
    return mCheckClusterRows.isSelected();
  }

  /**
   * Cluster columns.
   *
   * @return true, if successful
   */
  public boolean clusterColumns() {
    return mCheckClusterColumns.isSelected();
  }

  /**
   * Gets the creates the plot.
   *
   * @return the creates the plot
   */
  public boolean getCreatePlot() {
    return mCheckPlot.isSelected();
  }

  /**
   * Optimal leaf order.
   *
   * @return true, if successful
   */
  public boolean optimalLeafOrder() {
    return mCheckOptimalLeafOrder.isSelected();
  }

  public boolean getShowHeatMap() {
    return mCheckShowHeatMap.isSelected();
  }
}
