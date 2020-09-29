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

import javax.swing.Box;

import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.statistics.FDRType;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.dialog.ModernDialogMultiCardWindow;
import org.jebtk.modern.dialog.ModernDialogTaskType;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.FDRPanel;
import edu.columbia.rdf.matcalc.FilterPanel;
import edu.columbia.rdf.matcalc.GroupPanel;
import edu.columbia.rdf.matcalc.figure.PlotConstants;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapsePanel;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseType;

/**
 * The class TTestDialog.
 */
public class SupervisedDialog extends ModernDialogMultiCardWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private static class PlotTypePanel extends VBox {
    private static final long serialVersionUID = 1L;

    /**
     * The member check plot.
     */
    private CheckBox mCheckPlot = new ModernCheckSwitch(PlotConstants.MENU_CREATE_PLOT, true);

    private ModernRadioButton mHeatMapRadio = new ModernRadioButton("Heat map", true);

    private ModernRadioButton mVolcanoRadio = new ModernRadioButton("Volcano plot");

    public PlotTypePanel() {
      add(mCheckPlot);
      add(UI.createVGap(5));
      add(mHeatMapRadio);
      // add(UI.createVGap(5));
      add(mVolcanoRadio);

      new ModernButtonGroup(mHeatMapRadio, mVolcanoRadio);

      setBorder(ModernWidget.QUAD_BORDER);
    }

    public PlotType getCreatePlot() {
      if (mCheckPlot.isSelected()) {
        if (mHeatMapRadio.isSelected()) {
          return PlotType.HEATMAP;
        } else {
          return PlotType.VOLCANO;
        }
      } else {
        return PlotType.NONE;
      }
    }
  }

  /**
   * The member check is log2.
   */
  private ModernTwoStateWidget mCheckIsLog2 = new ModernCheckSwitch(PlotConstants.MENU_IS_LOG_TRANSFORMED);

  /**
   * The member check log2.
   */
  private ModernTwoStateWidget mCheckLog2 = new ModernCheckSwitch(PlotConstants.MENU_LOG_TRANSFORM);

  private ModernRadioButton mCheckOneOne = new ModernRadioButton("One vs One", true);

  private ModernRadioButton mCheckOneRest = new ModernRadioButton("One vs Rest");

  private ModernRadioButton mCheckPairwise = new ModernRadioButton("Pairwise");

  /**
   * The member groups.
   */
  private XYSeriesGroup mGroups;

  /**
   * The check reset.
   */
  private CheckBox mCheckReset = new ModernCheckSwitch(PlotConstants.MENU_RESET_HISTORY);

  private CheckBox mCheckHistory = new ModernCheckSwitch("Keep history", true);

  /**
   * The member group panel.
   */
  private GroupPanel mGroupPanel;

  /**
   * The member collapse panel.
   */
  private CollapsePanel mCollapsePanel;

  /**
   * The member fdr panel.
   */
  private FDRPanel mFdrPanel = new FDRPanel();

  /**
   * The member filter panel.
   */
  private FilterPanel mFilterPanel = new FilterPanel();

  /**
   * The member matrix.
   */
  private DataFrame mMatrix;

  /**
   * The member expression field.
   */
  // private ModernCompactSpinner mExpressionField =
  // new ModernCompactSpinner(1, 10000, 1);

  /** The m up down P field. */
  private ModernCompactSpinner mUpDownPField = new ModernCompactSpinner(0, 1, 0.05, 0.01);

  private TestCombo mTestCombo = new TestCombo();

  private SortCombo mSortCombo = new SortCombo();

  private PlotTypePanel mPlotPanel = new PlotTypePanel();

  /**
   * Instantiates a new t test dialog.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param groups the groups
   */
  public SupervisedDialog(ModernWindow parent, DataFrame matrix, XYSeriesGroup groups) {
    super(parent, "Supervised Classification", "matcalc.ttest.help.url", ModernDialogTaskType.OK_CANCEL);

    mGroups = groups;
    mMatrix = matrix;

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    createUi();

    new ModernButtonGroup(mCheckOneOne, mCheckOneRest, mCheckPairwise);

    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(640, 480);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    Box box = VBox.create();

    // sectionHeader("Data Transform", box);

    box.add(mCheckIsLog2);
    box.add(UI.createVGap(5));
    box.add(mCheckIsLog2);
    box.add(UI.createVGap(5));
    box.add(mCheckLog2);

    // box.add(new HExpandBox("Minimum Expression", mExpressionField));

    // midSectionHeader("Groups", box);

    box.add(UI.createVGap(20));

    mGroupPanel = new GroupPanel(mGroups);

    box.add(mGroupPanel);
    box.add(UI.createVGap(5));
    box.add(mCheckOneOne);
    box.add(mCheckOneRest);
    box.add(mCheckPairwise);

    box.add(UI.createVGap(20));
    box.add(mCheckHistory);

    addTab("Input", box);

    box = VBox.create();

    // sectionHeader("Filter Options", box);
    box.add(mFilterPanel);

    addTab("Filter", box);

    box = VBox.create();

    // sectionHeader("Collapse Options", box);
    mCollapsePanel = new CollapsePanel(mMatrix, mGroups);

    box.add(mCollapsePanel);

    addTab("Collapse", box);

    box = VBox.create();

    sectionHeader("Test", box);

    box.add(mTestCombo);

    box.add(UI.createVGap(5));

    box.add(new HExpandBox("Sort by", mSortCombo));

    midSectionHeader("Correction", box);

    // sectionHeader("Multiple Comparison Options", box);
    box.add(mFdrPanel);

    // midSectionHeader("Differential Expression Options", box);

    box.add(UI.createVGap(5));

    box.add(new HExpandBox("Up/down p-value", mUpDownPField));

    addTab("Statistics", box);

    box = VBox.create();

    left().setFooter(mPlotPanel);

    getTabsModel().changeTab(0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.dialog.ModernDialogTaskWindow#clicked(org.abh.common.ui.
   * event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {

      // Check we have enough samples

      XYSeries g1 = getGroup1();

      List<Integer> i1 = MatrixGroup.findColumnIndices(mMatrix, g1);

      if (i1.size() < 2) {
        ModernMessageDialog.createWarningDialog(mParent,
            TextUtils.cat("There must be at least 2 samples in ", g1.getName(), "."));

        return;
      }

      XYSeries g2 = getGroup2();

      List<Integer> i2 = MatrixGroup.findColumnIndices(mMatrix, g2);

      if (i2.size() < 2) {
        ModernMessageDialog.createWarningDialog(mParent,
            TextUtils.cat("There must be at least 2 samples in ", g2.getName(), "."));

        return;
      }

    }

    super.clicked(e);
  }

  /**
   * Gets the group1.
   *
   * @return the group1
   */
  public XYSeries getGroup1() {
    return mGroupPanel.getGroup1();
  }

  /**
   * Gets the group2.
   *
   * @return the group2
   */
  public XYSeries getGroup2() {
    return mGroupPanel.getGroup2();
  }

  /**
   * Gets the equal variance.
   *
   * @return the equal variance
   */
  public boolean getEqualVariance() {
    return mGroupPanel.getEqualVariance();
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
   * Gets the up down P.
   *
   * @return the up down P
   */
  public double getUpDownP() {
    return mUpDownPField.getValue();
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
   * Gets the creates the plot.
   *
   * @return the creates the plot
   */
  public PlotType getCreatePlot() {
    return mPlotPanel.getCreatePlot();
  }

  /**
   * Gets the log2 transform.
   *
   * @return the log2 transform
   */
  public boolean getLog2Transform() {
    return mCheckLog2.isSelected();
  }

  /**
   * Gets the checks if is log2 transformed.
   *
   * @return the checks if is log2 transformed
   */
  public boolean getIsLog2Transformed() {
    return mCheckIsLog2.isSelected();
  }

  /**
   * Gets the min exp.
   *
   * @return the min exp
   * @throws ParseException the parse exception
   */
  // public double getMinExp() {
  // return mExpressionField.getValue();
  // //TextUtils.parseDouble(expressionField.getText());
  // }

  /**
   * Gets the min zscore.
   *
   * @return the min zscore
   */
  public double getMinZscore() {
    return mFilterPanel.getMinZscore(); // TextUtils.parseDouble(mZScoreField.getText());
  }

  /**
   * Gets the top genes.
   *
   * @return the top genes
   */
  public int getTopGenes() {
    return mFilterPanel.getTopGenes(); // TextUtils.parseDouble(mZScoreField.getText());
  }

  /**
   * Gets the pos z.
   *
   * @return the pos z
   */
  public boolean getPosZ() {
    return mFilterPanel.getPosZ();
  }

  /**
   * Gets the neg z.
   *
   * @return the neg z
   */
  public boolean getNegZ() {
    return mFilterPanel.getNegZ();
  }

  /**
   * Gets the reset.
   *
   * @return the reset
   */
  public boolean getReset() {
    return mCheckReset.isSelected();
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

  /**
   * Gets the min fold change.
   *
   * @return the min fold change
   */
  public double getMinFoldChange() {
    return mFilterPanel.getMinFoldChange();
  }

  public TestType getTest() {
    return mTestCombo.getTest();
  }

  public SortType getSortType() {
    return mSortCombo.getSortType();
  }

  public boolean getKeepHistory() {
    return mCheckHistory.isSelected();
  }

  public ComparisonType getComparisonType() {
    if (mCheckOneRest.isSelected()) {
      return ComparisonType.ONE_VS_REST;
    } else if (mCheckPairwise.isSelected()) {
      return ComparisonType.PAIRWISE;
    } else {
      return ComparisonType.ONE_VS_ONE;
    }
  }
}
