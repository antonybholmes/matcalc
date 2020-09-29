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

import org.jebtk.graphplot.figure.Figure;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.PlotMatrixTransform;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;

/**
 * Transform the rows of a matrix.
 * 
 * @author Antony Holmes
 *
 */
public class PlotFigureTransform extends PlotMatrixTransform {

  /**
   * The member canvas.
   */
  private Figure mFigure;

  /**
   * Instantiates a new volcano plot matrix transform.
   *
   * @param parent      the parent
   * @param inputMatrix the input matrix
   * @param figure      the canvas
   */
  public PlotFigureTransform(ModernWindow parent, String name, DataFrame m, Figure figure) {
    super(parent, name, m);

    mFigure = figure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.PlotMatrixTransform#createWindow()
   */
  @Override
  public Graph2dWindow createWindow() {
    return new Graph2dWindow((MainMatCalcWindow) getParent(), mFigure);
  }
}
