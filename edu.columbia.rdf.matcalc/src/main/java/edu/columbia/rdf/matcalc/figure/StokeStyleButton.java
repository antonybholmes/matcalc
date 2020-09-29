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

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.graphplot.figure.props.StrokeStyle;
import org.jebtk.modern.dialog.ControlDropDownButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.menu.ModernScrollPopupMenu;
import org.jebtk.modern.theme.ModernTheme;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allow users to select a line style.
 * 
 * @author Antony Holmes
 *
 */
public class StokeStyleButton extends ControlDropDownButton implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant DASHED_LINE_CHANGED.
   */
  private static final String DASHED_LINE_CHANGED = "dashed_line_change";

  /**
   * The member popup.
   */
  private ModernScrollPopupMenu mPopup = null;

  /**
   * The member selected stroke.
   */
  private StrokeStyle mSelectedStroke = StrokeStyle.SINGLE;

  /**
   * Instantiates a new dashed line type button.
   *
   * @param parent the parent
   * @param style  the style
   */
  public StokeStyleButton(ModernWindow parent, StrokeStyle style) {
    super("Dashed Line");

    mPopup = new ModernScrollPopupMenu();

    DashedLineMenuItem menuItem = new DashedLineMenuItem("Solid", ModernTheme.SINGLE_LINE_STROKE);
    menuItem.addClickListener(this);
    mPopup.addScrollMenuItem(menuItem);

    menuItem = new DashedLineMenuItem("Dotted", ModernTheme.DOTTED_LINE_STROKE);
    menuItem.addClickListener(this);
    mPopup.addScrollMenuItem(menuItem);

    menuItem = new DashedLineMenuItem("Dashed", ModernTheme.DASHED_LINE_STROKE);
    menuItem.addClickListener(this);
    mPopup.addScrollMenuItem(menuItem);

    menuItem = new DashedLineMenuItem("Dashed Dotted", ModernTheme.DASHED_DOTTED_LINE_STROKE);
    menuItem.addClickListener(this);
    mPopup.addScrollMenuItem(menuItem);

    menuItem = new DashedLineMenuItem("Long Dashed", ModernTheme.LONG_DASHED_LINE_STROKE);
    menuItem.addClickListener(this);
    mPopup.addScrollMenuItem(menuItem);

    // mPopup.addClickListener(this);

    setMenu(mPopup);

    setStroke(style);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.button.ModernDropDownButton#drawForegroundAA(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawForegroundAA(Graphics2D g2) {
    int x = PADDING;

    int y = getHeight() / 2;

    g2.setColor(Color.BLACK);
    g2.setStroke(StrokeStyle.getStroke(mSelectedStroke, 1));
    g2.drawLine(x, y, x + 20, y);

    TRIANGLE_ICON.drawIcon(g2, mRect.getW() - 16, (getHeight() - 16) / 2, 16);
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
    mPressed = false;
    mHighlight = false;

    if (e.getMessage().equals("Dashed")) {
      mSelectedStroke = StrokeStyle.DASHED;
    } else if (e.getMessage().equals("Dotted")) {
      mSelectedStroke = StrokeStyle.DOTTED;
    } else if (e.getMessage().equals("Long Dashed")) {
      mSelectedStroke = StrokeStyle.LONG_DASH;
    } else if (e.getMessage().equals("Dashed Dotted")) {
      mSelectedStroke = StrokeStyle.DASHED_DOTTED;
    } else {
      mSelectedStroke = StrokeStyle.SINGLE;
    }

    repaint();

    fireClicked(new ModernClickEvent(this, DASHED_LINE_CHANGED));
  }

  /**
   * Gets the selected stroke.
   *
   * @return the selected stroke
   */
  public StrokeStyle getSelectedStroke() {
    return mSelectedStroke;
  }

  /**
   * Sets the stroke.
   *
   * @param stroke the new stroke
   */
  public void setStroke(StrokeStyle stroke) {
    mSelectedStroke = stroke;

    repaint();
  }
}
