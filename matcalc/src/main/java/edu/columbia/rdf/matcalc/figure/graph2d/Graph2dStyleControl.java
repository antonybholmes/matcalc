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

import javax.swing.Box;

import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class Graph2dStyleControl.
 */
public class Graph2dStyleControl extends HBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member style button.
   */
  private Graph2dStyleButton mStyleButton;

  /**
   * The member axes.
   */
  private Axes mAxes;

  /**
   * The member style.
   */
  private PlotStyle mStyle = PlotStyle.BARS;

  /**
   * Instantiates a new graph2d style control.
   *
   * @param parent the parent
   * @param axes   the axes
   */
  public Graph2dStyleControl(ModernWindow parent, Axes axes) {

    mAxes = axes;

    mStyleButton = new Graph2dStyleButton(PlotStyle.BARS);

    add(new ModernLabel("Style"));
    add(Box.createHorizontalGlue());
    add(mStyleButton);

    mStyleButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
   * event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getMessage().equals("Filled")) {
      mStyle = PlotStyle.FILLED;
    } else if (e.getMessage().equals("Filled Smooth")) {
      mStyle = PlotStyle.FILLED_SMOOTH;
    } else if (e.getMessage().equals("Joined")) {
      mStyle = PlotStyle.JOINED;
    } else if (e.getMessage().equals("Joined Smooth")) {
      mStyle = PlotStyle.JOINED_SMOOTH_ZERO_ENDS;
    } else if (e.getMessage().equals("Lines")) {
      mStyle = PlotStyle.LINES;
    } else if (e.getMessage().equals("Bars")) {
      mStyle = PlotStyle.BARS;
    } else if (e.getMessage().equals("Scatter")) {
      mStyle = PlotStyle.SCATTER;
    } else {
      mStyle = PlotStyle.BARS;
    }

    mAxes.setStyle(mStyle);
  }

}
