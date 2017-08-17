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
package edu.columbia.rdf.matcalc.toolbox.plot.volcano;

import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.Mathematics;
import org.jebtk.graphplot.PlotFactory;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.icons.ShapeStyle;
import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.DynamicDoubleMatrix;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.math.matrix.MatrixUtils;
import org.jebtk.math.statistics.FDRType;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.theme.ThemeService;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.icons.VolcanoPlot32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseModule;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseType;

// TODO: Auto-generated Javadoc
/**
 * The class VolcanoPlotModule.
 */
public class VolcanoPlotModule extends CalcModule implements ModernClickListener {

	/**
	 * The constant CREATE_GROUPS_MESSAGE.
	 */
	private static final String CREATE_GROUPS_MESSAGE = 
			"You must load or create some groups.";

	/**
	 * The member parent.
	 */
	private MainMatCalcWindow mParent;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Volcano Plot";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mParent = window;

		RibbonLargeButton button = new RibbonLargeButton("Volcano", "Plot", 
				new Raster32Icon(new VolcanoPlot32VectorIcon()),
				"Volcano Plot",
				"Generate a volcano plot");
		button.addClickListener(this);
		//button.setEnabled(false);

		mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		try {
			volcanoPlot();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}


	/**
	 * Volcano plot.
	 *
	 * @throws ParseException the parse exception
	 */
	private void volcanoPlot() throws ParseException {
		XYSeriesGroup groups = mParent.getGroups();

		if (groups.getCount() < 2) {
			ModernMessageDialog.createWarningDialog(mParent, CREATE_GROUPS_MESSAGE);

			return;
		}

		AnnotationMatrix m = mParent.getCurrentMatrix();

		if (m == null) {
			return;
		}

		VolcanoDialog dialog = new VolcanoDialog(mParent, m, groups);

		dialog.setVisible(true);

		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return;
		}

		// We are only interested in the opened matrix
		// without transformations.

		if (dialog.getReset()) {
			mParent.resetHistory();
		}

		XYSeries g1 = dialog.getGroup1(); // new Group("g1");
		XYSeries g2 = dialog.getGroup2(); //new Group("g2");

		groups = new XYSeriesGroup();
		groups.add(g1);
		groups.add(g2);

		double minExp = dialog.getMinExp();
		double pvalue = dialog.getMaxP();

		boolean logData = dialog.getLog2Transform();
		boolean equalVariance = dialog.getEqualVariance();
		boolean plot = dialog.getCreatePlot();

		FDRType fdrType = dialog.getFDRType();

		CollapseType collapseType = dialog.getCollapseType();

		String collapseName = dialog.getCollapseName();

		volcanoPlot(m, 
				minExp, 
				pvalue, 
				collapseType, 
				collapseName, 
				fdrType, 
				g1, 
				g2, 
				logData, 
				equalVariance, 
				plot);
	}

	/**
	 * Volcano plot.
	 *
	 * @param m the m
	 * @param minExp the min exp
	 * @param alpha the alpha
	 * @param collapseType the collapse type
	 * @param collapseName the collapse name
	 * @param fdrType the fdr type
	 * @param g1 the g1
	 * @param g2 the g2
	 * @param logData the log data
	 * @param equalVariance the equal variance
	 * @param plot the plot
	 * @throws ParseException the parse exception
	 */
	private void volcanoPlot(AnnotationMatrix m,
			double minExp,
			double alpha,
			CollapseType collapseType,
			String collapseName,
			FDRType fdrType,
			MatrixGroup g1, 
			MatrixGroup g2,
			boolean logData,
			boolean equalVariance,
			boolean plot) throws ParseException {

		List<MatrixGroup> groups = new ArrayList<MatrixGroup>();
		groups.add(g1);
		groups.add(g2);


		AnnotationMatrix mColumnFiltered = 
				mParent.addToHistory("Keep group columns", AnnotatableMatrix.copyInnerColumns(m, groups));


		AnnotationMatrix mlog2;

		if (logData) {
			mlog2 = mParent.addToHistory("log2", MatrixOperations.log2(MatrixOperations.min(mColumnFiltered, minExp)));
		} else {
			mlog2 = m;
		}


		List<Double> p = MatrixUtils.tTest(mlog2, g1, g2, equalVariance);

		List<Double> foldChanges = MatrixUtils.logFoldChange(mlog2, g1, g2);

		AnnotationMatrix annM = new AnnotatableMatrix(mlog2);
		annM.setNumRowAnnotations("Log2 Fold Change", foldChanges);
		mParent.addToHistory("Add log2 fold changes", annM);

		AnnotationMatrix pValuesM = new AnnotatableMatrix(annM);
		pValuesM.setNumRowAnnotations("P-value", p);
		mParent.addToHistory("Add p-values", pValuesM);


		AnnotationMatrix mcollapsed = CollapseModule.collapse(pValuesM,
				collapseName,
				g1,
				g2,
				collapseType,
				mParent);

		mParent.addToHistory("Collapse rows", mcollapsed);

		double[] fdr = Statistics.fdr(mcollapsed.getRowAnnotationValues("P-value"), 
				fdrType);

		AnnotationMatrix fdrM = new AnnotatableMatrix(mcollapsed);
		fdrM.setNumRowAnnotations("FDR", fdr);
		mParent.addToHistory("False discovery rate", fdrM);


		// filter by fdr
		//List<IndexedValue<Integer, Double>> pValueIndices = 
		//		Statistics.threshold(fdr, alpha);

		//AnnotationMatrix mfdrfiltered = addFlowItem("False discovery filter", 
		//		new RowFilterMatrixView(mfdr, IndexedValueInt.indices(pValueIndices)));


		double[] fdrAnnotation = fdrM.getRowAnnotationValues("FDR");

		double[] pFoldChangeAnnotation = 
				fdrM.getRowAnnotationValues("Log2 Fold Change");


		// Create a plot



		// need to convert p-values to -log10

		List<Double> minusLog10PValues = 
				new ArrayList<Double>(fdrAnnotation.length);

		for (Double s : fdrAnnotation) {
			minusLog10PValues.add(-Mathematics.log10(s));
		}


		List<Double> log2FoldChanges = 
				new ArrayList<Double>(pFoldChangeAnnotation.length);

		for (Double s : pFoldChangeAnnotation) {
			log2FoldChanges.add(s);
		}

		//
		// Create plot
		//

		Figure figure = new Figure();

		SubFigure subFigure = figure.getCurrentSubFigure();

		Axes axes = subFigure.getCurrentAxes();

		XYSeries notSigSeries = new XYSeries("Non-significant");
		XYSeries foldUpSeries = new XYSeries("Up Significant");
		XYSeries foldDownSeries = new XYSeries("Down Significant");

		notSigSeries.getStyle().getFillStyle().setColor(ThemeService.getInstance().colors().getHighlight(2));
		notSigSeries.getStyle().getLineStyle().setColor(ThemeService.getInstance().colors().getHighlight(4));
		notSigSeries.setMarker(ShapeStyle.CIRCLE);


		foldUpSeries.getStyle().getFillStyle().setColor(ColorUtils.decodeHtmlColor("#ff5555"));
		foldUpSeries.getStyle().getLineStyle().setColor(Color.RED);
		foldUpSeries.setMarker(ShapeStyle.CIRCLE);

		foldDownSeries.getStyle().getFillStyle().setColor(ColorUtils.decodeHtmlColor("#5555ff"));
		foldDownSeries.getStyle().getLineStyle().setColor(Color.BLUE);
		foldDownSeries.setMarker(ShapeStyle.CIRCLE);


		double pthres = -Mathematics.log10(alpha);

		Matrix foldM;

		int c;

		//
		// No Signal
		//

		foldM = new DynamicDoubleMatrix(log2FoldChanges.size(), 2);

		c = 0;

		for (int i = 0; i < log2FoldChanges.size(); ++i) {
			if (minusLog10PValues.get(i) <= pthres) {
				foldM.update(c, 0, log2FoldChanges.get(i));
				foldM.update(c, 1, minusLog10PValues.get(i));

				++c;
			}
		}
		
		double minX = 0;
		double minY = 0;
		double maxX = 1;
		double maxY = 1;

		if (foldM.getRowCount() > 0) {
			AnnotationMatrix noSigM = new AnnotatableMatrix(foldM);

			noSigM.setColumnNames("Non-significant x", "Non-significant y");

			PlotFactory.createScatterPlot(noSigM, axes, notSigSeries);
			
			minX = Math.min(minX, MatrixUtils.minInColumn(noSigM, 0));
			maxX = Math.max(maxX, MatrixUtils.maxInColumn(noSigM, 0));
			minY = Math.min(minY, MatrixUtils.minInColumn(noSigM, 1));
			maxY = Math.max(maxY, MatrixUtils.maxInColumn(noSigM, 1));
		}

		//
		// Down
		//

		foldM = new DynamicDoubleMatrix(log2FoldChanges.size(), 2);

		c = 0;

		for (int i = 0; i < log2FoldChanges.size(); ++i) {
			if (minusLog10PValues.get(i) > pthres && log2FoldChanges.get(i) < 0) {
				foldM.update(c, 0, log2FoldChanges.get(i));
				foldM.update(c, 1, minusLog10PValues.get(i));

				++c;
			}
		}

		if (foldM.getRowCount() > 0) {
			AnnotationMatrix foldDownM = new AnnotatableMatrix(foldM);

			foldDownM.setColumnNames("Down Significant x", "Down Significant y");

			PlotFactory.createScatterPlot(foldDownM, axes, foldDownSeries);
			
			minX = Math.min(minX, MatrixUtils.minInColumn(foldDownM, 0));
			maxX = Math.max(maxX, MatrixUtils.maxInColumn(foldDownM, 0));
			minY = Math.min(minY, MatrixUtils.minInColumn(foldDownM, 1));
			maxY = Math.max(maxY, MatrixUtils.maxInColumn(foldDownM, 1));
		}

		//
		// up
		//

		foldM = new DynamicDoubleMatrix(log2FoldChanges.size(), 2);

		c = 0;

		for (int i = 0; i < log2FoldChanges.size(); ++i) {
			if (minusLog10PValues.get(i) > pthres && log2FoldChanges.get(i) >= 0) {
				foldM.update(c, 0, log2FoldChanges.get(i));
				foldM.update(c, 1, minusLog10PValues.get(i));

				++c;
			}
		}

		if (foldM.getRowCount() > 0) {
			AnnotationMatrix foldUpM = new AnnotatableMatrix(foldM);

			foldUpM.setColumnNames("Up Significant x", "Up Significant y");

			PlotFactory.createScatterPlot(foldUpM, axes, foldUpSeries);
			
			minX = Math.min(minX, MatrixUtils.minInColumn(foldUpM, 0));
			maxX = Math.max(maxX, MatrixUtils.maxInColumn(foldUpM, 0));
			minY = Math.min(minY, MatrixUtils.minInColumn(foldUpM, 1));
			maxY = Math.max(maxY, MatrixUtils.maxInColumn(foldUpM, 1));
		}

		/*
			if (minusLog10PValues.get(i) > pthres) {
				if (log2FoldChanges.get(i) >= 0) {
					foldUpSeries.add(mfdr.getRowName(i), log2FoldChanges.get(i), minusLog10PValues.get(i));
				} else {
					foldDownSeries.add(mfdr.getRowName(i), log2FoldChanges.get(i), minusLog10PValues.get(i));
				}
			} else {
				notSigSeries.add(mfdr.getRowName(i), log2FoldChanges.get(i), minusLog10PValues.get(i));
			}
		 */






		//gp.getPlotLayout().setPlotSize(new Dimension(800, 800));


		// How big to make the x axis
		//double min = Mathematics.min(log2FoldChanges);
		//double max = Mathematics.max(log2FoldChanges);

		double x = Math.max(Math.abs(minX), Math.abs(maxX));

		axes.getX1Axis().setLimitsAutoRound(Math.signum(minX) * x, Math.signum(maxX) * x);
		//gp.getXAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
		//gp.getXAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min, max, inc));
		axes.getX1Axis().getTitle().setText("Log2 fold change");


		double y = Math.max(Math.abs(minY), Math.abs(maxY));
		
		axes.getY1Axis().setLimitsAutoRound(Math.signum(minY) * y, Math.signum(maxY) * y);
		//gp.getYAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
		//gp.getYAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min, max, inc));
		axes.getY1Axis().getTitle().setText("-Log10 p-value");

		axes.setMargins(100);

		Graph2dWindow window = new Graph2dWindow(mParent, figure);

		window.setVisible(true);
	}
}
