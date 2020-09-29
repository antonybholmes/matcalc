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
package edu.columbia.rdf.matcalc.figure.graph2d;

import org.jebtk.graphplot.icons.ShapeBarStyleIcon;
import org.jebtk.graphplot.icons.ShapeCircleStyleIcon;
import org.jebtk.graphplot.icons.ShapeCrossStyleIcon;
import org.jebtk.graphplot.icons.ShapeDiamondStyleIcon;
import org.jebtk.graphplot.icons.ShapeMinusStyleIcon;
import org.jebtk.graphplot.icons.ShapePlusStyleIcon;
import org.jebtk.graphplot.icons.ShapeSquareStyleIcon;
import org.jebtk.graphplot.icons.ShapeTriangleStyleIcon;
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.graphics.icons.Raster16Icon;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernPopupMenu;

/**
 * The class ShapeStyleMenu.
 */
public class ShapeStyleMenu extends ModernPopupMenu {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The class PeakStyleMenuItem.
   */
  private class PeakStyleMenuItem extends ModernIconMenuItem {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new peak style menu item.
     *
     * @param text the text
     * @param icon the icon
     */
    public PeakStyleMenuItem(String text, ModernIcon icon) {
      super(text, icon);

      // Ui.setSize(this, MENU_SIZE);
    }
  }

  /**
   * Instantiates a new shape style menu.
   */
  public ShapeStyleMenu() {
    addMenuItem(new PeakStyleMenuItem("Bar", new Raster16Icon(new ShapeBarStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Circle", new Raster16Icon(new ShapeCircleStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Cross", new Raster16Icon(new ShapeCrossStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Diamond", new Raster16Icon(new ShapeDiamondStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Minus", new Raster16Icon(new ShapeMinusStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Plus", new Raster16Icon(new ShapePlusStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Square", new Raster16Icon(new ShapeSquareStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Triangle", new Raster16Icon(new ShapeTriangleStyleIcon())));
  }
}
