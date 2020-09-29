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
package edu.columbia.rdf.matcalc.colormap;

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class LineStyleControl.
 */
public class ColorMapControl extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The color button.
   */
  private ColorMapButton mColorMapButton;

  /** The m plot. */
  private Plot mPlot;

  /**
   * Instantiates a new line style control.
   *
   * @param parent the parent
   * @param plot   the plot
   */
  public ColorMapControl(ModernWindow parent, Plot plot) {
    mPlot = plot;

    mColorMapButton = new ColorMapButton(parent, plot.getColorMap());

    add(new ModernAutoSizeLabel("Color map"));
    add(Box.createHorizontalGlue());
    add(ModernPanel.createHGap());
    add(mColorMapButton);
    // setBorder(ModernWidget.BORDER);

    mColorMapButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mPlot.setColorMap(mColorMapButton.getSelectedColorMap());
      }
    });

    plot.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mColorMapButton.setSelectedColorMap(mPlot.getColorMap());
      }
    });
  }
}
