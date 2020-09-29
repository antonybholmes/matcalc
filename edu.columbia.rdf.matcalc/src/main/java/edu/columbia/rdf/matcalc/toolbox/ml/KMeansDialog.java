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
package edu.columbia.rdf.matcalc.toolbox.ml;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

/**
 * The class TTestDialog.
 */
public class KMeansDialog extends ModernDialogHelpWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private ModernCompactSpinner mKmeansField = new ModernCompactSpinner(1, 100, 5, 1);

  /**
   * The member check is log2.
   */
  private ModernTwoStateWidget mCheckZscore = new ModernCheckSwitch("Z-score", true);

  /**
   * The member check log2.
   */
  private ModernTwoStateWidget mCheckSort = new ModernCheckSwitch("Sort by cluster", true);

  private ModernTwoStateWidget mCheckRename = new ModernCheckSwitch("Rename clusters");

  /**
   * Instantiates a new t test dialog.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param groups the groups
   */
  public KMeansDialog(ModernWindow parent) {
    super(parent, "matcalc.kmeans.help.url");

    setTitle("K-means");

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    createUi();

    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(480, 280);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    Box box = VBox.create();

    // sectionHeader("Data Transform", box);

    box.add(new HExpandBox("Number of clusters", mKmeansField));
    box.add(UI.createVGap(5));
    box.add(mCheckZscore);
    box.add(UI.createVGap(5));
    box.add(mCheckSort);
    box.add(UI.createVGap(5));
    box.add(mCheckRename);

    setCard(box);
  }

  public int getK() {
    return mKmeansField.getIntValue();
  }

  public boolean getZScore() {
    return mCheckZscore.isSelected();
  }

  public boolean getSort() {
    return mCheckSort.isSelected();
  }

  public boolean getRename() {
    return mCheckRename.isSelected();
  }
}
