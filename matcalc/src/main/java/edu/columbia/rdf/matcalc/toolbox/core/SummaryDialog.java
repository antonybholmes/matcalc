/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.matcalc.toolbox.core;

import javax.swing.Box;

import org.jebtk.core.Mathematics;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogTaskType;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernClipboardNumericalTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * User can enter an integer option value.
 * 
 * @author Antony Holmes
 *
 */
public class SummaryDialog extends ModernDialogTaskWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  private MainMatCalcWindow mWindow;

  /**
   * Instantiates a new modern int input dialog.
   *
   * @param parent  the parent
   * @param checked the checked
   */
  public SummaryDialog(MainMatCalcWindow parent) {
    super(parent, false, ModernDialogTaskType.CLOSE);

    setTitle("Summary");

    mWindow = parent;

    setup();

    createUi();
  }

  /**
   * Setup.
   */
  private void setup() {
    setSize(400, 300);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    DataFrame m = mWindow.getCurrentMatrix();

    Box box = VBox.create();

    box.add(new HExpandBox("Min", new ModernTextBorderPanel(
        new ModernClipboardNumericalTextField(Mathematics.round(MatrixOperations.min(m), 4), false), 120)));
    box.add(UI.createVGap(ModernWidget.PADDING));
    box.add(new HExpandBox("Max", new ModernTextBorderPanel(
        new ModernClipboardNumericalTextField(Mathematics.round(MatrixOperations.max(m), 4), false), 120)));
    box.add(UI.createVGap(ModernWidget.PADDING));
    box.add(new HExpandBox("Sum", new ModernTextBorderPanel(
        new ModernClipboardNumericalTextField(Mathematics.round(MatrixOperations.sum(m), 4), false), 120)));
    box.add(UI.createVGap(ModernWidget.PADDING));
    box.add(new HExpandBox("Mean", new ModernTextBorderPanel(
        new ModernClipboardNumericalTextField(Mathematics.round(MatrixOperations.mean(m), 4), false), 120)));
    box.add(UI.createVGap(ModernWidget.PADDING));
    box.add(new HExpandBox("Median", new ModernTextBorderPanel(
        new ModernClipboardNumericalTextField(Mathematics.round(MatrixOperations.median(m), 4), false), 120)));

    setCard(box);
  }
}
