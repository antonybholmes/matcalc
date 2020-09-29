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

import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.FillStyleControl;
import edu.columbia.rdf.matcalc.figure.LineStyleControl;

/**
 * The class XYSeriesPlotControl.
 */
public class StyleControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new XY series plot control.
   *
   * @param parent the parent
   * @param style  the style
   */
  public StyleControl(ModernWindow parent, StyleProps style) {
    add(new LineStyleControl(parent, style.getLineStyle()));

    add(ModernPanel.createVGap());

    add(new FillStyleControl(parent, style.getFillStyle()));

    add(ModernPanel.createVGap());

    // setBorder(ModernWidget.BORDER);
  }
}
