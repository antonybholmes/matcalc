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

import org.jebtk.core.Properties;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.AspectRatio;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixPlotElement;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.widget.ModernWidget;

// TODO: Auto-generated Javadoc
/**
 * The class HeatMapProperties.
 */
public class HeatMapProperties extends Properties {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new heat map properties.
	 */
	public HeatMapProperties() {
		setProperty("plot.row-label-max-chars", 
				SettingsService.getInstance().getAsInt("plot.row-label-max-chars"));
		
		setProperty("plot.border-color", Color.BLACK);
		setProperty("plot.grid-color", ModernWidget.LINE_COLOR);
		setProperty("plot.outline-color", Color.BLACK);
		
		setProperty("plot.border-color-enabled", true);
		setProperty("plot.show-grid-color", true);
		setProperty("plot.show-outline-color", true);
		setProperty("plot.colormap", ColorMap.createBlueWhiteRedMap());
		setProperty("plot.show-legend", true);
		setProperty("plot.aspect-ratio", new AspectRatio());
		setProperty("plot.block-size", MatrixPlotElement.DEFAULT_BLOCK);
		setProperty("plot.show-row-labels", true);
		setProperty("plot.color.standardization", ColorNormalizationType.ZSCORE_ROW);
		setProperty("plot.color.intensity", 0);
		setProperty("plot.tree.hoz.width", 200);
		setProperty("plot.tree.hoz.visible", true);
		setProperty("plot.tree.hoz.color", Color.BLACK);
		setProperty("plot.tree.vert.width", 200);
		setProperty("plot.tree.vert.visible", true);
		setProperty("plot.tree.vert.color", Color.BLACK);
		
		// Whether to color labels by their group color or not
		setProperty("plot.labels.color-by-group", false);
		
		setProperty("plot.heatmap.visible", true);
	}
	
}
