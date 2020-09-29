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
package edu.columbia.rdf.matcalc.colormap;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;

import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ControlDropDownButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allow users to select a color for an object etc.
 * 
 * @author Antony Holmes
 *
 */
public class ColorMapButton extends ControlDropDownButton implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member popup.
   */
  private ColorMapPopupMenu mPopup;

  /** The m color map. */
  private ColorMap mColorMap;

  /**
   * Instantiates a new color map button.
   *
   * @param parent the parent
   */
  public ColorMapButton(ModernWindow parent) {
    this(parent, ColorMapService.getInstance().get("Jet"));
  }

  /**
   * Instantiates a new color swatch button.
   *
   * @param parent   the parent
   * @param colorMap the color map
   */
  public ColorMapButton(ModernWindow parent, ColorMap colorMap) {
    super("Color Map");

    mPopup = new ColorMapPopupMenu(parent);

    setMenu(mPopup);

    mColorMap = colorMap;

    mMenu.addClickListener(this);

    UI.setSize(this, SIZE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.button.ModernDropDownButton#drawBackground(java.awt.
   * Graphics2D)
   */
  /*
   * @Override public void drawBackground(Graphics2D g2) { if(mHighlight ||
   * mPopupShown) { paintHighlightedOutlined(g2, getRect()); } else if(hasFocus())
   * { paintOutlinedFocused(g2, getRect()); } else { paintOutlined(g2, getRect());
   * } }
   */

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.button.ModernDropDownButton#drawForegroundAA(java.
   * awt. Graphics2D)
   */
  @Override
  public void drawForegroundAA(Graphics2D g2) {
    int x = PADDING;

    int y = (getHeight() - 16) / 2;

    int w = 16;

    LinearGradientPaint paint = mColorMap.getAnchorColors().toGradientPaint(new Point2D.Float(x, 0),
        new Point2D.Float(x + w, 0));

    g2.setPaint(paint);

    g2.fillRect(PADDING, y, w, 16);

    /*
     * double c = 0; double inc = (mColorMap.getColorCount() - 1) / (double)w;
     * 
     * for (int i = 0; i < w; ++i) { g2.setColor(mColorMap.getColorByIndex((int)c));
     * 
     * g2.drawLine(x, y, x, y + 16);
     * 
     * ++x;
     * 
     * c += inc; }
     */

    g2.setColor(LINE_COLOR);
    g2.drawRect(PADDING, y, w, 16);

    // g2.setColor(ThemeService.getInstance().getColors().getHighlight(4));
    // g2.drawRect(x, y, 32, Resources.ICON_SIZE_16);

    TRIANGLE_ICON.drawIcon(g2, mRect.getW() - 16, (getHeight() - 16) / 2, 16);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    mPressed = false;
    mHighlight = false;

    if (mPopup.getSelectedColorMap() != null) {
      mColorMap = mPopup.getSelectedColorMap();
    }

    repaint();

    fireClicked();
  }

  /**
   * Gets the selected color.
   *
   * @return the selected color
   */
  public ColorMap getSelectedColorMap() {
    return mColorMap;
  }

  /**
   * Sets the selected color map.
   *
   * @param colorMap the new selected color map
   */
  public void setSelectedColorMap(ColorMap colorMap) {
    mColorMap = colorMap;

    repaint();
  }
}
