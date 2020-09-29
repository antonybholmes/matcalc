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

import org.jebtk.graphplot.icons.Graph2dStyleMultiIcon24;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;

/**
 * The class Graph2dStyleRibbonButton.
 */
public class Graph2dStyleRibbonButton extends RibbonLargeDropDownButton2 {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The class ClickEvents.
   */
  private class ClickEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
     * event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      if (e.getMessage().equals("Joined")) {
        setIcon(0);
      } else if (e.getMessage().equals("Joined Smooth")) {
        setIcon(1);
      } else if (e.getMessage().equals("Filled")) {
        setIcon(2);
      } else if (e.getMessage().equals("Filled Smooth")) {
        setIcon(3);
      } else if (e.getMessage().equals("Lines")) {
        setIcon(4);
      } else if (e.getMessage().equals("Bars")) {
        setIcon(5);
      } else if (e.getMessage().equals("Scatter")) {
        setIcon(6);
      } else if (e.getMessage().equals("Heat Map")) {
        setIcon(7);
      } else {
        setIcon(0);
      }

      repaint();
    }
  }

  /**
   * Instantiates a new graph2d style ribbon button.
   */
  public Graph2dStyleRibbonButton() {
    super(new Graph2dStyleMultiIcon24(), new Graph2dStyleMenu());

    addClickListener(new ClickEvents());

    mCompactIcon = mIcon;
  }

  /**
   * Sets the icon.
   *
   * @param index the new icon
   */
  private void setIcon(int index) {
    ((Graph2dStyleMultiIcon24) mIcon).setIcon(index);

    // mCompactIcon = mIcon; //new RasterIcon(mIcon, 24);
  }
}
