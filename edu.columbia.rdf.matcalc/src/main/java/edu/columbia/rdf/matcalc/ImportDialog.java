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

import java.util.List;

import javax.swing.Box;

import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.Splitter;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

// TODO: Auto-generated Javadoc
/**
 * User can select how many annotations there are.
 *
 * @author Antony Holmes Holmes
 */
public class ImportDialog extends ModernDialogHelpWindow implements ModernClickListener {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The member spinner.
	 */
	private ModernCompactSpinner mSpinner;
	
	/**
	 * The member check header.
	 */
	private CheckBox mCheckHeader = new ModernCheckSwitch("Has header row", 
			SettingsService.getInstance().getAsBool("matcalc.import.file.has-header"));
	
	/** The m check skip. */
	private CheckBox mCheckSkip = new ModernCheckSwitch("Skip lines starting with", 
			true); //SettingsService.getInstance().getAsBool("matcalc.import.file.skip.lines"));

	/** The m field skip. */
	private ModernTextField mFieldSkip = 
			new ModernClipboardTextField(SettingsService.getInstance().getAsString("matcalc.import.file.skip.matches"));

	private CheckBox mNumericalCheck = new ModernCheckSwitch("Numerical", true);
	
	/** The m delimiter combo. */
	private DelimiterCombo mDelimiterCombo = new DelimiterCombo();
	
	/**
	 * Instantiates a new row annotation dialog.
	 *
	 * @param parent the parent
	 * @param rowAnnotations the row annotations
	 * @param isExcel the is excel
	 * @param delimiter the delimiter
	 */
	public ImportDialog(ModernWindow parent, 
			int rowAnnotations,
			boolean isExcel,
			String delimiter,
			boolean isNumerical) {
		super(parent, "matcalc.import.help.url");
		
		setTitle("Import");
	
		mSpinner = new ModernCompactSpinner(0, 100, rowAnnotations);
		mDelimiterCombo.setDelimiter(delimiter);
		mNumericalCheck.setSelected(isNumerical);
		
		createUi(isExcel);
		
		setup();

	}

	/**
	 * Setup.
	 */
	private void setup() {
		setSize(480, 300);
		
		mCheckSkip.setEnabled(false);
		
		addWindowFocusListener(new WindowWidgetFocusEvents(mOkButton));
		
		UI.centerWindowToScreen(this);
	}

	/**
	 * Creates the ui.
	 *
	 * @param isExcel the is excel
	 */
	private final void createUi(boolean isExcel) {
		//this.getContentPane().add(new JLabel("Change " + getProductDetails().getProductName() + " settings", JLabel.LEFT), BorderLayout.PAGE_START);
		
		Box box = VBox.create();

		// Text columns is more intuitive terminology than row annotations
		box.add(new HExpandBox("Text columns", mSpinner));
		box.add(UI.createVGap(10));
		box.add(mCheckHeader);
		
		if (!isExcel) {
			box.add(UI.createVGap(5));
			box.add(new HExpandBox(mCheckSkip, new ModernTextBorderPanel(mFieldSkip, 80)));
			box.add(UI.createVGap(5));
			box.add(new HExpandBox("Delimiter", mDelimiterCombo));
		}
		
		box.add(UI.createVGap(5));
		box.add(mNumericalCheck);
		
		setDialogCardContent(box);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public final void clicked(ModernClickEvent e) {
		if (e.getSource().equals(mOkButton)) {
			SettingsService.getInstance().update("matcalc.import.file.has-header", 
					mCheckHeader.isSelected());
			
			SettingsService.getInstance().update("matcalc.import.file.skip.lines", 
					mCheckSkip.isSelected());
			
			SettingsService.getInstance().update("matcalc.import.file.skip.matches", 
					mFieldSkip.getText());
		}
		
		super.clicked(e);
	}

	/**
	 * Gets the row annotations.
	 *
	 * @return the row annotations
	 */
	public int getRowAnnotations() {
		return mSpinner.getIntValue();
	}
	
	/**
	 * Gets the checks for header.
	 *
	 * @return the checks for header
	 */
	public boolean getHasHeader() {
		// If there are row annotations, there must be a header
		return mCheckHeader.isSelected() || getRowAnnotations() > 0;
	}

	/**
	 * Return the line prefixes to skip on on.
	 *
	 * @return the skip matches
	 */
	public List<String> getSkipLines() {
		return Splitter.onComma().text(mFieldSkip.getText());
	}
	
	/**
	 * Gets the delimiter.
	 *
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return mDelimiterCombo.getDelimiter();
	}
	
	public boolean isNumerical() {
		return mNumericalCheck.isSelected();
	}
}
