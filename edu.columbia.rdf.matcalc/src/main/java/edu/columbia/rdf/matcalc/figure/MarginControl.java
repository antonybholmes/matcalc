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
import org.jebtk.graphplot.figure.props.MarginProps;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.text.ModernNumericalTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class MarginControl.
 */
public class MarginControl extends VBox implements KeyListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member field top.
   */
  private ModernTextField mFieldTop = new ModernNumericalTextField();

  /**
   * The member field left.
   */
  private ModernTextField mFieldLeft = new ModernNumericalTextField();

  /**
   * The member field bottom.
   */
  private ModernTextField mFieldBottom = new ModernNumericalTextField();

  /**
   * The member field right.
   */
  private ModernTextField mFieldRight = new ModernNumericalTextField();

  /** The m layer. */
  private PlotBox mLayer;

  /**
   * Instantiates a new margin control.
   *
   * @param parent the parent
   * @param layer  the layer
   */
  public MarginControl(ModernWindow parent, PlotBox layer) {

    mLayer = layer;

    MarginProps margins = layer.getMargins();

    Box box;

    // top
    box = HBox.create();
    box.add(new ModernLabel("Top", ModernWidget.TINY_SIZE));
    box.add(Box.createHorizontalGlue());
    box.add(new ModernTextBorderPanel(mFieldTop, ModernWidget.SMALL_SIZE));
    add(box);
    add(ModernPanel.createVGap());
    mFieldTop.setText(margins.getTop());
    mFieldTop.addKeyListener(this);

    // left
    box = HBox.create();
    box.add(new ModernLabel("Left", ModernWidget.TINY_SIZE));
    box.add(Box.createHorizontalGlue());
    box.add(new ModernTextBorderPanel(mFieldLeft, ModernWidget.SMALL_SIZE));
    add(box);
    add(ModernPanel.createVGap());
    mFieldLeft.setText(margins.getLeft());
    mFieldLeft.addKeyListener(this);

    // bottom
    box = HBox.create();
    box.add(new ModernLabel("Bottom", ModernWidget.TINY_SIZE));
    box.add(Box.createHorizontalGlue());
    box.add(new ModernTextBorderPanel(mFieldBottom, ModernWidget.SMALL_SIZE));
    add(box);
    add(ModernPanel.createVGap());
    mFieldBottom.setText(margins.getBottom());
    mFieldBottom.addKeyListener(this);

    // right
    box = HBox.create();
    box.add(new ModernLabel("Right", ModernWidget.TINY_SIZE));
    box.add(Box.createHorizontalGlue());
    box.add(new ModernTextBorderPanel(mFieldRight, ModernWidget.SMALL_SIZE));
    add(box);
    add(ModernPanel.createVGap());
    mFieldRight.setText(margins.getRight());
    mFieldRight.addKeyListener(this);

    layer.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        update();
      }
    });
  }

  private void update() {
    mFieldTop.setText(mLayer.getMargins().getTop());
    mFieldLeft.setText(mLayer.getMargins().getLeft());
    mFieldBottom.setText(mLayer.getMargins().getBottom());
    mFieldRight.setText(mLayer.getMargins().getRight());
  }

  /**
   * Sets the margins.
   *
   * @throws ParseException the parse exception
   */
  private void setMargins() throws ParseException {
    mLayer.setMargins(mFieldTop.getInt(), mFieldLeft.getInt(), mFieldBottom.getInt(), mFieldRight.getInt());
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
        setMargins();
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
