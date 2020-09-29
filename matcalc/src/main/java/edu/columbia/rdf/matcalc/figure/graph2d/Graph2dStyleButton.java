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

import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.icons.Graph2dStyleMultiIcon16;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogFlatDropDownIconButton2;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;

/**
 * The class Graph2dStyleButton.
 */
public class Graph2dStyleButton extends ModernDialogFlatDropDownIconButton2 {

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
      /*
       * if (getText().equals("Filled")) {
       * ((Graph2dStyleMultiIcon16)mIcon).setIcon(0); } else if
       * (getText().equals("Filled Transparent")) {
       * ((Graph2dStyleMultiIcon16)mIcon).setIcon(1); } else if
       * (getText().equals("Joined")) { ((Graph2dStyleMultiIcon16)mIcon).setIcon(2); }
       * else if (getText().equals("Lines")) {
       * ((Graph2dStyleMultiIcon16)mIcon).setIcon(3); } else if
       * (getText().equals("Bars")) { ((Graph2dStyleMultiIcon16)mIcon).setIcon(4); }
       * else if (getText().equals("Scatter")) {
       * ((Graph2dStyleMultiIcon16)mIcon).setIcon(5); } else {
       * ((Graph2dStyleMultiIcon16)mIcon).setIcon(0); }
       */

      if (getText().equals("Joined Smooth")) {
        setIcon(1);
      } else if (getText().equals("Filled")) {
        setIcon(2);
      } else if (getText().equals("Filled Smooth")) {
        setIcon(3);
      } else if (getText().equals("Lines")) {
        setIcon(4);
      } else if (getText().equals("Bars")) {
        setIcon(5);
      } else if (getText().equals("Scatter")) {
        setIcon(6);
      } else if (getText().equals("Heat Map")) {
        setIcon(7);
      } else {
        // Joined
        setIcon(0);
      }

      repaint();
    }
  }

  /**
   * Sets the icon.
   *
   * @param index the new icon
   */
  private void setIcon(int index) {
    ((Graph2dStyleMultiIcon16) mIcon).setIcon(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.button.ModernDropDownButton#drawBackground(java.awt.
   * Graphics2D)
   */
  /*
   * @Override public void drawBackground(Graphics2D g2) { if(mHighlight ||
   * mPopupShown) { paintHighlightedOutlined(g2, getRect()); } else if(hasFocus())
   * { paintOutlinedFocused(g2, getRect()); } else { paintOutlined(g2, getRect());
   * } }
   */

  /**
   * Instantiates a new graph2d style button.
   *
   * @param style the style
   */
  public Graph2dStyleButton(PlotStyle style) {
    super(new Graph2dStyleMultiIcon16(), new Graph2dStyleMenu());

    addClickListener(new ClickEvents());

    setStyle(style);

    UI.setSize(this, ModernWidget.SIZE_48);
  }

  /**
   * Sets the style.
   *
   * @param style the new style
   */
  public void setStyle(PlotStyle style) {

    switch (style) {
    case JOINED_SMOOTH:
      setSelectedIndex(1);
      break;
    case FILLED:
      setSelectedIndex(2);
      break;
    case FILLED_SMOOTH:
      setSelectedIndex(3);
      break;
    case LINES:
      setSelectedIndex(4);
      break;
    case BARS:
      setSelectedIndex(5);
      break;
    case SCATTER:
      setSelectedIndex(6);
      break;
    case HEATMAP:
      setSelectedIndex(7);
      break;
    default:
      setSelectedIndex(0);
      break;
    }
  }

  /**
   * Gets the style.
   *
   * @return the style
   */
  public PlotStyle getStyle() {
    if (getText().equals("Filled")) {
      return PlotStyle.FILLED;
    } else if (getText().equals("Filled Smooth")) {
      return PlotStyle.FILLED_SMOOTH;
    } else if (getText().equals("Joined")) {
      return PlotStyle.JOINED;
    } else if (getText().equals("Joined Smooth")) {
      return PlotStyle.JOINED_SMOOTH;
    } else if (getText().equals("Lines")) {
      return PlotStyle.LINES;
    } else if (getText().equals("Bars")) {
      return PlotStyle.BARS;
    } else if (getText().equals("Scatter")) {
      return PlotStyle.SCATTER;
    } else if (getText().equals("Heat Map")) {
      return PlotStyle.HEATMAP;
    } else {
      return PlotStyle.BARS;
    }
  }
}
