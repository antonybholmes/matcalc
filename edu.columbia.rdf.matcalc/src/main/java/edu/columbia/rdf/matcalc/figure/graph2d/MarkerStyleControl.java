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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.icons.ShapeStyle;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.VisibleControl;

/**
 * The class ShapeStyleControl.
 */
public class MarkerStyleControl extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member shape button.
   */
  private ShapeStyleButton mShapeButton;

  /** The m size spinner. */
  private ModernCompactSpinner mSizeSpinner;

  /**
   * The member series.
   */
  private XYSeries mSeries;

  /** The m vc. */
  private VisibleControl mVc;

  /**
   * The class ShapeEvents.
   */
  private class ShapeEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
     * event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      ShapeStyle shape;

      if (e.getMessage().equals("Bar")) {
        shape = ShapeStyle.BAR;
      } else if (e.getMessage().equals("Circle")) {
        shape = ShapeStyle.CIRCLE;
      } else if (e.getMessage().equals("Cross")) {
        shape = ShapeStyle.CROSS;
      } else if (e.getMessage().equals("Diamond")) {
        shape = ShapeStyle.DIAMOND;
      } else if (e.getMessage().equals("Minus")) {
        shape = ShapeStyle.MINUS;
      } else if (e.getMessage().equals("Plus")) {
        shape = ShapeStyle.PLUS;
      } else if (e.getMessage().equals("Square")) {
        shape = ShapeStyle.SQUARE;
      } else if (e.getMessage().equals("Triangle")) {
        shape = ShapeStyle.TRIANGLE;
      } else {
        shape = ShapeStyle.CIRCLE;
      }

      mSeries.setMarker(shape);

      // setColors();
    }
  }

  /**
   * Instantiates a new shape style control.
   *
   * @param parent the parent
   * @param series the series
   */
  public MarkerStyleControl(ModernWindow parent, XYSeries series) {
    mSeries = series;

    mSizeSpinner = new ModernCompactSpinner(1, 100, series.getMarker().getSize());
    UI.setSize(mSizeSpinner, ModernWidget.TINY_SIZE);

    mShapeButton = new ShapeStyleButton(series.getMarker().getType());

    mVc = new VisibleControl(parent, series.getMarker());

    add(mVc);
    add(Box.createHorizontalGlue());
    add(mSizeSpinner);
    add(ModernWidget.createHGap());
    add(mShapeButton);

    series.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        // Change the image but do not trigger a change event.
        mShapeButton.changeType(mSeries.getMarker().getType());

        // Change the shape the control is operating since we may
        // have changed the shape so it will require updating the
        // controller otherwise the control will be left editing an
        // orphaned shape.
        mVc.changeProperties(mSeries.getMarker());
      }
    });

    mShapeButton.addClickListener(new ShapeEvents());

    mSizeSpinner.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        mSeries.getMarker().setSize(mSizeSpinner.getIntValue());
      }
    });
  }
}
