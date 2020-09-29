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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.Axis;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class LimitsPlotControl.
 */
public class LimitsPlotControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The min text field.
   */
  private ModernTextField mMinTextField = new ModernClipboardTextField();

  /**
   * The max text field.
   */
  private ModernTextField mMaxTextField = new ModernClipboardTextField();

  /**
   * The member axis.
   */
  private Axis mAxis;

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
    public void keyPressed(KeyEvent arg0) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() != KeyEvent.VK_ENTER) {
        return;
      }

      mAxis.setLimits(Double.parseDouble(mMinTextField.getText()), Double.parseDouble(mMaxTextField.getText()));
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
   * Instantiates a new limits plot control.
   *
   * @param parent the parent
   * @param axis   the axis
   */
  public LimitsPlotControl(ModernWindow parent, Axis axis) {
    mAxis = axis;

    Box box = HBox.create();

    ModernAutoSizeLabel label = new ModernAutoSizeLabel("Limits");
    // Ui.setSize(label, ModernWidget.STANDARD_SIZE);

    box.add(label);
    box.add(Box.createHorizontalGlue());
    box.add(new ModernTextBorderPanel(mMinTextField, ModernWidget.SMALL_SIZE));
    box.add(ModernPanel.createHGap());
    box.add(new ModernTextBorderPanel(mMaxTextField, ModernWidget.SMALL_SIZE));
    add(box);

    add(ModernPanel.createVGap());

    mMinTextField.addKeyListener(new KeyEvents());
    mMaxTextField.addKeyListener(new KeyEvents());

    mAxis.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        update();
      }
    });

    update();
  }

  /**
   * Update.
   */
  private void update() {
    mMinTextField.setText(Double.toString(mAxis.getLimits().getMin()));
    mMaxTextField.setText(Double.toString(mAxis.getLimits().getMax()));
  }
}
