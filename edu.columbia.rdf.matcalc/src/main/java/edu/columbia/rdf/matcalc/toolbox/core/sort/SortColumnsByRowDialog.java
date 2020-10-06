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
package edu.columbia.rdf.matcalc.toolbox.core.sort;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allow sorting of columns or rows in a table.
 *
 * @author Antony Holmes
 */
public class SortColumnsByRowDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The model.
   */
  private CheckBox mCheckByGroups = new ModernCheckSwitch("Sort within groups");

  /** The m check asc. */
  private CheckBox mCheckAsc = new ModernCheckSwitch("Ascending", true);

  /**
   * Instantiates a new sort column groups by row dialog.
   *
   * @param parent the parent
   */
  public SortColumnsByRowDialog(ModernWindow parent) {
    super(parent, "matcalc.modules.sort-col-by-row.help.url");

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    setTitle("Sort Columns By Row");

    createUi();

    setSize(420, 240);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private void createUi() {
    Box box = VBox.create();

    box.add(mCheckAsc);
    box.add(UI.createVGap(5));
    box.add(mCheckByGroups);

    setCard(box);
    // addBlock(box);
  }

  /**
   * Gets the sort within groups.
   *
   * @return the sort within groups
   */
  public boolean getSortWithinGroups() {
    return mCheckByGroups.isSelected();
  }

  /**
   * Gets the sort asc.
   *
   * @return the sort asc
   */
  public boolean getSortAsc() {
    return mCheckAsc.isSelected();
  }
}
