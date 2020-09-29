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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy;

import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.FormatPlotPane;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public class HeatMapPlotWindow extends HeatMapWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new heat map plot window.
   *
   * @param window      the window
   * @param matrix      the matrix
   * @param groups      the groups
   * @param rowGroups   the row groups
   * @param countGroups the count groups
   * @param history     the history
   * @param Props       the properties
   */
<<<<<<< HEAD:matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/plot/heatmap/legacy/HeatMapPlotWindow.java
  public HeatMapPlotWindow(ModernWindow window, DataFrame matrix, XYSeriesModel groups, XYSeriesModel rowGroups,
      CountGroups countGroups, List<String> history, Props properties) {
    this(window, "Heat Map", matrix, groups, rowGroups, countGroups, history, properties);
=======
  public HeatMapPlotWindow(ModernWindow window, DataFrame matrix,
      XYSeriesModel groups, XYSeriesModel rowGroups, CountGroups countGroups,
      List<String> history, Props properties) {
    this(window, "Heat Map", matrix, groups, rowGroups, countGroups, history,
        properties);
>>>>>>> 0c6b302bf0a6d2eac18dbec9d0d2195af92c4605:edu.columbia.rdf.matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/plot/heatmap/legacy/HeatMapPlotWindow.java
  }

  /**
   * Instantiates a new heat map plot window.
   *
   * @param window      the window
   * @param name        the name
   * @param matrix      the matrix
   * @param groups      the groups
   * @param rowGroups   the row groups
   * @param countGroups the count groups
   * @param history     the history
   * @param Props       the properties
   */
<<<<<<< HEAD:matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/plot/heatmap/legacy/HeatMapPlotWindow.java
  public HeatMapPlotWindow(ModernWindow window, String name, DataFrame matrix, XYSeriesModel groups,
      XYSeriesModel rowGroups, CountGroups countGroups, List<String> history, Props properties) {
=======
  public HeatMapPlotWindow(ModernWindow window, String name, DataFrame matrix,
      XYSeriesModel groups, XYSeriesModel rowGroups, CountGroups countGroups,
      List<String> history, Props properties) {
>>>>>>> 0c6b302bf0a6d2eac18dbec9d0d2195af92c4605:edu.columbia.rdf.matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/plot/heatmap/legacy/HeatMapPlotWindow.java
    super(window, matrix, groups, rowGroups, countGroups, history, properties);

    setFormatPane(createFormatPane());
  }

  /**
   * Creates the format pane.
   *
   * @return the format plot pane
   */
  public FormatPlotPane createFormatPane() {
    return new HeatMapPanel(this, mMatrix, mGroups, mRowGroups, mCountGroups, mHistory, mZoomModel, mColorMapModel,
        mColorModel, mScaleModel, tabsPane().tabs(), mProps);
  }
}
