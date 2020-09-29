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

import org.jebtk.graphplot.figure.props.FillPattern;
import org.jebtk.graphplot.icons.FillPatternMultiIcon16;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogFlatDropDownIconButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;

/**
 * The class FillPatternButton.
 */
public class FillPatternButton extends ModernDialogFlatDropDownIconButton {

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
      if (e.getMessage().equals("Cross")) {
        ((FillPatternMultiIcon16) mIcon).setIcon(1);
      } else if (e.getMessage().equals("Back")) {
        ((FillPatternMultiIcon16) mIcon).setIcon(2);
      } else if (e.getMessage().equals("Forward")) {
        ((FillPatternMultiIcon16) mIcon).setIcon(3);
      } else if (e.getMessage().equals("Vert")) {
        ((FillPatternMultiIcon16) mIcon).setIcon(4);
      } else if (e.getMessage().equals("Hoz")) {
        ((FillPatternMultiIcon16) mIcon).setIcon(5);
      } else {
        ((FillPatternMultiIcon16) mIcon).setIcon(0);
      }

      repaint();
    }
  }

  /**
   * Instantiates a new fill pattern button.
   *
   * @param style the style
   */
  public FillPatternButton(FillPattern style) {
    super(new FillPatternMultiIcon16(), new FillPatternMenu());

    addClickListener(new ClickEvents());

    setStyle(style);

    UI.setSize(this, ModernWidget.SIZE_48);
  }

  /**
   * Sets the style.
   *
   * @param style the new style
   */
  public void setStyle(FillPattern style) {
    switch (style) {
    case CROSS_HATCH:
      setSelectedIndex(1);
      break;
    case BACK_HATCH:
      setSelectedIndex(2);
      break;
    case FORWARD_HATCH:
      setSelectedIndex(3);
      break;
    case VERT_HATCH:
      setSelectedIndex(4);
      break;
    case HOZ_HATCH:
      setSelectedIndex(5);
      break;
    default:
      setSelectedIndex(0);
      break;
    }
  }
}
