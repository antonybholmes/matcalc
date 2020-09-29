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

import java.util.List;

import org.jebtk.core.Indexed;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.modern.table.ModernTableModel;

/**
 * Table model for ordering rows/columns.
 * 
 * @author Antony Holmes
 *
 */
public class RowOrderTableModel extends ModernTableModel {

  /**
   * The constant HEADER.
   */
  private static final String[] HEADER = { "", "", "Heading" };

  /**
   * The member ids.
   */
  private List<Indexed<Integer, String>> mIds;

  /**
   * The member visible.
   */
  private List<Boolean> mVisible;

  /**
   * Instantiates a new row order table model.
   *
   * @param ids the ids
   */
  public RowOrderTableModel(List<Indexed<Integer, String>> ids) {

    mIds = ids;
    mVisible = CollectionUtils.replicate(true, ids.size());

    fireDataChanged();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.dataview.ModernDataModel#getColumnCount()
   */
  public final int getColCount() {
    return HEADER.length;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.dataview.ModernDataModel#getRowCount()
   */
  public final int getRowCount() {
    // System.out.println("row count" + rows.size());

    return mIds.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.dataview.ModernDataModel#getColumn().getAnnotations(
   * int)
   */
  @Override
  public final String getColumnName(int column) {
    return HEADER[column];
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.dataview.ModernDataModel#getValueAt(int, int)
   */
  @Override
  public final Object getValueAt(int row, int col) {
    switch (col) {
    case 0:
      return mVisible.get(row);
    case 1:
      return mIds.get(row).getIndex() + 1;
    case 2:
      return mIds.get(row).getValue();
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.dataview.ModernDataModel#setValueAt(int, int,
   * java.lang.Object)
   */
  @Override
  public final void setValueAt(int row, int col, Object o) {
    if (col == 0) {
      mVisible.set(row, (Boolean) o);
    }
  }

  /**
   * Swap up.
   *
   * @param indices the indices
   */
  public void swapUp(List<Integer> indices) {

    List<Integer> sorted = CollectionUtils.sort(indices);

    for (int i : sorted) {
      if (i == 0) {
        continue;
      }

      Indexed<Integer, String> t = mIds.get(i - 1);

      mIds.set(i - 1, mIds.get(i));
      mIds.set(i, t);

      boolean b = mVisible.get(i - 1);

      mVisible.set(i - 1, mVisible.get(i));
      mVisible.set(i, b);
    }

    fireDataChanged();
  }

  /**
   * Swap down.
   *
   * @param indices the indices
   */
  public void swapDown(List<Integer> indices) {
    // System.err.println("swap down " + indices.toString());

    List<Integer> sorted = CollectionUtils.reverseSort(indices);

    for (int i : sorted) {
      if (i == mIds.size() - 1) {
        continue;
      }

      Indexed<Integer, String> t = mIds.get(i + 1);

      mIds.set(i + 1, mIds.get(i));
      mIds.set(i, t);

      boolean b = mVisible.get(i + 1);

      mVisible.set(i + 1, mVisible.get(i));
      mVisible.set(i, b);
    }

    fireDataChanged();
  }

  /**
   * Gets the.
   *
   * @param index the index
   * @return the indexed value
   */
  public final Indexed<Integer, String> get(int index) {
    return mIds.get(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.dataview.ModernDataGridModel#getIsCellEditable(int,
   * int)
   */
  @Override
  public boolean getIsCellEditable(int row, int column) {
    return column == 0;
  }

  /**
   * Gets the visible.
   *
   * @param i the i
   * @return the visible
   */
  public boolean getVisible(int i) {
    return mVisible.get(i);
  }
}
