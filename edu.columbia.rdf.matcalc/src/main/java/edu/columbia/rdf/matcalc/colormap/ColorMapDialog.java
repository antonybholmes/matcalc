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

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapEditPanel;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.graphics.colormap.ColorMapType;
import org.jebtk.modern.panel.CardPanel2;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class ColorDialog.
 */
public class ColorMapDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m check two color. */
  private ModernRadioButton mCheckTwoColor = new ModernRadioButton("Two");

  /** The m check three color. */
  private ModernRadioButton mCheckThreeColor = new ModernRadioButton("Three");

  /** The m check five color. */
  private ModernRadioButton mCheckFiveColor = new ModernRadioButton("Five");

  /**
   * The member html color panel.
   */
  private ModernCompactSpinner mTextColors = new ModernCompactSpinner(1, 256, 65);

  /** The m color map editor. */
  private ColorMapEditPanel mColorMapEditor;

  /** The m text name. */
  private ModernTextField mTextName = new ModernTextField(TextUtils.EMPTY_STRING);

  /*
   * private class AlphaSpinEvents implements ModernClickListener {
   * 
   * @Override public void clicked(ModernClickEvent e) {
   * mAlphaSlider.updateValue(mAlphaSpinner.getValue()); }
   * 
   * }
   * 
   * private class AlphaSliderEvents implements ChangeListener {
   * 
   * @Override public void changed(ChangeEvent e) {
   * mAlphaSpinner.updateValue(mAlphaSlider.getValue()); }
   * 
   * }
   */

  /**
   * Instantiates a new color dialog.
   *
   * @param parent the parent
   */
  public ColorMapDialog(ModernWindow parent) {
    super(parent, "matcalc.color-map.help.url");

    setup();
  }

  /**
   * Instantiates a new color map dialog.
   *
   * @param parent   the parent
   * @param colorMap the color map
   */
  public ColorMapDialog(ModernWindow parent, ColorMap colorMap) {
    this(parent);

    mTextName.setText(colorMap.getName());

    mTextColors.setValue(colorMap.getColorCount());

    switch (colorMap.getAnchorColors().getColorCount()) {
    case 5:
      mCheckFiveColor.setSelected(true);
      mColorMapEditor.setColors(colorMap.getAnchorColors().getAnchorColor(0),
          colorMap.getAnchorColors().getAnchorColor(1), colorMap.getAnchorColors().getAnchorColor(2),
          colorMap.getAnchorColors().getAnchorColor(3), colorMap.getAnchorColors().getAnchorColor(4),
          mTextColors.getIntValue());
      break;
    case 3:
      mCheckThreeColor.setSelected(true);
      mColorMapEditor.setColors(colorMap.getAnchorColors().getAnchorColor(0),
          colorMap.getAnchorColors().getAnchorColor(1), colorMap.getAnchorColors().getAnchorColor(2),
          mTextColors.getIntValue());
      break;
    default:
      mCheckTwoColor.setSelected(true);
      mColorMapEditor.setColors(colorMap.getAnchorColors().getAnchorColor(0),
          colorMap.getAnchorColors().getAnchorColor(1), mTextColors.getIntValue());
      break;
    }
  }

  /**
   * Sets the up.
   */
  private void setup() {
    setResizable(false);
    setSize(600, 440);
    setDarkBackground();
    setTitle("Color Map Editor");

    Box box1 = VBox.create();

    Box box2;

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Name", 100));
    box2.add(new ModernTextBorderPanel(mTextName, 300));
    box1.add(box2);

    midSectionHeader("Control Points", box1);

    box1.add(mCheckTwoColor);
    // box1.add(UI.createVGap(5));
    box1.add(mCheckThreeColor);
    // box1.add(UI.createVGap(5));
    box1.add(mCheckFiveColor);

    midSectionHeader("Colors", box1);

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Shades", 100));
    box2.add(mTextColors);
    box1.add(box2);

    box1.add(UI.createVGap(20));

    mColorMapEditor = new ColorMapEditPanel(mParent);
    mColorMapEditor.setColors(ColorMap.BLUE, Color.CYAN, ColorMap.GREEN, ColorMap.YELLOW, Color.RED, 65);

    box1.add(mColorMapEditor);

    setContent(new CardPanel2(box1));

    //
    // Non UI
    //

    new ModernButtonGroup(mCheckTwoColor, mCheckThreeColor, mCheckFiveColor);

    UI.centerWindowToScreen(this);

    mCheckTwoColor.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mColorMapEditor.setColors(ColorMapType.TWO_COLOR, mTextColors.getIntValue());
      }
    });

    mCheckThreeColor.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mColorMapEditor.setColors(ColorMapType.THREE_COLOR, mTextColors.getIntValue());
      }
    });

    mCheckFiveColor.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mColorMapEditor.setColors(ColorMapType.FIVE_COLOR, mTextColors.getIntValue());
      }
    });

    mTextColors.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        ColorMapType type;

        if (mCheckFiveColor.isSelected()) {
          type = ColorMapType.FIVE_COLOR;
        } else if (mCheckThreeColor.isSelected()) {
          type = ColorMapType.THREE_COLOR;
        } else {
          type = ColorMapType.TWO_COLOR;
        }

        mColorMapEditor.setColors(type, mTextColors.getIntValue());
      }
    });

    // Respond to enter key on color counts
    mTextColors.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          ColorMapType type;

          if (mCheckFiveColor.isSelected()) {
            type = ColorMapType.FIVE_COLOR;
          } else if (mCheckThreeColor.isSelected()) {
            type = ColorMapType.THREE_COLOR;
          } else {
            type = ColorMapType.TWO_COLOR;
          }

          mColorMapEditor.setColors(type, mTextColors.getIntValue());
        }
      }
    });

    mCheckFiveColor.doClick();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {

      String name = mTextName.getText();

      if (name.length() == 0) {
        ModernMessageDialog.createWarningDialog(mParent, "The color map must have a name.");

        return;
      }

      if (ColorMapService.getInstance().contains(name)) {
        ModernMessageDialog.createWarningDialog(mParent,
            "'" + name + "' is already in use. Please choose another name.");

        return;
      }
    }

    super.clicked(e);
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public ColorMap getColorMap() {
    return new ColorMap(mTextName.getText(), mColorMapEditor.getColorMap(), false);
  }
}
