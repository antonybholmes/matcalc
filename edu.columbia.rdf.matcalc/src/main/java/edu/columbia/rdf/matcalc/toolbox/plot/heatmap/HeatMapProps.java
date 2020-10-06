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

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.AspectRatio;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixPlotElement;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * The class HeatMapProperties.
 */
public class HeatMapProps extends Props {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new heat map properties.
   */
  public HeatMapProps() {
    set("plot.row-label-max-chars", SettingsService.getInstance().getInt("plot.row-label-max-chars"));

    set("plot.border-color", Color.BLACK);

    set("plot.outline-color", ModernWidget.LINE_COLOR);

    set("plot.grid.rows.show", true);
    set("plot.grid.rows.color", Color.BLACK);
    set("plot.grid.columns.show", true);
    set("plot.grid.columns.color", Color.BLACK);

    set("plot.border-color-enabled", true);

    set("plot.show-outline-color", false);
    set("plot.colormap", ColorMap.createBlueWhiteRedMap());
    set("plot.show-legend", true);
    set("plot.show-colorbar", true);
    set("plot.show-summary", true);
    set("plot.aspect-ratio", new AspectRatio());
    set("plot.block-size", new DoubleDim(MatrixPlotElement.BLOCK_SIZE));
    set("plot.show-row-labels", true);
    set("plot.show-feature-counts", true);
    set("plot.color.standardization", ColorNormalizationType.ZSCORE_ROW);
    set("plot.color.intensity", 0);
    set("plot.tree.hoz.width", 200);
    set("plot.tree.hoz.visible", true);
    set("plot.tree.hoz.color", Color.BLACK);
    set("plot.tree.vert.width", 200);
    set("plot.tree.vert.visible", true);
    set("plot.tree.vert.color", Color.BLACK);

    // Whether to color labels by their group color or not
    set("plot.labels.color-by-group", false);

    set("plot.heatmap.visible", true);
  }

}
