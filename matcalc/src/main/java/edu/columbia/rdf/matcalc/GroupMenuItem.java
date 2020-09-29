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
package edu.columbia.rdf.matcalc;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.menu.ModernIconMenuItem;

/**
 * The class GroupMenuItem.
 */
public class GroupMenuItem extends ModernIconMenuItem {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The group.
   */
  private MatrixGroup group;

  /**
   * Instantiates a new group menu item.
   *
   * @param group the group
   */
  public GroupMenuItem(MatrixGroup group) {
    super(group.getName());

    this.group = group;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.menu.ModernIconMenuItem#drawForegroundAA(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawForegroundAA(Graphics2D g2) {
    int x = PADDING;
    int y = (mRect.getH() - AssetService.ICON_SIZE_16) / 2;

    g2.setColor(group.getColor());

    g2.fillRect(x, y, AssetService.ICON_SIZE_16, AssetService.ICON_SIZE_16);

    Point p = getStringCenterPlotCoordinates(g2, getRect(), mText1);

    g2.setColor(getForeground());
    g2.drawString(mText1, AssetService.ICON_SIZE_32, p.y);
  }
}