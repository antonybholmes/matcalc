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
package edu.columbia.rdf.matcalc.toolbox.plot.boxwhisker;

import java.awt.Color;

import org.jebtk.graphplot.PlotFactory;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.icons.BoxWhiskerScatter32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class BoxWhiskerPlotModule.
 */
public class BoxWhiskerScatterPlotModule extends Module implements ModernClickListener {

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
    return "Box Whisker Scatter Plot";
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

    RibbonLargeButton button = new RibbonLargeButton("Box Whisker Scatter",
        new Raster32Icon(new BoxWhiskerScatter32VectorIcon()), "Box Whisker Scatter Plot",
        "Generate a box whisker scatter plot");
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
    boxWhiskerPlot();
  }

  /**
   * Box whisker plot.
   */
  private void boxWhiskerPlot() {
    DataFrame m = mParent.getCurrentMatrix();

    boxWhiskerPlot(m, true);
  }

  /**
   * Box whisker plot.
   *
   * @param m    the m
   * @param plot the plot
   */
  private void boxWhiskerPlot(DataFrame m, boolean plot) {
    Figure figure = Figure.createFigure(); // window.getFigure();

    SubFigure graph = figure.currentSubFigure();

    Axes axes = graph.currentAxes();

    XYSeriesGroup allSeries = new XYSeriesGroup("Box Whisker");

    for (int c = 0; c < m.getCols(); ++c) {
      XYSeries series = new XYSeries(m.getColumnName(c), Color.BLACK);

      allSeries.add(series);
    }

    PlotFactory.createBoxWhiskerScatterPlot(m, axes, allSeries);

    Graph2dWindow window = new Graph2dWindow(mParent, figure);

    window.setVisible(true);
  }
}
