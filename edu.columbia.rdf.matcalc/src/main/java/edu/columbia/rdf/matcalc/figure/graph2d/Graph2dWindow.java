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

import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.MovableLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.ZModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.modern.UI;
import org.jebtk.modern.contentpane.CloseableHTab;
import org.jebtk.modern.contentpane.SizableContentPane;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.FigureWindow;
import edu.columbia.rdf.matcalc.figure.LegendRibbonSection;
import edu.columbia.rdf.matcalc.figure.MarginsRibbonSection;
import edu.columbia.rdf.matcalc.figure.PlotSizeModel;
import edu.columbia.rdf.matcalc.figure.PlotSizeRibbonSection;

// TODO: Auto-generated Javadoc
/**
 * Window for showing 2D graphs such as a scatter plot.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class Graph2dWindow extends FigureWindow {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	//protected MultiPlotCanvas mCanvas = new MultiPlotCanvas();


	/** The m size model. */
	private PlotSizeModel mSizeModel = new PlotSizeModel();

	/** The m matrices. */
	private List<AnnotationMatrix> mMatrices = 
			new ArrayList<AnnotationMatrix>();

	/**
	 * The class ColorMapEvents.
	 */
	private class ColorMapEvents implements ChangeListener {

		/* (non-Javadoc)
		 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			//mFigure.getCurrentSubFigure().getCurrentAxes().getCurrentPlot().setColorMap(mColorMapModel.get());

			mFigure.setColorMap(mColorMapModel.get());

		}	
	}

	/**
	 * The Class SizeEvents.
	 */
	private class SizeEvents implements ChangeListener {

		/* (non-Javadoc)
		 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			updateSizes();
		}	
	}

	/**
	 * The class ColorStandardizationEvents.
	 */
	private class ColorStandardizationEvents implements ChangeListener {

		/* (non-Javadoc)
		 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			normalizeMatrix();
		}

	}

	/**
	 * Instantiates a new graph2d window.
	 *
	 * @param window the window
	 * @param subFigure the sub figure
	 */
	public Graph2dWindow(MainMatCalcWindow window,
			SubFigure subFigure) {
		this(window, new Figure(subFigure));
	}

	/**
	 * Instantiates a new graph 2 d window.
	 *
	 * @param window the window
	 * @param figure the figure
	 */
	public Graph2dWindow(MainMatCalcWindow window, Figure figure) {
		this(window, figure, true);
	}
	
	/**
	 * Instantiates a new graph 2 d window.
	 *
	 * @param window the window
	 * @param figure the figure
	 * @param allowStyle the allow style
	 */
	public Graph2dWindow(MainMatCalcWindow window, 
			Figure figure, 
			boolean allowStyle) {
		super(window, figure, allowStyle);

		// add canvas to the plot
		mFigure = figure;

		setup();
	}

	/**
	 * Setup.
	 */
	private void setup() {
		mSizeModel.set(mFigure.getCurrentSubFigure().getCurrentAxes());

		mFormatPane = new AxesControlPanel(this, mFigure);

		mColorMapModel.addChangeListener(new ColorMapEvents());
		mColorModel.addChangeListener(new ColorStandardizationEvents());
		mScaleModel.addChangeListener(new ColorStandardizationEvents());
		mSizeModel.addChangeListener(new SizeEvents());
		//mColorMapModel.set((ColorMap)properties.getProperty("plot.colormap"));
		//mColorModel.set((ColorStandardization)properties.getProperty("plot.color.standardization"));

		mStyleModel.addChangeListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent e) {
				//mFigure.getCurrentSubFigure().getCurrentAxes().setStyle(mStyleModel.get());

				for (int z : mFigure.getSubFigureZModel()) {
					mFigure
					.getSubFigureZModel()
					.getAtZ(z)
					.getCurrentAxes()
					.setStyle(mStyleModel.get());
				}
			}});
		
		getRibbon().getToolbar("Plot").add(new LegendRibbonSection(getRibbon(), mFigure.getCurrentSubFigure().getCurrentAxes().getLegend()));
		getRibbon().getToolbar("Layout").add(new PlotSizeRibbonSection(getRibbon(), mSizeModel));
		getRibbon().getToolbar("Layout").add(new MarginsRibbonSection(getRibbon(), mSizeModel));
		
		addFormatPane();

		//normalizeMatrix();

		//updateSizes();

		UI.centerWindowToScreen(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.window.ModernWindow#createUi()
	 */
	@Override
	public final void createUi() {
		super.createUi();

		//ZoomCanvas zoomCanvas = new ZoomCanvas(mFigure);

		mFigure.setZoomModel(mZoomModel);

		//BackgroundCanvas backgroundCanvas = new BackgroundCanvas(zoomCanvas);

		ModernScrollPane scrollPane = new ModernScrollPane(mFigure)
				.setScrollBarLocation(ScrollBarLocation.FLOATING)
				.setScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW);

		ModernPanel panel = new ModernPanel(scrollPane, ModernWidget.BORDER);

		mContentPane.getModel().setCenterTab(panel);
	}

	/**
	 * Adds the history pane to the layout if it is not already showing.
	 */
	private void addFormatPane() {
		if (!mContentPane.getModel().getRightTabs().containsTab("Format")) {
			mContentPane.getModel().getRightTabs().addTab(new SizableContentPane("Format", 
				new CloseableHTab("Format", mFormatPane, mContentPane), 320, 320, 600));
		}
	}
	
	/**
	 * Removes the format pane.
	 *
	 * @return the graph 2 d window
	 */
	public Graph2dWindow removeFormatPane() {
		mContentPane.getModel().getRightTabs().removeTab("Format");
		
		return this;
	}

	/**
	 * Gets the canvas.
	 *
	 * @return the canvas
	 */
	public ModernPlotCanvas getCanvas() {
		return mFigure;
	}

	/**
	 * Normalize matrix.
	 */
	private void normalizeMatrix() {
		// First cache the original matrices since we are going to normalize
		// them
		if (mMatrices.size() == 0) {
			for (int z : mFigure.getSubFigureZModel()) {
				mMatrices.add(mFigure
						.getSubFigureZModel()
						.getAtZ(z)
						.getCurrentAxes()
						.getCurrentPlot()
						.getMatrix());
			}
		}

		

		int c = 0;

		// Cycle through the matrices

		for (int z : mFigure.getSubFigureZModel()) {
			AnnotationMatrix m = mMatrices.get(c);

			if (m == null) {
				continue;
			}

			double max = mScaleModel.get(); //mIntensityModel.getBaseline();
			double min = -max;

			switch(mColorModel.get().getType()) {
			case ZSCORE_MATRIX:
				m = MatrixOperations.zscore(m); // new MatrixZTransformView(mMatrix);
				break;
			case ZSCORE_ROW:
				m = MatrixOperations.rowZscore(m); //new RowZTransformMatrixView(mMatrix);
				break;
			case ZSCORE_COLUMN:
				m = MatrixOperations.columnZscore(m); //new ColumnZTransformMatrixView(mMatrix);
				break;
			case NORMALIZE:
				if (Double.isNaN(mColorModel.get().getMax())) {
					min = MatrixOperations.min(m);
					max = MatrixOperations.max(m);
				} else {
					min = mColorModel.get().getMin();
					max = mColorModel.get().getMax();
				}

				break;
			default:
				// For the scale of no standardization, i.e we do not adjust
				// or normalize the colors, the plot range must be extremes
				// of the matrix.
				min = MatrixOperations.min(m);
				max = MatrixOperations.max(m);

				break;
			}

			//m = MatrixOperations.minMaxThreshold(m, min, max);

			// Scale the min and max to reflect whether we are contracting
			// or expanding the range
			

			if (mColorModel.get().getType() != ColorNormalizationType.NONE) {
				//min /= scale;
				//max /= scale;
				
				m = MatrixOperations.normalize(m, min, max);
			}

			System.err.println("scale " + min + " " + max + " " + MatrixOperations.min(m));

			// new MinMaxBoundedMatrixView(zMatrix, min, max);

			SubFigure subFigure = mFigure.getSubFigureZModel().getAtZ(z);

			subFigure.setMatrix(m);
			subFigure.setColorMap(mColorMapModel.get());



			//PlotFactory.createVColorBar(min, max, subFigure, axes, mColorMapModel.get());

			++c;
		}
	}

	/**
	 * Update sizes.
	 */
	private void updateSizes() {
		MovableLayer a = mSizeModel.get();

		for (int z : mFigure.getSubFigureZModel()) {
			SubFigure subFigure = mFigure.getSubFigureZModel().getAtZ(z);

			//subFigure.setInternalPlotSize(a.getInternalPlotSize());
			//subFigure.setMargins(a.getMargins());

			ZModel<MovableLayer> axesModel = 
					subFigure.getGridLocations().get(GridLocation.CENTER);

			for (int za : axesModel) {
				MovableLayer layer = axesModel.getAtZ(za);

				layer.setInternalPlotSize(a.getInternalPlotSize());

				//layer.setMargins(a.getMargins());

				//if (layer instanceof Axes) {
				//	Axes axes = (Axes)layer;

				//	layer.setInternalPlotSize(a.getInternalPlotSize());
				//layer.set
				//axes.setMargins(a.getMargins());
				//}

			}

			//for (int za : subFigure.getAxesZModel()) {
			//	Axes axes = subFigure.getAxesZModel().getAtZ(za);

			//axes.updateInternalPlotSize(a.getInternalPlotSize());
			//axes.getMargins().updateMargins(a.getMargins());

			//
			// Auto adjust properties depending on size
			//

			/*
				AnnotationMatrix m = axes.getCurrentPlot().getMatrix();

				boolean fullView = axes.getInternalPlotSize().getW() >= m.getColumnCount() * PlotFactory.DEFAULT_HEATMAP_SIZE;

				Axis axis = axes.getXAxis();

				axis.setLimits(0, m.getColumnCount(), 1);
				axis.getTicks().setTicks(Linspace.evenlySpaced(0.5, m.getColumnCount() - 0.5, 1));
				axis.getTicks().getMajorTicks().setLabels(m.getColumnNames());
				axis.getTicks().getMajorTicks().setRotation(-Mathematics.HALF_PI);
				axis.getTicks().getMajorTicks().getLineStyle().setVisible(true);
				axis.getTicks().getMajorTicks().getFontStyle().setVisible(true);
				axis.getTicks().getMinorTicks().getLineStyle().setVisible(false);
				axis.getTitle().getFontStyle().setVisible(false);
				axis.getLineStyle().setVisible(false);
				axis.getGrid().setVisible(fullView);
				axis.setVisible(fullView);
				//PlotFactory.autoSetXLabelMargin(axes);

				// If the height of the heatmap is less than the ideal height,
				// turn off labels and the grid.

				fullView = axes.getInternalPlotSize().getH() >= m.getRowCount() * PlotFactory.DEFAULT_HEATMAP_SIZE;

				axis = axes.getY1Axis();

				axis.setLimits(0, m.getRowCount(), 1);
				axis.setVisible(false);
				axis.getGrid().setVisible(false);
				//axis.getTicks().setTicks(Linspace.evenlySpaced(0.5, m.getRowCount() - 0.5, 1));
				//axis.getTicks().getMajorTicks().setLabels(m.getRowNames());
				//axis.getTicks().getMajorTicks().getLineStyle().setVisible(false);
				//axis.getTicks().getMajorTicks().getFontStyle().setVisible(false);
				//axis.getTicks().getMinorTicks().getLineStyle().setVisible(false);
				//axis.getTitle().getFontStyle().setVisible(false);
				//autoSetY1LabelMargin(axes);

				axis = axes.getY2Axis();

				axis.setLimits(0, m.getRowCount(), 1);
				axis.getTicks().setTicks(Linspace.evenlySpaced(0.5, m.getRowCount() - 0.5, 1));
				axis.getTicks().getMajorTicks().setLabels(m.getRowNames());
				axis.getTicks().getMajorTicks().getLineStyle().setVisible(true);
				axis.getTicks().getMajorTicks().getFontStyle().setVisible(true);
				axis.getTicks().getMinorTicks().getLineStyle().setVisible(false);
				axis.getTitle().getFontStyle().setVisible(false);
				axis.getGrid().setVisible(fullView);
				axis.setVisible(fullView);
				//PlotFactory.autoSetY2LabelMargin(axes);
			 */
			//}

			//subFigure.refresh();
		}

		//mFigure.refresh();
	}

	/**
	 * Gets the figure.
	 *
	 * @return the figure
	 */
	public Figure getFigure() {
		return mFigure;
	}

	/* (non-Javadoc)
	 * @see org.matcalc.graph2d.FigureWindow#getColorNormalizationModel()
	 */
	public ColorNormalizationModel getColorNormalizationModel() {
		return mColorModel;
	}
}
