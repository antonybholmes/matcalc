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

import org.jebtk.core.Properties;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.FormatPlotPane;


// TODO: Auto-generated Javadoc
/**
 * Merges designated segments together using the merge column. Consecutive rows with the same
 * merge id will be merged together. Coordinates and copy number will be adjusted but
 * genes, cytobands etc are not.
 *
 * @author Antony Holmes Holmes
 *
 */
public class HeatMapPlotWindow extends HeatMapWindow  {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new heat map plot window.
	 *
	 * @param window the window
	 * @param matrix the matrix
	 * @param groups the groups
	 * @param rowGroups the row groups
	 * @param countGroups the count groups
	 * @param history the history
	 * @param properties the properties
	 */
	public HeatMapPlotWindow(ModernWindow window,
			AnnotationMatrix matrix,
			XYSeriesModel groups,
			XYSeriesModel rowGroups,
			CountGroups countGroups,
			List<String> history,
			Properties properties) {
		this(window, 
				"Heat Map", 
				matrix, 
				groups, 
				rowGroups,
				countGroups,
				history, 
				properties);
	}
	
	/**
	 * Instantiates a new heat map plot window.
	 *
	 * @param window the window
	 * @param name the name
	 * @param matrix the matrix
	 * @param groups the groups
	 * @param rowGroups the row groups
	 * @param countGroups the count groups
	 * @param history the history
	 * @param properties the properties
	 */
	public HeatMapPlotWindow(ModernWindow window,
			String name,
			AnnotationMatrix matrix,
			XYSeriesModel groups,
			XYSeriesModel rowGroups,
			CountGroups countGroups,
			List<String> history,
			Properties properties) {
		super(window, matrix, groups, rowGroups, countGroups, history, properties);

		setFormatPane(createFormatPane());
	}

	/**
	 * Creates the format pane.
	 *
	 * @return the format plot pane
	 */
	public FormatPlotPane createFormatPane() {
		return new HeatMapPanel(this,
				mMatrix,
				mGroups,
				mRowGroups,
				mCountGroups,
				mHistory,
				mZoomModel,
				mColorMapModel,
				mColorModel,
				mScaleModel,
				getTabsPane().getModel(),
				mProperties);
	}
}
