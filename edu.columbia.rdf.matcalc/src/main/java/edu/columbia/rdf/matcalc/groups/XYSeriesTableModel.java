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
import java.util.List;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.modern.table.ModernTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class XYSeriesTableModel.
 */
public class XYSeriesTableModel extends ModernTableModel {

  /** The Constant COLUMNS. */
  private static final String[] COLUMNS = { "", "Name", "Color" };

  /** The m all series. */
  private List<XYSeries> mAllSeries;

  /** The m use. */
  private List<Boolean> mUse;

  /**
   * Instantiates a new XY series table model.
   *
   * @param allSeries
   *          the all series
   */
  public XYSeriesTableModel(List<XYSeries> allSeries) {
    mAllSeries = allSeries;

    mUse = CollectionUtils.replicate(true, allSeries.size());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.dataview.ModernDataModel#getRowCount()
   */
  @Override
  public int getRowCount() {
    return mAllSeries.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.dataview.ModernDataModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
    return COLUMNS.length;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.dataview.ModernDataGridModel#getIsCellEditable(int,
   * int)
   */
  @Override
  public boolean getIsCellEditable(int row, int column) {
    return column == 0 || column == 2;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.dataview.ModernDataModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int row, int column) {
    switch (column) {
    case 0:
      return mUse.get(row);
    case 1:
      return mAllSeries.get(row).getName();
    default:
      return mAllSeries.get(row).getColor();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.dataview.ModernDataModel#setValueAt(int, int,
   * java.lang.Object)
   */
  @Override
  public void setValueAt(int row, int column, Object o) {
    switch (column) {
    case 0:
      mUse.set(row, (Boolean) o);
      break;
    case 2:
      mAllSeries.get(row).setColor((Color) o);
      break;
    }
  }

  /**
   * Gets the column annotations.
   *
   * @param column
   *          the column
   * @return the column annotations
   */
  public List<? extends Object> getColumnAnnotations(int column) {
    return CollectionUtils.asList(COLUMNS[column]);
  }

  /**
   * Checks if is selected.
   *
   * @param i
   *          the i
   * @return true, if is selected
   */
  public boolean isSelected(int i) {
    return mUse.get(i);
  }

  /**
   * Gets the series.
   *
   * @param i
   *          the i
   * @return the series
   */
  public XYSeries getSeries(int i) {
    return mAllSeries.get(i);
  }
}
