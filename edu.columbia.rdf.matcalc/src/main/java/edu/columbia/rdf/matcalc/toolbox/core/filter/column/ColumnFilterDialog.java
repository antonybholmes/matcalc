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
package edu.columbia.rdf.matcalc.toolbox.core.filter.column;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.Indexed;
import org.jebtk.core.IndexedInt;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.ui.external.microsoft.ExcelUI;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dataview.ModernDataModel;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.ArrowDownVectorIcon;
import org.jebtk.modern.graphics.icons.ArrowUpVectorIcon;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.table.ModernRowTable;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allow ordering of columns or rows in a table.
 * 
 * @author Antony Holmes
 *
 */
public class ColumnFilterDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member model.
   */
  private ColumnFilterTableModel mModel = null;

  /**
   * The member table.
   */
  private ModernRowTable mTable = new ColumnFilterTable();

  /**
   * The member up button.
   */
  private ModernButton mUpButton = new ModernButton(AssetService.getInstance().loadIcon(ArrowUpVectorIcon.class, 16));

  /**
   * The member down button.
   */
  private ModernButton mDownButton = new ModernButton(
      AssetService.getInstance().loadIcon(ArrowDownVectorIcon.class, 16));

  /**
   * The member alphabetical button.
   */
  private ModernButton mAlphabeticalButton = new ModernButton("Alphabetical",
      AssetService.getInstance().loadIcon("alphabetical", 16));

  /** The m select all button. */
  private ModernButton mSelectAllButton = new ModernButton("Select All");

  /**
   * The member load button.
   */
  private ModernButton mLoadButton = new ModernButton("Load order",
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

  /**
   * The member m.
   */
  private DataFrame mM;

  /** The m select all. */
  private boolean mSelectAll = false;

  /**
   * Instantiates a new column order dialog.
   *
   * @param parent the parent
   * @param m      the m
   */
  public ColumnFilterDialog(ModernWindow parent, DataFrame m) {
    super(parent, "matcalc.filter.columns.help.url");

    setAutoDispose(false);

    mM = m;

    setTitle("Filter Columns");

    ModernComponent content = new ModernComponent();

    mTable.setShowHeader(false);

    // ModernScrollPane scrollPane = new ModernScrollPane(mTable);
    // scrollPane.setBorder(ModernTheme.getInstance().getClass("widget").getBorder("dialog"));
    // scrollPane.setBorder(ModernWidget.LINE_BORDER);
    // scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
    // scrollPane.getViewport().setBackground(Color.WHITE);

    content.setBody(new ModernScrollPane(mTable).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
        .setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS));

    Box box = VBox.create();

    mUpButton.addClickListener(this);
    box.add(mUpButton);

    box.add(ModernPanel.createVGap());

    mDownButton.addClickListener(this);
    box.add(mDownButton);

    box.setBorder(BorderService.getInstance().createLeftBorder(10));

    content.add(box, BorderLayout.LINE_END);

    box = HBox.create();

    box.setBorder(BorderService.getInstance().createBottomBorder(10));
    box.add(mSelectAllButton);
    box.add(ModernPanel.createHGap());
    box.add(mLoadButton);
    box.add(ModernPanel.createHGap());
    box.add(mAlphabeticalButton);

    content.setHeader(box);

    setCard(content);

    changeIds();

    setSize(600, 400);
    setResizable(true);
    UI.centerWindowToScreen(this);

    mSelectAllButton.addClickListener(this);
    mLoadButton.addClickListener(this);
    mAlphabeticalButton.addClickListener(this);
  }

  /**
   * Load ids.
   *
   * @param ids the ids
   */
  private void loadIds(List<Indexed<Integer, String>> ids) {
    loadIds(ids, null);
  }

  /**
   * Load ids.
   *
   * @param ids        the ids
   * @param visibleMap the visible map
   */
  private void loadIds(List<Indexed<Integer, String>> ids, Map<Integer, Boolean> visibleMap) {
    mModel = new ColumnFilterTableModel(ids, visibleMap);

    mTable.setModel(mModel);
    mTable.getColumnModel().setWidth(0, 50);
    mTable.getColumnModel().setWidth(1, 50);
    mTable.getColumnModel().setWidth(2, 400);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mSelectAllButton)) {
      selectAll();
    } else if (e.getSource().equals(mUpButton)) {
      swapUp();
    } else if (e.getSource().equals(mDownButton)) {
      swapDown();
    } else if (e.getSource().equals(mLoadButton)) {
      try {
        sortByExternalIdList();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (InvalidFormatException e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(mAlphabeticalButton)) {
      sortAlphabetically();
    } else {
      super.clicked(e);
    }
  }

  /**
   * Select all.
   */
  private void selectAll() {
    ModernDataModel model = mTable.getModel();

    for (int i = 0; i < model.getRowCount(); ++i) {
      model.setValueAt(i, 0, mSelectAll);
    }

    mSelectAll = !mSelectAll;
  }

  /**
   * Sort alphabetically.
   */
  private void sortAlphabetically() {
    Map<String, List<Integer>> rowMap = new HashMap<String, List<Integer>>();

    for (int i = 0; i < mTable.getRowCount(); ++i) {
      Indexed<Integer, String> column = mModel.get(i);

      // We must deal with multiple samples with the same name.
      if (!rowMap.containsKey(column.getValue())) {
        rowMap.put(column.getValue(), new ArrayList<Integer>());
      }

      rowMap.get(column.getValue()).add(column.getIndex());
    }

    List<String> sortedNames = CollectionUtils.sort(rowMap.keySet());

    List<Indexed<Integer, String>> columns = new ArrayList<Indexed<Integer, String>>();

    for (String name : sortedNames) {
      List<Integer> ids = CollectionUtils.sort(rowMap.get(name));

      for (int id : ids) {
        columns.add(new Indexed<Integer, String>(id, name));
      }
    }

    loadIds(columns);
  }

  /**
   * Swap up.
   */
  private void swapUp() {
    List<Integer> indices = new ArrayList<Integer>();

    for (int i = 0; i < mTable.getRowModel().size(); ++i) {
      if (!mTable.getRowModel().isSelected(i)) {
        continue;
      }

      indices.add(i);
    }

    mModel.swapUp(indices);

    // columnTable.getCellSelectionModel().clear();
    mTable.getRowModel().unselectAll();

    for (int i : indices) {
      if (i == 0) {
        continue;
      }

      // columnTable.getCellSelectionModel().getRowSelectionModel().add(i - 1);

      mTable.getRowModel().setSelected(i - 1);
    }
  }

  /**
   * Swap down.
   */
  private void swapDown() {
    List<Integer> indices = new ArrayList<Integer>();

    for (int i = 0; i < mTable.getRowModel().size(); ++i) {
      if (!mTable.getRowModel().isSelected(i)) {
        continue;
      }

      indices.add(i);
    }

    mModel.swapDown(indices);

    mTable.getRowModel().unselectAll();

    for (int i : indices) {
      if (i == mTable.getRowCount() - 1) {
        continue;
      }

      mTable.getRowModel().setSelected(i + 1);
    }
  }

  /**
   * Sort by external id list.
   *
   * @throws IOException            Signals that an I/O exception has occurred.
   * @throws InvalidFormatException the invalid format exception
   */
  private void sortByExternalIdList() throws IOException, InvalidFormatException {

    Path file = ExcelUI.openExcelFileDialog(mParent, RecentFilesService.getInstance().getPwd());

    if (file != null) {
      sortByExternalIdList(file);
    }
  }

  /**
   * Sort by external id list.
   *
   * @param file the file
   * @throws IOException            Signals that an I/O exception has occurred.
   * @throws InvalidFormatException the invalid format exception
   */
  private void sortByExternalIdList(Path file) throws IOException, InvalidFormatException {
    // BufferedReader reader = FileUtils.newBufferedReader(file);
    // String line;

    String[] ids = Excel.getTextFromFile(file, true);

    /*
     * try { // Skip header reader.readLine();
     * 
     * while ((line = reader.readLine()) != null) { if (Io.isEmptyLine(line)) {
     * continue; }
     * 
     * List<String> tokens = Splitter.onTab().text(line);
     * 
     * ids.add(tokens.get(0)); } } finally { reader.close(); }
     */

    // Now find those items in the list of indices

    List<Indexed<Integer, String>> sorted = new ArrayList<Indexed<Integer, String>>();

    Map<Integer, Boolean> visibleMap = new HashMap<Integer, Boolean>();

    Set<Integer> used = new HashSet<Integer>();

    // List<IndexedValue<Integer, String>> indexedRows =
    // mModel.getIndexedRows();

    // Map<String, Collection<IndexedValue<Integer, String>>> indexedRowMap =
    // IndexedValue.listToMap(indexedRows);

    int r = 0;

    for (String id : ids) {
      // if (indexedRowMap.containsKey(id)) {
      // for (IndexedValue<Integer, String> index : indexedRowMap.get(id)) {
      //
      // }
      // }

      if (id.equals(TextUtils.EMPTY_STRING)) {
        continue;
      }

      for (int i = 0; i < mModel.getRowCount(); ++i) {
        Indexed<Integer, String> v = mModel.get(i);

        if (v.getValue().contains(id)) {
          visibleMap.put(r++, true);

          sorted.add(v);

          used.add(i);

          break;
        }
      }
    }

    // Add all the ids that are not sorted by this method and uncheck
    // them in the dialog as we assume the user doesn't care about then

    for (int i = 0; i < mModel.getRowCount(); ++i) {
      if (!used.contains(i)) {
        visibleMap.put(r++, false);
        sorted.add(mModel.get(i));
      }
    }

    loadIds(sorted, visibleMap);
  }

  /**
   * Change ids.
   */
  private void changeIds() {
    loadIds(IndexedInt.index(mM.getColumnNames()));
  }

  /**
   * Gets the columns.
   *
   * @return the columns
   */
  public List<Integer> getColumns() {

    List<Integer> ids = new ArrayList<Integer>();

    for (int i = 0; i < mModel.getRowCount(); ++i) {
      if (mModel.getVisible(i)) {
        ids.add(mModel.get(i).getIndex());
      }
    }

    return ids;
  }
}
