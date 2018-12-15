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
package edu.columbia.rdf.matcalc.toolbox.supervised;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jebtk.core.Indexed;
import org.jebtk.core.IndexedInt;
import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.cli.Args;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.sys.SysUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroup;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.MathUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.DoubleMatrixParser;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.math.statistics.FDRType;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.matcalc.History;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.icons.DiffExp32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.Module;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseModule;
import edu.columbia.rdf.matcalc.toolbox.core.collapse.CollapseType;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ClusterProperties;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy.LegacyHeatMapModule;
import edu.columbia.rdf.matcalc.toolbox.plot.volcano.VolcanoPlotModule;

/**
 * The class OneWayAnovaModule.
 */
public class SupervisedModule extends Module
implements ModernClickListener {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  private static final Logger LOG = LoggerFactory
      .getLogger(SupervisedModule.class);

  private static final int MAX_ROW_COUNT = 
      SettingsService.getInstance().getInt("matcalc.modules.supervised.max-plot-rows");

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Supervised";
  }

  @Override
  public void run(String... args) throws IOException {
    String mode = "ttest";
    Path file = null;
    Path groupFile = null;
    String prefix = TextUtils.EMPTY_STRING;

    double alpha = 1;
    double classificationAlpha = 0.05;
    int topGenes = -1;
    double minFold = 0;
    double minZ = 0;
    boolean posZ = true;
    boolean negZ = true;
    TestType test = TestType.TTEST_UNEQUAL_VARIANCE;
    FDRType fdrType = FDRType.BENJAMINI_HOCHBERG;
    boolean isLog2Data = true;

    Args options = new Args()
        .add('f', "file", true)
        .add('m', "mode", true)
        .add('g', "group-file", true)
        .add('p', "prefix", true);

    ArgParser cmdArgs = new ArgParser(options).parse(args);


    for (Entry<String, List<String>> item : cmdArgs) {
      switch (item.getKey()) {
      case "file":
        file = PathUtils.getPath(item.getValue().get(0));
      case "mode":
        mode = item.getValue().get(0);
        break;
      case "group-file":
        groupFile = PathUtils.getPath(item.getValue().get(0));
      case "prefix":
        prefix = item.getValue().get(0);
      }
    }

    String name = PathUtils.getNameNoExt(file);

    Path dir = PathUtils.getDir(file);

    DataFrame m = new DoubleMatrixParser(1, TextUtils.EMPTY_LIST, 1, TextUtils.TAB_DELIMITER)
        .parse(file);

    List<XYSeries> groups = XYSeries.loadJson(groupFile);

    if (mode.equals("ttest")) {
      cliTTest(name,
          m,
          groups,
          alpha,
          classificationAlpha,
          topGenes,
          minFold,
          minZ,
          posZ,
          negZ,
          test,
          fdrType,
          isLog2Data,
          prefix,
          dir);
    }
  }

  private static void cliTTest(String name,
      DataFrame m,
      List<XYSeries> groups,
      double alpha,
      double classificationAlpha,
      int topGenes,
      double minFold,
      double minZ,
      boolean posZ,
      boolean negZ,
      TestType test,
      FDRType fdrType,
      boolean isLog2Data,
      String prefix,
      Path dir) throws IOException {
    for (int i = 0; i < groups.size(); ++i) {
      XYSeries g1 = groups.get(i);

      for (int j = i + 1; j < groups.size(); ++j) {
        XYSeries g2 = groups.get(j);

        Path out = dir.resolve(name + "_" + prefix + "_" + g1.getName().toLowerCase() + "_" + g2.getName().toLowerCase() + "_ttest.txt");

        LOG.info("Writing {}...", out);

        DataFrame ret = ttest(m,
            alpha,
            classificationAlpha,
            minFold,
            minZ,
            posZ,
            negZ,
            topGenes,
            test,
            fdrType,
            g1,
            g2,
            isLog2Data);


        DataFrame.writeDataFrame(ret, out);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mParent = window;

    RibbonLargeButton button = new RibbonLargeButton("Supervised",
        new Raster32Icon(new DiffExp32VectorIcon()),
        "Supervised Classification", "Supervised classification.");
    button.addClickListener(this);

    mParent.getRibbon().getToolbar("Classification").getSection("Classifier")
    .add(button);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    try {
      classification();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Ttest.
   *
   * @param properties the properties
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void classification() throws IOException, ParseException {
    DataFrame m = mParent.getCurrentMatrix();

    if (m == null) {
      return;
    }

    XYSeriesModel groups = XYSeriesModel.create(mParent.getGroups());

    if (groups.getCount() == 0) {
      MainMatCalcWindow.createGroupWarningDialog(mParent);

      return;
    }

    XYSeriesModel rowGroups = XYSeriesModel.create(mParent.getRowGroups());

    SupervisedDialog dialog = new SupervisedDialog(mParent, m,
        mParent.getGroups());

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

    double minFold = dialog.getMinFoldChange();
    double alpha = dialog.getMaxP();
    double minZ = dialog.getMinZscore();
    boolean posZ = dialog.getPosZ();
    boolean negZ = dialog.getNegZ();
    boolean keepHistory = dialog.getKeepHistory();
    ComparisonType comparisonType = dialog.getComparisonType();

    int topGenes = dialog.getTopGenes();

    boolean isLog2 = dialog.getIsLog2Transformed();
    boolean log2Data = dialog.getLog2Transform();
    PlotType plotType = dialog.getCreatePlot();

    FDRType fdrType = dialog.getFDRType();

    CollapseType collapseType = dialog.getCollapseType();

    String collapseName = dialog.getCollapseName();

    double classificationAlpha = dialog.getUpDownP(); // 0.05;

    TestType testType = dialog.getTest();

    DataFrame collapsedM = CollapseModule
        .collapse(m, collapseName, g1, g2, collapseType, mParent);

    SortType sortType = dialog.getSortType();


    switch (plotType) {
    case VOLCANO:
      VolcanoPlotModule.volcanoPlot(mParent,
          collapsedM,
          alpha,
          TestType.TTEST_UNEQUAL_VARIANCE,
          fdrType,
          g1,
          g2,
          !dialog.getIsLog2Transformed() || dialog.getLog2Transform(),
          true);

      break;
    default:
      switch (comparisonType) {
      case ONE_VS_REST:
        statTestOneVsRest(collapsedM,
            alpha,
            classificationAlpha,
            minFold,
            minZ,
            posZ,
            negZ,
            topGenes,
            testType,
            fdrType,
            sortType,
            groups,
            rowGroups,
            isLog2,
            log2Data);
        break;
      case PAIRWISE:
        statTestPairwise(collapsedM,
            alpha,
            classificationAlpha,
            minFold,
            minZ,
            posZ,
            negZ,
            topGenes,
            testType,
            fdrType,
            sortType,
            groups,
            rowGroups,
            isLog2,
            log2Data);
        break;
      default:
        // One group vs another
        statTest(collapsedM,
            alpha,
            classificationAlpha,
            minFold,
            minZ,
            posZ,
            negZ,
            topGenes,
            testType,
            fdrType,
            sortType,
            g1,
            g2,
            groups,
            rowGroups,
            isLog2,
            log2Data,
            plotType,
            keepHistory);
        break;
      }
    }
  }

  /**
   * Test each group against the union of the other groups
   * 
   * @param m
   * @param alpha
   * @param classificationAlpha
   * @param minFold
   * @param minZ
   * @param posZ
   * @param negZ
   * @param topGenes
   * @param test
   * @param fdrType
   * @param g1
   * @param g2
   * @param allSeries
   * @param rowSeries
   * @param isLog2Data
   * @param log2Data
   * @param plotType
   * @throws IOException
   * @throws ParseException
   */
  public void statTestOneVsRest(DataFrame m,
      double alpha,
      double classificationAlpha,
      double minFold,
      double minZ,
      boolean posZ,
      boolean negZ,
      int topGenes,
      TestType test,
      FDRType fdrType,
      SortType sortType,
      XYSeriesModel allSeries,
      XYSeriesModel rowSeries,
      boolean isLog2Data,
      boolean log2Data) {

    for (XYSeries s : allSeries) {

      // Make a new group from all the old groups

      // Clone original
      // XYSeries s1 = XYSeries.createXYSeries(s, Color.RED);

      // Create name from union of names of other groups
      StringBuilder buffer = new StringBuilder();

      for (XYSeries st : allSeries) {
        if (!st.equals(s)) {
          buffer
          .append(TextUtils.sentenceCase(TextUtils.head(st.getName(), 5)));
        }
      }

      XYSeries s2 = XYSeries.createXYSeries(buffer.toString(), Color.BLUE);

      for (XYSeries st : allSeries) {
        if (!st.equals(s)) {
          s2.union(st);
        }
      }

      SysUtils.err().println(s2.getName(),
          MatrixGroup.findColumnIndices(m, s2));

      XYSeriesGroup newGroup = new XYSeriesGroup(s, s2);

      statTest(m,
          alpha,
          classificationAlpha,
          minFold,
          minZ,
          posZ,
          negZ,
          topGenes,
          test,
          fdrType,
          sortType,
          s,
          s2,
          XYSeriesModel.create(newGroup),
          rowSeries,
          isLog2Data,
          log2Data,
          PlotType.NONE,
          false);

    }
  }

  public void statTestPairwise(DataFrame m,
      double alpha,
      double classificationAlpha,
      double minFold,
      double minZ,
      boolean posZ,
      boolean negZ,
      int topGenes,
      TestType test,
      FDRType fdrType,
      SortType sortType,
      XYSeriesModel allSeries,
      XYSeriesModel rowSeries,
      boolean isLog2Data,
      boolean log2Data) {

    Set<XYSeries> used = new HashSet<XYSeries>();

    for (XYSeries s : allSeries) {

      for (XYSeries s2 : allSeries) {
        if (!s2.equals(s) && !used.contains(s2)) {
          XYSeriesGroup newGroup = new XYSeriesGroup(s, s2);

          statTest(m,
              alpha,
              classificationAlpha,
              minFold,
              minZ,
              posZ,
              negZ,
              topGenes,
              test,
              fdrType,
              sortType,
              s,
              s2,
              XYSeriesModel.create(newGroup),
              rowSeries,
              isLog2Data,
              log2Data,
              PlotType.NONE,
              false);
        }
      }

      used.add(s);
    }
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
   * @param plotType the plot
   * @param properties the properties
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public void statTest(DataFrame m,
      double alpha,
      double classificationAlpha,
      double minFold,
      double minZ,
      boolean posZ,
      boolean negZ,
      int topGenes,
      TestType test,
      FDRType fdrType,
      SortType sortType,
      XYSeries g1,
      XYSeries g2,
      XYSeriesModel groups,
      XYSeriesModel rowGroups,
      boolean isLog2Data,
      boolean log2Data,
      PlotType plotType,
      boolean keepHistory) {

    XYSeriesGroup comparisonGroups = new XYSeriesGroup();
    comparisonGroups.add(g1);
    comparisonGroups.add(g2);

    // DataFrame colFilteredM =
    // history.addToHistory("Extract grouped columns",
    // AnnotatableMatrix.copyInnerColumns(m, comparisonGroups));

    DataFrame colFilteredM = m;

    History history = mParent.history().keep(keepHistory);

    //
    // Remove bad gene symbols
    //

    // DataFrame mCleaned = AnnotatableMatrix.copyRows(mColumnFiltered,
    // rowAnnotationName,
    // "^---$",
    // false);

    // history.addToHistory("Filter Bad Gene Symbols", mCleaned);

    //
    // Log the matrix
    //

    DataFrame log2M;

    if (log2Data) {
      log2M = history.addToHistory("log2(1 + data)",
          MatrixOperations.log2(MatrixOperations.add(colFilteredM, 1)));
    } else {
      log2M = colFilteredM;
    }

    //
    // P-values
    //

    double[] pValues = getP(log2M, g1, g2, test);

    DataFrame pValuesM = new DataFrame(log2M);

    pValuesM.setRowAnnotations("P-value", pValues);

    // System.err.println("p " + pValues);
    // System.err.println("p2 " +
    // Arrays.toString(pValuesM.getRowAnnotationValues("P-value")));

    // Set the p-values of genes with bad names to NaN so they can be
    // excluded from analysis
    // DataFrame.setAnnotation(mpvalues,
    // "P-value",
    // DataFrame.matchRows(mpvalues, rowAnnotation, BLANK_ROW_REGEX),
    // Double.NaN);

    history.addToHistory("Add P-values", pValuesM);

    //
    // Fold Changes
    //

    double[] foldChanges;

    if (isLog2Data || log2Data) {
      foldChanges = DoubleMatrix.logFoldChange(pValuesM, g1, g2);
    } else {
      foldChanges = DoubleMatrix.foldChange(pValuesM, g1, g2);
    }

    // filter by fold changes
    // filter by fdr

    String name = isLog2Data || log2Data ? "Log2 Fold Change" : "Fold Change";

    DataFrame foldChangesM = new DataFrame(pValuesM);
    foldChangesM.setRowAnnotations(name, foldChanges);

    history.addToHistory(name, foldChangesM);

    //
    // Group means
    //

    DataFrame meansM = new DataFrame(foldChangesM);

    meansM.setRowAnnotations(g1.getName() + " mean",
        DoubleMatrix.means(foldChangesM, g1));
    meansM.setRowAnnotations(g2.getName() + " mean",
        DoubleMatrix.means(foldChangesM, g2));

    history.addToHistory("Group Means", meansM);

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

    List<Indexed<Integer, Double>> pFoldIndices = Statistics
        .outOfRange(foldChanges, negMinFold, posMinFold);

    name = isLog2Data || log2Data ? "Log2 Fold Change Filter"
        : "Fold Change Filter";

    DataFrame foldFilterM = DataFrame.copyRows(meansM,
        IndexedInt.indices(pFoldIndices));

    history.addToHistory(name, foldFilterM);

    //
    // Collapse rows
    //

    // DataFrame collapsedM = CollapseModule.collapse(foldFilterM,
    // rowAnnotation,
    // g1,
    // g2,
    // collapseType,
    // mParent);

    double[] fdr = Statistics.fdr(foldFilterM.getRowAnnotationValues("P-value"),
        fdrType);

    DataFrame mfdr = new DataFrame(foldFilterM);
    mfdr.setRowAnnotations("FDR", fdr);

    history.addToHistory("FDR", mfdr);

    // DataFrame mfdr = addFlowItem("False discovery rate",
    // new RowDataFrameView(mcollapsed,
    // "FDR",
    // ArrayUtils.toObjects(fdr)));

    // filter by fdr
    List<Indexed<Integer, Double>> pValueIndices = Statistics.threshold(fdr,
        alpha);

    DataFrame fdrFilteredM = DataFrame.copyRows(mfdr,
        IndexedInt.indices(pValueIndices));
    history.addToHistory("False discovery filter", fdrFilteredM);

    // DataFrame mfdrfiltered = addFlowItem("False discovery filter",
    // new RowFilterMatrixView(mfdr,
    // IndexedValueInt.indices(pValueIndices)));

    double[] zscores = DoubleMatrix.diffGroupZScores(fdrFilteredM, g1, g2);

    DataFrame zscoresM = new DataFrame(fdrFilteredM);

    zscoresM.setRowAnnotations("Z-score", zscores);
    history.addToHistory("Z-score", zscoresM);

    // DataFrame mzscores = addFlowItem("Add row z-scores",
    // new RowDataFrameView(mfdrfiltered,
    // "Z-score",
    // ArrayUtils.toObjects(zscores)));

    // Lets give a default classification to each row based on a p-value of 0.05
    // and
    // a zscore > 1

    
    Matrix im = zscoresM.getMatrix();

    String[] classifications = new String[im.getRows()];

    
    for (int i = 0; i < im.getRows(); ++i) {
      String classification = "not_expressed";

      if (MatrixOperations.sumRow(im, i) > 0) {
        classification = "not_moving";
      }

      double zscore = zscoresM.getRowAnnotationValue("Z-score", i);
      double p = zscoresM.getRowAnnotationValue("FDR", i);

      // if (p <= 0.05) {
      if (p <= classificationAlpha) {
        if (zscore > 0) {
          classification = "up";
        } else if (zscore < 0) {
          classification = "down";
        } else {
          // do nothing
        }
      }

      classifications[i] = classification;
    }

    String comparison = g1.getName() + "_vs_" + g2.getName() + " (p <= "
        + classificationAlpha + ")";

    DataFrame classM = new DataFrame(zscoresM);

    classM.setRowAnnotations(comparison, classifications);

    history.addToHistory("Add row classification", classM);

    //
    // Sort into up/down categories such as zscores up and zscore down
    //

    List<Indexed<Integer, Double>> scoresIndexed;

    switch(sortType) {
    case FOLD_CHANGE:
      scoresIndexed = IndexedInt.index(foldChanges);
      break;
    default:
      scoresIndexed = IndexedInt.index(zscores);
      break;
    }

    List<Indexed<Integer, Double>> posScores;

    if (posZ) {
      posScores = CollectionUtils.reverseSort(CollectionUtils
          .subList(scoresIndexed, MathUtils.ge(scoresIndexed, minZ)));
    } else {
      posScores = Collections.emptyList();
    }

    List<Indexed<Integer, Double>> negScores;

    if (negZ) {
      negScores = CollectionUtils.sort(CollectionUtils.subList(scoresIndexed,
          MathUtils.lt(scoresIndexed, -minZ)));
    } else {
      negScores = Collections.emptyList();
    }

    // Filter for top genes if necessary

    List<Integer> ui = Indexed.indices(posScores);
    List<Integer> li = Indexed.indices(negScores);

    if (topGenes != -1) {
      ui = CollectionUtils.head(ui, topGenes);
      li = CollectionUtils.head(li, topGenes);
    }

    // Now make a list of the new zscores in the correct order,
    // positive decreasing, negative, decreasing
    // List<IndexedValue<Integer, Double>> sortedZscores =
    // CollectionUtils.append(posZScores, negZScores);

    // Put the scores in order

    List<Integer> indices = CollectionUtils.append(ui, li); // IndexedValue.indices(sortedZscores);

    DataFrame mDeltaSorted = DataFrame.copyRows(classM, indices);

    switch(sortType) {
    case FOLD_CHANGE:
      history.addToHistory("Sort by row fold change", mDeltaSorted);
      break;
    default:
      history.addToHistory("Sort by row z-score", mDeltaSorted);
      break;
    }

    // DataFrame mDeltaSorted = addFlowItem("Sort by row z-score",
    // new RowFilterMatrixView(mclassification, indices));

    DataFrame mNormalized = MatrixOperations.groupZScore(mDeltaSorted,
        comparisonGroups);

    history.addToHistory("Normalize expression within groups", mNormalized);

    // DataFrame mNormalized = addFlowItem("Normalize expression within groups",
    // new GroupZScoreMatrixView(mDeltaSorted, groups));

    // DataFrame mMinMax = addFlowItem("Min/max threshold",
    // "min: " + Plot.MIN_STD + ", max: "+ Plot.MAX_STD,
    // new MinMaxBoundedMatrixView(mNormalized,
    // Plot.MIN_STD,
    // Plot.MAX_STD));

    // DataFrame mStandardized =
    // addFlowItem("Row normalize", new RowNormalizedMatrixView(mMinMax));

    CountGroups countGroups = new CountGroups()
        .add(new CountGroup("up", 0, ui.size() - 1))
        .add(new CountGroup("down", ui.size(), indices.size() - 1));

    if (plotType != PlotType.NONE) {
      boolean plot = true;

      if (mNormalized.getRows() > MAX_ROW_COUNT) {
        // Warn users that they are going to make a huge plot
        ModernDialogStatus status = ModernMessageDialog.createDialog(mParent, 
            MessageDialogType.WARNING_OK_CANCEL,
            "You are trying to plot more than " + MAX_ROW_COUNT + " rows.",
            "Are you sure you want to create this plot?");

        if (status == ModernDialogStatus.CANCEL) {
          plot = false;
        }
      }

      if (plot) {
        ClusterProperties p = new ClusterProperties();
        
        LegacyHeatMapModule.scaleLargeMatrixImage(m, p);
        
        mParent.history().addToHistory(new HeatMapMatrixTransform(mParent, mNormalized,
            groups, comparisonGroups, rowGroups, countGroups,
            mParent.getTransformationHistory(), p));
      }
    }

    // Add a reference at the end so that it is easy for users to find
    // the matrix they probably want the most
    mParent.history().addToHistory("Results", mDeltaSorted);
  }



  public static DataFrame ttest(DataFrame m,
      double alpha,
      double classificationAlpha,
      double minFold,
      double minZ,
      boolean posZ,
      boolean negZ,
      int topGenes,
      TestType test,
      FDRType fdrType,
      XYSeries g1,
      XYSeries g2,
      boolean isLog2Data) {

    XYSeriesGroup comparisonGroups = new XYSeriesGroup();
    comparisonGroups.add(g1);
    comparisonGroups.add(g2);

    // DataFrame colFilteredM =
    // history.addToHistory("Extract grouped columns",
    // AnnotatableMatrix.copyInnerColumns(m, comparisonGroups));

    //
    // Remove bad gene symbols
    //

    // DataFrame mCleaned = AnnotatableMatrix.copyRows(mColumnFiltered,
    // rowAnnotationName,
    // "^---$",
    // false);

    // history.addToHistory("Filter Bad Gene Symbols", mCleaned);

    //
    // P-values
    //

    double[] pValues = getP(m, g1, g2, test);

    DataFrame pValuesM = new DataFrame(m);

    pValuesM.setRowAnnotations("P-value", pValues);

    //System.err.println("p " + pValues);
    // System.err.println("p2 " +
    // Arrays.toString(pValuesM.getRowAnnotationValues("P-value")));

    // Set the p-values of genes with bad names to NaN so they can be
    // excluded from analysis
    // DataFrame.setAnnotation(mpvalues,
    // "P-value",
    // DataFrame.matchRows(mpvalues, rowAnnotation, BLANK_ROW_REGEX),
    // Double.NaN);

    //
    // Fold Changes
    //

    double[] foldChanges;

    if (isLog2Data) {
      foldChanges = DoubleMatrix.logFoldChange(pValuesM, g1, g2);
    } else {
      foldChanges = DoubleMatrix.foldChange(pValuesM, g1, g2);
    }

    // filter by fold changes
    // filter by fdr

    DataFrame foldChangesM = new DataFrame(pValuesM);

    foldChangesM.setRowAnnotations(isLog2Data ? "Log2 Fold Change" : "Fold Change", foldChanges);

    //
    // Group means
    //

    DataFrame meansM = new DataFrame(foldChangesM);

    meansM.setRowAnnotations(g1.getName() + " mean",
        DoubleMatrix.means(foldChangesM, g1));
    meansM.setRowAnnotations(g2.getName() + " mean",
        DoubleMatrix.means(foldChangesM, g2));

    //
    // Fold change filter
    //

    double posMinFold = minFold;
    double negMinFold = posMinFold;

    if (minFold > 0) {
      if (isLog2Data) {
        negMinFold = -negMinFold;
      } else {
        negMinFold = 1.0 / negMinFold;
      }
    }

    List<Indexed<Integer, Double>> pFoldIndices = Statistics
        .outOfRange(foldChanges, negMinFold, posMinFold);

    DataFrame foldFilterM = DataFrame.copyRows(meansM,
        IndexedInt.indices(pFoldIndices));

    //
    // Collapse rows
    //

    // DataFrame collapsedM = CollapseModule.collapse(foldFilterM,
    // rowAnnotation,
    // g1,
    // g2,
    // collapseType,
    // mParent);

    double[] fdr = Statistics.fdr(foldFilterM.getRowAnnotationValues("P-value"),
        fdrType);

    DataFrame mfdr = new DataFrame(foldFilterM);
    mfdr.setRowAnnotations("FDR", fdr);

    // DataFrame mfdr = addFlowItem("False discovery rate",
    // new RowDataFrameView(mcollapsed,
    // "FDR",
    // ArrayUtils.toObjects(fdr)));

    // filter by fdr
    List<Indexed<Integer, Double>> pValueIndices = Statistics.threshold(fdr,
        alpha);

    DataFrame fdrFilteredM = DataFrame.copyRows(mfdr,
        IndexedInt.indices(pValueIndices));
    // DataFrame mfdrfiltered = addFlowItem("False discovery filter",
    // new RowFilterMatrixView(mfdr,
    // IndexedValueInt.indices(pValueIndices)));

    double[] zscores = DoubleMatrix.diffGroupZScores(fdrFilteredM, g1, g2);

    DataFrame zscoresM = new DataFrame(fdrFilteredM);

    zscoresM.setRowAnnotations("Z-score", zscores);

    // DataFrame mzscores = addFlowItem("Add row z-scores",
    // new RowDataFrameView(mfdrfiltered,
    // "Z-score",
    // ArrayUtils.toObjects(zscores)));

    // Lets give a default classification to each row based on a p-value of 0.05
    // and
    // a zscore > 1

    
    Matrix im = zscoresM.getMatrix();

    String[] classifications = new String[im.getRows()];

    
    for (int i = 0; i < im.getRows(); ++i) {
      String classification = "not_expressed";

      if (MatrixOperations.sumRow(im, i) > 0) {
        classification = "not_moving";
      }

      double zscore = zscoresM.getRowAnnotationValue("Z-score", i);
      double p = zscoresM.getRowAnnotationValue("FDR", i);

      // if (p <= 0.05) {
      if (p <= classificationAlpha) {
        if (zscore > 0) {
          classification = "up";
        } else if (zscore < 0) {
          classification = "down";
        } else {
          // do nothing
        }
      }

      classifications[i] = classification;
    }

    String comparison = g1.getName() + "_vs_" + g2.getName() + " (p <= "
        + classificationAlpha + ")";

    DataFrame classM = new DataFrame(zscoresM);

    classM.setRowAnnotations(comparison, classifications);

    // DataFrame mclassification = addFlowItem("Add row classification",
    // new RowDataFrameView(mzscores,
    // comparison,
    // ArrayUtils.toObjects(classifications)));

    List<Indexed<Integer, Double>> zscoresIndexed = IndexedInt.index(zscores);

    List<Indexed<Integer, Double>> posZScores;

    if (posZ) {
      posZScores = CollectionUtils.reverseSort(CollectionUtils
          .subList(zscoresIndexed, MathUtils.ge(zscoresIndexed, minZ)));
    } else {
      posZScores = Collections.emptyList(); // new ArrayList<Indexed<Integer,
      // Double>>();
    }

    List<Indexed<Integer, Double>> negZScores;

    if (negZ) {
      negZScores = CollectionUtils.sort(CollectionUtils.subList(zscoresIndexed,
          MathUtils.lt(zscoresIndexed, -minZ)));
    } else {
      negZScores = Collections.emptyList(); // new ArrayList<Indexed<Integer,
      // Double>>();
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
    // List<IndexedValue<Integer, Double>> sortedZscores =
    // CollectionUtils.append(posZScores, negZScores);

    // Put the zscores in order

    List<Integer> indices = CollectionUtils.append(ui, li); // IndexedValue.indices(sortedZscores);

    DataFrame mDeltaSorted = DataFrame.copyRows(classM, indices);

    // DataFrame mDeltaSorted = addFlowItem("Sort by row z-score",
    // new RowFilterMatrixView(mclassification, indices));

    DataFrame mNormalized = MatrixOperations.groupZScore(mDeltaSorted,
        comparisonGroups);

    // DataFrame mNormalized = addFlowItem("Normalize expression within groups",
    // new GroupZScoreMatrixView(mDeltaSorted, groups));

    // DataFrame mMinMax = addFlowItem("Min/max threshold",
    // "min: " + Plot.MIN_STD + ", max: "+ Plot.MAX_STD,
    // new MinMaxBoundedMatrixView(mNormalized,
    // Plot.MIN_STD,
    // Plot.MAX_STD));

    // DataFrame mStandardized =
    // addFlowItem("Row normalize", new RowNormalizedMatrixView(mMinMax));

    return mDeltaSorted;
  }

  public static double[] getP(DataFrame m,
      XYSeries g1,
      XYSeries g2,
      TestType test) {
    double[] pValues;

    switch (test) {
    case MANN_WHITNEY:
      pValues = MatrixOperations.mannWhitney(m, g1, g2);
      break;
    default:
      pValues = MatrixOperations
      .tTest(m, g1, g2, test == TestType.TTEST_EQUAL_VARIANCE);
    }

    return pValues;
  }
}
