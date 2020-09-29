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
package edu.columbia.rdf.matcalc.toolbox.core.venn;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.bioinformatics.ui.groups.GroupsModel;
import org.jebtk.core.Props;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * The Class VennCanvas.
 */
public abstract class VennCanvas extends ModernPlotCanvas {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant SIZE. */
  protected static final Dimension SIZE = new Dimension(1000, 1000);

  /** The m groups. */
  protected GroupsModel mGroups;

  /** The m properties. */
<<<<<<< HEAD:matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/core/venn/VennCanvas.java
  protected Props mProps = new VennProps();
=======
  protected Props mProperties = new VennProperties();
>>>>>>> 0c6b302bf0a6d2eac18dbec9d0d2195af92c4605:edu.columbia.rdf.matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/core/venn/VennCanvas.java

  /**
   * The Class ChangeEvents.
   */
  private class ChangeEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.
     * ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      layout();
    }

  }

  /**
   * Instantiates a new venn canvas.
   */
  public VennCanvas() {
    setCanvasSize(SIZE);

    mProps.addChangeListener(new ChangeEvents());
  }

  /**
   * Sets the groups.
   *
   * @param groups the groups
   * @param style  the style
   */
  public void setGroups(GroupsModel groups, CircleStyle style) {
    mGroups = groups;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.Container#layout()
   */
  public void layout() {

  }

  /**
   * Gets the properties.
   *
   * @return the properties
   */
  public Props getProperties() {
<<<<<<< HEAD:matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/core/venn/VennCanvas.java
    return mProps;
=======
    return mProperties;
>>>>>>> 0c6b302bf0a6d2eac18dbec9d0d2195af92c4605:edu.columbia.rdf.matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/core/venn/VennCanvas.java
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.ModernPlotCanvas#plot(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext)
   */
<<<<<<< HEAD:matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/core/venn/VennCanvas.java
  public void plot(Graphics2D g2, DrawingContext context, Props params) {
=======
  public void plot(Graphics2D g2, DrawingContext context, Props props) {
>>>>>>> 0c6b302bf0a6d2eac18dbec9d0d2195af92c4605:edu.columbia.rdf.matcalc/src/main/java/edu/columbia/rdf/matcalc/toolbox/core/venn/VennCanvas.java
    aaPlot(g2, context);
  }

  /**
   * Aa plot.
   *
   * @param g2      the g 2
   * @param context the context
   */
  public void aaPlot(Graphics2D g2, DrawingContext context) {

    Graphics2D g2Temp = ImageUtils.createAATextGraphics(g2);

    try {
      drawPlot(g2Temp, context);
    } finally {
      g2Temp.dispose();
    }
  }

  /**
   * Draw plot.
   *
   * @param g2Temp  the g 2 temp
   * @param context the context
   */
  public void drawPlot(Graphics2D g2Temp, DrawingContext context) {
    // do nothing
  }
}
