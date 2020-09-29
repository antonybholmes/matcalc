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
package edu.columbia.rdf.matcalc;

import java.awt.BorderLayout;
import java.awt.Dimension;

import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapPanel;
import org.jebtk.modern.panel.MatrixPanel;

/**
 * Allow users to pick a color map for their plot.
 * 
 * @author Antony Holmes
 *
 */
public class ColorMapChooser extends ModernWidget {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant BLUE_RED.
   */
  public static final String BLUE_RED = "blue_red";

  /**
   * The constant GREEN_RED.
   */
  public static final String GREEN_RED = "green_red";

  /**
   * The constant BLUE_YELLOW.
   */
  public static final String BLUE_YELLOW = "blue_yellow";

  /**
   * The check blue red.
   */
  private ModernRadioButton checkBlueRed = new ModernRadioButton();

  /**
   * The check green red.
   */
  private ModernRadioButton checkGreenRed = new ModernRadioButton();

  /**
   * The check blue yellow.
   */
  private ModernRadioButton checkBlueYellow = new ModernRadioButton();

  /**
   * The check invert.
   */
  private CheckBox checkInvert = new ModernCheckBox("Invert");

  /**
   * Instantiates a new color map chooser.
   */
  public ColorMapChooser() {
    int[] rows = { WIDGET_HEIGHT };
    int[] cols = { WIDGET_HEIGHT, 150 };

    MatrixPanel matrixPanel = new MatrixPanel(rows, cols, 0, 0);

    ColorMapPanel panel = new ColorMapPanel(ColorMap.createBlueWhiteRedMap(false));
    panel.setBorder(BORDER);
    matrixPanel.add(checkBlueRed);
    matrixPanel.add(panel);

    panel = new ColorMapPanel(ColorMap.createGreenBlackRedMap(false));
    panel.setBorder(BORDER);
    matrixPanel.add(checkGreenRed);
    matrixPanel.add(panel);

    panel = new ColorMapPanel(ColorMap.createBlueYellowMap(false));
    panel.setBorder(BORDER);
    matrixPanel.add(checkBlueYellow);
    matrixPanel.add(panel);

    checkBlueRed.setClickMessage(BLUE_RED);
    checkGreenRed.setClickMessage(GREEN_RED);
    checkBlueYellow.setClickMessage(BLUE_YELLOW);

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(checkBlueRed);
    group.add(checkGreenRed);
    group.add(checkBlueYellow);

    checkBlueRed.setSelected(true);

    // matrixPanel.setBorder(ModernPanel.BORDER);

    add(matrixPanel, BorderLayout.CENTER);
    add(checkInvert, BorderLayout.PAGE_END);

    setMaximumSize(new Dimension(Short.MAX_VALUE, 120));
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    checkBlueRed.addClickListener(l);
    checkGreenRed.addClickListener(l);
    checkBlueYellow.addClickListener(l);
    checkInvert.addClickListener(l);
  }

  /**
   * Gets the invert scheme.
   *
   * @return the invert scheme
   */
  public boolean getInvertScheme() {
    return checkInvert.isSelected();
  }

  /**
   * Gets the color map.
   *
   * @return the color map
   */
  public ColorMap getColorMap() {
    if (checkGreenRed.isSelected()) {
      return ColorMap.createGreenBlackRedMap(getInvertScheme());
    } else if (checkBlueYellow.isSelected()) {
      return ColorMap.createBlueYellowMap(getInvertScheme());
    } else {
      return ColorMap.createBlueWhiteRedMap(getInvertScheme());
    }

  }

}
