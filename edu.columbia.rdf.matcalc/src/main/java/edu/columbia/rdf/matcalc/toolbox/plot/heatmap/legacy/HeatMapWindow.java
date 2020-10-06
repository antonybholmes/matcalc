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
import org.jebtk.graphplot.figure.heatmap.ColorNormalization;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.contentpane.CloseableHTab;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.FigureWindow;
import edu.columbia.rdf.matcalc.figure.FormatPlotPane;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public abstract class HeatMapWindow extends FigureWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member all groups.
   */
  protected XYSeriesModel mGroups = null;

  /**
   * The member properties.
   */
  protected Props mProps; // = new HeatMapProperties();

  /** The m history. */
  protected List<String> mHistory;

  /** The m matrix. */
  protected DataFrame mMatrix;

  /** The m count groups. */
  protected CountGroups mCountGroups;

  /** The m row groups. */
  protected XYSeriesModel mRowGroups;

  /**
   * Instantiates a new heat map window.
   *
   * @param window      the window
   * @param matrix      the matrix
   * @param groups      the all groups
   * @param rowGroups   the row groups
   * @param countGroups the count groups
   * @param history     the history
   * @param Props       the properties
   */
  public HeatMapWindow(ModernWindow window, DataFrame matrix, XYSeriesModel groups, XYSeriesModel rowGroups,
      CountGroups countGroups, List<String> history, Props properties) {
    super(window, null);

    setMatrix(matrix);
    mGroups = groups;
    mRowGroups = rowGroups;
    mCountGroups = countGroups;
    mHistory = history;
    mProps = properties;

    mColorModel.set(ColorNormalization.ZSCORE_ROW);
  }

  /**
   * Sets the matrix.
   *
   * @param matrix the new matrix
   */
  public void setMatrix(DataFrame matrix) {
    mMatrix = matrix;
  }

  /**
   * Sets the format pane.
   *
   * @param formatPane the new format pane
   */
  public void setFormatPane(FormatPlotPane formatPane) {
    mFormatPane = formatPane;

    tabsPane().tabs().right().clear();
    tabsPane().tabs().right().add("Format", new CloseableHTab("Format", mFormatPane, tabsPane()), 300, 200, 500);

    mFormatPane.update();
  }

  /**
   * Gets the canvas.
   *
   * @return the canvas
   */
  @Override
  public PlotBox getPlot() {
    return mFormatPane.getCanvas();
  }

  /**
   * Sets the color map.
   *
   * @param colormap the new color map
   */
  public void setColorMap(ColorMap colormap) {
    if (colormap != null) {
      mColorMapModel.set(colormap);
    }
  }
}
