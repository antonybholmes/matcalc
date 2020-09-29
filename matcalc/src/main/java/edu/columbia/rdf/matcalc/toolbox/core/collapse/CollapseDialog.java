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
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class CollapseDialog.
 */
public class CollapseDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member collapse panel.
   */
  private CollapsePanel mCollapsePanel;

  /**
   * The member matrix.
   */
  private DataFrame mMatrix;

  /**
   * The member groups.
   */
  private XYSeriesGroup mGroups;

  /**
   * Instantiates a new collapse dialog.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param groups the groups
   */
  public CollapseDialog(ModernWindow parent, DataFrame matrix, XYSeriesGroup groups) {
    super(parent, "matcalc.modules.collapse.help.url");

    setTitle("Collapse Rows");

    mMatrix = matrix;
    mGroups = groups;

    setup();

    createUi();

  }

  /**
   * Setup.
   */
  private void setup() {
    setSize(500, 300);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    mCollapsePanel = new CollapsePanel(mMatrix, mGroups);

    setCard(mCollapsePanel);
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
   * Gets the group1.
   *
   * @return the group1
   */
  public MatrixGroup getGroup1() {
    return mCollapsePanel.getGroup1();
  }

  /**
   * Gets the group2.
   *
   * @return the group2
   */
  public MatrixGroup getGroup2() {
    return mCollapsePanel.getGroup2();
  }
}
