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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap;

import javax.swing.Box;

import org.jebtk.core.settings.SettingsService;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.VBoxAutoWidth;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardNumericalTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

/**
 * Allow users to choose whether to create a new figure or add to an existing
 * figure.
 * 
 * @author Antony Holmes
 *
 */
public class NormalizeDialog extends ModernDialogTaskWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member radio new.
   */
  private ModernRadioButton mRadioAuto = new ModernRadioButton("Auto");

  /**
   * The member radio existing.
   */
  private ModernRadioButton mRadioFixed = new ModernRadioButton("Fixed");

  private ModernTextField mTextAutoMin = new ModernClipboardNumericalTextField(-1);

  private ModernTextField mTextAutoMax = new ModernClipboardNumericalTextField(-1);

  private ModernTextField mTextMin = new ModernClipboardNumericalTextField(-1);

  private ModernTextField mTextMax = new ModernClipboardNumericalTextField(-1);

  /**
   * Instantiates a new figure dialog.
   *
   * @param parent the parent
   */
  public NormalizeDialog(ModernWindow parent) {
    super(parent);

    setTitle("Normalize");

    setup();

    createUi();

  }

  /**
   * Setup.
   */
  private void setup() {
    mTextMin.setText(SettingsService.getInstance().getDouble("org.matcalc.figure.heatmap.normalize.min", 0));
    mTextMax.setText(SettingsService.getInstance().getDouble("org.matcalc.figure.heatmap.normalize.max", 1));

    new ModernButtonGroup(mRadioFixed, mRadioAuto);

    if (SettingsService.getInstance().getBool("org.matcalc.figure.heatmap.normalize.mode.auto", true)) {
      mRadioAuto.doClick();
    } else {
      mRadioFixed.doClick();
    }

    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(480, 220);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    VBoxAutoWidth content = new VBoxAutoWidth();

    content.add(mRadioAuto);
    content.add(UI.createVGap(10));

    Box box = HBox.create();

    box.add(mRadioFixed);
    box.add(UI.createHGap(20));
    box.add(new ModernAutoSizeLabel("Min"));
    box.add(UI.createHGap(10));
    box.add(new ModernTextBorderPanel(mTextMin, 80));
    box.add(UI.createHGap(20));
    box.add(new ModernAutoSizeLabel("Max"));
    box.add(UI.createHGap(10));
    box.add(new ModernTextBorderPanel(mTextMax, 80));

    content.add(box);

    setCard(content);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {
      SettingsService.getInstance().set("org.matcalc.figure.heatmap.normalize.min", mTextMin.getDouble());

      SettingsService.getInstance().set("org.matcalc.figure.heatmap.normalize.max", mTextMax.getDouble());

      SettingsService.getInstance().set("org.matcalc.figure.heatmap.normalize.mode.auto", mRadioAuto.isSelected());
    }

    super.clicked(e);
  }

  public double getMin() {
    return mTextMin.getDouble();
  }

  public double getMax() {
    return mTextMax.getDouble();
  }

  public boolean getAuto() {
    return mRadioAuto.isSelected();
  }
}
