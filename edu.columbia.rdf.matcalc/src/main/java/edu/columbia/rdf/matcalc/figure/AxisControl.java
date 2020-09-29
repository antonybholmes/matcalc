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

import javax.swing.Box;

import org.jebtk.graphplot.figure.Axis;
import org.jebtk.modern.collapsepane.ModernSubCollapsePane;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class AxisPlotControl.
 */
public class AxisControl extends ModernSubCollapsePane {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new axis plot control.
   *
   * @param parent     the parent
   * @param axis       the axis
   * @param editLimits the edit limits
   */
  public AxisControl(ModernWindow parent, Axis axis, boolean editLimits) {

    Box box = VBox.create();
    box.add(new VisibleControl(parent, axis));
    box.add(ModernPanel.createVGap());
    box.add(new AxisLinePlotControl(parent, axis));
    box.add(ModernPanel.createVGap());
    box.add(new GridColorPlotControl(parent, axis.getGrid()));
    box.setBorder(BORDER);

    if (editLimits) {
      box.add(ModernPanel.createVGap());
      box.add(new LimitsPlotControl(parent, axis));
    }

    addTab("Options", box);

    box = VBox.create();
    box.add(new VisibleControl(parent, axis.getTitle().getFontStyle()));
    box.add(ModernPanel.createVGap());
    box.add(new PlotTitleControl(parent, axis.getTitle()));
    box.add(ModernPanel.createVGap());
    box.add(new FontControl(parent, axis.getTitle().getFontStyle()));
    box.setBorder(BORDER);

    addTab("Title", box);

    box = VBox.create();

    box.add(new TickColorPlotControl(parent, "Major", axis.getTicks().getMajorTicks()));

    box.add(ModernPanel.createVGap());

    box.add(new TickColorPlotControl(parent, "Minor", axis.getTicks().getMinorTicks()));

    box.add(ModernPanel.createVGap());

    box.add(new TicksInsidePlotControl(parent, axis.getTicks()));
    box.setBorder(BORDER);

    addTab("Ticks", box);

    box = VBox.create();
    box.add(new VisibleControl(parent, axis.getTicks().getMajorTicks().getFontStyle()));
    box.add(ModernPanel.createVGap());
    box.add(new TickLabelPlotControl(parent, axis.getTicks().getMajorTicks()));
    box.add(ModernPanel.createVGap());
    box.add(new FontControl(parent, axis.getTicks().getMajorTicks().getFontStyle()));
    box.setBorder(BORDER);

    addTab("Tick Labels", box);

  }
}
