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
package edu.columbia.rdf.matcalc.toolbox.core.filter.row;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.Box;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.ui.external.microsoft.XlsxTxtGuiFileFilter;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernContentPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.text.ModernClipboardTextArea;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.text.ModernTextArea;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.toolbox.ColumnsCombo;

/**
 * The class MatrixRowFilterDialog.
 */
public class MatrixRowFilterDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The row list.
   */
  // private ModernList<String> mRowList = new ModernList<String>();

  private ModernTextArea mIdTextBox = new ModernClipboardTextArea();

  /**
   * The member model.
   */
  // private ModernListModel<String> mModel =
  // new ModernListModel<String>();

  /**
   * The member check exact.
   */
  private ModernTwoStateWidget mCheckExact = new ModernCheckSwitch("Match entire cell contents", false);

  /**
   * The member check in list.
   */
  private ModernTwoStateWidget mCheckInList = new ModernCheckSwitch("Find in list", true);

  /** The m check missing. */
  private ModernTwoStateWidget mCheckMissing = new ModernCheckSwitch("Include missing entries");

  /** The m check case. */
  private ModernTwoStateWidget mCheckCase = new ModernCheckSwitch("Case sensitive");

  /**
   * The add button.
   */
  private ModernButton mAddButton = new RibbonButton(AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /**
   * The remove button.
   */
  // private ModernButton mRemoveButton =
  // new RibbonButton(Ui.MENU_REMOVE,
  // UIResources.getInstance().loadIcon("trash_bw", 16));

  /**
   * The clear button.
   */
  private ModernButton mClearButton = new ModernButton(UI.MENU_CLEAR, AssetService.getInstance().loadIcon("clear", 16),
      80);

  /**
   * The import button.
   */
  private ModernButton importButton = new ModernButton(UI.BUTTON_LOAD,
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16), 80);

  /**
   * The member new row text field.
   */
  // private ModernTextField mNewRowTextField = new ModernTextField();

  /**
   * The member columns combo.
   */
  private ModernComboBox mColumnsCombo;

  /**
   * The member m.
   */
  private DataFrame mM;

  /**
   * The class TypeChangeEvents.
   */
  private class TypeChangeEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
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
   * @param m      the m
   */
  public MatrixRowFilterDialog(ModernWindow parent, DataFrame m) {
    super(parent, "matcalc.filter.rows.help.url");

    setAutoDispose(false);

    mM = m;

    mCheckMissing.setSelected(SettingsService.getInstance().getBool("matcalc.modules.rowfilter.include-missing"));

    mCheckMissing.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        SettingsService.getInstance().update("matcalc.modules.rowfilter.include-missing", mCheckMissing.isSelected());
      }
    });

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

    // mRowList.setModel(mModel);

    // TabbedPane tabbedPane = new TabbedPane();

    // samples

    ModernComponent content = new ModernComponent();

    /*
     * ModernComponent content = new ModernComponent();
     * 
     * int[] rows = {ModernWidget.WIDGET_HEIGHT}; int[] columns = {100, 400,
     * ModernWidget.WIDGET_HEIGHT};
     * 
     * MatrixPanel panel = new MatrixPanel(rows, columns, ModernWidget.PADDING,
     * ModernWidget.PADDING);
     * 
     * panel.add(new ModernLabel("Column")); panel.add(mColumnsCombo);
     * panel.addEmpty();
     * 
     * panel.add(new ModernAutoSizeLabel("New row id")); panel.add(new
     * ModernTextBorderPanel(mNewRowTextField)); mAddButton.setToolTip(new
     * ModernToolTip("Add Row Id",
     * "Add a new row identifier to search for. The row identifier applys only to the first column in a table."
     * )); mAddButton.addClickListener(this); panel.add(mAddButton);
     */

    Box box = HBox.create();
    box.add(new ModernLabel("Column", 80));
    UI.setSize(mColumnsCombo, 300, ModernWidget.WIDGET_HEIGHT);
    box.add(mColumnsCombo);
    box.setBorder(ModernPanel.LARGE_BORDER);
    content.setHeader(box);

    // content.setBody(new ModernLineBorderPanel(new
    // ModernScrollPane(mRowList)));

    ModernScrollPane scrollPane = new ModernScrollPane(mIdTextBox).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
        .setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS);

    content.setBody(new ModernContentPanel(scrollPane));

    box = VBox.create();
    box.setBorder(ModernWidget.LEFT_BORDER);

    box.add(importButton);
    box.add(ModernPanel.createVGap());
    // box2.add(mRemoveButton);
    // box2.add(ModernPanel.createHGap());
    box.add(mClearButton);
    content.setRight(box);

    box = VBox.create();
    box.setBorder(ModernWidget.TOP_BORDER);
    box.add(UI.createVGap(10));
    box.add(mCheckExact);
    box.add(UI.createVGap(5));
    box.add(mCheckInList);
    box.add(UI.createVGap(5));
    box.add(mCheckCase);
    box.add(UI.createVGap(5));
    box.add(mCheckMissing);

    content.setFooter(box);

    setCard(content);

    setSize(600, 500);

    setResizable(true);

    UI.centerWindowToScreen(this);

    // mNewRowTextField.addKeyListener(this);

    importButton.setToolTip("Import Row Ids",
        "Import multiple row identifiers from a text file. There should be one row id per line in the file.");
    importButton.addClickListener(this);

    // mRemoveButton.setToolTip("Remove Row Ids",
    // "Remove selected row identifiers from the list.");
    // mRemoveButton.addClickListener(this);

    mClearButton.setToolTip("Clear All Ids", "Remove all row identifiers from the list.");
    mClearButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mAddButton)) {
      // addId();
    } else if (e.getSource().equals(importButton)) {
      try {
        importIds();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (InvalidFormatException e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(mClearButton)) {
      clearIds();
    } else {
      super.clicked(e);
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
  /*
   * private void addId() {
   * 
   * if (mNewRowTextField.getText() != null &&
   * !mNewRowTextField.getText().equals("")) {
   * mModel.addValue(mNewRowTextField.getText()); }
   * 
   * //rowList.adjustSize(); //scrollPane.adjustDisplay(); }
   */

  /**
   * Removes the id.
   */
  /*
   * private void removeId() {
   * 
   * Stack<Integer> ids = new Stack<Integer>();
   * 
   * for (int i : mRowList.getSelectionModel()) { ids.push(i); }
   * 
   * while (!ids.empty()) { mModel.removeValueAt(ids.pop()); } }
   */

  /**
   * Clear ids.
   */
  private void clearIds() {
    // mModel.clear();
    mIdTextBox.clear();
  }

  /**
   * Import ids.
   *
   * @throws IOException            Signals that an I/O exception has occurred.
   * @throws InvalidFormatException the invalid format exception
   */
  private void importIds() throws IOException, InvalidFormatException {

    Path file = FileDialog.open(mParent).filter(new XlsxTxtGuiFileFilter())
        .getFile(RecentFilesService.getInstance().getPwd());

    if (file != null) {
      importIds(file);
    }
  }

  /**
   * Import ids.
   *
   * @param file the file
   * @throws IOException            Signals that an I/O exception has occurred.
   * @throws InvalidFormatException the invalid format exception
   */
  private void importIds(Path file) throws IOException, InvalidFormatException {
    /*
     * List<String> ids = new ArrayList<String>();
     * 
     * BufferedReader reader = new BufferedReader(new FileReader(file));
     * 
     * String line;
     * 
     * try { // Skip header reader.readLine();
     * 
     * while ((line = reader.readLine()) != null) { if (Io.isEmptyLine(line)) {
     * continue; }
     * 
     * List<String> tokens = TextUtils.fastSplit(line, TextUtils.TAB_DELIMITER);
     * 
     * ids.add(tokens.get(0)); } } finally { reader.close(); }
     */

    loadIds(Excel.load(file, true));
  }

  /**
   * Change ids.
   */
  private void changeIds() {
    clearIds();

    int numRowAnnotations = mM.getIndex().getNames().size();

    if (mColumnsCombo.getSelectedIndex() < numRowAnnotations) {
      // filter on row annotation

      loadIds(mM.getIndex().getText(mColumnsCombo.getText()));
    } else {
      // filter on column

      loadIds(mM.columnToText(mColumnsCombo.getSelectedIndex() - numRowAnnotations));
    }
  }

  /**
   * Load ids.
   *
   * @param ids the ids
   */
  private void loadIds(String[] ids) {
    /*
     * for (String id : ids) { mModel.addValue(id); }
     */

    mIdTextBox.setText(ids);
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
    return mIdTextBox.getLines(); // CollectionUtils.toList(mModel);
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

  /**
   * Gets the case sensitive.
   *
   * @return the case sensitive
   */
  public boolean getCaseSensitive() {
    return mCheckCase.isSelected();
  }
}
