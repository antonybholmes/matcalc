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
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnLabelProperties;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelProperties;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.graphics.colormap.ColorMapModel;
import org.jebtk.modern.tabs.TabsModel;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.zoom.ZoomModel;

import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ScaleModel;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.HeatMapPanel;

// TODO: Auto-generated Javadoc
/**
 * The class ClusterPanel.
 */
public class ClusterPanel extends HeatMapPanel {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new cluster panel.
	 *
	 * @param window the window
	 * @param matrix the matrix
	 * @param groups the groups
	 * @param rowGroups the row groups
	 * @param rowCluster the row cluster
	 * @param columnCluster the column cluster
	 * @param zoomModel the zoom model
	 * @param colorMapModel the color map model
	 * @param colorStandardizationModel the color standardization model
	 * @param intensityModel the intensity model
	 * @param contentModel the content model
	 * @param countGroups the count groups
	 * @param history the history
	 * @param properties the properties
	 */
	public ClusterPanel(ModernRibbonWindow window,
			AnnotationMatrix matrix,
			XYSeriesModel groups,
			XYSeriesModel rowGroups,
			Cluster rowCluster,
			Cluster columnCluster,
			ZoomModel zoomModel,
			ColorMapModel colorMapModel,
			ColorNormalizationModel colorStandardizationModel,
			ScaleModel intensityModel,
			TabsModel contentModel,
			CountGroups countGroups,
			List<String> history,
			Properties properties) {
		super(window,
			matrix,
			rowCluster,
			columnCluster,
			groups,
			rowGroups,
			countGroups,
			history,
			zoomModel,
			colorMapModel,
			colorStandardizationModel,
			intensityModel,
			contentModel,
			properties);
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.plot.heatmap.legacy.HeatMapPanel#update()
	 */
	@Override
	public void update() {
		double max = mScaleModel.get(); //mIntensityModel.getBaseline();
		double min = -max; //mIntensityModel.getBaseline(); //PlotConstants.MIN_STD; // / scale;
		//PlotConstants.MAX_STD; // / scale;

		// Create version of the matrix with only the groups of interest
		XYSeriesGroup seriesOfInterest = new XYSeriesGroup();

		for (XYSeries series : mGroupsModel) {
			seriesOfInterest.add(series);
		}

		XYSeriesGroup rowSeriesOfInterest = new XYSeriesGroup();
		
		for (XYSeries series : mRowGroupsModel) {
			rowSeriesOfInterest.add(series);
		}
		
		AnnotationMatrix m = createMatrix(mMatrix, min, max);
		
		display(m, seriesOfInterest, rowSeriesOfInterest, min, max);
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.plot.heatmap.legacy.HeatMapPanel#createCanvas(org.abh.common.math.matrix.AnnotationMatrix, org.graphplot.figure.series.XYSeriesGroup, org.graphplot.figure.series.XYSeriesGroup, double, double, org.graphplot.figure.heatmap.legacy.RowLabelProperties, org.graphplot.figure.heatmap.legacy.ColumnLabelProperties)
	 */
	@Override
	public ModernPlotCanvas createCanvas(AnnotationMatrix m,
			XYSeriesGroup groupsOfInterest,
			XYSeriesGroup rowGroupsOfInterest,
			double min,
			double max,
			RowLabelProperties rowLabelProperties,
			ColumnLabelProperties columnLabelProperties) {
		
		return new ClusterCanvas(m,
				mRowCluster,
				mColumnCluster,
				groupsOfInterest,
				rowGroupsOfInterest,
				mCountGroups,
				mHistory,
				min,
				max,
				rowLabelProperties,
				columnLabelProperties,
				mGroupsElement.getProperties(),
				mProperties);
	}
}