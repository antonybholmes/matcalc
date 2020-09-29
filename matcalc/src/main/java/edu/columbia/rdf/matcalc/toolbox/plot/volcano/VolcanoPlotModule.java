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
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.DoubleWorksheet;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.math.matrix.utils.MatrixUtils;
import org.jebtk.math.statistics.FDRType;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.icons.VolcanoPlot32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.Module;
import edu.columbia.rdf.matcalc.toolbox.supervised.SupervisedModule;
import edu.columbia.rdf.matcalc.toolbox.supervised.TestType;

/**
 * The class VolcanoPlotModule.
 */
public class VolcanoPlotModule extends Module implements ModernClickListener {

  /**
   * The constant CREATE_GROUPS_MESSAGE.
   */
  private static final String CREATE_GROUPS_MESSAGE = "You must load or create some groups.";

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Volcano Plot";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mParent = window;

    RibbonLargeButton button = new RibbonLargeButton("Volcano", "Plot", new Raster32Icon(new VolcanoPlot32VectorIcon()),
        "Volcano Plot", "Generate a volcano plot");
    button.addClickListener(this);
    // button.setEnabled(false);

    mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
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

    DataFrame m = mParent.getCurrentMatrix();

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
    XYSeries g2 = dialog.getGroup2(); // new Group("g2");

    groups = new XYSeriesGroup();
    groups.add(g1);
    groups.add(g2);

    double pvalue = dialog.getMaxP();

    boolean logData = dialog.getLog2Transform();
    boolean plot = dialog.getCreatePlot();
    TestType test = dialog.getTest();

    FDRType fdrType = dialog.getFDRType();

    // CollapseType collapseType = dialog.getCollapseType();
    // String collapseName = dialog.getCollapseName();

    volcanoPlot(mParent, m, pvalue, test, fdrType, g1, g2, logData, plot);
  }

  /**
   * Volcano plot.
   *
   * @param m             the m
   * @param minExp        the min exp
   * @param alpha         the alpha
   * @param fdrType       the fdr type
   * @param g1            the g1
   * @param g2            the g2
   * @param logData       the log data
   * @param equalVariance the equal variance
   * @param plot          the plot
   * @throws ParseException the parse exception
   */
  public static void volcanoPlot(MainMatCalcWindow parent, DataFrame m, double alpha, TestType test, FDRType fdrType,
      XYSeries g1, XYSeries g2, boolean logData, boolean plot) {

    List<MatrixGroup> groups = new ArrayList<MatrixGroup>();
    groups.add(g1);
    groups.add(g2);

    DataFrame columnFilteredM = parent.history().addToHistory("Keep group columns",
        DataFrame.copyInnerColumns(m, groups));

    DataFrame logM;

    if (logData) {
      logM = parent.history().addToHistory("log2(1 + data)",
          MatrixOperations.log2(MatrixOperations.add(columnFilteredM, 1)));
    } else {
      logM = m;
    }

    double[] p = SupervisedModule.getP(logM, g1, g2, test);

    List<Double> foldChanges = MatrixUtils.logFoldChange(logM, g1, g2);

    DataFrame annM = new DataFrame(logM);
    annM.getIndex().setAnnotation("Log2 Fold Change", foldChanges.toArray());
    parent.history().addToHistory("Add log2 fold changes", annM);

    DataFrame pValuesM = new DataFrame(annM);
    pValuesM.getIndex().setAnnotation("P-value", p);
    parent.history().addToHistory("Add p-values", pValuesM);

    // DataFrame mcollapsed = CollapseModule.collapse(pValuesM,
    // collapseName,
    // g1,
    // g2,
    // collapseType,
    // mParent);

    // mParent.history().addToHistory("Collapse rows", mcollapsed);

    double[] fdr = Statistics.fdr(pValuesM.getIndex().getValues("P-value"), fdrType);

    DataFrame fdrM = new DataFrame(pValuesM);
    fdrM.getIndex().setAnnotation("FDR", fdr);
    parent.history().addToHistory("False discovery rate", fdrM);

    // filter by fdr
    // List<IndexedValue<Integer, Double>> pValueIndices =
    // Statistics.threshold(fdr, alpha);

    // DataFrame mfdrfiltered = addFlowItem("False discovery filter",
    // new RowFilterMatrixView(mfdr, IndexedValueInt.indices(pValueIndices)));

    double[] fdrAnnotation = fdrM.getIndex().getValues("FDR");

    double[] pFoldChangeAnnotation = fdrM.getIndex().getValues("Log2 Fold Change");

    // need to convert p-values to -log10

    List<Double> minusLog10PValues = new ArrayList<Double>(fdrAnnotation.length);

    for (Double s : fdrAnnotation) {
      minusLog10PValues.add(-Mathematics.log10(s));
    }

    List<Double> log2FoldChanges = new ArrayList<Double>(pFoldChangeAnnotation.length);

    for (Double s : pFoldChangeAnnotation) {
      log2FoldChanges.add(s);
    }

    //
    // Create plot
    //

    Figure figure = Figure.createFigure();

    SubFigure subFigure = figure.currentSubFigure();

    Axes axes = subFigure.currentAxes();

    axes.getLegend().setInside(false);

    XYSeries notSigSeries1 = new XYSeries("Non-significant");
    XYSeries foldUpSeries = new XYSeries("Up");
    XYSeries foldDownSeries = new XYSeries("Down");

    notSigSeries1.getStyle().setVisible(false);
    notSigSeries1.setMarker(ShapeStyle.CIRCLE);
    notSigSeries1.getMarkerStyle().getFillStyle().setColor(ColorUtils.tint(Color.GRAY, 0.5));
    notSigSeries1.getMarkerStyle().getLineStyle().setColor(Color.GRAY);

    foldUpSeries.getStyle().setVisible(false);
    foldUpSeries.setMarker(ShapeStyle.CIRCLE);
    foldUpSeries.getMarkerStyle().getFillStyle().setColor(ColorUtils.tint(Color.RED, 0.5));
    foldUpSeries.getMarkerStyle().getLineStyle().setColor(Color.RED);

    foldDownSeries.getStyle().setVisible(false);

    foldDownSeries.setMarker(ShapeStyle.CIRCLE);
    foldDownSeries.getMarkerStyle().getFillStyle().setColor(ColorUtils.tint(Color.BLUE, 0.5));
    foldDownSeries.getMarkerStyle().getLineStyle().setColor(Color.BLUE);

    double pthres = -Mathematics.log10(alpha);

    int c;

    //
    // No Signal
    //

    Matrix foldM1 = new DoubleWorksheet(log2FoldChanges.size(), 2);

    c = 0;

    for (int i = 0; i < log2FoldChanges.size(); ++i) {
      if (minusLog10PValues.get(i) <= pthres) {
        foldM1.update(c, 0, log2FoldChanges.get(i));
        foldM1.update(c, 1, minusLog10PValues.get(i));

        ++c;
      }
    }

    double minX = 0;
    double minY = 0;
    double maxX = 1;
    double maxY = 1;

    if (foldM1.getRows() > 0) {
      DataFrame noSigM = new DataFrame(foldM1);

      noSigM.setColumnNames("Non-significant x", "Non-significant y");

      PlotFactory.createScatterPlot(noSigM, axes, notSigSeries1);

      minX = Math.min(minX, MatrixUtils.minInColumn(noSigM, 0));
      maxX = Math.max(maxX, MatrixUtils.maxInColumn(noSigM, 0));
      minY = Math.min(minY, MatrixUtils.minInColumn(noSigM, 1));
      maxY = Math.max(maxY, MatrixUtils.maxInColumn(noSigM, 1));
    }

    //
    // Down
    //

    foldM1 = new DoubleWorksheet(log2FoldChanges.size(), 2);

    c = 0;

    for (int i = 0; i < log2FoldChanges.size(); ++i) {
      if (minusLog10PValues.get(i) > pthres && log2FoldChanges.get(i) < 0) {
        foldM1.update(c, 0, log2FoldChanges.get(i));
        foldM1.update(c, 1, minusLog10PValues.get(i));

        ++c;
      }
    }

    if (foldM1.getRows() > 0) {
      DataFrame foldDownM = new DataFrame(foldM1);

      foldDownM.setColumnNames(foldDownSeries.getName() + " x", foldDownSeries.getName() + " y");

      PlotFactory.createScatterPlot(foldDownM, axes, foldDownSeries);

      minX = Math.min(minX, MatrixUtils.minInColumn(foldDownM, 0));
      maxX = Math.max(maxX, MatrixUtils.maxInColumn(foldDownM, 0));
      minY = Math.min(minY, MatrixUtils.minInColumn(foldDownM, 1));
      maxY = Math.max(maxY, MatrixUtils.maxInColumn(foldDownM, 1));
    }

    //
    // up
    //

    foldM1 = new DoubleWorksheet(log2FoldChanges.size(), 2);

    c = 0;

    for (int i = 0; i < log2FoldChanges.size(); ++i) {
      if (minusLog10PValues.get(i) > pthres && log2FoldChanges.get(i) >= 0) {
        foldM1.update(c, 0, log2FoldChanges.get(i));
        foldM1.update(c, 1, minusLog10PValues.get(i));

        ++c;
      }
    }

    if (foldM1.getRows() > 0) {
      DataFrame foldUpM = new DataFrame(foldM1);

      foldUpM.setColumnNames(foldUpSeries.getName() + " x", foldUpSeries.getName() + " y");

      PlotFactory.createScatterPlot(foldUpM, axes, foldUpSeries);

      minX = Math.min(minX, MatrixUtils.minInColumn(foldUpM, 0));
      maxX = Math.max(maxX, MatrixUtils.maxInColumn(foldUpM, 0));
      minY = Math.min(minY, MatrixUtils.minInColumn(foldUpM, 1));
      maxY = Math.max(maxY, MatrixUtils.maxInColumn(foldUpM, 1));
    }

    double x = Math.max(Math.abs(minX), Math.abs(maxX));

    axes.getX1Axis().setLimitsAutoRound(Math.signum(minX) * x, Math.signum(maxX) * x);
    axes.getX1Axis().getTitle().setText("Log2 fold change");

    double y = Math.max(Math.abs(minY), Math.abs(maxY));

    axes.getY1Axis().setLimitsAutoRound(Math.signum(minY) * y, Math.signum(maxY) * y);
    axes.getY1Axis().getTitle().setText("-Log10 p-value");

    axes.setMargins(100);

    parent.history().addToHistory(new VolcanoPlotMatrixTransform(parent, fdrM, figure));

    parent.history().addToHistory("Results", fdrM);
  }
}
