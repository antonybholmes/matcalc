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
import org.jebtk.modern.graphics.icons.ModernVectorIcon;
import org.jebtk.modern.theme.ThemeService;

/**
 * The class VolcanoPlot32VectorIcon.
 */
public class VolcanoPlot32VectorIcon extends ModernVectorIcon {

  /**
   * The constant WIDTH.
   */
  private static final int WIDTH = 28;

  /**
   * The constant HALF_WIDTH.
   */
  private static final int HALF_WIDTH = WIDTH / 2;

  /**
   * The constant SIZE.
   */
  private static final int SIZE = 4;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
    x = x + WIDTH / 2;
    y = y + WIDTH / 2;

    g2.setColor(ThemeService.getInstance().getColors().getTheme(3));

    for (double i = Math.PI / 2; i >= 0; i -= 0.3) {
      int x1 = (int) (x - HALF_WIDTH + Math.sin(i) * HALF_WIDTH);
      int y1 = (int) (y + WIDTH / 2 - SIZE - Math.cos(i) * HALF_WIDTH * 1.5);

      g2.setColor(ThemeService.getInstance().getColors().getTheme(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getTheme(4));
      g2.drawOval(x1, y1, SIZE, SIZE);

      x1 = (int) (x + HALF_WIDTH - Math.sin(i) * HALF_WIDTH);

      g2.setColor(ThemeService.getInstance().getColors().getTheme(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getTheme(4));
      g2.drawOval(x1, y1, SIZE, SIZE);
    }

    for (double i = Math.PI / 2; i >= 0; i -= 0.3) {
      int x1 = (int) (x - HALF_WIDTH + Math.sin(i) * HALF_WIDTH);
      int y1 = (int) (y + WIDTH / 2 - SIZE - Math.cos(i) * HALF_WIDTH);

      g2.setColor(ThemeService.getInstance().getColors().getGray(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getGray(5));
      g2.drawOval(x1, y1, SIZE, SIZE);

      x1 = (int) (x + HALF_WIDTH - Math.sin(i) * HALF_WIDTH);

      g2.setColor(ThemeService.getInstance().getColors().getGray(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getGray(5));
      g2.drawOval(x1, y1, SIZE, SIZE);

    }

    for (double i = Math.PI / 2; i >= 0.1; i -= 0.3) {
      int x1 = (int) (x - HALF_WIDTH + Math.sin(i) * HALF_WIDTH);
      int y1 = (int) (y + WIDTH / 2 - SIZE - Math.cos(i) * HALF_WIDTH * 0.7);

      g2.setColor(ThemeService.getInstance().getColors().getGray(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getGray(5));
      g2.drawOval(x1, y1, SIZE, SIZE);

      x1 = (int) (x + HALF_WIDTH - Math.sin(i) * HALF_WIDTH);

      g2.setColor(ThemeService.getInstance().getColors().getGray(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getGray(5));
      g2.drawOval(x1, y1, SIZE, SIZE);
    }

    for (double i = Math.PI / 2; i >= 0.5; i -= 0.3) {
      int x1 = (int) (x - HALF_WIDTH + Math.sin(i) * HALF_WIDTH);
      int y1 = (int) (y + WIDTH / 2 - SIZE - Math.cos(i) * HALF_WIDTH * 0.5);

      g2.setColor(ThemeService.getInstance().getColors().getGray(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getGray(5));
      g2.drawOval(x1, y1, SIZE, SIZE);

      x1 = (int) (x + HALF_WIDTH - Math.sin(i) * HALF_WIDTH);

      g2.setColor(ThemeService.getInstance().getColors().getGray(2));
      g2.fillOval(x1, y1, SIZE, SIZE);
      g2.setColor(ThemeService.getInstance().getColors().getGray(5));
      g2.drawOval(x1, y1, SIZE, SIZE);
    }

    /*
     * g2.setColor(Color.BLACK); g2.setStroke(ModernTheme.QUAD_LINE_STROKE);
     * 
     * g2.drawArc(x, y, WIDTH / 2, WIDTH, 0, 90); g2.drawArc(x + WIDTH / 2, y, WIDTH
     * / 2, WIDTH, 90, 90);
     */

    /*
     * g2.setColor(ThemeService.getInstance().getColors().getHighlight(4));
     * 
     * g2.fillOval(x, y + WIDTH / 2 - SIZE, SIZE, SIZE);
     * 
     * g2.fillOval(x - SIZE / 2, y + WIDTH / 2 - SIZE - SIZE / 2, SIZE, SIZE);
     * g2.fillOval(x + SIZE / 2, y + WIDTH / 2 - SIZE - SIZE / 2, SIZE, SIZE);
     * 
     * g2.fillOval(x - SIZE, y + WIDTH / 2 - 2 * SIZE, SIZE, SIZE); g2.fillOval(x -
     * SIZE / 2, y + WIDTH / 2 - 2 * SIZE, SIZE, SIZE); g2.fillOval(x + SIZE / 2, y
     * + WIDTH / 2 - 2 * SIZE, SIZE, SIZE); g2.fillOval(x + SIZE, y + WIDTH / 2 - 2
     * * SIZE, SIZE, SIZE);
     * 
     * 
     * g2.fillOval(x - SIZE - SIZE / 2, y + WIDTH / 2 - 2 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x - SIZE, y + WIDTH / 2 - 2 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x - SIZE / 2, y + WIDTH / 2 - 2 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x + SIZE / 2, y + WIDTH / 2 - 2 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x + SIZE, y + WIDTH / 2 - 2 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x + SIZE + SIZE / 2, y + WIDTH / 2 - 2 * SIZE - SIZE / 2,
     * SIZE, SIZE);
     * 
     * g2.setColor(ThemeService.getInstance().getThemeColor(3));
     * 
     * 
     * g2.fillOval(x - SIZE - SIZE / 2, y + WIDTH / 2 - 3 * SIZE, SIZE, SIZE);
     * g2.fillOval(x + SIZE, y + WIDTH / 2 - 3 * SIZE, SIZE, SIZE); g2.fillOval(x -
     * SIZE, y + WIDTH / 2 - 3 * SIZE, SIZE, SIZE); g2.fillOval(x + SIZE + SIZE / 2,
     * y + WIDTH / 2 - 3 * SIZE, SIZE, SIZE);
     * 
     * g2.fillOval(x - SIZE - SIZE / 2, y + WIDTH / 2 - 3 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x + SIZE, y + WIDTH / 2 - 3 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x - SIZE, y + WIDTH / 2 - 3 * SIZE - SIZE / 2, SIZE,
     * SIZE); g2.fillOval(x + SIZE + SIZE / 2, y + WIDTH / 2 - 3 * SIZE - SIZE / 2,
     * SIZE, SIZE);
     * 
     * g2.fillOval(x - 2 * SIZE, y + WIDTH / 2 - 5 * SIZE, SIZE, SIZE);
     * g2.fillOval(x + 2 * SIZE, y + WIDTH / 2 - 5 * SIZE, SIZE, SIZE);
     * 
     * g2.fillOval(x + SIZE / 2, y + WIDTH / 2 - 6 * SIZE, SIZE, SIZE);
     */
  }
}
