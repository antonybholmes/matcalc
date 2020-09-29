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

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernDialogTaskType;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class LogDialog.
 */
public class LogDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member radio none.
   */
  private ModernRadioButton mRadioNone = new ModernRadioButton("None");

  /**
   * The member radio min.
   */
  private ModernRadioButton mRadioMin = new ModernRadioButton("Minimum");

  /**
   * The member radio add.
   */
  private ModernRadioButton mRadioAdd = new ModernRadioButton("Add");

  /** The m radio base. */
  private ModernRadioButton mRadioBase = new ModernRadioButton("Base", true);

  /** The m radio natural. */
  private ModernRadioButton mRadioNatural = new ModernRadioButton("Natural");

  /** The m base field. */
  private ModernCompactSpinner mBaseField = new ModernCompactSpinner(1, 100, 1);

  /**
   * The member min field.
   */
  private ModernCompactSpinner mMinField = new ModernCompactSpinner(1, 100, 1);

  /**
   * The member add field.
   */
  private ModernCompactSpinner mAddField = new ModernCompactSpinner(1, 100, 1);

  /**
   * Instantiates a new log dialog.
   *
   * @param parent  the parent
   * @param min     the min
   * @param base    the base
   * @param natural the natural
   */
  public LogDialog(ModernWindow parent, double min, int base, boolean natural) {
    super(parent, "matcalc.modules.math.log.url", ModernDialogTaskType.OK_CANCEL);

    setTitle("Logarithm Options");

    setup(min, base, natural);

    createUi();
  }

  /**
   * Sets the up.
   *
   * @param min     the new up
   * @param base    the base
   * @param natural the natural
   */
  private void setup(double min, int base, boolean natural) {

    mMinField.setValue(min);

    new ModernButtonGroup(mRadioBase, mRadioNatural);

    new ModernButtonGroup(mRadioAdd, mRadioMin, mRadioNone);

    mRadioNone.doClick();

    if (natural) {
      mRadioNatural.doClick();
    } else {
      mRadioBase.doClick();
      mBaseField.setValue(base);
    }

    setSize(420, 380);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    Box box = VBox.create();

    sectionHeader("Base", box);

    box.add(new HExpandBox(mRadioBase, mBaseField));
    box.add(UI.createVGap(5));
    box.add(mRadioNatural);

    midSectionHeader("Adjustments", box);

    box.add(mRadioNone);
    box.add(UI.createVGap(5));
    box.add(new HExpandBox(mRadioAdd, mAddField));
    box.add(UI.createVGap(5));
    box.add(new HExpandBox(mRadioMin, mMinField));

    // JPanel buttonPanel = new Panel(new FlowLayout(FlowLayout.LEFT));

    // importButton.setCanvasSize(new Dimension(100,
    // ModernTheme.getInstance().getClass("widget").getInt("height")));
    // exportButton.setCanvasSize(new Dimension(100,
    // ModernTheme.getInstance().getClass("widget").getInt("height")));

    // buttonPanel.add(importButton);
    // buttonPanel.add(exportButton);

    // panel.add(buttonPanel, BorderLayout.PAGE_END);

    setCard(box);
  }

  /**
   * Gets the min amount.
   *
   * @return the min amount
   */
  public double getMinAmount() {
    return mMinField.getValue();
  }

  /**
   * Gets the adds the amount.
   *
   * @return the adds the amount
   */
  public double getAddAmount() {
    return mAddField.getValue();
  }

  /**
   * Gets the do nothing.
   *
   * @return the do nothing
   */
  public boolean getDoNothing() {
    return mRadioNone.isSelected();
  }

  /**
   * Gets the adds the.
   *
   * @return the adds the
   */
  public boolean getAdd() {
    return mRadioAdd.isSelected();
  }

  /**
   * Gets the min.
   *
   * @return the min
   */
  public boolean getMin() {
    return mRadioMin.isSelected();
  }

  /**
   * Gets the natural.
   *
   * @return the natural
   */
  public boolean getNatural() {
    return mRadioNatural.isSelected();
  }

  /**
   * Gets the base.
   *
   * @return the base
   */
  public int getBase() {
    return mBaseField.getIntValue();
  }
}
