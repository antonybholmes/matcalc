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

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;
import org.jebtk.modern.theme.ThemeService;

/**
 * Simple pane icon.
 * 
 * @author Antony Holmes
 *
 */
public class TTest32VectorIcon extends ModernVectorIcon {

  /**
   * The constant WIDTH.
   */
  protected static final int WIDTH = 6;

  /**
   * The member bar color.
   */
  protected Color mBarColor;

  /**
   * Instantiates a new t test32 vector icon.
   */
  public TTest32VectorIcon() {
    this(ThemeService.getInstance().getColors().getTheme(4));
  }

  /**
   * Instantiates a new t test32 vector icon.
   *
   * @param barColor the bar color
   */
  public TTest32VectorIcon(Color barColor) {
    mBarColor = barColor;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
    x = x + 4;
    y = y + 4;

    int i = 0;

    for (int r = 0; r < 4; ++r) {
      x = x + 4;

      for (int c = 0; c < 4; ++c) {
        if (i % 2 == 0) {
          g2.setColor(mBarColor);
        } else {
          g2.setColor(Color.WHITE);
        }

        g2.fillRect(x, y, WIDTH, WIDTH);

        x += WIDTH;

        ++i;
      }

      ++i;

      y += WIDTH;
    }

    g2.setColor(ModernWidget.LINE_COLOR);

    g2.drawRect(x + 4, y + 4, 24, 24);
  }
}
