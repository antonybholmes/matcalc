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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap.cluster.legacy;

import java.util.List;

import org.jebtk.core.Properties;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.ColorBar;
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
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixSummaryPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelPosition;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelProperties;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelsPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.TopColumnLabelPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.clustering.ColumnHTreeTopPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.clustering.GroupsHierarchicalPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.clustering.RowHTreeLeftPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.clustering.RowHTreeRightPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.expression.GroupColorBarPlotElement;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxColumn;
import org.jebtk.graphplot.plotbox.PlotBoxEmpty;
import org.jebtk.graphplot.plotbox.PlotBoxRow;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.colormap.ColorMap;


// TODO: Auto-generated Javadoc
/**
 * The class ClusterCanvas.
 */
public class ClusterCanvas extends PlotBoxRow {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant BLOCK_SIZE.
	 */
	private static final int BLOCK_SIZE = 
			SettingsService.getInstance().getAsInt("plot.block-size");

	/**
	 * The horizontal gap.
	 */
	private static IntDim HOZ_GAP = new IntDim(BLOCK_SIZE, 0);

	/**
	 * The vertical gap.
	 */
	private static IntDim VERTICAL_GAP = new IntDim(0, BLOCK_SIZE);


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
	 * Instantiates a new cluster canvas.
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
	public ClusterCanvas(DataFrame matrix,
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
		this(matrix, 
				null, 
				null, 
				groups, 
				rowGroups, 
				countGroups, 
				history, 
				min, 
				max, 
				rowLabelProperties, 
				columnLabelProperties, 
				groupProperties, 
				properties);
	}

	/**
	 * Instantiates a new cluster canvas.
	 *
	 * @param matrix the matrix
	 * @param rowCluster the row cluster
	 * @param columnCluster the column cluster
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
	public ClusterCanvas(DataFrame matrix,
			Cluster rowCluster,
			Cluster columnCluster,
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

		PlotBox emptyVBox = new PlotBoxEmpty(VERTICAL_GAP);
		PlotBox emptyHBox = new PlotBoxEmpty(HOZ_GAP);

		// Add space at the top
		addChild(emptyVBox);


		PlotElement element;

		PlotBox columnBox;



		// row labels

		PlotBox rowLabelsBox = new PlotBoxColumn();

		DoubleDim aspectRatio = 
				(DoubleDim)properties.getProperty("plot.block-size");

		ColorMap colorMap = 
				(ColorMap)properties.getProperty("plot.colormap");

		int maxChars = properties.getAsInt("plot.row-label-max-chars");

		int treeWidthH = properties.getAsInt("plot.tree.hoz.width");
		boolean treeVisH = properties.getAsBool("plot.tree.hoz.visible");
		int treeWidthV = properties.getAsInt("plot.tree.vert.width");
		boolean treeVisV = properties.getAsBool("plot.tree.vert.visible");


		System.err.println("dsdsd " + rowLabelProperties.showFeatureCounts);

		if (rowLabelProperties.showFeatureCounts &&
				rowLabelProperties.position == RowLabelPosition.RIGHT && 
				matrix.getRows() > 1) {
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


		/*
			if (rowCluster != null) {
				element = new RowHierarchicalLabelPlotElement(matrix, 
						rowCluster, 
						rowLabelProperties,
						aspectRatio, 
						CHAR_WIDTH);
		 */
		//} else {
		if (rowLabelProperties.show) {
			element = new RowLabelsPlotElement(matrix,
					rowLabelProperties,
					aspectRatio,
					CHAR_WIDTH);


			rowLabelsBox.addChild(element);
		}

		if (rowLabelProperties.showFeatureCounts &&
				rowLabelProperties.position == RowLabelPosition.LEFT && 
				matrix.getRows() > 1) {
			rowLabelsBox.addChild(emptyHBox);

			element = new CountPlotElement(matrix, 
					countGroups,
					100, 
					aspectRatio, 
					rowLabelProperties.color);

			rowLabelsBox.addChild(element);

			rowLabelsBox.addChild(emptyHBox);

			element = new CountBracketLeftPlotElement(matrix,
					countGroups,
					10, 
					aspectRatio,
					rowLabelProperties.color);
			rowLabelsBox.addChild(element);
		}

		PlotBox rowLabelsSpaceBox = 
				new PlotBoxEmpty(rowLabelsBox.getPreferredSize().width, 0);

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

		// Now we build the plot


		if (columnCluster != null) {
			columnBox = new PlotBoxColumn();
			columnBox.addChild(emptyHBox);

			if (treeVisH && rowCluster != null) {
				columnBox.addChild(new PlotBoxEmpty(treeWidthH, -1));
				columnBox.addChild(emptyHBox);
			}

			if (rowLabelProperties.show && 
					rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsSpaceBox);
				columnBox.addChild(emptyHBox);
			}

			if (treeVisV && columnCluster != null) {
				element = new ColumnHTreeTopPlotElement(matrix,
						groups,
						aspectRatio,
						treeWidthV, 
						columnCluster,
						properties);
				//canvas.setCanvasSize(new Dimension(-1, columnClusterHeight));
				columnBox.addChild(element);
			}

			addChild(columnBox);

			addChild(emptyVBox);
		}


		//
		// Color bar
		//

		if (groupProperties.showColors && groups.getCount() > 0) {
			columnBox = new PlotBoxColumn();

			columnBox.addChild(emptyHBox);

			if (rowLabelProperties.show && 
					rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsSpaceBox);
				columnBox.addChild(emptyHBox);
			}

			if (treeVisH && rowCluster != null) {
				columnBox.addChild(new PlotBoxEmpty(treeWidthH, -1));
				columnBox.addChild(emptyHBox);
			}

			/*
			if (columnCluster != null) {
				element = new GroupHierarchicalColorBarPlotElement(matrix,
						aspectRatio,
						BLOCK_SIZE, 
						columnCluster, 
						groups,
						groupProperties);
			} else {
			 */
			element = new GroupColorBarPlotElement(matrix, 
					aspectRatio, 
					groups, 
					groupProperties);
			//}

			columnBox.addChild(element);
			addChild(columnBox);

			addChild(emptyVBox);
		}


		//
		// Column labels
		//

		if (columnLabelProperties.show && 
				columnLabelProperties.position == ColumnLabelPosition.TOP) {
			columnBox = new PlotBoxColumn();
			columnBox.addChild(emptyHBox);

			if (rowLabelProperties.show) {
				if (rowLabelProperties.position == RowLabelPosition.LEFT) {
					columnBox.addChild(rowLabelsSpaceBox);
					columnBox.addChild(emptyHBox);
				} else {
					if (treeVisH && rowCluster != null) {
						columnBox.addChild(new PlotBoxEmpty(treeWidthH, -1));
						columnBox.addChild(emptyHBox);
					}
				}
			}

			columnBox.addChild(columnLabelsBox);

			addChild(columnBox);

			addChild(emptyVBox);
		}

		//
		// cluster
		//

		boolean heatMapVisible = properties.getAsBool("plot.heatmap.visible");

		if (heatMapVisible) {
			columnBox = new PlotBoxColumn();

			columnBox.addChild(emptyHBox);


			if (rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsBox);
				columnBox.addChild(emptyHBox);
			} else {
				// If labels are on the right, then tree appears on the left

				if (treeVisH && rowCluster != null) {
					element = new RowHTreeLeftPlotElement(matrix, 
							treeWidthH, 
							aspectRatio, 
							rowCluster,
							properties.getAsColor("plot.tree.hoz.color"));
					//canvas.setCanvasSize(new Dimension(rowTreeProperties.width, -1));
					columnBox.addChild(element);

					columnBox.addChild(emptyHBox);
				}
			}

			element = new HeatMapPlotElement(matrix,
					colorMap, 
					aspectRatio);

			((HeatMapPlotElement)element).setGridColor(properties.getAsColor("plot.grid-color"));
			((HeatMapPlotElement)element).setOutlineColor(properties.getAsColor("plot.outline-color"));
			((HeatMapPlotElement)element).setBorderColor(properties.getAsColor("plot.border-color"));


			columnBox.addChild(element);

			if (rowLabelProperties.position == RowLabelPosition.RIGHT) {
				columnBox.addChild(emptyHBox);
				columnBox.addChild(rowLabelsBox);
			} else {
				// If labels are on the right, then tree appears on the left

				if (treeVisH && rowCluster != null) {
					columnBox.addChild(emptyHBox);

					element = new RowHTreeRightPlotElement(matrix, 
							treeWidthH, 
							aspectRatio, 
							rowCluster,
							properties.getAsColor("plot.tree.hoz.color"));
					//canvas.setCanvasSize(new Dimension(rowTreeProperties.width, -1));
					columnBox.addChild(element);

					//columnBox.addChild(emptyHBox);
				}
			}

			addChild(columnBox);

			//
			// Space
			//
			addChild(emptyVBox);
		}

		//
		// labels
		//

		columnBox = new PlotBoxColumn();

		columnBox.addChild(emptyHBox);

		if (rowLabelProperties.show) {
			if (rowLabelProperties.position == RowLabelPosition.LEFT) {
				columnBox.addChild(rowLabelsSpaceBox);
				columnBox.addChild(emptyHBox);
			} else {
				if (treeVisH && rowCluster != null) {
					columnBox.addChild(new PlotBoxEmpty(treeWidthH, 0));
					columnBox.addChild(emptyHBox);
				}
			}
		}

		if (columnLabelProperties.show && 
				columnLabelProperties.position == ColumnLabelPosition.BOTTOM) {
			columnBox.addChild(columnLabelsBox);
			columnBox.addChild(emptyHBox);
		}

		PlotBox rowBox = new PlotBoxRow();

		if (properties.getAsBool("plot.show-legend")) {
			if (columnCluster != null) {
				element = new GroupsHierarchicalPlotElement(matrix, 
						aspectRatio, 
						LEGEND_WIDTH, 
						columnCluster, 
						groups);
			} else {
				// Conventional heat map
				element = new GroupsLegendPlotElement(matrix, 
						aspectRatio, 
						LEGEND_WIDTH, 
						groups);
			}

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

		addChild(columnBox);
	}
}
