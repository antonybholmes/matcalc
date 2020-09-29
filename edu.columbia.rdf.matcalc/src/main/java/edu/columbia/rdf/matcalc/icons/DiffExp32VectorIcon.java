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
import org.jebtk.graphplot.icons.HeatMap32VectorIcon;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * Simple pane icon.
 * 
 * @author Antony Holmes
 *
 */
public class DiffExp32VectorIcon extends ModernVectorIcon {

  /**
   * The constant WIDTH.
   */
  protected static final int WIDTH = 12;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override
<<<<<<< HEAD:matcalc/src/main/java/edu/columbia/rdf/matcalc/icons/DiffExp32VectorIcon.java
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
=======
  public void drawIcon(Graphics2D g2,
      int x,
      int y,
      int w,
      int h,
      Props props) {
>>>>>>> 0c6b302bf0a6d2eac18dbec9d0d2195af92c4605:edu.columbia.rdf.matcalc/src/main/java/edu/columbia/rdf/matcalc/icons/DiffExp32VectorIcon.java
    int x1 = x + 4;
    int y1 = y + 4;

    g2.setColor(ModernWidget.BACKGROUND_COLOR);

    g2.fillRect(x1 - 2, y1 - 2, 28, 28);

    g2.setColor(ModernWidget.LINE_COLOR);

    g2.drawRect(x1 - 2, y1 - 2, 27, 27);

    int i = 0;

    for (int r = 0; r < 2; ++r) {
      x1 = x + 4;

      for (int c = 0; c < 2; ++c) {
        if (i % 2 == 0) {
          g2.setColor(HeatMap32VectorIcon.COLOR_1);
        } else {
          g2.setColor(HeatMap32VectorIcon.COLOR_2);
        }

        // g2.setColor(COLOR_2);

        g2.fillRect(x1, y1, WIDTH, WIDTH);

        x1 += WIDTH;

        ++i;
      }

      ++i;

      y1 += WIDTH;
    }

  }
}
