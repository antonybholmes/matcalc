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
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.Mathematics;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.props.TickMarkProps;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.figure.graph2d.RotationButton;

/**
 * The class TickLabelPlotControl.
 */
public class TickLabelPlotControl extends HBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member tick.
   */
  private TickMarkProps mTick;

  /**
   * The member label field.
   */
  private ModernTextField mLabelField = new ModernTextField();

  /**
   * The member rotation button.
   */
  private RotationButton mRotationButton;

  /**
   * The class RotationEvents.
   */
  private class RotationEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
     * event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      double rotation;

      if (e.getMessage().equals("90")) {
        rotation = Mathematics.HALF_PI;
      } else if (e.getMessage().equals("180")) {
        rotation = Mathematics.PI;
      } else if (e.getMessage().equals("270")) {
        rotation = Mathematics.PI_32;
      } else {
        rotation = 0;
      }

      mTick.setRotation(rotation);

      // setColors();
    }
  }

  /**
   * The class TickLabelEvents.
   */
  private class TickLabelEvents implements KeyListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        mTick.setLabels(TextUtils.scSplit(mLabelField.getText()));
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

  /**
   * Instantiates a new tick label plot control.
   *
   * @param parent the parent
   * @param tick   the tick
   */
  public TickLabelPlotControl(ModernWindow parent, TickMarkProps tick) {
    mTick = tick;

    mRotationButton = new RotationButton(mTick.getRotation());

    mRotationButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mTick.setRotation(mRotationButton.getRotation());
      }
    });

    ModernTextBorderPanel panel = new ModernTextBorderPanel(mLabelField);
    UI.setMinMaxSize(panel, ModernWidget.MIN_SIZE, ModernWidget.MAX_SIZE);

    add(panel);

    add(ModernPanel.createHGap());
    add(mRotationButton);
    setBorder(ModernWidget.BORDER);

    mLabelField.addKeyListener(new TickLabelEvents());

    listTicks();
  }

  /**
   * List ticks.
   */
  private void listTicks() {
    List<String> labels = new ArrayList<String>();

    for (int i = 0; i < mTick.getTickCount(); ++i) {
      labels.add(mTick.getLabel(i));
    }

    mLabelField.setText(TextUtils.scJoin(labels));
  }
}
