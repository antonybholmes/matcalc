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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * Displays a color bar.
 * 
 * @author Antony Holmes
 *
 */
public class ColorIcon extends ModernVectorIcon {

  /**
   * The color.
   */
  private Color mColor;

  /**
   * Instantiates a new color icon.
   *
   * @param color the color
   */
  public ColorIcon(Color color) {
    mColor = color;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
    y = (h - h / 4) / 2;
    h = h / 4;

    g2.setColor(mColor);
    g2.fillRect(x, y, w, h);

    ModernWidget.paintDarkOutline(g2, new Rectangle(x, y, w, h));
  }
}