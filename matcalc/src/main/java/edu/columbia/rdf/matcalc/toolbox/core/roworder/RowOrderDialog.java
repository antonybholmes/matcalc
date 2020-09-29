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
package edu.columbia.rdf.matcalc.toolbox.core.roworder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;

import org.jebtk.core.Indexed;
import org.jebtk.core.IndexedInt;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.TokenFunction;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.ArrowDownVectorIcon;
import org.jebtk.modern.graphics.icons.ArrowUpVectorIcon;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.table.ModernRowTable;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.toolbox.ColumnsCombo;

/**
 * Allow ordering of columns or rows in a table.
 * 
 * @author Antony Holmes
 */
public class RowOrderDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member model.
   */
  private RowOrderTableModel mModel = null;

  /**
   * The member table.
   */
  private ModernRowTable mTable = new RowOrderTable();

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

  /**
   * The member load button.
   */
  private ModernButton mLoadButton = new ModernButton("Load order",
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

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
   * Instantiates a new row order dialog.
   *
   * @param parent the parent
   * @param m      the m
   */
  public RowOrderDialog(ModernWindow parent, DataFrame m) {
    super(parent, "matcalc.modules.row-order.help.url");

    mM = m;

    setTitle("Row Order");

    mColumnsCombo = new ColumnsCombo(m);

    mColumnsCombo.setSelectedIndex(0);

    mColumnsCombo.addClickListener(new TypeChangeEvents());

    ModernComponent content = new ModernComponent();

    Box box = HBox.create();
    box.add(new ModernLabel("Column", 80));
    UI.setSize(mColumnsCombo, 300, 24);
    box.add(mColumnsCombo);
    box.setBorder(ModernPanel.LARGE_BORDER);

    content.setHeader(box);

    mTable.setShowHeader(false);

    // ModernScrollPane scrollPane = new ModernScrollPane(mTable);
    // scrollPane.setBorder(ModernTheme.getInstance().getClass("widget").getBorder("dialog"));
    // scrollPane.setBorder(ModernWidget.LINE_BORDER);
    // scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
    // scrollPane.getViewport().setBackground(Color.WHITE);

    content.setBody(new ModernScrollPane(mTable).setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS));

    box = VBox.create();

    mUpButton.addClickListener(this);
    box.add(mUpButton);

    box.add(ModernPanel.createVGap());

    mDownButton.addClickListener(this);
    box.add(mDownButton);

    box.setBorder(BorderService.getInstance().createLeftBorder(10));

    content.setRight(box);

    box = HBox.create();

    box.setBorder(ModernPanel.TOP_BORDER);

    box.add(mLoadButton);
    box.add(ModernPanel.createHGap());
    box.add(mAlphabeticalButton);

    content.setFooter(box);

    setCard(content);

    changeIds();

    setSize(640, 480);
    setResizable(true);
    UI.centerWindowToScreen(this);

    mLoadButton.addClickListener(this);
    mAlphabeticalButton.addClickListener(this);
  }

  /**
   * Load ids.
   *
   * @param ids the ids
   */
  private void loadIds(List<Indexed<Integer, String>> ids) {
    mModel = new RowOrderTableModel(ids);

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
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mUpButton)) {
      swapUp();
    } else if (e.getSource().equals(mDownButton)) {
      swapDown();
    } else if (e.getSource().equals(mLoadButton)) {
      try {
        sortByExternalIdList();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(mAlphabeticalButton)) {
      sortAlphabetically();
    } else {
      super.clicked(e);
    }
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
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void sortByExternalIdList() throws IOException {

    Path file = FileDialog.open(mParent).all().getFile(RecentFilesService.getInstance().getPwd());

    if (file == null) {
      return;
    }

    sortByExternalIdList(file);
  }

  /**
   * Sort by external id list.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void sortByExternalIdList(Path file) throws IOException {
    final List<String> ids = new ArrayList<String>();

    FileUtils.tokenize(file, new TokenFunction() {
      @Override
      public void parse(List<String> tokens) {
        ids.add(tokens.get(0).toLowerCase());
      }
    });

    // Now find those items in the list of indices

    List<Indexed<Integer, String>> sorted = new ArrayList<Indexed<Integer, String>>();

    Set<Integer> used = new HashSet<Integer>();

    for (String id : ids) {
      for (int i = 0; i < mModel.getRowCount(); ++i) {
        Indexed<Integer, String> v = mModel.get(i);

        if (v.getValue().toLowerCase().equals(id)) {
          sorted.add(v);
          used.add(i);
          break;
        }
      }
    }

    // Add all the ids that are not sorted by this method

    for (int i = 0; i < mModel.getRowCount(); ++i) {
      if (!used.contains(i)) {
        sorted.add(mModel.get(i));
      }
    }

    loadIds(sorted);
  }

  /**
   * Change ids.
   */
  private void changeIds() {
    int numRowAnnotations = mM.getIndex().getNames().size();

    if (mColumnsCombo.getSelectedIndex() < numRowAnnotations) {
      // filter on row annotation

      loadIds(IndexedInt.index(mM.getIndex().getText(mColumnsCombo.getText())));
    } else {
      // filter on column

      loadIds(IndexedInt.index(mM.columnToText(mColumnsCombo.getSelectedIndex() - numRowAnnotations)));
    }

  }

  /**
   * Gets the rows.
   *
   * @return the rows
   */
  public List<Integer> getRows() {

    List<Integer> ids = new ArrayList<Integer>();

    for (int i = 0; i < mModel.getRowCount(); ++i) {
      if (mModel.getVisible(i)) {
        ids.add(mModel.get(i).getIndex());
      }
    }

    return ids;
  }
}
