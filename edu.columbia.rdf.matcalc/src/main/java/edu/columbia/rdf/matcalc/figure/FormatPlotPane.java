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

import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.tabs.SegmentTabsPanel;
import org.jebtk.modern.tabs.TabsModel;

/**
 * The class FormatPlotPane.
 */
public abstract class FormatPlotPane extends ModernComponent {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member group tabs model.
   */
  protected TabsModel mGroupTabsModel = new TabsModel();

  /**
   * Instantiates a new format plot pane.
   */
  public FormatPlotPane() {
    setBody(new SegmentTabsPanel(mGroupTabsModel, 80, 20));

    setBorder(BORDER);
  }

  /**
   * Gets the tabs.
   *
   * @return the tabs
   */
  public TabsModel getTabs() {
    return mGroupTabsModel;
  }

  /**
   * Gets the canvas.
   *
   * @return the canvas
   */
  public abstract PlotBox getCanvas();

  /**
   * Update.
   */
  public abstract void update();
}
