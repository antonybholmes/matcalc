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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.event.ModernClickListeners;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.text.ModernNumericalLabel;

import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ScaleModel;

/**
 * The class IntensityControl.
 */
public class ScaleControl extends HBox implements ChangeListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member slider.
   */
  private ModernCompactSpinner mSlider = null;

  /**
   * The member spinner.
   */
  // private ModernCompactSpinner mSpinner =
  // new ModernCompactSpinner(1, 10, 3);

  /**
   * The member listeners.
   */
  private ModernClickListeners mListeners = new ModernClickListeners();

  /** The m scale model. */
  private ScaleModel mScaleModel;

  /** The m scale label. */
  private ModernLabel mScaleLabel = new ModernNumericalLabel("0", 30);

  /**
   * The class ChangeEvents.
   */
  private class ChangeEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      set();
    }

  }

  /**
   * The class KeyEvents.
   */
  private class KeyEvents extends KeyAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        set();
      }
    }
  }

  /**
   * Instantiates a new intensity control.
   *
   * @param intensity the intensity
   */
  public ScaleControl(double intensity) {
    init();

    mSlider.setValue(intensity);
  }

  /**
   * Instantiates a new scale control.
   *
   * @param intensityModel the intensity model
   */
  public ScaleControl(ScaleModel intensityModel) {
    mScaleModel = intensityModel;

    init();

    intensityModel.addChangeListener(this);

    mSlider.setValue(intensityModel.get());
  }

  /**
   * Inits the.
   */
  private void init() {
    mSlider = new ScaleSpinner(3); // new ModernOrbSlider(3, 0.25, 0.5, 1, 2, 3,
                                   // 4, 5, 6, 7, 8, 9, 10);

    // Ui.setSize(mSlider, 80, ModernWidget.WIDGET_HEIGHT);

    add(new ModernAutoSizeLabel("Color scale"));
    add(Box.createHorizontalGlue());
    add(mSlider);
    // add(Ui.createHGap(10));
    // add(mScaleLabel);
    // add(Ui.createHGap(5));

    // add(ModernPanel.createVGap());
    // box = new HBoxPanel();
    // box.add(new ModernLabel("Min"));
    // box.add(Box.createHorizontalGlue());
    // box.add(new ModernTextPanel(mMinField, ModernWidget.SMALL_SIZE));
    // add(box);

    // add(ModernPanel.createVGap());
    // box = new HBoxPanel();
    // box.add(new ModernLabel("Range"));
    // box.add(Box.createHorizontalGlue());
    // box.add(new ModernTextBorderPanel(mMaxField, ModernWidget.SMALL_SIZE));
    // add(box);
    setAlignmentY(TOP_ALIGNMENT);

    UI.setSize(this, ModernWidget.MAX_SIZE);

    // mSpinner.addClickListener(new ClickEvents());
    mSlider.addChangeListener(new ChangeEvents());
  }

  /**
   * Update.
   */
  private void set() {

    // mSlider.updateValue(mSpinner.getIntValue());

    mScaleModel.set(mSlider.getValue());

    mScaleLabel.setText(mSlider.getValue());

    mListeners.fireClicked(new ModernClickEvent(this));
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mListeners.addClickListener(l);
  }

  /**
   * Returns the scaling factor of the intensity.
   *
   * @return the value
   */
  public double getValue() {
    return mSlider.getValue();

    /*
     * double v = mSpinner.getValue();
     * 
     * if (v > 0) { ++v; } else if (v < 0) { v = 1.0 / (1.0 - v); } else { v = 1.0;
     * }
     * 
     * return v;
     */
  }

  // public double getMin() {
  // return mMinField.getDouble();
  // }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.
   * ChangeEvent)
   */
  @Override
  public void changed(ChangeEvent e) {
    if (mScaleModel.get() != mSlider.getValue()) {
      System.err.println("scale " + mScaleModel.get());

      mSlider.setValue(mScaleModel.get());
    }
  }
}
