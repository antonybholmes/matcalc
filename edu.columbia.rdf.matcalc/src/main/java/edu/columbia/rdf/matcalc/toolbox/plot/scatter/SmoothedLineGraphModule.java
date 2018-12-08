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
package edu.columbia.rdf.matcalc.toolbox.plot.scatter;

import java.awt.Color;
import java.util.Map.Entry;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.ColorCycle;
import org.jebtk.graphplot.PlotFactory;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;

/**
 * The class SmoothedLineGraphModule.
 */
public class SmoothedLineGraphModule extends CalcModule
    implements ModernClickListener {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  /**
   * The member axes.
   */
  private Axes mAxes;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Line Graph";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.apps.matcalc.modules.CalcModule#run(java.lang.String[])
   */
  public void run(String... args) {
    plot();

    for (String a : args) {
      Entry<String, String> arg = ArgParser.parsePosixArg(a);

      if (arg.getKey().equals("switch-tab")) {
        mParent.getRibbon().changeTab("Plot");
      } else if (arg.getKey().equals("x-axis-name")) {
        mAxes.getX1Axis().getTitle().setText(arg.getValue());
      } else if (arg.getKey().equals("y-axis-name")) {
        mAxes.getY1Axis().getTitle().setText(arg.getValue());
      } else if (arg.getKey().equals("show-legend")) {
        mAxes.getLegend().setVisible(true);
      } else if (arg.getKey().equals("x-min")) {
        mAxes.getX1Axis().setMin(TextUtils.parseDouble(arg.getValue()));
      } else if (arg.getKey().equals("x-max")) {
        mAxes.getX1Axis().setMax(TextUtils.parseDouble(arg.getValue()));
      } else {
        // do nothing
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

    RibbonLargeButton button = new RibbonLargeButton("Line",
        AssetService.getInstance().loadIcon("line_graph", 24), "Line Graph",
        "Generate a line graph.");
    // button.setShowText(false);
    button.addClickListener(this);
    // button.setEnabled(false);

    mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
  }

  /**
   * Creates the plot.
   */
  private void plot() {
    DataFrame m = mParent.getCurrentMatrix();

    if (m == null) {
      showLoadMatrixError(mParent);

      return;
    }

    Figure figure = Figure.createFigure(); // window.getFigure();

    SubFigure subFigure = figure.currentSubFigure();

    mAxes = subFigure.currentAxes();

    mAxes.setMargins(100);

    ColorCycle colorCycle = new ColorCycle();

    for (int i = 0; i < m.getCols(); i += 2) {
      Color color = colorCycle.next();

      XYSeries series = new XYSeries(
          TextUtils.commonPrefix(m.getColumnName(i), m.getColumnName(i + 1)),
          color);

      series.getStyle().getFillStyle()
          .setColor(ColorUtils.getTransparentColor50(color));
      series.getStyle().getLineStyle().setColor(color);

      PlotFactory.createSplineLinePlot(m, mAxes, series);
    }

    mAxes.setAxisLimitsAutoRound();

    // gp.getPlotLayout().setPlotSize(new Dimension(800, 800));

    // How big to make the x axis
    // double min = Mathematics.min(log2FoldChanges);
    // double max = Mathematics.max(log2FoldChanges);

    // gp.getXAxis().autoSetLimits(min, max);
    // gp.getXAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
    // gp.getXAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min,
    // max,
    // inc));
    // mAxes.getXAxis().getTitle().setText("Series");

    // min = Mathematics.min(minusLog10PValues);
    // max = Mathematics.max(minusLog10PValues);

    // System.err.println("my " + min + " " + max);

    // gp.getYAxis().autoSetLimits(min, max);
    // gp.getYAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
    // gp.getYAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min,
    // max,
    // inc));
    // mAxes.getY1Axis().getTitle().setText("Count");

    // mWindow.addToHistory(new BarChartMatrixTransform(mWindow, m, canvas));

    Graph2dWindow window = new Graph2dWindow(mParent, figure);

    window.setVisible(true);
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
    plot();
  }

}
