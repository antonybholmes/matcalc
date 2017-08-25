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
import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.core.Properties;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.ColorBar;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.graphplot.PlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.BottomColumnLabelPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnLabelPosition;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnLabelProperties;
import org.jebtk.graphplot.figure.heatmap.legacy.CountBracketLeftPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.CountBracketRightPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.heatmap.legacy.CountPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProperties;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupsLegendPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.HeatMapPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixSummaryPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelPosition;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelProperties;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelsPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.TopColumnLabelPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.expression.GroupColorBarPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.expression.GroupLabelPlotElement;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxColumn;
import org.jebtk.graphplot.plotbox.PlotBoxEmpty;
import org.jebtk.graphplot.plotbox.PlotBoxRow;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;

// TODO: Auto-generated Javadoc
/**
 * Generates a heatmap from a data matrix.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class HeatMapCanvas extends ModernPlotCanvas {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * The horizontal gap.
	 */
	private Dimension horizontalGap = 
			new Dimension(MatrixPlotElement.BLOCK_SIZE, 0);
	
	/**
	 * The vertical gap.
	 */
	private Dimension verticalGap = 
			new Dimension(0, MatrixPlotElement.BLOCK_SIZE);

	/**
	 * The constant CHAR_WIDTH.
	 */
	private static final int CHAR_WIDTH = 
			SettingsService.getInstance().getAsInt("graphplot.plot.char-width");

	/**
	 * The constant LEGEND_WIDTH.
	 */
	private static final int LEGEND_WIDTH = 
			SettingsService.getInstance().getAsInt("plot.legend-width");

	/**
	 * The constant COLOR_BAR_WIDTH.
	 */
	private static final int COLOR_BAR_WIDTH = 
			SettingsService.getInstance().getAsInt("graphplot.plot.color-bar-width");

	/** The Constant COLOR_BAR_HEIGHT. */
	private static final int COLOR_BAR_HEIGHT = 
			SettingsService.getInstance().getAsInt("graphplot.plot.color-bar-height");

	/**
	 * The member plot box.
	 */
	private PlotBox mPlotBox;
	
	/**
	 * Instantiates a new heat map canvas.
	 *
	 * @param matrix the matrix
	 * @param groups the groups
	 * @param rowGroups the row groups
	 * @param countGroups the count groups
	 * @param history the history
	 * @param min the min
	 * @param max the max
	 * @param rowLabelProperties the row label properties
	 * @param columnLabelProperties the column label properties
	 * @param groupProperties the group properties
	 * @param properties the properties
	 */
	public HeatMapCanvas(AnnotationMatrix matrix,
			XYSeriesGroup groups,
			XYSeriesGroup rowGroups,
			CountGroups countGroups,
			List<String> history,
			double min,
			double max,
			RowLabelProperties rowLabelProperties,
			ColumnLabelProperties columnLabelProperties,
			GroupProperties groupProperties,
			Properties properties) {

		IntDim aspectRatio = 
				(IntDim)properties.getProperty("plot.block-size");
		
		ColorMap colorMap = (ColorMap)properties.getProperty("plot.colormap");
		
		HeatMapPlotElement heatMapElement = new HeatMapPlotElement(matrix, 
				colorMap, 
				aspectRatio);

		heatMapElement.setGridColor((Color)properties.getProperty("plot.grid-color"));
		heatMapElement.setOutlineColor((Color)properties.getProperty("plot.outline-color"));
		heatMapElement.setBorderColor((Color)properties.getProperty("plot.border-color"));

		mPlotBox = new PlotBoxRow();

		PlotElement element;

		PlotBox columnBox;

		PlotBox emptyVBox = new PlotBoxEmpty(verticalGap);
		PlotBox emptyHBox = new PlotBoxEmpty(horizontalGap);
		
		mPlotBox.addChild(emptyVBox);
		
		
		//
		// Row labels
		//

		PlotBox rowLabelsBox = new PlotBoxColumn();

		if (rowLabelProperties.showFeatures && 
				rowLabelProperties.position == RowLabelPosition.RIGHT && 
				matrix.getRowCount() > 1) {
			rowLabelsBox.addChild(emptyHBox);

			element = new CountBracketRightPlotElement(matrix, 
					countGroups,
					10, 
					aspectRatio,
					rowLabelProperties.color);
			rowLabelsBox.addChild(element);


			rowLabelsBox.addChild(emptyHBox);

			element = new CountPlotElement(matrix,
					countGroups,
					100, 
					aspectRatio, 
					rowLabelProperties.color);
			
			rowLabelsBox.addChild(element);
		}

		element = new RowLabelsPlotElement(matrix, 
				rowLabelProperties, 
				aspectRatio,
				CHAR_WIDTH);

		rowLabelsBox.addChild(element);

		if (rowLabelProperties.showFeatures && 
				rowLabelProperties.position == RowLabelPosition.LEFT 
				&& matrix.getRowCount() > 1) {
			rowLabelsBox.addChild(emptyHBox);

			element = new CountPlotElement(matrix,
					countGroups,
					100, 
					aspectRatio, 
					rowLabelProperties.color);
			rowLabelsBox.addChild(element);

			rowLabelsBox.addChild(emptyHBox);

			element = new CountBracketLeftPlotElement(matrix, 
					10, 
					aspectRatio,
					rowLabelProperties.color);
			rowLabelsBox.addChild(element);
		}



		
		PlotBox rowLabelsSpaceBox = new PlotBoxEmpty(rowLabelsBox.getPreferredSize().height, 0);

		//
		// Vertical labels
		//

		int maxChars = properties.getAsInt("plot.row-label-max-chars");
		
		//
		// Create space for the column labels
		//

		if (columnLabelProperties.position == ColumnLabelPosition.TOP) {
			element = new TopColumnLabelPlotElement(matrix,
					groups,
					aspectRatio,
					properties,
					columnLabelProperties.color, 
					CHAR_WIDTH, 
					maxChars);
		} else {
			element = new BottomColumnLabelPlotElement(matrix, 
					aspectRatio,
					columnLabelProperties.color, 
					CHAR_WIDTH, 
					maxChars);
		}

		PlotBox columnLabelsBox = element;

		if (groupProperties.show && groups.getCount() > 0) {
			columnBox = new PlotBoxColumn();
			columnBox.addChild(emptyHBox);

			if (rowLabelProperties.show && 
					rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsSpaceBox);
				columnBox.addChild(emptyHBox);
			}

			if (rowGroups.getCount() > 0) {
				columnBox.addChild(emptyHBox);
				columnBox.addChild(emptyHBox);
			}

			element = new GroupLabelPlotElement(matrix,
					aspectRatio,
					groups, 
					groupProperties);
			columnBox.addChild(element);

			mPlotBox.addChild(columnBox);

			// space
			mPlotBox.addChild(emptyVBox);
		}



		//
		// Color bar
		//

		if (groupProperties.showColors && groups != null && groups.getCount() > 0) {
			columnBox = new PlotBoxColumn();

			columnBox.addChild(emptyHBox);

			// If left labels exist
			if (rowLabelProperties.show && 
					rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsSpaceBox);
				columnBox.addChild(emptyHBox);
			}

			element = new GroupColorBarPlotElement(matrix, aspectRatio, groups, groupProperties);
			columnBox.addChild(element);
			mPlotBox.addChild(columnBox);

			mPlotBox.addChild(emptyVBox);
		}


		//
		// Top labels
		//

		if (columnLabelProperties.show && 
				columnLabelProperties.position == ColumnLabelPosition.TOP) {
			columnBox = new PlotBoxColumn();

			columnBox.addChild(emptyHBox);

			// If left labels exist
			if (rowLabelProperties.show && 
					rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsSpaceBox);
				columnBox.addChild(emptyHBox);
			}

			columnBox.addChild(columnLabelsBox);

			mPlotBox.addChild(columnBox);

			mPlotBox.addChild(emptyVBox);
		} else {
			mPlotBox.addChild(emptyVBox);
			mPlotBox.addChild(emptyVBox);
		}

		
		//
		// cluster
		//

		columnBox = new PlotBoxColumn();

		columnBox.addChild(emptyHBox);

		if (rowLabelProperties.show && 
				rowLabelProperties.position == RowLabelPosition.LEFT) {
			columnBox.addChild(rowLabelsBox);
			columnBox.addChild(emptyHBox);
		}

		// For coloring purposes we use a
		// normalized matrix, regardless of how the user
		// manipulated the matrix before

		columnBox.addChild(heatMapElement);


		if (rowLabelProperties.show && 
				rowLabelProperties.position == RowLabelPosition.RIGHT) {
			columnBox.addChild(emptyHBox);
			columnBox.addChild(rowLabelsBox);
		}

		mPlotBox.addChild(columnBox);

		mPlotBox.addChild(emptyVBox);

		//
		// Labels and legend
		//

		columnBox = new PlotBoxColumn();

		columnBox.addChild(emptyHBox);

		// If left labels exist
		if (rowLabelProperties.show && 
				rowLabelProperties.position == RowLabelPosition.LEFT) {
			columnBox.addChild(rowLabelsSpaceBox);
			columnBox.addChild(emptyHBox);
		}

		if (columnLabelProperties.show && 
				columnLabelProperties.position == ColumnLabelPosition.BOTTOM) {
			columnBox.addChild(columnLabelsBox);

			// legend
			columnBox.addChild(emptyHBox);
			//columnBox.addChild(emptyHBox);
		}

		PlotBox rowBox = new PlotBoxRow();

		if ((boolean)properties.getProperty("plot.show-legend")) {
			element = new GroupsLegendPlotElement(matrix, aspectRatio, LEGEND_WIDTH, groups);
			rowBox.addChild(element);
			rowBox.addChild(emptyVBox);
			element = new ColorBar(colorMap, min, max, new IntDim(COLOR_BAR_WIDTH, COLOR_BAR_HEIGHT));
			rowBox.addChild(element);

			rowLabelsBox.addChild(emptyVBox);
		}

		element = new MatrixSummaryPlotElement(matrix, 
				history, 
				aspectRatio, 
				400);
		
		rowBox.addChild(element);

		columnBox.addChild(rowBox);

		mPlotBox.addChild(columnBox);
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, DrawingContext context, Object... params) {
		mPlotBox.plot(g2, context, params);
	}
}
