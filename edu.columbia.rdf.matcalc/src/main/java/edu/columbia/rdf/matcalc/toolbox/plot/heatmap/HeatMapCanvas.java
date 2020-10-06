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
import java.awt.Dimension;
import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.ColorBar;
import org.jebtk.graphplot.PlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.BottomColumnLabelPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnLabelPosition;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnLabelProps;
import org.jebtk.graphplot.figure.heatmap.legacy.CountBracketLeftPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.CountBracketRightPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.heatmap.legacy.CountPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProps;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupsLegendPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.HeatMapPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixSummaryPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelPosition;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelProps;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelsPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.TopColumnLabelPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.expression.GroupColorBarPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.expression.GroupLabelPlotElement;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxColumn;
import org.jebtk.graphplot.plotbox.PlotBoxEmpty;
import org.jebtk.graphplot.plotbox.PlotBoxRow;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * Generates a heatmap from a data matrix.
 * 
 * @author Antony Holmes
 *
 */
public class HeatMapCanvas extends PlotBoxRow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The horizontal gap.
   */
  private Dimension horizontalGap = new Dimension(MatrixPlotElement.BLOCK_SIZE, 0);

  /**
   * The vertical gap.
   */
  private Dimension verticalGap = new Dimension(0, MatrixPlotElement.BLOCK_SIZE);

  /**
   * The constant CHAR_WIDTH.
   */
  private static final int CHAR_WIDTH = SettingsService.getInstance().getInt("graphplot.plot.char-width");

  /**
   * The constant LEGEND_WIDTH.
   */
  private static final int LEGEND_WIDTH = SettingsService.getInstance().getInt("plot.legend-width");

  /**
   * The constant COLOR_BAR_WIDTH.
   */
  private static final int COLOR_BAR_WIDTH = SettingsService.getInstance().getInt("graphplot.plot.color-bar-width");

  /** The Constant COLOR_BAR_HEIGHT. */
  private static final int COLOR_BAR_HEIGHT = SettingsService.getInstance().getInt("graphplot.plot.color-bar-height");

  /**
   * Instantiates a new heat map canvas.
   *
   * @param matrix           the matrix
   * @param groups           the groups
   * @param rowGroups        the row groups
   * @param countGroups      the count groups
   * @param history          the history
   * @param min              the min
   * @param max              the max
   * @param rowLabelProps    the row label properties
   * @param columnLabelProps the column label properties
   * @param groupProps       the group properties
   * @param Props            the properties
   */
  public HeatMapCanvas(DataFrame matrix, XYSeriesGroup groups, XYSeriesGroup rowGroups, CountGroups countGroups,
      List<String> history, double min, double max, RowLabelProps rowLabelProperties,
      ColumnLabelProps columnLabelProperties, GroupProps groupProperties, Props properties) {

    DoubleDim aspectRatio = (DoubleDim) properties.get("plot.block-size");

    ColorMap colorMap = (ColorMap) properties.get("plot.colormap");

    HeatMapPlotElement heatMapElement = new HeatMapPlotElement(matrix, colorMap, aspectRatio);

    heatMapElement.setGridColor((Color) properties.get("plot.grid-color"));
    heatMapElement.setOutlineColor((Color) properties.get("plot.outline-color"));
    heatMapElement.setBorderColor((Color) properties.get("plot.border-color"));

    PlotElement element;

    PlotBox columnBox;

    PlotBox emptyVBox = new PlotBoxEmpty(verticalGap);
    PlotBox emptyHBox = new PlotBoxEmpty(horizontalGap);

    addChild(emptyVBox);

    //
    // Row labels
    //

    PlotBox rowLabelsBox = new PlotBoxColumn();

    if (rowLabelProperties.showFeatureCounts && rowLabelProperties.position == RowLabelPosition.RIGHT
        && matrix.getRows() > 1) {
      rowLabelsBox.addChild(emptyHBox);

      element = new CountBracketRightPlotElement(matrix, countGroups, 10, aspectRatio, rowLabelProperties.color);
      rowLabelsBox.addChild(element);

      rowLabelsBox.addChild(emptyHBox);

      element = new CountPlotElement(matrix, countGroups, 100, aspectRatio, rowLabelProperties.color);

      rowLabelsBox.addChild(element);
    }

    if (rowLabelProperties.show) {
      element = new RowLabelsPlotElement(matrix, rowLabelProperties, aspectRatio, CHAR_WIDTH);

      rowLabelsBox.addChild(element);
    }

    if (rowLabelProperties.showFeatureCounts && rowLabelProperties.position == RowLabelPosition.LEFT
        && matrix.getRows() > 1) {
      rowLabelsBox.addChild(emptyHBox);

      element = new CountPlotElement(matrix, countGroups, 100, aspectRatio, rowLabelProperties.color);
      rowLabelsBox.addChild(element);

      rowLabelsBox.addChild(emptyHBox);

      element = new CountBracketLeftPlotElement(matrix, countGroups, 10, aspectRatio, rowLabelProperties.color);
      rowLabelsBox.addChild(element);
    }

    PlotBox rowLabelsSpaceBox = new PlotBoxEmpty(rowLabelsBox.getPreferredSize().height, 0);

    //
    // Vertical labels
    //

    int maxChars = properties.getInt("plot.row-label-max-chars");

    //
    // Create space for the column labels
    //

    if (columnLabelProperties.position == ColumnLabelPosition.TOP) {
      element = new TopColumnLabelPlotElement(matrix, groups, aspectRatio, properties, columnLabelProperties.color,
          CHAR_WIDTH, maxChars);
    } else {
      element = new BottomColumnLabelPlotElement(matrix, aspectRatio, columnLabelProperties.color, CHAR_WIDTH,
          maxChars);
    }

    PlotBox columnLabelsBox = element;

    if (groupProperties.show && groups.getCount() > 0) {
      columnBox = new PlotBoxColumn();
      columnBox.addChild(emptyHBox);

      if (rowLabelProperties.show && rowLabelProperties.position == RowLabelPosition.LEFT) {
        columnBox.addChild(rowLabelsSpaceBox);
        columnBox.addChild(emptyHBox);
      }

      if (rowGroups.getCount() > 0) {
        columnBox.addChild(emptyHBox);
        columnBox.addChild(emptyHBox);
      }

      element = new GroupLabelPlotElement(matrix, aspectRatio, groups, groupProperties);
      columnBox.addChild(element);

      addChild(columnBox);

      // space
      addChild(emptyVBox);
    }

    //
    // Color bar
    //

    if (groupProperties.showColors && groups != null && groups.getCount() > 0) {
      columnBox = new PlotBoxColumn();

      columnBox.addChild(emptyHBox);

      // If left labels exist
      if (rowLabelProperties.show && rowLabelProperties.position == RowLabelPosition.LEFT) {
        columnBox.addChild(rowLabelsSpaceBox);
        columnBox.addChild(emptyHBox);
      }

      element = new GroupColorBarPlotElement(matrix, aspectRatio, groups, groupProperties);
      columnBox.addChild(element);
      addChild(columnBox);

      addChild(emptyVBox);
    }

    //
    // Top labels
    //

    if (columnLabelProperties.show && columnLabelProperties.position == ColumnLabelPosition.TOP) {
      columnBox = new PlotBoxColumn();

      columnBox.addChild(emptyHBox);

      // If left labels exist
      if (rowLabelProperties.show && rowLabelProperties.position == RowLabelPosition.LEFT) {
        columnBox.addChild(rowLabelsSpaceBox);
        columnBox.addChild(emptyHBox);
      }

      columnBox.addChild(columnLabelsBox);

      addChild(columnBox);

      addChild(emptyVBox);
    } else {
      addChild(emptyVBox);
      addChild(emptyVBox);
    }

    //
    // cluster
    //

    columnBox = new PlotBoxColumn();

    columnBox.addChild(emptyHBox);

    if (rowLabelProperties.show && rowLabelProperties.position == RowLabelPosition.LEFT) {
      columnBox.addChild(rowLabelsBox);
      columnBox.addChild(emptyHBox);
    }

    // For coloring purposes we use a
    // normalized matrix, regardless of how the user
    // manipulated the matrix before

    columnBox.addChild(heatMapElement);

    if (rowLabelProperties.show && rowLabelProperties.position == RowLabelPosition.RIGHT) {
      columnBox.addChild(emptyHBox);
      columnBox.addChild(rowLabelsBox);
    }

    addChild(columnBox);

    addChild(emptyVBox);

    //
    // Labels and legend
    //

    columnBox = new PlotBoxColumn();

    columnBox.addChild(emptyHBox);

    // If left labels exist
    if (rowLabelProperties.show && rowLabelProperties.position == RowLabelPosition.LEFT) {
      columnBox.addChild(rowLabelsSpaceBox);
      columnBox.addChild(emptyHBox);
    }

    if (columnLabelProperties.show && columnLabelProperties.position == ColumnLabelPosition.BOTTOM) {
      columnBox.addChild(columnLabelsBox);

      // legend
      columnBox.addChild(emptyHBox);
      // columnBox.addChild(emptyHBox);
    }

    PlotBox rowBox = new PlotBoxRow();

    if (properties.getBool("plot.show-legend")) {
      element = new GroupsLegendPlotElement(matrix, aspectRatio, LEGEND_WIDTH, groups);
      rowBox.addChild(element);
      rowBox.addChild(emptyVBox);
    }

    if (properties.getBool("plot.show-colorbar")) {
      element = new ColorBar(colorMap, min, max, new IntDim(COLOR_BAR_WIDTH, COLOR_BAR_HEIGHT));
      rowBox.addChild(element);
      rowLabelsBox.addChild(emptyVBox);
    }

    if (properties.getBool("plot.show-legend")) {
      element = new MatrixSummaryPlotElement(matrix, history, aspectRatio, 400);
      rowBox.addChild(element);
      rowLabelsBox.addChild(emptyVBox);
    }

    columnBox.addChild(rowBox);

    addChild(columnBox);
  }
}
