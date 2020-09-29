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

import org.jebtk.graphplot.icons.FillPatternBackIcon;
import org.jebtk.graphplot.icons.FillPatternCrossIcon;
import org.jebtk.graphplot.icons.FillPatternForwardIcon;
import org.jebtk.graphplot.icons.FillPatternHozIcon;
import org.jebtk.graphplot.icons.FillPatternSolidIcon;
import org.jebtk.graphplot.icons.FillPatternVertIcon;
import org.jebtk.modern.graphics.icons.Raster16Icon;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernPopupMenu;

/**
 * The class FillPatternMenu.
 */
public class FillPatternMenu extends ModernPopupMenu {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new fill pattern menu.
   */
  public FillPatternMenu() {
    addMenuItem(new ModernIconMenuItem("Solid", new Raster16Icon(new FillPatternSolidIcon())));
    addMenuItem(new ModernIconMenuItem("Cross", new Raster16Icon(new FillPatternCrossIcon())));
    addMenuItem(new ModernIconMenuItem("Back", new Raster16Icon(new FillPatternBackIcon())));
    addMenuItem(new ModernIconMenuItem("Forward", new Raster16Icon(new FillPatternForwardIcon())));
    addMenuItem(new ModernIconMenuItem("Vert", new Raster16Icon(new FillPatternVertIcon())));
    addMenuItem(new ModernIconMenuItem("Hoz", new Raster16Icon(new FillPatternHozIcon())));
  }
}
