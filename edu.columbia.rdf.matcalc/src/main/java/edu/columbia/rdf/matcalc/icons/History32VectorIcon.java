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
package edu.columbia.rdf.matcalc.icons;

import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.RightPane32VectorIcon;

/**
 * The class History32VectorIcon.
 */
public class History32VectorIcon extends RightPane32VectorIcon {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.icons.RightPane32VectorIcon#drawForeground(java.awt.
   * Graphics2D, java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
    super.drawIcon(g2, x, y, w, h, params);

    x = x + (w - WIDTH) / 2 + WIDTH - PANE_WIDTH + 2;
    y = y + (h - HEIGHT) / 2 + BAR_HEIGHT + 2;

    w = PANE_WIDTH - 5;

    g2.setColor(ModernWidget.LINE_COLOR);

    g2.drawLine(x, y, x + w, y);

    y += 4;

    g2.drawLine(x, y, x + w, y);

    y += 4;

    g2.drawLine(x, y, x + w, y);

    y += 4;

    g2.drawLine(x, y, x + w, y);

    y += 4;

    g2.drawLine(x, y, x + w, y);
  }
}
