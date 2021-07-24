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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.cli.Args;
import org.jebtk.graphplot.PlotFactory;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.heatmap.ColorNormalization;
import org.jebtk.graphplot.plotbox.PlotBoxGridLayout;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class HeatMapModule.
 */
public class HeatMapModule extends Module implements ModernClickListener {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  private static final Args ARGS = new Args();

  static {
    ARGS.add('p', "plot");
  }

  /** The maps. */
  private static ColorMap[] MAPS = { ColorMapService.getInstance().get("white_red"),
      ColorMapService.getInstance().get("white_green"), ColorMapService.getInstance().get("white_blue"),
      ColorMap.createWhiteToColorMap("white_orange", Color.ORANGE),
      ColorMap.createWhiteToColorMap("white_pink", Color.PINK),
      ColorMap.createWhiteToColorMap("white_gray", Color.GRAY),
      ColorMap.createWhiteToColorMap("white_black", Color.BLACK) };

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Heat Map";
  }

  @Override
  public Args getArgs() {
    return ARGS;
  }

  @Override
  public void run(ArgParser ap) {
    
    if (ap.contains("plot")) {
      try {
        plot();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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

    RibbonLargeButton button = new RibbonLargeButton("Heat Map", AssetService.getInstance().loadIcon("heatmap", 24),
        "Heat Map", "Generate a heat map.");
    button.addClickListener(this);

    mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
  }

  /**
   * Creates the.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void plot() throws IOException {
    Figure figure = Figure.createFigure();

    figure.setLayout(new PlotBoxGridLayout(1, mParent.getMatrices().size()));

    int c = 0;

    for (DataFrame m : mParent.getMatrices()) {
      SubFigure subFigure = figure.newSubFigure();

      // Add some filler space around the plot

      // subFigure.getAxesZModel().get(BorderLocation.N).addZ(new
      // SubFigureVFiller(100));
      // subFigure.getAxesZModel().get(BorderLocation.W).addZ(new
      // SubFigureHFiller(100));
      // subFigure.getAxesZModel().get(BorderLocation.E).addZ(new
      // SubFigureHFiller(100));
      // subFigure.getAxesZModel().get(BorderLocation.S).addZ(new
      // SubFigureVFiller(100));

      Axes axes = subFigure.currentAxes();

      PlotFactory.createHeatMap(m, subFigure, axes, mParent.getGroups());

      axes.getX1Axis().getTitle().setText("Series").setVisible(false);
      axes.getY1Axis().getTitle().setText("Count").setVisible(false);
      axes.getY2Axis().getTitle().setText("Count").setVisible(false);
      axes.getTitle().setText(m.getName()).setVisible(false);

      // Cycle through color maps to make plots
      axes.currentPlot().setColorMap(MAPS[c % MAPS.length]);

      ++c;
    }

    Graph2dWindow window = new Graph2dWindow(mParent, figure);

    window.getColorNormalizationModel().set(ColorNormalization.NORMALIZE);

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
    try {
      plot();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
