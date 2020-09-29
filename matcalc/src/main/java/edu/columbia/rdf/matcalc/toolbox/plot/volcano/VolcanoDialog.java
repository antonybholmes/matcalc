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
package edu.columbia.rdf.matcalc.toolbox.plot.volcano;

import javax.swing.Box;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.statistics.FDRType;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.FDRPanel;
import edu.columbia.rdf.matcalc.GroupMenuItem;
import edu.columbia.rdf.matcalc.figure.PlotConstants;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapsePanel;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseType;
import edu.columbia.rdf.matcalc.toolbox.supervised.TestCombo;
import edu.columbia.rdf.matcalc.toolbox.supervised.TestType;

/**
 * The class VolcanoDialog.
 */
public class VolcanoDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The check log2.
   */
  private CheckBox checkLog2 = new ModernCheckSwitch(PlotConstants.MENU_LOG_TRANSFORM, true);

  /**
   * The member group1 combo.
   */
  private ModernComboBox mGroup1Combo = new ModernComboBox();

  /**
   * The member group2 combo.
   */
  private ModernComboBox mGroup2Combo = new ModernComboBox();

  /**
   * The member groups.
   */
  private XYSeriesGroup mGroups;

  /**
   * The check plot.
   */
  private CheckBox checkPlot = new ModernCheckSwitch(PlotConstants.MENU_CREATE_PLOT, true);

  private TestCombo mTestCombo = new TestCombo();

  /**
   * The check reset.
   */
  private CheckBox checkReset = new ModernCheckBox(PlotConstants.MENU_RESET_HISTORY, true);

  /**
   * The member fdr panel.
   */
  private FDRPanel mFdrPanel = new FDRPanel();

  /**
   * The member collapse panel.
   */
  private CollapsePanel mCollapsePanel;

  /**
   * Instantiates a new volcano dialog.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param groups the groups
   */
  public VolcanoDialog(ModernWindow parent, DataFrame matrix, XYSeriesGroup groups) {
    super(parent, "matcalc.modules.volcano.help.url");

    setTitle("Volcano Plot");

    mGroups = groups;

    mCollapsePanel = new CollapsePanel(matrix, mGroups);

    setup();

    createUi();

  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    loadGroups(mGroups, mGroup1Combo);
    loadGroups(mGroups, mGroup2Combo);

    mGroup1Combo.setSelectedIndex(0);
    mGroup2Combo.setSelectedIndex(1);

    setSize(500, 520);

    UI.centerWindowToScreen(this);
  }

  /**
   * Load groups.
   *
   * @param groups the groups
   * @param combo  the combo
   */
  private void loadGroups(XYSeriesGroup groups, ModernComboBox combo) {
    for (XYSeries group : groups) {
      combo.addMenuItem(new GroupMenuItem(group));
    }
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    Box box = Box.createVerticalBox();

    // sectionHeader("Expression", box);

    box.add(checkLog2);

    /*
     * int[] rows = {ModernWidget.WIDGET_HEIGHT}; int[] cols = {200, 120};
     * 
     * MatrixPanel matrixPanel;
     * 
     * matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING,
     * ModernWidget.PADDING);
     * 
     * matrixPanel.add(checkLog2); matrixPanel.add(new ModernComponent());
     * matrixPanel.add(new ModernAutoSizeLabel("Minimum expression"));
     * matrixPanel.add(new ModernTextBorderPanel(mExpressionField));
     * matrixPanel.setBorder(ModernWidget.DOUBLE_BORDER);
     * 
     * box.add(matrixPanel);
     * 
     * box.add(ModernPanel.createVGap());
     */

    midSectionHeader("Test", box);

    box.add(new HExpandBox("Group 1", mGroup1Combo));
    box.add(UI.createVGap(5));
    box.add(new HExpandBox("Group 2", mGroup2Combo));
    box.add(UI.createVGap(5));
    box.add(mTestCombo);

    midSectionHeader("False Discovery Rate", box);

    box.add(mFdrPanel);

    box.add(UI.createVGap(20));
    box.add(checkPlot);

    setCard(box);
  }

  /**
   * Gets the group1.
   *
   * @return the group1
   */
  public XYSeries getGroup1() {
    return mGroups.get(mGroup1Combo.getSelectedIndex());
  }

  /**
   * Gets the group2.
   *
   * @return the group2
   */
  public XYSeries getGroup2() {
    return mGroups.get(mGroup2Combo.getSelectedIndex());
  }

  /**
   * Gets the max p.
   *
   * @return the max p
   */
  public double getMaxP() {
    return mFdrPanel.getMaxP();
  }

  /**
   * Gets the creates the plot.
   *
   * @return the creates the plot
   */
  public boolean getCreatePlot() {
    return checkPlot.isSelected();
  }

  /**
   * Gets the log2 transform.
   *
   * @return the log2 transform
   */
  public boolean getLog2Transform() {
    return checkLog2.isSelected();
  }

  public TestType getTest() {
    return mTestCombo.getTest();
  }

  /**
   * Gets the reset.
   *
   * @return the reset
   */
  public boolean getReset() {
    return checkReset.isSelected();
  }

  /**
   * Gets the FDR type.
   *
   * @return the FDR type
   */
  public FDRType getFDRType() {
    return mFdrPanel.getFDRType();
  }

  /**
   * Gets the collapse type.
   *
   * @return the collapse type
   */
  public CollapseType getCollapseType() {
    return mCollapsePanel.getCollapseType();
  }

  /**
   * Gets the collapse name.
   *
   * @return the collapse name
   */
  public String getCollapseName() {
    return mCollapsePanel.getCollapseName();
  }
}
