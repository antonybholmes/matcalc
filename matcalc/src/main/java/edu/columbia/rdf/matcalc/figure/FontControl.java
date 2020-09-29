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

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.props.FontProps;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckButton;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.font.FontSizesComboBox;
import org.jebtk.modern.font.FontsComboBox;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class TickColorPlotControl.
 */
public class FontControl extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check visible.
   */
  private ModernComboBox mFontsCombo = new FontsComboBox();

  /** The m font sizes combo. */
  private ModernComboBox mFontSizesCombo = new FontSizesComboBox();

  /** The m bold button. */
  private ModernCheckButton mBoldButton = new ModernCheckButton(
      AssetService.getInstance().loadIcon("font_bold", AssetService.ICON_SIZE_16));

  /**
   * The italic button.
   */
  private ModernCheckButton mItalicButton = new ModernCheckButton(
      AssetService.getInstance().loadIcon("font_italic", AssetService.ICON_SIZE_16));

  /**
   * The underline button.
   */
  private ModernCheckButton mUnderlineButton = new ModernCheckButton(
      AssetService.getInstance().loadIcon("font_underline", AssetService.ICON_SIZE_16));

  /** The m properties. */
  private FontProps mProps;

  /** The m color button. */
  private ColorSwatchButton mColorButton;

  /**
   * Instantiates a new tick color plot control.
   *
   * @param parent the parent
   * @param Props  the properties
   */
  public FontControl(ModernWindow parent, FontProps properties) {
    mProps = properties;

    mColorButton = new ColorSwatchButton(parent, properties.getColor());

    mColorButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mProps.setColor(mColorButton.getSelectedColor());
      }
    });

    Box box = HBox.create();

    UI.setMinMaxSize(mFontsCombo, ModernWidget.MIN_SIZE, ModernWidget.MAX_SIZE);
    box.add(mFontsCombo);
    box.add(ModernPanel.createHGap());
    box.add(mFontSizesCombo);
    add(box);

    add(ModernPanel.createVGap());

    box = HBox.create();
    box.add(mBoldButton);
    box.add(mItalicButton);
    box.add(mUnderlineButton);
    box.add(ModernPanel.createHGap());
    box.add(mColorButton);
    add(box);
    // setBorder(ModernWidget.BORDER);

    mProps.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        setDetails();
      }
    });

    mFontsCombo.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mProps.setFamily(mFontsCombo.getText());
      }
    });

    mFontSizesCombo.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mProps.setFontSize(TextUtils.parseInt(mFontSizesCombo.getText()));
      }
    });

    mBoldButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        setBoldItalic();
      }
    });

    mItalicButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        setBoldItalic();
      }
    });

    mUnderlineButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        setBoldItalic();
      }
    });

    setDetails();
  }

  /**
   * Sets the details.
   */
  private void setDetails() {
    mFontsCombo.setText(mProps.getFont().getFamily());
    mFontSizesCombo.setText(Integer.toString(mProps.getFont().getSize()));
    mBoldButton.setSelected(mProps.getFont().isBold());
    mItalicButton.setSelected(mProps.getFont().isItalic());
    mUnderlineButton.setSelected(mProps.getFont().getAttributes().containsKey(TextAttribute.UNDERLINE)
        && mProps.getFont().getAttributes().get(TextAttribute.UNDERLINE).equals(TextAttribute.UNDERLINE_ON));
    mColorButton.setSelectedColor(mProps.getColor());
  }

  /**
   * Sets the bold italic.
   */
  private void setBoldItalic() {
    Font font = null;

    if (mBoldButton.isSelected() && mItalicButton.isSelected()) {
      font = new Font(mProps.getFont().getFamily(), Font.BOLD | Font.ITALIC, mProps.getFont().getSize());
    } else if (mBoldButton.isSelected()) {
      font = new Font(mProps.getFont().getFamily(), Font.BOLD, mProps.getFont().getSize());
    } else if (mItalicButton.isSelected()) {
      font = new Font(mProps.getFont().getFamily(), Font.ITALIC, mProps.getFont().getSize());
    } else {
      font = new Font(mProps.getFont().getFamily(), Font.PLAIN, mProps.getFont().getSize());
    }

    if (mUnderlineButton.isSelected()) {
      Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
      map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      font = font.deriveFont(map);
    }

    mProps.setFont(font);
  }
}
