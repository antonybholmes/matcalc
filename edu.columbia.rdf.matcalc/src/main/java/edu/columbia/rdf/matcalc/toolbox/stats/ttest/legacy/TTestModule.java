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
package edu.columbia.rdf.matcalc.toolbox.stats.ttest.legacy;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.Indexed;
import org.jebtk.core.IndexedInt;
import org.jebtk.core.Properties;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroup;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.MathUtils;
import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.math.statistics.FDRType;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.icons.DiffExp32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseModule;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseType;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ClusterProperties;

// TODO: Auto-generated Javadoc
/**
 * The class OneWayAnovaModule.
 */
public class TTestModule extends CalcModule implements ModernClickListener {
	
	/**
	 * The member parent.
	 */
	private MainMatCalcWindow mParent;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "T-Test";
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mParent = window;
		
		RibbonLargeButton button = new RibbonLargeButton("T-Test", 
				new Raster32Icon(new DiffExp32VectorIcon()),
				"T-Test",
				"Supervised clustering using a T-test.");
		button.addClickListener(this);

		mParent.getRibbon().getToolbar("Statistics").getSection("Statistics").add(button);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		try {
			ttest();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Ttest.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private void ttest() throws IOException, ParseException {
		ttest(new ClusterProperties());
	}

	/**
	 * Ttest.
	 *
	 * @param properties the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private void ttest(Properties properties) throws IOException, ParseException {
		AnnotationMatrix m = mParent.getCurrentMatrix();
		
		if (m == null) {
			return;
		}

		XYSeriesModel groups = XYSeriesModel.create(mParent.getGroups());

		if (groups.getCount() == 0) {
			MainMatCalcWindow.createGroupWarningDialog(mParent);

			return;
		}
		
		XYSeriesModel rowGroups = XYSeriesModel.create(mParent.getRowGroups());

		TTestDialog dialog = new TTestDialog(mParent, m, mParent.getGroups());

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
		XYSeries g2 = dialog.getGroup2(); // new Group("g2");


		double minExp = dialog.getMinExp();
		double minFold = dialog.getMinFoldChange();
		double alpha = dialog.getMaxP();
		double minZ = dialog.getMinZscore();
		boolean posZ = dialog.getPosZ();
		boolean negZ = dialog.getNegZ();

		int topGenes = dialog.getTopGenes();
		
		boolean isLog2 = dialog.getIsLog2Transformed();
		boolean log2Data = dialog.getLog2Transform();
		boolean equalVariance = dialog.getEqualVariance();
		boolean plot = dialog.getCreatePlot();

		FDRType fdrType = dialog.getFDRType();

		CollapseType collapseType = dialog.getCollapseType();

		String collapseName = dialog.getCollapseName();

		double classificationAlpha = dialog.getUpDownP(); //0.05;

		ttest(m, 
				minExp, 
				alpha,
				classificationAlpha,
				minFold,
				minZ,
				posZ,
				negZ,
				topGenes,
				collapseType,
				collapseName,
				fdrType, 
				g1, 
				g2,
				groups,
				rowGroups,
				isLog2,
				log2Data, 
				equalVariance, 
				plot,
				properties);
	}

	/**
	 * Ttest.
	 *
	 * @param m the m
	 * @param minExp the min exp
	 * @param alpha the alpha
	 * @param classificationAlpha the classification alpha
	 * @param minFold the min fold
	 * @param minZ the min z
	 * @param posZ the pos z
	 * @param negZ the neg z
	 * @param topGenes the top genes
	 * @param collapseType the collapse type
	 * @param rowAnnotation the row annotation
	 * @param fdrType the fdr type
	 * @param g1 the g1
	 * @param g2 the g2
	 * @param groups the groups
	 * @param rowGroups the row groups
	 * @param isLog2Data the is log2 data
	 * @param log2Data the log2 data
	 * @param equalVariance the equal variance
	 * @param plot the plot
	 * @param properties the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public void ttest(AnnotationMatrix m,
			double minExp,
			double alpha,
			double classificationAlpha,
			double minFold,
			double minZ,
			boolean posZ,
			boolean negZ,
			int topGenes,
			CollapseType collapseType,
			String rowAnnotation,
			FDRType fdrType,
			XYSeries g1, 
			XYSeries g2,
			XYSeriesModel groups,
			XYSeriesModel rowGroups,
			boolean isLog2Data,
			boolean log2Data,
			boolean equalVariance,
			boolean plot,
			Properties properties) throws IOException, ParseException {

		XYSeriesGroup comparisonGroups = new XYSeriesGroup();
		comparisonGroups.add(g1);
		comparisonGroups.add(g2);

		//AnnotationMatrix colFilteredM = 
		//		mParent.addToHistory("Extract grouped columns", 
		//				AnnotatableMatrix.copyInnerColumns(m, comparisonGroups));

		
		AnnotationMatrix colFilteredM = m;
		
		//
		// Remove bad gene symbols
		//

		//AnnotationMatrix mCleaned = AnnotatableMatrix.copyRows(mColumnFiltered, 
		//		rowAnnotationName, 
		//		"^---$", 
		//		false);

		//mParent.addToHistory("Filter Bad Gene Symbols", mCleaned);

		//
		// Log the matrix
		//
		
		AnnotationMatrix log2M;

		if (log2Data) {
			log2M = mParent.addToHistory("Log 2",
					"Log 2", 
					MatrixOperations.log2(MatrixOperations.min(colFilteredM, minExp)));
		} else {
			log2M = colFilteredM;
		}

		//
		// P-values
		//

		List<Double> pValues = 
				MatrixOperations.tTest(log2M, g1, g2, equalVariance);

		AnnotationMatrix pValuesM = new AnnotatableMatrix(log2M);
				
		pValuesM.setNumRowAnnotations("P-value", pValues);
		
		//System.err.println("p " + pValues);
		//System.err.println("p2 " + Arrays.toString(pValuesM.getRowAnnotationValues("P-value")));
		
		// Set the p-values of genes with bad names to NaN so they can be
		// excluded from analysis
		//AnnotationMatrix.setAnnotation(mpvalues,
		//		"P-value",
		//		AnnotationMatrix.matchRows(mpvalues, rowAnnotation, BLANK_ROW_REGEX),
		//		Double.NaN);

		mParent.addToHistory("Add P-values", pValuesM);

		//
		// Fold Changes
		//

		List<Double> foldChanges;

		if (isLog2Data || log2Data) {
			foldChanges = DoubleMatrix.logFoldChange(pValuesM, g1, g2);
		} else {
			foldChanges = DoubleMatrix.foldChange(pValuesM, g1, g2);
		}

		// filter by fold changes
		// filter by fdr

		String name = isLog2Data || log2Data ? "Log2 Fold Change" : "Fold Change";

		AnnotationMatrix foldChangesM = new AnnotatableMatrix(pValuesM);
		foldChangesM.setNumRowAnnotations(name, foldChanges);
		
		mParent.addToHistory(name, foldChangesM);


		//
		// Group means
		//

		AnnotationMatrix meansM = new AnnotatableMatrix(foldChangesM);

		meansM.setNumRowAnnotations(g1.getName() + " mean", 
				DoubleMatrix.means(foldChangesM, g1));
		meansM.setNumRowAnnotations(g2.getName() + " mean", 
				DoubleMatrix.means(foldChangesM, g2));

		mParent.addToHistory("Group Means", meansM);

		//
		// Fold change filter
		//

		double posMinFold = minFold;
		double negMinFold = posMinFold;

		if (minFold > 0) {
			if (isLog2Data || log2Data) {
				negMinFold = -negMinFold;
			} else {
				negMinFold = 1.0 / negMinFold;
			}
		}

		List<Indexed<Integer, Double>> pFoldIndices = 
				Statistics.outOfRange(foldChanges, negMinFold, posMinFold);

		name = isLog2Data || log2Data ? "Log2 Fold Change Filter" : "Fold Change Filter";
		
		AnnotationMatrix foldFilterM = AnnotatableMatrix.copyRows(meansM, 
				IndexedInt.indices(pFoldIndices));
		
		
		
		mParent.addToHistory(name, foldFilterM);

		//
		// Collapse rows
		//

		AnnotationMatrix collapsedM = CollapseModule.collapse(foldFilterM,
				rowAnnotation,
				g1,
				g2,
				collapseType,
				mParent);

		double[] fdr = Statistics.fdr(collapsedM.getRowAnnotationValues("P-value"), 
				fdrType);
		
		AnnotationMatrix mfdr = new AnnotatableMatrix(collapsedM);
		mfdr.setNumRowAnnotations("FDR", fdr);

		mParent.addToHistory("FDR", mfdr);

		//AnnotationMatrix mfdr = addFlowItem("False discovery rate", 
		//		new RowAnnotationMatrixView(mcollapsed, 
		//				"FDR", 
		//				ArrayUtils.toObjects(fdr)));



		// filter by fdr
		List<Indexed<Integer, Double>> pValueIndices = 
				Statistics.threshold(fdr, alpha);
		
		AnnotationMatrix fdrFilteredM = 
				AnnotatableMatrix.copyRows(mfdr, IndexedInt.indices(pValueIndices));
		mParent.addToHistory("False discovery filter", fdrFilteredM);

		//AnnotationMatrix mfdrfiltered = addFlowItem("False discovery filter", 
		//		new RowFilterMatrixView(mfdr, 
		//				IndexedValueInt.indices(pValueIndices)));


		List<Double> zscores = 
				DoubleMatrix.diffGroupZScores(fdrFilteredM, g1, g2);

		AnnotationMatrix zscoresM = new AnnotatableMatrix(fdrFilteredM);

		zscoresM.setNumRowAnnotations("Z-score", zscores);
		mParent.addToHistory("Z-score", zscoresM);


		//AnnotationMatrix mzscores = addFlowItem("Add row z-scores", 
		//		new RowAnnotationMatrixView(mfdrfiltered, 
		//				"Z-score", 
		//				ArrayUtils.toObjects(zscores)));

		// Lets give a default classification to each row based on a p-value of 0.05 and
		// a zscore > 1

		List<String> classifications = new ArrayList<String>();
		
		Matrix im = zscoresM.getInnerMatrix();

		for (int i = 0; i < im.getRowCount(); ++i) {
			String classification = "not_expressed";
			
			if (MatrixOperations.sumRow(im, i) > 0) {
				classification = "not_moving";
			}

			double zscore = zscoresM.getRowAnnotationValue("Z-score", i);
			double p = zscoresM.getRowAnnotationValue("FDR", i);

			//if (p <= 0.05) {
			if (p <= classificationAlpha) {	
				if (zscore > 0) {
					classification = "up";
				} else if (zscore < 0) {
					classification = "down";
				} else {
					// do nothing
				}
			}

			classifications.add(classification);
		}

		String comparison = g1.getName() + "vs" + g2.getName() + " (p <= " + classificationAlpha + ")";

		AnnotationMatrix classM = new AnnotatableMatrix(zscoresM);
		
		classM.setTextRowAnnotations(comparison, classifications);
		
		mParent.addToHistory("Add row classification", classM);
		

		//AnnotationMatrix mclassification = addFlowItem("Add row classification", 
		//		new RowAnnotationMatrixView(mzscores, 
		//				comparison, 
		//				ArrayUtils.toObjects(classifications)));

		List<Indexed<Integer, Double>> zscoresIndexed = 
				IndexedInt.index(zscores);

		List<Indexed<Integer, Double>> posZScores;

		if (posZ) {
			posZScores = CollectionUtils.reverseSort(CollectionUtils.subList(zscoresIndexed, 
					MathUtils.ge(zscoresIndexed, minZ)));
		} else {
			posZScores = new ArrayList<Indexed<Integer, Double>>();
		}
		
		List<Indexed<Integer, Double>> negZScores;

		if (negZ) {
			negZScores = CollectionUtils.sort(CollectionUtils.subList(zscoresIndexed, 
					MathUtils.lt(zscoresIndexed, -minZ)));
		} else {
			negZScores = new ArrayList<Indexed<Integer, Double>>();
		}
		
		// Filter for top genes if necessary
		
		List<Integer> ui = Indexed.indices(posZScores);
		List<Integer> li = Indexed.indices(negZScores);
		
		if (topGenes != -1) {
			ui = CollectionUtils.head(ui, topGenes);
			li = CollectionUtils.head(li, topGenes);
		}

		
		// Now make a list of the new zscores in the correct order,
		// positive decreasing, negative, decreasing
		//List<IndexedValue<Integer, Double>> sortedZscores = 
		//		CollectionUtils.append(posZScores, negZScores);

		// Put the zscores in order

		List<Integer> indices = CollectionUtils.append(ui, li); // IndexedValue.indices(sortedZscores);

		AnnotationMatrix mDeltaSorted = 
				AnnotatableMatrix.copyRows(classM, indices);

		mParent.addToHistory("Sort by row z-score", mDeltaSorted);

		//AnnotationMatrix mDeltaSorted = addFlowItem("Sort by row z-score", 
		//		new RowFilterMatrixView(mclassification, indices));

		AnnotationMatrix mNormalized = 
				MatrixOperations.groupZScore(mDeltaSorted, comparisonGroups);

		mParent.addToHistory("Normalize expression within groups", mNormalized);

		//AnnotationMatrix mNormalized = addFlowItem("Normalize expression within groups", 
		//		new GroupZScoreMatrixView(mDeltaSorted, groups));



		//AnnotationMatrix mMinMax = addFlowItem("Min/max threshold", 
		//		"min: " + Plot.MIN_STD + ", max: "+ Plot.MAX_STD,
		//		new MinMaxBoundedMatrixView(mNormalized, 
		//				Plot.MIN_STD, 
		//				Plot.MAX_STD));

		//AnnotationMatrix mStandardized = 
		//		addFlowItem("Row normalize", new RowNormalizedMatrixView(mMinMax));

		if (plot) {
			List<String> history = mParent.getTransformationHistory();
			
			CountGroups countGroups = new CountGroups()
					.add(new CountGroup("up", 0, ui.size() - 1))
					.add(new CountGroup("down", ui.size(), indices.size() - 1));
			
			
			mParent.addToHistory(new TTestPlotMatrixTransform(mParent, 
					mNormalized, 
					groups,
					comparisonGroups,
					rowGroups,
					countGroups,
					history, 
					properties));
		}
		
		// Add a reference at the end so that it is easy for users to find
		// the matrix they probably want the most
		mParent.addToHistory("Results", mDeltaSorted);
	}
}
