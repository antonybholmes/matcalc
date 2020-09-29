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

import org.jebtk.core.geom.IntDim;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.UI;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapIcon;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.graphics.icons.TriangleDownVectorIcon;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allow users to select a color for an object etc.
 * 
 * @author Antony Holmes
 *
 */
public class RibbonColorMapButton2 extends RibbonLargeDropDownButton2 implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private static final IntDim MAP_SIZE = new IntDim(36, 24);

  /**
   * The member popup.
   */
  private ColorMapPopupMenu2 mPopup;

  /** The m color map. */
  private ColorMap mColorMap;

  /**
   * Instantiates a new ribbon color map button.
   *
   * @param parent the parent
   */
  public RibbonColorMapButton2(ModernWindow parent) {
    this(parent, ColorMapService.getInstance().get("Jet"));
  }

  /**
   * Instantiates a new color swatch button.
   *
   * @param parent   the parent
   * @param colorMap the color map
   */
  public RibbonColorMapButton2(ModernWindow parent, ColorMap colorMap) {
    super("Color Map", new ColorMapIcon(colorMap), new ColorMapPopupMenu2(parent));

    mPopup = (ColorMapPopupMenu2) mMenu;

    mColorMap = colorMap;

    mMenu.addClickListener(this);
  }

  @Override
  public void setCompactSize() {
    UI.setSize(this, 60, Ribbon.COMPACT_BUTTON_HEIGHT);
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

    int y = (getHeight() - MAP_SIZE.getH()) / 2;

    LinearGradientPaint paint = mColorMap.getAnchorColors().toGradientPaint(new Point2D.Float(x, 0),
        new Point2D.Float(x + MAP_SIZE.getW(), 0));

    g2.setPaint(paint);
    g2.fillRect(x, y, MAP_SIZE.getW(), MAP_SIZE.getH());

    /*
     * Graphics2D g2Temp = ImageUtils.createAAStrokeGraphics(g2);
     * 
     * try { g2Temp.setPaint(paint); g2Temp.fillRect(x, y, MAP_SIZE.getW(),
     * MAP_SIZE.getH()); //g2Temp.fillRect(x, y, w, MAP_SIZE);
     * 
     * //g2Temp.setColor(LIGHT_LINE_COLOR); //g2Temp.drawRect(x, y, MAP_SIZE.getW(),
     * MAP_SIZE.getH()); //g2Temp.drawRect(x, y, w, MAP_SIZE); } finally {
     * g2Temp.dispose(); }
     */

    /*
     * Graphics2D g2Temp = ImageUtils.createAATextGraphics(g2);
     * 
     * try { g2Temp.setPaint(paint);
     * 
     * g2Temp.fillRoundRect(x, y, w, 16,
     * MaterialService.getInstance().getInts().cornerRadius();,
     * MaterialService.getInstance().getInts().cornerRadius();); } finally {
     * g2Temp.dispose(); }
     */

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

    // g2.setColor(LINE_COLOR);
    // g2.drawRect(PADDING, y, w, 16);

    // g2.setColor(ThemeService.getInstance().getColors().getHighlight(4));
    // g2.drawRect(x, y, 32, Resources.ICON_SIZE_16);

    AssetService.getInstance().loadIcon(TriangleDownVectorIcon.class, 16).drawIcon(g2, getWidth() - 16,
        (getHeight() - 16) / 2, 16);
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
