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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class XYSeriesTitleControl.
 */
public class XYSeriesTitleControl extends HBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member title.
   */
  private XYSeries mSeries;

  /**
   * The member text field.
   */
  private ModernTextField mTextField = new ModernClipboardTextField();

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
        update();
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
    public void keyTyped(KeyEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * Instantiates a new XY series title control.
   *
   * @param parent the parent
   * @param series the series
   */
  public XYSeriesTitleControl(ModernWindow parent, XYSeries series) {
    mSeries = series;

    mTextField.setText(series.getName());

    ModernTextBorderPanel panel = new ModernTextBorderPanel(mTextField);

    UI.setMinMaxSize(panel, ModernWidget.MIN_SIZE, ModernWidget.MAX_SIZE);

    add(panel);

    // setBorder(ModernWidget.BORDER);

    mTextField.addKeyListener(new KeyEvents());
  }

  /**
   * Update.
   */
  private void update() {
    mSeries.setName(mTextField.getText());
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
    update();
  }

}
