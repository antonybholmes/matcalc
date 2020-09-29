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

import org.jebtk.graphplot.icons.ShapeStyle;
import org.jebtk.graphplot.icons.ShapeStyleMultiIcon16;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogFlatDropDownIconButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;

/**
 * The class ShapeStyleButton.
 */
public class ShapeStyleButton extends ModernDialogFlatDropDownIconButton {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The class ClickEvents.
   */
  private class ClickEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
     * event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      if (e.getMessage().equals("Bar")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(0);
      } else if (e.getMessage().equals("Circle")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(1);
      } else if (e.getMessage().equals("Cross")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(2);
      } else if (e.getMessage().equals("Diamond")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(3);
      } else if (e.getMessage().equals("Minus")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(4);
      } else if (e.getMessage().equals("Plus")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(5);
      } else if (e.getMessage().equals("Square")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(6);
      } else if (e.getMessage().equals("Triangle")) {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(7);
      } else {
        ((ShapeStyleMultiIcon16) mIcon).setIcon(1);
      }

      repaint();
    }
  }

  /**
   * Instantiates a new shape style button.
   *
   * @param style the style
   */
  public ShapeStyleButton(ShapeStyle style) {
    super(new ShapeStyleMultiIcon16(), new ShapeStyleMenu());

    addClickListener(new ClickEvents());

    setType(style);

    UI.setSize(this, ModernWidget.SIZE_48);
  }

  /**
   * Sets the style.
   *
   * @param style the new style
   */
  public void setType(ShapeStyle style) {
    switch (style) {
    case BAR:
      setSelectedIndex(0);
      break;
    case CIRCLE:
      setSelectedIndex(1);
      break;
    case CROSS:
      setSelectedIndex(2);
      break;
    case DIAMOND:
      setSelectedIndex(3);
      break;
    case MINUS:
      setSelectedIndex(4);
      break;
    case PLUS:
      setSelectedIndex(5);
      break;
    case SQUARE:
      setSelectedIndex(6);
      break;
    case TRIANGLE:
      setSelectedIndex(7);
      break;
    default:
      setSelectedIndex(1);
      break;
    }
  }

  /**
   * Change type.
   *
   * @param style the style
   */
  public void changeType(ShapeStyle style) {

    switch (style) {
    case BAR:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(0);
      break;
    case CIRCLE:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(1);
      break;
    case CROSS:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(2);
      break;
    case DIAMOND:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(3);
      break;
    case MINUS:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(4);
      break;
    case PLUS:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(5);
      break;
    case SQUARE:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(6);
      break;
    case TRIANGLE:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(7);
      break;
    default:
      ((ShapeStyleMultiIcon16) mIcon).setIcon(1);
      break;
    }

    repaint();
  }
}
