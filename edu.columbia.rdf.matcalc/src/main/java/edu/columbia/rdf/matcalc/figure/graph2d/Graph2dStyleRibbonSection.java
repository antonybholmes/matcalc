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

import org.jebtk.graphplot.figure.Graph2dStyleModel;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;

/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes
 *
 */
public class Graph2dStyleRibbonSection extends RibbonSection implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /*
   * private ModernCheckButton mJoinedFilledButton = new
   * RibbonLargeRadioButton2("Joined Filled", new Raster24Icon(new
   * JoinedFilledStyleIcon()), "Style Joined Filled",
   * "Peaks are drawn as a continuous filled line.");
   * 
   * private ModernCheckButton mJoinedFilledTransButton = new
   * RibbonLargeRadioButton2("Joined Filled Transparent", new Raster24Icon(new
   * JoinedFilledTransStyleIcon()), "Style Joined Filled Transparent",
   * "Peaks are drawn as a continuous filled line.");
   * 
   * private ModernCheckButton mJoinedButton = new
   * RibbonLargeRadioButton2("Joined", new Raster24Icon(new JoinedStyleIcon()),
   * "Style Joined", "Peaks are drawn as a continuous line.");
   * 
   * private ModernCheckButton mLinesButton = new RibbonLargeRadioButton2("Line",
   * new Raster24Icon(new LinesStyleIcon()), "Style Line",
   * "Peaks are drawn as lines.");
   * 
   * private ModernCheckButton mBarsButton = new RibbonLargeRadioButton2("Bars",
   * new Raster24Icon(new BarsStyleIcon()), "Style Bars",
   * "Peaks are drawn as bars.");
   */

  /**
   * The member button.
   */
  private Graph2dStyleRibbonButton mButton = new Graph2dStyleRibbonButton();

  /**
   * The member style model.
   */
  private Graph2dStyleModel mStyleModel;

  /**
   * Instantiates a new graph2d style ribbon section2.
   *
   * @param ribbon     the ribbon
   * @param styleModel the style model
   */
  public Graph2dStyleRibbonSection(Ribbon ribbon, Graph2dStyleModel styleModel) {
    super(ribbon, "Style");

    mStyleModel = styleModel;

    // buttonContainer.add(mJoinedFilledButton);
    // buttonContainer.add(mJoinedFilledTransButton);
    // buttonContainer.add(mJoinedButton);
    // buttonContainer.add(mLinesButton);
    // buttonContainer.add(mBarsButton);

    add(mButton);

    /*
     * ModernButtonGroup group = new ModernButtonGroup();
     * 
     * group.add(mJoinedFilledButton); group.add(mJoinedFilledTransButton);
     * group.add(mJoinedButton); group.add(mLinesButton); group.add(mBarsButton);
     * 
     * mJoinedFilledButton.addClickListener(this);
     * mJoinedFilledTransButton.addClickListener(this);
     * mJoinedButton.addClickListener(this); mLinesButton.addClickListener(this);
     * mBarsButton.addClickListener(this);
     */

    mButton.addClickListener(this);

    // System.err.println("style " + styleModel.get());

    switch (styleModel.get()) {
    case JOINED:
      mButton.setSelectedIndex(0);
      break;
    case JOINED_SMOOTH:
      mButton.setSelectedIndex(1);
      break;
    case FILLED:
      mButton.setSelectedIndex(2);
      break;
    case FILLED_SMOOTH:
      mButton.setSelectedIndex(3);
      break;
    case LINES:
      mButton.setSelectedIndex(4);
      break;
    case BARS:
      mButton.setSelectedIndex(5);
      break;
    case SCATTER:
      mButton.setSelectedIndex(6);
      break;
    case HEATMAP:
      mButton.setSelectedIndex(7);
      break;
    default:
      mButton.setSelectedIndex(0);
      break;
    }
  }

  /**
   * Change.
   *
   * @param e the e
   */
  private void change(ModernClickEvent e) {
    if (e.getMessage().equals("Filled")) {
      mStyleModel.set(PlotStyle.FILLED);
    } else if (e.getMessage().equals("Filled Smooth")) {
      mStyleModel.set(PlotStyle.FILLED_SMOOTH);
    } else if (e.getMessage().equals("Joined")) {
      mStyleModel.set(PlotStyle.JOINED);
    } else if (e.getMessage().equals("Joined Smooth")) {
      mStyleModel.set(PlotStyle.JOINED_SMOOTH);
    } else if (e.getMessage().equals("Lines")) {
      mStyleModel.set(PlotStyle.LINES);
    } else if (e.getMessage().equals("Bars")) {
      mStyleModel.set(PlotStyle.BARS);
    } else if (e.getMessage().equals("Scatter")) {
      mStyleModel.set(PlotStyle.SCATTER);
    } else if (e.getMessage().equals("Heat Map")) {
      mStyleModel.set(PlotStyle.HEATMAP);
    } else {
      mStyleModel.set(PlotStyle.BARS);
    }

    /*
     * if (mJoinedFilledButton.isSelected()) { mStyleModel.set(PeakStyle.FILLED); }
     * else if (mJoinedButton.isSelected()) { mStyleModel.set(PeakStyle.JOINED); }
     * else if (mLinesButton.isSelected()) { mStyleModel.set(PeakStyle.LINES); }
     * else if (mBarsButton.isSelected()) { mStyleModel.set(PeakStyle.BARS); } else
     * { mStyleModel.set(PeakStyle.FILLED_TRANS); }
     */
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
    change(e);
  }
}
