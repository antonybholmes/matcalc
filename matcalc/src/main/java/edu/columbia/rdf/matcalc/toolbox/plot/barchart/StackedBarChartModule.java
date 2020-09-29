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
package edu.columbia.rdf.matcalc.toolbox.plot.barchart;

import java.awt.Color;
import java.util.Set;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.graphplot.ColorCycle;
import org.jebtk.graphplot.PlotFactory;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class StackedBarChartModule.
 */
public class StackedBarChartModule extends Module implements ModernClickListener {

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
    return "Stacked Bar Chart";
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

    RibbonLargeButton button = new RibbonLargeButton("Stacked", "Bar Chart",
        AssetService.getInstance().loadIcon("stacked_bar_chart", 24), "Stacked Bar Chart",
        "Generate a stacked bar chart.");
    button.addClickListener(this);
    // button.setEnabled(false);

    mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
  }

  /**
   * Creates the bar chart.
   */
  private void createBarChart() {
    DataFrame m = mParent.getCurrentMatrix();

    Figure figure = Figure.createFigure(); // window.getFigure();

    SubFigure graph = figure.currentSubFigure();

    XYSeriesGroup groups = mParent.getRowGroups();

    Axes axes = graph.currentAxes();

    ColorCycle colorCycle = new ColorCycle();

    XYSeriesGroup seriesGroup = new XYSeriesGroup();

    for (int i = 0; i < m.getRows(); ++i) {
      Color color = null;

      // See if the column matches a group, and if so use the group's
      // color rather than a random color
      for (MatrixGroup group : groups) {
        Set<Integer> indices = CollectionUtils.unique(MatrixGroup.findRowIndices(m, group));

        if (indices.contains(i)) {
          color = group.getColor();

          break;
        }
      }

      if (color == null) {
        color = colorCycle.next();
      }

      XYSeries series = new XYSeries(m.getRowName(i), color);

      series.getStyle().getFillStyle().setColor(ColorUtils.getTransparentColor50(color));
      series.getStyle().getLineStyle().setColor(color);

      seriesGroup.add(series);
    }

    PlotFactory.createStackedBarPlot(m, axes, seriesGroup);

    // gp.getPlotLayout().setPlotSize(new Dimension(800, 800));

    // How big to make the x axis
    // double min = Mathematics.min(log2FoldChanges);
    // double max = Mathematics.max(log2FoldChanges);

    // gp.getXAxis().autoSetLimits(min, max);
    // gp.getXAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
    // gp.getXAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min,
    // max,
    // inc));
    axes.getX1Axis().getTitle().setText("Series");

    // min = Mathematics.min(minusLog10PValues);
    // max = Mathematics.max(minusLog10PValues);

    // System.err.println("my " + min + " " + max);

    // gp.getYAxis().autoSetLimits(min, max);
    // gp.getYAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
    // gp.getYAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min,
    // max,
    // inc));

    axes.getY1Axis().getTitle().setText("Count");

    Graph2dWindow window = new Graph2dWindow(mParent, figure);
    window.setVisible(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    createBarChart();
  }

}
