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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.text.ModernNumericalTextField;
import org.jebtk.modern.text.ModernSubHeadingLabel;
import org.jebtk.modern.text.ModernTextBorderPanel;

/**
 * The Class RadiusControl.
 */
public class RadiusControl extends HBox implements KeyListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m properties. */
  private Props mProps;

  /** The m text radius. */
  private ModernNumericalTextField mTextRadius = new ModernNumericalTextField("150");

  /**
   * Instantiates a new radius control.
   *
   * @param Props the properties
   */
  public RadiusControl(Props properties) {
    mProps = properties;

    add(new ModernSubHeadingLabel("Radius"));
    add(new ModernTextBorderPanel(mTextRadius, ModernWidget.STANDARD_SIZE));

    mTextRadius.addKeyListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      mProps.set("venn.reference.radius", mTextRadius.getInt());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
   */
  @Override
  public void keyReleased(KeyEvent e) {
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
