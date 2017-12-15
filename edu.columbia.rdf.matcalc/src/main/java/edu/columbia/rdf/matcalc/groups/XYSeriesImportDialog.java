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
package edu.columbia.rdf.matcalc.groups;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.Box;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.ui.external.microsoft.ExcelDialog;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ButtonsBox;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.dialog.ModernDialogButton;
import org.jebtk.modern.dialog.ModernDialogFlatButton;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernDialogWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.CrossVectorIcon;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

// TODO: Auto-generated Javadoc
/**
 * Allows a matrix group to be edited.
 *
 * @author Antony Holmes Holmes
 */
public class XYSeriesImportDialog extends ModernDialogWindow implements ModernClickListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m check select all. */
	private CheckBox mCheckSelectAll = 
			new ModernCheckBox("Select All", true);

	/**
	 * The member ok button.
	 */
	private ModernButton mOkButton = 
			new ModernDialogButton(UI.BUTTON_OK);

	/**
	 * The member close button.
	 */
	private ModernButton mCloseButton = 
			new ModernDialogButton(UI.BUTTON_CANCEL);
	
	/**
	 * The member load button.
	 */
	private ModernButton mFilterButton = 
			new ModernDialogFlatButton("Filter...", UIService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));
	
	/** The m clear filter button. */
	private ModernButton mClearFilterButton = 
			new ModernDialogFlatButton("Clear Filter", UIService.getInstance().loadIcon(CrossVectorIcon.class, 16));

	/** The m groups matrix. */
	private DataFrame mGroupsMatrix;

	/** The m matrix. */
	private DataFrame mMatrix;

	/** The m series map. */
	private Map<String, XYSeries> mSeriesMap;

	/** The m use map. */
	private Map<String, ModernCheckBox> mUseMap;

	/** The m names. */
	private Set<String> mNames;
	
	/** The m filtered names. */
	private Set<String> mFilteredNames = null;

	/** The m check complementary group. */
	private CheckBox mCheckComplementaryGroup =
			new ModernCheckBox("Complementary groups", true);

	/** The m sample map. */
	private Map<String, Set<String>> mSampleMap;

	/** The m content. */
	private ModernPanel mContent;

	/** The m table. */
	private XYSeriesTable mTable;

	/** The m model. */
	private XYSeriesTableModel mModel;


	/**
	 * Instantiates a new XY series dialog.
	 *
	 * @param parent the parent
	 * @param matrix the matrix
	 * @param groupsMatrix the groups matrix
	 */
	public XYSeriesImportDialog(ModernWindow parent, 
			DataFrame matrix, 
			DataFrame groupsMatrix) {
		super(parent);

		mMatrix = matrix;
		mGroupsMatrix = groupsMatrix;
		
		mTable = new XYSeriesTable(parent);

		setTitle("Import groups");

		setup();

		createUi();
		
		refresh();
	}

	/**
	 * Setup.
	 */
	private void setup() {
		mSampleMap = new TreeMap<String, Set<String>>();

		mNames = new TreeSet<String>();

		for (int i = 0; i < mGroupsMatrix.getRows(); ++i) {
			String name = mGroupsMatrix.getText(i, 0);
			String sample = createSampleRegex(mGroupsMatrix.getText(i, 1));

			if (!mSampleMap.containsKey(mGroupsMatrix.getText(i, 0))) {
				mSampleMap.put(name, new TreeSet<String>());
			}

			mNames.add(name);

			mSampleMap.get(name).add(sample);
		}
		
		mSeriesMap = new TreeMap<String, XYSeries>();
		
		for (String name : mNames) {
			XYSeries series = new XYSeries(name, ColorUtils.randomColor());

			for (String sample : mSampleMap.get(name)) {
				series.addRegex(sample);
			}

			mSeriesMap.put(name, series);
		}
		
		mFilteredNames = mNames;
		
		mCheckSelectAll.addClickListener(new ModernClickListener() {

			@Override
			public void clicked(ModernClickEvent e) {
				for (ModernCheckBox c : mUseMap.values()) {
					c.setSelected(mCheckSelectAll.isSelected());
				}
			}});
		
		mFilterButton.addClickListener(new ModernClickListener() {

			@Override
			public void clicked(ModernClickEvent e) {
				try {
					filter();
				} catch (InvalidFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		
		mClearFilterButton.addClickListener(new ModernClickListener() {

			@Override
			public void clicked(ModernClickEvent e) {
				clearFilter();
			}});
		
		mClearFilterButton.setVisible(false);
		
		mOkButton.addClickListener(this);
		mCloseButton.addClickListener(this);

		setSize(640, 480);

		setResizable(true);

		UI.centerWindowToScreen(this);
	}


	/**
	 * Creates the ui.
	 */
	private final void createUi() {
		//this.getContentPane().add(new JLabel("Change " + getProductDetails().getProductName() + " settings", JLabel.LEFT), BorderLayout.PAGE_START);

		mContent = new ModernPanel();

		
		
		HBox box = HBox.create();
		box.setBorder(ModernWidget.TOP_BORDER);
		box.add(mFilterButton);
		box.add(ModernWidget.createHGap());
		box.add(mClearFilterButton);
	
		mContent.setFooter(box);

		mContent.setBody(new ModernScrollPane(mTable));
		
		setContent(mContent);

		ButtonsBox buttonPanel = new ButtonsBox();
		buttonPanel.addLeft(mCheckComplementaryGroup);
		buttonPanel.addRight(mOkButton);
		buttonPanel.addRight(ModernPanel.createHGap());
		buttonPanel.addRight(mCloseButton);

		setButtons(buttonPanel);
		
	}
	
	/**
	 * Refresh.
	 */
	private void refresh() {
		Box box = VBox.create();

		box.add(mCheckSelectAll);
		box.add(UI.createVGap(5));

		mUseMap = new HashMap<String, ModernCheckBox>();

		List<XYSeries> filteredSeries = new ArrayList<XYSeries>();
		
		System.err.println("filtered " + mFilteredNames);
		
		for (String name : mNames) {
			if (!mFilteredNames.contains(name)) {
				continue;
			}

			filteredSeries.add(mSeriesMap.get(name));

			/*
			Box box2 = HBox.create();

			ModernCheckBox checkUse = 
					new ModernCheckBox(name, true, ModernWidget.EXTRA_LARGE_SIZE);

			mUseMap.put(name, checkUse);

			box2.add(checkUse);
			box2.add(Ui.createHGap(5));

			ColorSwatchButton colorButton = new ColorSwatchButton(getParentWindow(), 
					series.getColor());

			box2.add(colorButton);

			mColorMap.put(name, colorButton);

			box.add(box2);

			box.add(Ui.createVGap(5));
			*/
		}
		
		mModel = new XYSeriesTableModel(filteredSeries);
		
		mTable.setModel(mModel);

		mTable.getColumnModel().setWidth(0, 24);
		mTable.getColumnModel().setWidth(1, 200);
		mTable.getColumnModel().setWidth(2, 48);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.ui.event.ModernClickEvent)
	 */
	public final void clicked(ModernClickEvent e) {
		if (e.getSource().equals(mOkButton)) {
			setStatus(ModernDialogStatus.OK);
		} 

		close();
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public List<XYSeries> getGroups() {
		List<XYSeries> ret = new ArrayList<XYSeries>();

		for (int i = 0; i < mModel.getRowCount(); ++i) {
			if (mModel.isSelected(i)) {
				XYSeries series = mModel.getSeries(i); //mSeriesMap.get(name);

				ret.add(series);

				// Add group covering everything this group does not so we
				// create one group per line

				if (mCheckComplementaryGroup.isSelected()) {
					XYSeries s2 = new XYSeries("Non " + series.getName(), Color.WHITE);

					Set<Integer> columns = CollectionUtils.toSet(XYSeries.findColumnIndices(mMatrix, series));

					for (int j = 0; j < mMatrix.getCols(); ++j) {
						if (!columns.contains(j)) {
							s2.addRegex(createSampleRegex(mMatrix.getColumnName(j)));
						}
					}

					ret.add(s2);
				}
			}
		}
		
		/*
		for (String name : mFilteredNames) {
			if (mUseMap.get(name).isSelected()) {
				XYSeries series = mSeriesMap.get(name);

				series.setColor(mColorMap.get(name).getSelectedColor());

				ret.add(series);

				// Add group covering everything this group does not so we
				// create one group per line

				if (mCheckComplementaryGroup.isSelected()) {
					XYSeries s2 = new XYSeries("Non " + name, Color.WHITE);

					Set<Integer> columns = CollectionUtils.toSet(XYSeries.findColumnIndices(mMatrix, series));

					for (int i = 0; i < mMatrix.getColumnCount(); ++i) {
						if (!columns.contains(i)) {
							s2.addRegex(createSampleRegex(mMatrix.getColumnName(i)));
						}
					}

					ret.add(s2);
				}
			}
		}
		*/

		return ret;
	}
	
	/**
	 * Filter.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvalidFormatException the invalid format exception
	 */
	private void filter() throws IOException, InvalidFormatException {
		Path file = ExcelDialog.open(getParentWindow()).xlsx().getFile(RecentFilesService.getInstance().getPwd());
		
		if (file == null) {
			return;
		}
		
		List<String> ids = Excel.getTextFromFile(file, true);
		
		// Unselect all
		
		mCheckSelectAll.setSelected(false);
		
		//for (ModernCheckBox c : mUseMap.values()) {
		//	c.setSelected(false);
		//}
		
		mFilteredNames = new TreeSet<String>();
		
		for (String id : ids) {
			mFilteredNames.add(id);
		}
		
		mClearFilterButton.setVisible(true);
		
		refresh();
	}
	
	/**
	 * Clear filter.
	 */
	private void clearFilter() {
		mFilteredNames = mNames;
		
		// Unselect all
		
		mCheckSelectAll.setSelected(true);
		
		for (ModernCheckBox c : mUseMap.values()) {
			c.setSelected(true);
		}
		
		mClearFilterButton.setVisible(false);
		
		refresh();
	}
	
	/**
	 * Creates the sample regex.
	 *
	 * @param name the name
	 * @return the string
	 */
	private static String createSampleRegex(String name) {
		String ret = name;
		
		ret = ret.replaceAll("\\(", "\\\\(");
		ret = ret.replaceAll("\\)", "\\\\)");
		
		return ret;
	}
}
