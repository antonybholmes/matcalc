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
import java.text.ParseException;

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.panel.HExBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernNumericalTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class MarginControl.
 */
public class SizeControl extends VBox implements KeyListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m field width. */
  private ModernTextField mFieldWidth = new ModernNumericalTextField();

  /** The m field height. */
  private ModernTextField mFieldHeight = new ModernNumericalTextField();

  /** The m axes. */
  private Axes mAxes;

  /**
   * Instantiates a new margin control.
   *
   * @param parent the parent
   * @param axes   the axes
   */
  public SizeControl(ModernWindow parent, Axes axes) {

    mAxes = axes;

    Box box;

    // top
    box = HExBox.createHExBox();
    box.add(new ModernAutoSizeLabel("Width", ModernWidget.TINY_SIZE));
    box.add(new ModernTextBorderPanel(mFieldWidth, ModernWidget.SMALL_SIZE));
    add(box);
    add(ModernPanel.createVGap());
    mFieldWidth.setText(axes.getInternalSize().getW());
    mFieldWidth.addKeyListener(this);

    // left
    box = HExBox.createHExBox();
    box.add(new ModernAutoSizeLabel("Height", ModernWidget.TINY_SIZE));
    box.add(new ModernTextBorderPanel(mFieldHeight, ModernWidget.SMALL_SIZE));
    add(box);
    add(ModernPanel.createVGap());
    mFieldHeight.setText(axes.getInternalSize().getH());
    mFieldHeight.addKeyListener(this);

    axes.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        resize();
      }
    });
  }

  private void resize() {
    mFieldWidth.setText(mAxes.getInternalSize().getW());
    mFieldHeight.setText(mAxes.getInternalSize().getH());
  }

  /**
   * Sets the margins.
   *
   * @throws ParseException the parse exception
   */
  private void setSize() throws ParseException {
    mAxes.setInternalSize(mFieldWidth.getInt(), mFieldHeight.getInt());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      try {
        setSize();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
   */
  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
   */
  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }
}
