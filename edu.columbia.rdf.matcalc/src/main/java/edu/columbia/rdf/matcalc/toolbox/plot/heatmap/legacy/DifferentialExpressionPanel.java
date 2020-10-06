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

import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.MinMax;
import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.modern.graphics.colormap.ColorMapModel;
import org.jebtk.modern.tabs.TabsModel;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.zoom.ZoomModel;

import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ScaleModel;

/**
 * The class PatternDiscoveryPanel.
 */
public class DifferentialExpressionPanel extends HeatMapPanel {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m comparison groups. */
  private XYSeriesGroup mComparisonGroups;

  /**
   * Instantiates a new pattern discovery panel.
   *
   * @param parent                    the parent
   * @param matrix                    the matrix
   * @param groups                    the groups
   * @param comparisonGroups          the comparison groups
   * @param rowGroups                 the row groups
   * @param zoomModel                 the zoom model
   * @param colorMapModel             the color map model
   * @param colorStandardizationModel the color standardization model
   * @param intensityModel            the intensity model
   * @param contentModel              the content model
   * @param countGroups               the count groups
   * @param history                   the history
   * @param Props                     the properties
   */
  public DifferentialExpressionPanel(ModernRibbonWindow parent, DataFrame matrix, XYSeriesModel groups,
      XYSeriesGroup comparisonGroups, XYSeriesModel rowGroups, ZoomModel zoomModel, ColorMapModel colorMapModel,
      ColorNormalizationModel colorStandardizationModel, ScaleModel intensityModel, TabsModel contentModel,
      CountGroups countGroups, List<String> history, Props properties) {
    super(parent, matrix, groups, rowGroups, countGroups, history, zoomModel, colorMapModel, colorStandardizationModel,
        intensityModel, contentModel, properties);

    mComparisonGroups = comparisonGroups;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.matcalc.toolbox.plot.heatmap.legacy.HeatMapPanel#createMatrix(org.abh.
   * common.math.matrix.DataFrame, org.graphplot.figure.series.XYSeriesGroup,
   * org.graphplot.figure.series.XYSeriesGroup, double, double)
   */
  @Override
  public DataFrame createMatrix(DataFrame m, XYSeriesGroup groupsOfInterest, XYSeriesGroup rowGroupsOfInterest,
      MinMax norm) {
    // First zscore
    DataFrame ret = groupZScoreMatrix(m, mComparisonGroups, groupsOfInterest);

    ret = DataFrame.copyColumns(ret, groupsOfInterest);

    // System.err.println("copy " + ret.getColumnNames() + " " +
    // ret.getRowCount());

    // zscore the matrix
    // DataFrame zMatrix =
    // new GroupZScoreMatrixView(mColumnFiltered, groupsOfInterest));

    // ret = PatternDiscoveryModule.groupZScoreMatrix(ret,
    // mComparisonGroups,
    // groupsOfInterest);

    if (mColorStandardizationModel.get().getType() != ColorNormalizationType.NONE) {
      // min /= scale;
      // max /= scale;

      ret = MatrixOperations.scale(ret, norm);

      // System.err.println("ret " + ret.getColumnNames() + " " +
      // ret.getRowCount());
    }

    return ret;
  }

  /*
   * @Override public ModernPlotCanvas createCanvas(DataFrame m, XYSeriesGroup
   * groupsOfInterest, XYSeriesGroup rowGroupsOfInterest, double min, double max,
   * ColorMap colorMap, RowLabelProps rowLabelProperties, ColumnLabelProps
   * columnLabelProperties) { return new DifferentialExpressionCanvas(m,
   * groupsOfInterest, null, colorMap, min, max, rowLabelProperties,
   * columnLabelProperties, mGroupsElement.getProperties(),
   * mRowsElement.getShowP(), mRowsElement.getShowZ(), mHistory, mProps); }
   */

  /**
   * Group Z score matrix.
   *
   * @param <X>              the generic type
   * @param m                the m
   * @param comparisonGroups the comparison groups
   * @param groups           the groups
   * @return the annotation matrix
   */
  public static <X extends MatrixGroup> DataFrame groupZScoreMatrix(DataFrame m, XYSeriesGroup comparisonGroups,
      List<X> groups) {

    DataFrame ret = DataFrame.createNumericalMatrix(m);

    // DataFrame.copyColumnHeaders(m, ret);
    // DataFrame.copyIndex(m, ret);

    // We normalize the comparison groups separately to the the others
    List<List<Integer>> comparisonIndices = MatrixGroup.findColumnIndices(m, comparisonGroups);

    /// for (XYSeries g : comparisonGroups) {
    // System.err.println("used " + g.getName());
    // }

    // We ignore these indices when calculating the means for
    // the other groups
    // Set<Integer> used =
    // CollectionUtils.toSet(CollectionUtils.flatten(comparisonIndices));

    List<List<Integer>> groupIndices = MatrixGroup.findColumnIndices(m, groups);

    // System.err.println("all " + CollectionUtils.flatten(groupIndices));

    // Now normalize the the other groups

    for (int r = 0; r < m.getRows(); ++r) {
      double mean = 0;
      double sd = 0;

      int groupCount = 0;

      // Only take the means and sd of the comparison groups

      for (List<Integer> indices : comparisonIndices) {
        List<Double> d1 = new ArrayList<Double>(indices.size());

        for (int c : indices) {
          // Do not count indices in the comparison groups
          // since the other groups must be normalized
          // independently
          // if (!used.contains(c)) {
          d1.add(m.getValue(r, c));
          // }
        }

        mean += Statistics.mean(d1);
        sd += Statistics.popStdDev(d1); // sampleStandardDeviation

        ++groupCount;
      }

      // System.err.println("m " + mean + " " + sd + " " + groupCount);

      mean /= groupCount;
      sd /= groupCount;

      // Normalize the values
      for (List<Integer> indices : groupIndices) {
        for (int c : indices) {
          // if (!used.contains(c)) {

          if (sd != 0) {
            ret.update(r, c, (m.getValue(r, c) - mean) / sd);
          } else {
            ret.update(r, c, 0);
          }

          // }
        }
      }
    }

    // Normalize the comparisons

    /*
     * for (int i = 0; i < m.getRowCount(); ++i) { double mean = 0; double sd = 0;
     * 
     * int groupCount = 0;
     * 
     * for (List<Integer> indices : comparisonIndices) { if (indices.size() == 0) {
     * continue; }
     * 
     * List<Double> d1 = new ArrayList<Double>(indices.size());
     * 
     * for (int c : indices) { d1.add(m.getValue(i, c)); }
     * 
     * mean += Statistics.mean(d1); sd += Statistics.popStdDev(d1); //
     * sampleStandardDeviation
     * 
     * ++groupCount; }
     * 
     * mean /= groupCount; sd /= groupCount;
     * 
     * // Normalize the values for (List<Integer> indices : comparisonIndices) { if
     * (indices.size() == 0) { continue; }
     * 
     * for (int c : indices) { ret.setValue(i, c, (m.getValue(i, c) - mean) / sd); }
     * } }
     */

    return ret;
  }
}
