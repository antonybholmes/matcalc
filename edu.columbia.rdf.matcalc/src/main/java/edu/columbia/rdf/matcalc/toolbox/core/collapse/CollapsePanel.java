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
package edu.columbia.rdf.matcalc.toolbox.core.collapse;

import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.UI;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;

import edu.columbia.rdf.matcalc.GroupPanel;
import edu.columbia.rdf.matcalc.MatrixRowAnnotationCombo;

/**
 * For choosing an FDR method.
 *
 * @author Antony Holmes
 */
public class CollapsePanel extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  // private ModernRadioButton mNoneButton = new ModernRadioButton("None");
  // private ModernRadioButton mMaxButton = new ModernRadioButton("Max");
  // private ModernRadioButton mMinButton = new ModernRadioButton("Min");
  // private ModernRadioButton mMaxStdButton = new ModernRadioButton("Max Std
  // Dev");
  // private ModernRadioButton mMaxMeanButton = new ModernRadioButton("Max
  // Mean");
  // private ModernRadioButton mMaxMedianButton = new ModernRadioButton("Max
  // Median");

  /**
   * The member header combo.
   */
  private MatrixRowAnnotationCombo mHeaderCombo;

  /**
   * The member collapse combo.
   */
  private CollapseCombo mCollapseCombo = new CollapseCombo();

  /**
   * The member groups panel.
   */
  private GroupPanel mGroupsPanel;

  /**
   * Instantiates a new collapse panel.
   *
   * @param matrix the matrix
   * @param groups the groups
   */
  public CollapsePanel(DataFrame matrix, XYSeriesGroup groups) {
    mHeaderCombo = new MatrixRowAnnotationCombo(matrix);
    add(new HExpandBox("Column", mHeaderCombo));

    add(UI.createVGap(10));

    add(new HExpandBox("Type", mCollapseCombo));

    add(UI.createVGap(10));

    mGroupsPanel = new GroupPanel(groups);
    add(mGroupsPanel);

    /*
     * box2 = new VBoxPanel(); box2.add(mNoneButton); box2.add(mMaxButton);
     * box2.add(mMinButton); box2.add(mMaxStdButton); box2.add(mMaxMeanButton);
     * box2.add(mMaxMedianButton);
     * box2.setBorder(BorderService.getInstance().createLeftBorder(ModernWidget.
     * TINY_SIZE.width)); add(box2);
     */

    /*
     * ModernButtonGroup group = new ModernButtonGroup();
     * 
     * group.add(mNoneButton); group.add(mMaxButton); group.add(mMinButton);
     * group.add(mMaxStdButton); group.add(mMaxMeanButton);
     * group.add(mMaxMedianButton);
     * 
     * mNoneButton.setSelected(true);
     */
  }

  /**
   * Gets the collapse type.
   *
   * @return the collapse type
   */
  public CollapseType getCollapseType() {
    switch (mCollapseCombo.getSelectedIndex()) {
    case 0:
      return CollapseType.NONE;
    case 1:
      return CollapseType.MAX;
    case 2:
      return CollapseType.MIN;
    case 3:
      return CollapseType.MAX_STDEV;
    case 4:
      return CollapseType.MAX_MEAN;
    case 5:
      return CollapseType.MAX_MEDIAN;
    case 6:
      return CollapseType.MAX_TSTAT;
    case 7:
      return CollapseType.MAX_IQR;
    case 8:
      return CollapseType.MAX_QUART_COEFF_DISP;
    default:
      return CollapseType.NONE;
    }
  }

  /**
   * Gets the collapse name.
   *
   * @return the collapse name
   */
  public String getCollapseName() {
    return mHeaderCombo.getText();
  }

  /**
   * Gets the group1.
   *
   * @return the group1
   */
  public MatrixGroup getGroup1() {
    return mGroupsPanel.getGroup1();
  }

  /**
   * Gets the group2.
   *
   * @return the group2
   */
  public MatrixGroup getGroup2() {
    return mGroupsPanel.getGroup2();
  }
}
