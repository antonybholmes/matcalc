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
package edu.columbia.rdf.matcalc.figure;

import org.jebtk.graphplot.figure.props.GridProps;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class GridColorPlotControl.
 */
public class GridColorPlotControl extends LineStyleControl {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new grid color plot control.
   *
   * @param parent the parent
   * @param grid   the grid
   */
  public GridColorPlotControl(ModernWindow parent, GridProps grid) {
    super(parent, "Grid", grid);
  }
}