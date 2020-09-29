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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;

import org.jebtk.graphplot.figure.Axes;
import org.jebtk.modern.UI;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.ribbon.RibbonStripContainer;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;

/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes
 *
 */
public class PlotSizeRibbonSection extends RibbonSection {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member w field.
   */
  private ModernCompactSpinner mWField = new ModernCompactSpinner(1, 10000, 100, false);

  /**
   * The member h field.
   */
  private ModernCompactSpinner mHField = new ModernCompactSpinner(1, 10000, 100, false);

  /** The m model. */
  private PlotSizeModel mModel;

  /**
   * The class KeyEvents.
   */
  private class KeyEvents implements KeyListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        change();
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
      // TODO Auto-generated method stub

    }
  }

  /**
   * Instantiates a new plot size ribbon section.
   *
   * @param ribbon the ribbon
   * @param model  the model
   */
  public PlotSizeRibbonSection(Ribbon ribbon, PlotSizeModel model) {
    super(ribbon, "Plot Size");

    mModel = model;

    Box box = new RibbonStripContainer();

    box.add(new ModernAutoSizeLabel("W"));
    box.add(UI.createHGap(5));
    box.add(mWField);
    // UI.setSize(mWField, ModernWidget.TINY_SIZE);
    box.add(UI.createHGap(10));

    box.add(new ModernAutoSizeLabel("H"));
    box.add(UI.createHGap(5));
    box.add(mHField);
    // UI.setSize(mHField, ModernWidget.TINY_SIZE);
    add(box);

    mWField.addKeyListener(new KeyEvents());
    mHField.addKeyListener(new KeyEvents());

    refresh();
  }

  /**
   * Refresh.
   */
  private void refresh() {
    mWField.setValue(mModel.get().getPreferredSize().width);
    mHField.setValue(mModel.get().getPreferredSize().height);
  }

  /**
   * Change.
   */
  private void change() {
    int w = mWField.getIntValue();
    int h = mHField.getIntValue();

    Axes axes = Axes.createAxes();

    axes.setInternalSize(w, h);

    mModel.set(axes);
  }
}
