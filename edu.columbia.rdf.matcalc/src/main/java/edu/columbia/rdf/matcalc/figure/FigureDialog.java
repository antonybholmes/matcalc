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
package edu.columbia.rdf.matcalc.figure;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

/**
 * Allow users to choose whether to create a new figure or add to an existing
 * figure.
 * 
 * @author Antony Holmes
 *
 */
public class FigureDialog extends ModernDialogTaskWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member radio new.
   */
  private ModernRadioButton mRadioNew = new ModernRadioButton("New");

  /**
   * The member radio existing.
   */
  private ModernRadioButton mRadioExisting = new ModernRadioButton("Existing");

  /**
   * The member fig combo.
   */
  private SubFigureCombo mFigCombo; // = new FigureCombo();

  /**
   * Instantiates a new figure dialog.
   *
   * @param parent the parent
   */
  public FigureDialog(ModernWindow parent) {
    super(parent);

    setTitle("Create Figure");

    setup();

    createUi();

  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(400, 240);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    Box content = VBox.create();

    content.add(mRadioNew);
    content.add(UI.createVGap(10));

    Box box = HBox.create();

    box.add(mRadioExisting);
    box.add(UI.createHGap(10));
    box.add(mFigCombo);

    content.add(box);

    setContent(content);

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mRadioNew);
    group.add(mRadioExisting);

    mRadioNew.setSelected(true);

    // mRadioExisting.setEnabled(SubFigureService.getInstance().getCount() > 0);
  }

  /**
   * Gets the adds the new figure.
   *
   * @return the adds the new figure
   */
  public boolean getAddNewFigure() {
    return mRadioNew.isSelected();
  }

  /**
   * Gets the figure name.
   *
   * @return the figure name
   */
  public String getFigureName() {
    return mFigCombo.getText();
  }
}
