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
package edu.columbia.rdf.matcalc;

import java.util.List;

import javax.swing.Box;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.combobox.ModernComboBox2;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernLabel;

/**
 * For choosing an FDR method.
 *
 * @author Antony Holmes
 */
public class GroupPanel extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The group1 combo.
   */
  private ModernComboBox2 group1Combo;

  /**
   * The group2 combo.
   */
  private ModernComboBox2 group2Combo;

  /**
   * The check equal variance.
   */
  private CheckBox checkEqualVariance = new ModernCheckBox("Equal variance");

  /**
   * The member groups.
   */
  private List<XYSeries> mGroups;

  /**
   * Instantiates a new group panel.
   *
   * @param groups the groups
   */
  public GroupPanel(List<XYSeries> groups) {
    mGroups = groups;

    group1Combo = new GroupsCombo(groups);
    group2Combo = new GroupsCombo(groups);

    group1Combo.setSelectedIndex(0);
    group2Combo.setSelectedIndex(1);

    ModernLabel label = new ModernAutoSizeLabel("Compare");
    label.setAlignmentY(TOP_ALIGNMENT);

    add(label);
    add(Box.createHorizontalGlue());

    Box box = VBox.create();
    box.setAlignmentY(TOP_ALIGNMENT);
    box.add(group1Combo);
    box.add(ModernPanel.createVGap());
    box.add(new ModernAutoSizeLabel("vs"));
    box.add(ModernPanel.createVGap());
    box.add(group2Combo);

    add(box);

    // setAlignmentY(TOP_ALIGNMENT);
  }

  /**
   * Gets the equal variance.
   *
   * @return the equal variance
   */
  public boolean getEqualVariance() {
    return checkEqualVariance.isSelected();
  }

  /**
   * Gets the group1.
   *
   * @return the group1
   */
  public XYSeries getGroup1() {
    if (mGroups == null || mGroups.size() == 0) {
      return null;
    }

    return mGroups.get(group1Combo.getSelectedIndex());
  }

  /**
   * Gets the group2.
   *
   * @return the group2
   */
  public XYSeries getGroup2() {
    if (mGroups == null || mGroups.size() == 0) {
      return null;
    }

    return mGroups.get(group2Combo.getSelectedIndex());
  }
}
