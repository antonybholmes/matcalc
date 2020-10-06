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

import org.jebtk.core.Props;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.modern.graphics.icons.TableVectorIcon;

/**
 * The class ColorStandardizationMatrixIcon.
 */
public class ColorStandardizationMatrixIcon extends TableVectorIcon {

  /**
   * The member color2.
   */
  private Color mColor2;

  /**
   * Instantiates a new color standardization matrix icon.
   */
  public ColorStandardizationMatrixIcon() {
    super(SettingsService.getInstance().getColor("theme.icons.filter-rows-icon.colors.highlight-1"));

    mColor2 = SettingsService.getInstance().getColor("theme.icons.filter-rows-icon.colors.highlight-2");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.icons.TableVectorIcon#drawForeground(java.awt.
   * Graphics2D, java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
    super.drawIcon(g2, x, y, w, h, params);

    double wf = w * WIDTH_SCALE;
    double hf = wf * HEIGHT_SCALE - 1;

    double b = hf * 0.25;
    double xf = x + (w - wf) / 2.0;
    double yf = y + b + (h - hf) / 2.0;

    g2.setColor(mColor2);
    g2.fillRect((int) Math.round(xf) + 1, (int) Math.round(yf), (int) Math.round(wf) - 1, (int) Math.round(hf - b));
  }
}
