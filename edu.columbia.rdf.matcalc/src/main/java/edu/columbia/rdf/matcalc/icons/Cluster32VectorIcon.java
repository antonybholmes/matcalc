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
public class Cluster32VectorIcon extends ModernVectorIcon {

  /**
   * The constant WIDTH.
   */
  protected static final int WIDTH = 6;

  /**
   * The bar color.
   */
  protected Color barColor;

  /**
   * The constant LINE_COLOR.
   */
  protected static final Color LINE_COLOR = ModernWidget.DARK_LINE_COLOR;

  /**
   * Instantiates a new cluster32 vector icon.
   */
  public Cluster32VectorIcon() {
    this(ThemeService.getInstance().getColors().getTheme(4));
  }

  /**
   * Instantiates a new cluster32 vector icon.
   *
   * @param barColor the bar color
   */
  public Cluster32VectorIcon(Color barColor) {
    this.barColor = barColor;
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
    x = x + 2;

    g2.setColor(LINE_COLOR);

    h = y + 24;

    g2.drawLine(x, h, x, h - 10);
    g2.drawLine(x + 5, h, x + 5, h - 10);
    g2.drawLine(x, h - 10, x + 5, h - 10);

    g2.drawLine(x + 20, h, x + 20, h - 5);
    g2.drawLine(x + 25, h, x + 25, h - 5);
    g2.drawLine(x + 20, h - 5, x + 25, h - 5);

    g2.drawLine(x + 22, h - 5, x + 22, h - 10);

    g2.drawLine(x + 2, h - 10, x + 2, h - 15);

    g2.drawLine(x + 2, h - 15, x + 17, h - 15);

    g2.setColor(barColor);

    g2.drawLine(x + 17, h - 10, x + 17, h - 15);
    g2.drawLine(x + 12, h - 10, x + 22, h - 10);
    g2.drawLine(x + 12, h - 5, x + 12, h - 10);

    g2.drawLine(x + 10, h, x + 10, h - 5);
    g2.drawLine(x + 15, h, x + 15, h - 5);
    g2.drawLine(x + 10, h - 5, x + 15, h - 5);
  }
}
