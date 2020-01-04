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

import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;

/**
 * The Class GridLocationCombo.
 */
public class LegendPositionCombo extends ModernComboBox {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new legend position combo.
   */
  public LegendPositionCombo() {
    addScrollMenuItem("NW");
    addScrollMenuItem("N");
    addScrollMenuItem("NE");
    addScrollMenuItem("W");
    addScrollMenuItem("Center");
    addScrollMenuItem("E");
    addScrollMenuItem("SW");
    addScrollMenuItem("S");
    addScrollMenuItem("SE");

    UI.setSize(this, ModernWidget.BIG_SIZE);
  }

  /**
   * Sets the position.
   *
   * @param p the new position
   */
  public void setPosition(GridLocation p) {
    // Change the position, but do not trigger click event
    switch (p) {
    case NW:
      changeSelectedIndex(0);
      break;
    case N:
      changeSelectedIndex(1);
      break;
    case NE:
      changeSelectedIndex(2);
      break;
    case W:
      changeSelectedIndex(3);
      break;
    case E:
      changeSelectedIndex(5);
      break;
    case SW:
      changeSelectedIndex(6);
      break;
    case S:
      changeSelectedIndex(7);
      break;
    case SE:
      changeSelectedIndex(8);
      break;
    default:
      // Center
      changeSelectedIndex(4);
      break;
    }
  }

  /**
   * Gets the position.
   *
   * @return the position
   */
  public GridLocation getPosition() {
    switch (getSelectedIndex()) {
    case 0:
      return GridLocation.NW;
    case 1:
      return GridLocation.N;
    case 2:
      return GridLocation.NE;
    case 3:
      return GridLocation.W;
    case 5:
      return GridLocation.E;
    case 6:
      return GridLocation.SW;
    case 7:
      return GridLocation.S;
    case 8:
      return GridLocation.SE;
    default:
      return GridLocation.CENTER;
    }
  }
}
