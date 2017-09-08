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
import org.jebtk.graphplot.figure.properties.FontProperties;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
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
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

// TODO: Auto-generated Javadoc
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
	private ModernCheckButton mBoldButton = 
			new ModernCheckButton(UIService.getInstance().loadIcon("font_bold", UIService.ICON_SIZE_16));

	/**
	 * The italic button.
	 */
	private ModernCheckButton mItalicButton = 
			new ModernCheckButton(UIService.getInstance().loadIcon("font_italic", UIService.ICON_SIZE_16));

	/**
	 * The underline button.
	 */
	private ModernCheckButton mUnderlineButton = 
			new ModernCheckButton(UIService.getInstance().loadIcon("font_underline", UIService.ICON_SIZE_16));

	/** The m properties. */
	private FontProperties mProperties;

	/** The m color button. */
	private ColorSwatchButton mColorButton;


	/**
	 * Instantiates a new tick color plot control.
	 *
	 * @param parent the parent
	 * @param properties the properties
	 */
	public FontControl(ModernWindow parent, FontProperties properties) {
		mProperties = properties;
		
		mColorButton = new ColorSwatchButton(parent, properties.getColor());
		
		mColorButton.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				mProperties.setColor(mColorButton.getSelectedColor());
			}});

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
		//setBorder(ModernWidget.BORDER);
		
		mProperties.addChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				setDetails();
			}});

		mFontsCombo.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				mProperties.setFamily(mFontsCombo.getText());
			}});

		mFontSizesCombo.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				mProperties.setFontSize(TextUtils.parseInt(mFontSizesCombo.getText()));
			}});

		mBoldButton.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				setBoldItalic();
			}});
		
		mItalicButton.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				setBoldItalic();
			}});
		
		mUnderlineButton.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				setBoldItalic();
			}});
		
		setDetails();
	}
	
	/**
	 * Sets the details.
	 */
	private void setDetails() {
		mFontsCombo.setText(mProperties.getFont().getFamily());
		mFontSizesCombo.setText(Integer.toString(mProperties.getFont().getSize()));
		mBoldButton.setSelected(mProperties.getFont().isBold());
		mItalicButton.setSelected(mProperties.getFont().isItalic());
		mUnderlineButton.setSelected(mProperties.getFont().getAttributes().containsKey(TextAttribute.UNDERLINE) &&
				mProperties.getFont().getAttributes().get(TextAttribute.UNDERLINE).equals(TextAttribute.UNDERLINE_ON));
		mColorButton.setSelectedColor(mProperties.getColor());
	}

	/**
	 * Sets the bold italic.
	 */
	private void setBoldItalic() {
		Font font = null;
		
		if (mBoldButton.isSelected() && mItalicButton.isSelected()) {
			font = new Font(mProperties.getFont().getFamily(), 
					Font.BOLD | Font.ITALIC, 
					mProperties.getFont().getSize());
		} else if (mBoldButton.isSelected()) {
			font = new Font(mProperties.getFont().getFamily(), 
					Font.BOLD, 
					mProperties.getFont().getSize());
		} else if (mItalicButton.isSelected()) {
			font = new Font(mProperties.getFont().getFamily(), 
					Font.ITALIC, 
					mProperties.getFont().getSize());
		} else {
			font = new Font(mProperties.getFont().getFamily(), 
					Font.PLAIN, 
					mProperties.getFont().getSize());
		}

		if (mUnderlineButton.isSelected()) {
			Map<TextAttribute, Object> map =
					new Hashtable<TextAttribute, Object>();
			map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			font = font.deriveFont(map);
		}
		
		mProperties.setFont(font);
	}
}
