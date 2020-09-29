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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

import javax.swing.Box;

import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;

/**
 * Allow users to optionally enable a filter etc.
 * 
 * @author Antony Holmes
 *
 */
public class OptionalEntry extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check box.
   */
  private ModernTwoStateWidget mCheckBox;

  /**
   * The member text.
   */
  private ModernCompactSpinner mText;

  /**
   * The class FocusEvents.
   */
  private class FocusEvents implements FocusListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    @Override
    public void focusGained(FocusEvent e) {
      mCheckBox.setSelected(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    @Override
    public void focusLost(FocusEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * Instantiates a new optional entry.
   *
   * @param name    the name
   * @param value   the value
   * @param checked the checked
   */
  public OptionalEntry(String name, double value, boolean checked) {
    mCheckBox = new ModernCheckSwitch(name, checked);

    add(mCheckBox);

    add(Box.createHorizontalGlue());

    mText = new ModernCompactSpinner(value);
    add(mText);

    mText.addFocusListener(new FocusEvents());

  }

  /**
   * Gets the selected.
   *
   * @return the selected
   */
  public boolean getSelected() {
    return mCheckBox.isSelected();
  }

  /**
   * Gets the value.
   *
   * @return the value
   * @throws ParseException the parse exception
   */
  public double getValue() throws ParseException {
    return mText.getValue();
  }

  /**
   * Gets the int value.
   *
   * @return the int value
   * @throws ParseException the parse exception
   */
  public int getIntValue() throws ParseException {
    return mText.getIntValue();
  }

}
