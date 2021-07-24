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

import java.io.IOException;
import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.cli.Args;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.HeatMapProps;

/**
 * The class HeatMapModule.
 */
public class LegacyHeatMapModule extends Module implements ModernClickListener {

  private static final Args ARGS = new Args();

  static {
    ARGS.add('p', "plot");
  }

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
    return "Heat Map";
  }

  @Override
  public Args getArgs() {
    return ARGS;
  }

  @Override
  public void run(ArgParser ap) {
    System.err.println("hatmap" + ap.getArgs() +  " " + ap.contains("plot"));
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
    DataFrame m = mParent.getCurrentMatrix();

    XYSeriesModel groups = XYSeriesModel.create(mParent.getGroups());

    XYSeriesModel rowGroups = XYSeriesModel.create(mParent.getRowGroups());

    CountGroups countsGroup = CountGroups.defaultGroup(m);

    List<String> history = mParent.getTransformationHistory();

    Props p = new HeatMapProps();

    scaleLargeMatrixImage(m, p);

    mParent.history()
        .addToHistory(new HeatMapPlotMatrixTransform(mParent, m, groups, rowGroups, countsGroup, history, p));
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

  /**
   * Reduce plot size to cope with large matrices.
   * 
   * @param m
   * @param properties
   */
  public static void scaleLargeMatrixImage(DataFrame m, Props properties) {

    double w = scale(m.getCols());
    double h = scale(m.getRows());

    if (w < 20 || h < 20) {
      // Don't show grid
      properties.set("plot.grid.rows.show", false);
      properties.set("plot.grid.columns.show", false);
      properties.set("plot.show-outline-color", false);
    }

    if (h < 20) {
      properties.set("plot.show-feature-counts", false);
      properties.set("plot.show-row-labels", false);
    }

    properties.set("plot.block-size", new DoubleDim(w, h));
  }

  /**
   * Scale the size of the plot size based on the row/col count so that the image
   * is not excessively large.
   * 
   * @param rows
   * @return
   */
  public static double scale(int rows) {
    if (rows > 20000) {
      return 0.1;
    } else if (rows > 10000) {
      return 0.5;
    } else if (rows > 2000) {
      return 1;
    } else if (rows > 1000) {
      return 5;
    } else if (rows > 500) {
      return 10;
    } else {
      return 20;
    }
  }

}
