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
import java.awt.geom.GeneralPath;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.Props;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * Help icon.
 * 
 * @author Antony Holmes
 *
 */
public class GraphVectorIcon extends ModernVectorIcon {

  /**
   * The constant SCALE.
   */
  private static final double SCALE = 0.9;

  /**
   * The constant OFFSET.
   */
  private static final double OFFSET = 0.1;

  /**
   * The constant COLOR1.
   */
  private static final Color COLOR1 = ColorUtils.decodeHtmlColor("#0060ff22");

  /**
   * The constant COLOR2.
   */
  private static final Color COLOR2 = ColorUtils.decodeHtmlColor("#0060ff");

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override

  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
    double wf = w * SCALE;
    double o = w * OFFSET;
    double xf = x + (w - wf) / 2.0;
    double yf = y + (h - wf) / 2.0;

    g2.setColor(Color.BLACK);

    g2.drawLine((int) Math.round(xf), (int) Math.round(yf + wf - o), (int) Math.round(xf + wf),
        (int) Math.round(yf + wf - o));
    g2.drawLine((int) Math.round(xf + o), (int) Math.round(yf), (int) Math.round(xf + o), (int) Math.round(yf + wf));

    g2.setColor(COLOR1);

    GeneralPath gp = new GeneralPath();

    gp.moveTo(xf + o, yf + wf - o);
    gp.lineTo(xf + wf / 4 + o, yf + wf - wf / 3 - o);
    gp.lineTo(xf + wf / 2, y + wf - wf / 8 - o);
    gp.lineTo(xf + wf, yf);
    gp.lineTo(xf + wf, yf + wf - o);
    gp.closePath();

    g2.fill(gp);

    g2.setColor(COLOR2);

    gp = new GeneralPath();

    gp.moveTo(xf + o, yf + wf - o);
    gp.lineTo(xf + wf / 4 + o, yf + wf - wf / 3 - o);
    gp.lineTo(xf + wf / 2, yf + wf - wf / 8 - o);
    gp.lineTo(xf + wf, y);
    // gp.lineTo(x + w, y + w - o);
    // gp.closePath();

    g2.draw(gp);
  }
}
