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
package edu.columbia.rdf.matcalc.figure.graph2d;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.FigurePanel;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.contentpane.CloseableHTab;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.figure.FigureWindow;
import edu.columbia.rdf.matcalc.figure.LegendRibbonSection;
import edu.columbia.rdf.matcalc.figure.MarginsRibbonSection;
import edu.columbia.rdf.matcalc.figure.PlotSizeModel;
import edu.columbia.rdf.matcalc.figure.PlotSizeRibbonSection;

/**
 * Window for showing 2D graphs such as a scatter plot.
 * 
 * @author Antony Holmes
 *
 */
public class Graph2dWindow extends FigureWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  // protected MultiPlotCanvas mCanvas = new MultiPlotCanvas();

  /** The m size model. */
  private PlotSizeModel mSizeModel = new PlotSizeModel();

  /** The m matrices. */
  private List<DataFrame> mMatrices = new ArrayList<DataFrame>();

  private FigurePanel mFigurePanel;

  /**
   * The class ColorMapEvents.
   */
  private class ColorMapEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      // mFigure.getCurrentSubFigure().getCurrentAxes().getCurrentPlot().setColorMap(mColorMapModel.get());

      mFigure.setColorMap(mColorMapModel.get());

    }
  }

  /**
   * The Class SizeEvents.
   */
  private class SizeEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      updateSizes();
    }
  }

  /**
   * The class ColorStandardizationEvents.
   */
  private class ColorStandardizationEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      normalizeMatrix();
    }

  }

  /**
   * Instantiates a new graph2d window.
   *
   * @param window    the window
   * @param subFigure the sub figure
   */
  public Graph2dWindow(MainMatCalcWindow window, SubFigure subFigure) {
    this(window, Figure.createFigure().addSubFigure(subFigure));
  }

  /**
   * Instantiates a new graph 2 d window.
   *
   * @param window the window
   * @param figure the figure
   */
  public Graph2dWindow(MainMatCalcWindow window, Figure figure) {
    this(window, figure, true);
  }

  /**
   * Instantiates a new graph 2 d window.
   *
   * @param window     the window
   * @param figure     the figure
   * @param allowStyle the allow style
   */
  public Graph2dWindow(MainMatCalcWindow window, Figure figure, boolean allowStyle) {
    super(window, figure, allowStyle);

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    mSizeModel.set(mFigure.currentSubFigure().currentAxes());

    mFormatPane = new AxesControlPanel(this, mFigure);

    mColorMapModel.addChangeListener(new ColorMapEvents());
    mColorModel.addChangeListener(new ColorStandardizationEvents());
    mScaleModel.addChangeListener(new ColorStandardizationEvents());
    mSizeModel.addChangeListener(new SizeEvents());
    // mColorMapModel.set((ColorMap)properties.getProperty("plot.colormap"));
    // mColorModel.set((ColorStandardization)properties.getProperty("plot.color.standardization"));

    mStyleModel.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        mFigure.setStyle(mStyleModel.get());

        // for (int z : mFigure.getSubFigureZModel()) {
        // mFigure
        // .getSubFigureZModel()
        // .getChild(z)
        // .getCurrentAxes()
        // .setStyle(mStyleModel.get());
        // }
      }
    });

    getRibbon().getToolbar("Plot")
        .add(new LegendRibbonSection(getRibbon(), mFigure.currentSubFigure().currentAxes().getLegend()));
    getRibbon().getToolbar("Layout").add(new PlotSizeRibbonSection(getRibbon(), mSizeModel));
    getRibbon().getToolbar("Layout").add(new MarginsRibbonSection(getRibbon(), mSizeModel));

    addFormatPane();

    // normalizeMatrix();

    // updateSizes();

    UI.centerWindowToScreen(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.window.ModernWindow#createUi()
   */
  @Override
  public final void createUi() {
    super.createUi();

    // ZoomCanvas zoomCanvas = new ZoomCanvas(mFigure);

    mFigurePanel = new FigurePanel(mFigure);

    mFigurePanel.setZoomModel(mZoomModel);

    // BackgroundCanvas backgroundCanvas = new BackgroundCanvas(zoomCanvas);

    ModernScrollPane scrollPane = new ModernScrollPane(mFigurePanel).setScrollBarLocation(ScrollBarLocation.FLOATING)
        .setScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW);

    ModernPanel panel = new ModernPanel(scrollPane, ModernWidget.BORDER);

    setCard(panel);
  }

  /**
   * Adds the history pane to the layout if it is not already showing.
   */
  private void addFormatPane() {
    if (!tabsPane().tabs().right().contains("Format")) {
      tabsPane().tabs().right().add("Format", new CloseableHTab("Format", mFormatPane, tabsPane()), 320, 320, 600);
    }
  }

  /**
   * Removes the format pane.
   *
   * @return the graph 2 d window
   */
  public Graph2dWindow removeFormatPane() {
    tabsPane().tabs().right().remove("Format");

    return this;
  }

  /**
   * Normalize matrix.
   */
  private void normalizeMatrix() {
    // First cache the original matrices since we are going to normalize
    // them
    if (mMatrices.size() == 0) {
      for (SubFigure subFigure : mFigure.getSubFigures()) { // for (int z :
                                                            // mFigure.getSubFigureZModel())
                                                            // {
        DataFrame m = subFigure.currentAxes().currentPlot().getMatrix();

        if (m != null) {
          mMatrices.add(m);
        }
      }
    }

    int c = 0;

    // Cycle through the matrices

    for (SubFigure subFigure : mFigure.getSubFigures()) { // for (int z :
                                                          // mFigure.getSubFigureZModel())
                                                          // {
      DataFrame m = subFigure.currentAxes().currentPlot().getMatrix();

      if (m == null) {
        continue;
      }

      double max = mScaleModel.get(); // mIntensityModel.getBaseline();
      double min = -max;

      switch (mColorModel.get().getType()) {
      case ZSCORE_MATRIX:
        m = MatrixOperations.zscore(m); // new MatrixZTransformView(mMatrix);
        break;
      case ZSCORE_ROW:
        m = MatrixOperations.rowZscore(m); // new
                                           // RowZTransformMatrixView(mMatrix);
        break;
      case ZSCORE_COLUMN:
        m = MatrixOperations.columnZscore(m); // new
                                              // ColumnZTransformMatrixView(mMatrix);
        break;
      case NORMALIZE:
        if (Double.isNaN(mColorModel.get().getMax())) {
          min = MatrixOperations.min(m);
          max = MatrixOperations.max(m);
        } else {
          min = mColorModel.get().getMin();
          max = mColorModel.get().getMax();
        }

        break;
      default:
        // For the scale of no standardization, i.e we do not adjust
        // or normalize the colors, the plot range must be extremes
        // of the matrix.
        min = MatrixOperations.min(m);
        max = MatrixOperations.max(m);

        break;
      }

      // m = MatrixOperations.minMaxThreshold(m, min, max);

      // Scale the min and max to reflect whether we are contracting
      // or expanding the range

      if (mColorModel.get().getType() != ColorNormalizationType.NONE) {
        // min /= scale;
        // max /= scale;

        m = MatrixOperations.scale(m, min, max);
      }

      System.err.println("scale " + min + " " + max + " " + MatrixOperations.min(m));

      // new MinMaxBoundedMatrixView(zMatrix, min, max);

      subFigure.setMatrix(m);
      subFigure.setColorMap(mColorMapModel.get());

      // PlotFactory.createVColorBar(min, max, subFigure, axes,
      // mColorMapModel.get());

      ++c;
    }
  }

  /**
   * Update sizes.
   */
  private void updateSizes() {
    PlotBox a = mSizeModel.get();

    Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

    stack.push(mFigure);

    while (!stack.isEmpty()) {
      PlotBox p = stack.pop();

      if (p instanceof Axes) {
        ((Axes) p).setInternalSize(a.getPreferredSize());
      }

      for (PlotBox c : p) {
        stack.push(c);
      }
    }

    // mFigure.refresh();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.graph2d.FigureWindow#getColorNormalizationModel()
   */
  public ColorNormalizationModel getColorNormalizationModel() {
    return mColorModel;
  }

  @Override
  public PlotBox getPlot() {
    return mFigure;
  }

  public Figure getFigure() {
    return mFigure;
  }
}
