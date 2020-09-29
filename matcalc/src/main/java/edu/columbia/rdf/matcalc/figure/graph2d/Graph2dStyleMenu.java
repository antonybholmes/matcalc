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

import java.awt.Dimension;

import org.jebtk.graphplot.icons.BarsStyleIcon;
import org.jebtk.graphplot.icons.HeatMap32VectorIcon;
import org.jebtk.graphplot.icons.JoinedFilledTransStyleIcon;
import org.jebtk.graphplot.icons.JoinedSmoothFilledStyleIcon;
import org.jebtk.graphplot.icons.JoinedSmoothStyleIcon;
import org.jebtk.graphplot.icons.JoinedStyleIcon;
import org.jebtk.graphplot.icons.LinesStyleIcon;
import org.jebtk.graphplot.icons.ScatterStyleIcon;
import org.jebtk.modern.UI;
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernPopupMenu2;

/**
 * The class Graph2dStyleMenu.
 */
public class Graph2dStyleMenu extends ModernPopupMenu2 {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant MENU_SIZE.
   */
  private static final Dimension MENU_SIZE = new Dimension(200, 40);

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

      UI.setSize(this, MENU_SIZE);
    }

  }

  /**
   * Instantiates a new graph2d style menu.
   */
  public Graph2dStyleMenu() {
    addMenuItem(new PeakStyleMenuItem("Joined", new Raster32Icon(new JoinedStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Joined Smooth", new Raster32Icon(new JoinedSmoothStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Filled", new Raster32Icon(new JoinedFilledTransStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Filled Smooth", new Raster32Icon(new JoinedSmoothFilledStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Lines", new Raster32Icon(new LinesStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Bars", new Raster32Icon(new BarsStyleIcon())));
    addMenuItem(new PeakStyleMenuItem("Scatter", new Raster32Icon(new ScatterStyleIcon())));

    // TODO: replace icon
    addMenuItem(new PeakStyleMenuItem("Heat Map", new Raster32Icon(new HeatMap32VectorIcon())));
  }
}
