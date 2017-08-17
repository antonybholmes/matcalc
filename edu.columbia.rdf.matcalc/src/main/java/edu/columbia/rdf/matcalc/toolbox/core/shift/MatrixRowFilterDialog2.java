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
package edu.columbia.rdf.matcalc.toolbox.core.shift;


import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;

import javax.swing.Box;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ButtonsBox;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogFlatButton;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernDialogWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.list.ModernList;
import org.jebtk.modern.list.ModernListModel;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.widget.tooltip.ModernToolTip;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.toolbox.ColumnsCombo;



// TODO: Auto-generated Javadoc
/**
 * The class MatrixRowFilterDialog.
 */
public class MatrixRowFilterDialog2 extends ModernDialogWindow implements ModernClickListener, KeyListener {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The row list.
	 */
	private ModernList<String> mRowList = new ModernList<String>();

	/**
	 * The member model.
	 */
	private ModernListModel<String> mModel = 
			new ModernListModel<String>();

	/**
	 * The member check exact.
	 */
	private CheckBox mCheckExact = 
			new ModernCheckBox("Match entire cell contents", false);

	/**
	 * The member check in list.
	 */
	private CheckBox mCheckInList = 
			new ModernCheckBox("Find in list", true);
	
	/** The m check missing. */
	private CheckBox mCheckMissing = 
			new ModernCheckBox("Include missing entries");

	/**
	 * The add button.
	 */
	private ModernButton mAddButton = 
			new ModernDialogFlatButton(UIService.getInstance().loadIcon(PlusVectorIcon.class, 16));

	/**
	 * The remove button.
	 */
	private ModernButton mRemoveButton = new ModernDialogFlatButton(UI.MENU_REMOVE,
			UIService.getInstance().loadIcon("trash_bw", 16));

	/**
	 * The clear button.
	 */
	private ModernButton mClearButton = new ModernDialogFlatButton(UI.MENU_CLEAR,
			UIService.getInstance().loadIcon("clear", 16));

	/**
	 * The import button.
	 */
	private ModernButton importButton = 
			new ModernDialogFlatButton(UI.BUTTON_IMPORT, UIService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));



	/**
	 * The member new row text field.
	 */
	private ModernTextField mNewRowTextField = new ModernTextField();

	/**
	 * The member columns combo.
	 */
	private ModernComboBox mColumnsCombo = new ModernComboBox();

	/**
	 * The member m.
	 */
	private AnnotationMatrix mM;

	/**
	 * The class TypeChangeEvents.
	 */
	private class TypeChangeEvents implements ModernClickListener {

		/* (non-Javadoc)
		 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
		 */
		@Override
		public void clicked(ModernClickEvent e) {
			changeIds();
		}

	}

	/**
	 * Instantiates a new matrix row filter dialog.
	 *
	 * @param parent the parent
	 * @param m the m
	 */
	public MatrixRowFilterDialog2(ModernWindow parent, AnnotationMatrix m) {
		super(parent);

		mM = m;
		
		mCheckMissing.setSelected(SettingsService.getInstance().getAsBool("matcalc.modules.rowfilter.include-missing"));

		mCheckMissing.addClickListener(new ModernClickListener() {

			@Override
			public void clicked(ModernClickEvent e) {
				SettingsService.getInstance().update("matcalc.modules.rowfilter.include-missing",
						mCheckMissing.isSelected());
			}});
		
		mColumnsCombo = new ColumnsCombo(m);
		
		setup();

		mColumnsCombo.setSelectedIndex(0);

		mColumnsCombo.addClickListener(new TypeChangeEvents());

		changeIds();
	}



	/**
	 * Setup.
	 */
	private void setup() {
		setTitle("Row Filter");

		mRowList.setModel(mModel);



		//TabbedPane tabbedPane = new TabbedPane();

		// samples

		ModernComponent content = new ModernComponent();

		int[] rows = {ModernWidget.WIDGET_HEIGHT};
		int[] columns = {100, 400, ModernWidget.WIDGET_HEIGHT};

		MatrixPanel panel = new MatrixPanel(rows, columns, ModernWidget.PADDING, ModernWidget.PADDING);

		panel.add(new ModernAutoSizeLabel("New row id"));
		panel.add(new ModernTextBorderPanel(mNewRowTextField));
		mAddButton.setToolTip(new ModernToolTip("Add Row Id", 
				"Add a new row identifier to search for. The row identifier applys only to the first column in a table."));
		mAddButton.addClickListener(this);
		panel.add(mAddButton);

		panel.add(new ModernAutoSizeLabel("Type"));
		
		panel.add(mColumnsCombo);


		content.add(panel, BorderLayout.PAGE_START);

		ModernScrollPane scrollPane = new ModernScrollPane(mRowList);
		scrollPane.setBorder(ModernPanel.TOP_BOTTOM_BORDER);
		content.add(scrollPane, BorderLayout.CENTER);

		Box box = HBox.create();

		box.add(importButton);
		box.add(ModernPanel.createHGap());
		box.add(mRemoveButton);
		box.add(ModernPanel.createHGap());
		box.add(mClearButton);
		content.add(box, BorderLayout.PAGE_END);


		setContent(content);

		ButtonsBox buttonPanel = new ButtonsBox();

		buttonPanel.addLeft(mCheckExact);
		buttonPanel.addLeft(ModernPanel.createHGap());
		buttonPanel.addLeft(mCheckInList);
		buttonPanel.addLeft(ModernPanel.createHGap());
		buttonPanel.addLeft(mCheckMissing);
	
		//addOkCancelButtons();

		setSize(720, 640);

		UI.centerWindowToScreen(this);

		mNewRowTextField.addKeyListener(this);

		importButton.setToolTip("Import Row Ids", 
				"Import multiple row identifiers from a text file. There should be one row id per line in the file.");

		importButton.addClickListener(this);

		mRemoveButton.setToolTip("Remove Row Ids", 
				"Remove selected row identifiers from the list.");

		mRemoveButton.addClickListener(this);

		mClearButton.setToolTip("Clear All Ids", 
				"Remove all row identifiers from the list.");

		mClearButton.addClickListener(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	public final void clicked(ModernClickEvent e) {
		if (e.getSource().equals(mAddButton)) {
			addId();
		} else if (e.getSource().equals(importButton)) {
			try {
				importIds();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InvalidFormatException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mRemoveButton)) {
			removeId();
		} else if (e.getSource().equals(mClearButton)) {
			clearIds();
		} else if (e.getMessage().equals(UI.BUTTON_OK)) {
			setStatus(ModernDialogStatus.OK);

			close();
		} else if (e.getMessage().equals(UI.BUTTON_CANCEL)) {
			close();
		} else {
			// do nothing
		}
	}

	/**
	 * Gets the rows.
	 *
	 * @return the rows
	 */
	

	/**
	 * Adds the id.
	 */
	private void addId() {

		if (mNewRowTextField.getText() != null && !mNewRowTextField.getText().equals("")) {
			mModel.addValue(mNewRowTextField.getText());
		}

		//rowList.adjustSize();
		//scrollPane.adjustDisplay();
	}

	/**
	 * Removes the id.
	 */
	private void removeId() {

		Stack<Integer> ids = new Stack<Integer>();

		for (int i : mRowList.getSelectionModel()) {
			ids.push(i);
		}

		while (!ids.empty()) {
			mModel.removeValueAt(ids.pop());
		}
	}

	/**
	 * Clear ids.
	 */
	private void clearIds() {
		mModel.clear();
	}

	/**
	 * Import ids.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvalidFormatException the invalid format exception
	 */
	private void importIds() throws IOException, InvalidFormatException {

		Path file = FileDialog.open(mParent).all().getFile(RecentFilesService.getInstance().getPwd());

		if (file == null) {
			return;
		}

		importIds(file);
	}

	/**
	 * Import ids.
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvalidFormatException the invalid format exception
	 */
	private void importIds(Path file) throws IOException, InvalidFormatException {
		/*
		List<String> ids = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line;

		try {
			// Skip header
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				List<String> tokens = TextUtils.fastSplit(line, TextUtils.TAB_DELIMITER);

				ids.add(tokens.get(0));
			}
		} finally {
			reader.close();
		}
		*/
		
		loadIds(Excel.load(file, true));
	}

	/**
	 * Change ids.
	 */
	private void changeIds() {
		clearIds();

		int numRowAnnotations = mM.getRowAnnotationNames().size();

		if (mColumnsCombo.getSelectedIndex() < numRowAnnotations) {
			// filter on row annotation

			loadIds(mM.getRowAnnotationText(mColumnsCombo.getText()));
		} else {
			// filter on column

			loadIds(mM.columnAsText(mColumnsCombo.getSelectedIndex() - numRowAnnotations));
		}


	}

	/**
	 * Load ids.
	 *
	 * @param ids the ids
	 */
	private void loadIds(List<String> ids) {
		for (String id : ids) {
			mModel.addValue(id);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public final void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public final void keyTyped(KeyEvent e) {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			addId();
		}
	}

	/**
	 * Gets the column text.
	 *
	 * @return the column text
	 */
	public String getColumnText() {
		return mColumnsCombo.getText();
	}
	
	/**
	 * Gets the column.
	 *
	 * @return the column
	 */
	public int getColumn() {
		return mColumnsCombo.getSelectedIndex();
	}

	/**
	 * Gets the exact match.
	 *
	 * @return the exact match
	 */
	public boolean getExactMatch() {
		return mCheckExact.isSelected();
	}



	/**
	 * Gets the names.
	 *
	 * @return the names
	 */
	public List<String> getNames() {
		return CollectionUtils.toList(mModel);
	}


	/**
	 * Gets the in list.
	 *
	 * @return the in list
	 */
	public boolean getInList() {
		return mCheckInList.isSelected();
	}
	
	/**
	 * Gets the include missing.
	 *
	 * @return the include missing
	 */
	public boolean getIncludeMissing() {
		return mCheckMissing.isSelected();
	}
}
