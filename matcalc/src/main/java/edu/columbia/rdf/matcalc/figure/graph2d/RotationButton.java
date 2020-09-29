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
package edu.columbia.rdf.matcalc.figure.graph2d;

import org.jebtk.core.Mathematics;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogFlatDropDownButton;

/**
 * The class RotationButton.
 */
public class RotationButton extends ModernDialogFlatDropDownButton {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /*
   * private class ClickEvents implements ModernClickListener {
   * 
   * @Override public void clicked(ModernClickEvent e) { if
   * (e.getMessage().equals("270")) { ((RotationMultiIcon16)mIcon).setIcon(3); }
   * else if (e.getMessage().equals("180")) {
   * ((RotationMultiIcon16)mIcon).setIcon(2); } else if
   * (e.getMessage().equals("90")) { ((RotationMultiIcon16)mIcon).setIcon(1); }
   * else { ((RotationMultiIcon16)mIcon).setIcon(0); }
   * 
   * repaint(); } }
   */

  /**
   * Instantiates a new rotation button.
   *
   * @param style the style
   */
  public RotationButton(double style) {
    super("0", new RotationMenu());

    // addClickListener(new ClickEvents());

    setStyle(style);

    UI.setSize(this, ModernWidget.SIZE_48);
  }

  /**
   * Sets the style.
   *
   * @param angle the new style
   */
  public void setStyle(double angle) {
    if (angle == Mathematics.HALF_PI) {
      setSelectedIndex(1);
    } else {
      setSelectedIndex(0);
    }
  }

  /**
   * Gets the rotation.
   *
   * @return the rotation
   */
  public double getRotation() {
    if (getText().equals("90")) {
      return Mathematics.HALF_PI;
    } else {
      return 0;
    }
  }
}
