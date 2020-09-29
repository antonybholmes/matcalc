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

import org.jebtk.core.ColorUtils;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.theme.ThemeService;

/**
 * The class MatrixGroupCheckBox.
 */
public class MatrixGroupCheckBox extends ModernCheckBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The color.
   */
  private Color mColor;

  /** The m light color. */
  private Color mLightColor;

  /** The Constant GRAY1. */
  private static final Color GRAY1 = ThemeService.getInstance().getColors().getGray(3);

  /** The Constant GRAY2. */
  private static final Color GRAY2 = ThemeService.getInstance().getColors().getGray(5);

  /**
   * Instantiates a new matrix group check box.
   *
   * @param group the group
   */
  public MatrixGroupCheckBox(XYSeries group) {
    this(group, false);
  }

  /**
   * Instantiates a new matrix group check box.
   *
   * @param group    the group
   * @param selected the selected
   */
  public MatrixGroupCheckBox(XYSeries group, boolean selected) {
    super(group.getName(), selected);

    mColor = group.getColor();
    mLightColor = ColorUtils.getTransparentColor50(mColor);

    UI.setSize(this, ModernButton.getButtonWidth(getFont(), mText1) + 42, ModernButton.getButtonHeight());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.button.ModernCheckBox#drawBackground(java.awt.
   * Graphics2D)
   */
  // @Override
  public void drawBackground(Graphics2D g2) {
    // Do nothing

    /*
     * int iconX = mInternalRect.getX(); int iconY = (getHeight() - 16) / 2;
     * 
     * paintOutlined(g2, new Rectangle(iconX, iconY, 16, 16));
     */
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.button.ModernCheckBox#drawForegroundAA(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawForegroundAA(Graphics2D g2) {
    int iconX = mInternalRect.getX() + 1;
    int iconY = (getHeight() - 16) / 2;
    int y2 = (getHeight() - 10) / 2;

    Graphics2D g2Temp = ImageUtils.createAAStrokeGraphics(g2);

    try {
      if (isSelected()) {
        g2Temp.setColor(mLightColor);
        g2Temp.fillRoundRect(iconX + 2, y2, 22, 10, 10, 10);

        g2Temp.setColor(mColor);
        g2Temp.fillOval(iconX + 10, iconY, 16, 16);

        // g2Temp.setColor(GRAY2);
        // g2Temp.drawOval(iconX + 16, iconY, 16, 16);
      } else {
        g2Temp.setColor(GRAY1);
        g2Temp.fillRoundRect(iconX + 2, y2, 22, 10, 10, 10);

        g2Temp.setColor(Color.WHITE);
        g2Temp.fillOval(iconX, iconY, 16, 16);

        g2Temp.setColor(GRAY2);
        g2Temp.drawOval(iconX, iconY, 16, 16);
      }
    } finally {
      g2Temp.dispose();
    }

    if (mText1 != null) {
      int x = iconX + 32 + PADDING;

      // g2.setColor(getForeground());
      g2.setColor(isSelected() ? mColor : GRAY2);
      g2.drawString(mText1, x, getTextYPosCenter(g2, getHeight()));
    }
  }
}
