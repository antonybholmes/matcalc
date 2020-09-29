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
package edu.columbia.rdf.matcalc.toolbox.math;

import javax.swing.Box;

import org.jebtk.core.Mathematics;
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
public class StatsDialog extends ModernDialogTaskWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  private double mValue;

  /**
   * Instantiates a new modern int input dialog.
   *
   * @param parent  the parent
   * @param checked the checked
   */
  public StatsDialog(MainMatCalcWindow parent, String name, double value) {
    super(parent, ModernDialogTaskType.CLOSE);

    setTitle(name);

    mValue = value;
    setup();

    createUi();
  }

  /**
   * Setup.
   */
  private void setup() {
    setSize(400, 180);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    Box box = VBox.create();

    box.add(new HExpandBox(getTitle(),
        new ModernTextBorderPanel(new ModernClipboardNumericalTextField(Mathematics.round(mValue, 4), false), 120)));

    setCard(box);
  }
}
