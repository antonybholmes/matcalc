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

public class VolcanoPlotMatrixTransform extends PlotFigureTransform {

  /**
   * Instantiates a new volcano plot matrix transform.
   *
   * @param parent      the parent
   * @param inputMatrix the input matrix
   * @param figure      the canvas
   */
  public VolcanoPlotMatrixTransform(ModernWindow parent, DataFrame m, Figure figure) {
    super(parent, "Create Volcano Plot", m, figure);
  }
}
