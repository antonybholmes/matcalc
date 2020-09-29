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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jebtk.modern.menu.ModernMenuItem;

/**
 * Menu item for displaying line styles.
 * 
 * @author Antony Holmes
 *
 */
public class DashedLineMenuItem extends ModernMenuItem {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member stroke.
   */
  private Stroke mStroke;

  /**
   * Construct a new menu item with a given name for a given line style.
   *
   * @param name   the name
   * @param stroke the stroke
   */
  public DashedLineMenuItem(String name, Stroke stroke) {
    super(name);

    mStroke = stroke;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.menu.ModernMenuItem#drawForegroundAA(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawForegroundAA(Graphics2D g2) {
    g2.setColor(Color.BLACK);
    g2.setStroke(mStroke);

    int y = mRect.getH() / 2;

    g2.drawLine(PADDING, y, getWidth() - PADDING, y);
  }
}
