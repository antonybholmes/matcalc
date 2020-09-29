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
package edu.columbia.rdf.matcalc.toolbox.core.sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jebtk.core.Indexed;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.tooltip.ModernToolTip;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Can compare a column of values to another list to see what is common and
 * record this in a new column next to the reference column. Useful for doing
 * overlaps and keeping data in a specific order in a table.
 *
 * @author Antony Holmes
 *
 */
public class SortColumnsByRowModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mSortButton = new RibbonLargeButton(
      AssetService.getInstance().loadIcon("order_columns", 32));

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Sort Columns By Row";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    mSortButton.setToolTip(new ModernToolTip("Sort", "Sort columns."));

    window.getRibbon().getToolbar("Data").getSection("Sort").add(mSortButton);

    mSortButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    sort();
  }

  /**
   * Match.
   */
  private void sort() {
    DataFrame m = mWindow.getCurrentMatrix();

    List<Integer> rows = mWindow.getSelectedRows();

    if (rows.size() == 0) {
      ModernMessageDialog.createDialog(mWindow, "You must select a row to sort on.", MessageDialogType.WARNING);
      return;
    }

    int r = rows.get(0);

    SortColumnsByRowDialog dialog = new SortColumnsByRowDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.isCancelled()) {
      return;
    }

    if (dialog.getSortWithinGroups()) {
      sortByRowWithinGroups(m, r, mWindow.getGroups(), dialog.getSortAsc());
    } else {
      sortByRow(m, r, dialog.getSortAsc());
    }
  }

  /**
   * Sort by row.
   *
   * @param m   the m
   * @param r   the r
   * @param asc the asc
   */
  private void sortByRow(DataFrame m, int r, boolean asc) {

    // If a column belongs to more than one group, we
    // will just assign it to the first group we
    // encounter that it belongs to. The column will
    // then be marked as used so it cannot be put
    // in another group. This ensures consistent ordering.

    List<Indexed<Integer, Double>> valueIndices = new ArrayList<Indexed<Integer, Double>>();

    for (int i = 0; i < m.getCols(); ++i) {
      valueIndices.add(new Indexed<Integer, Double>(i, m.getValue(r, i)));
    }

    // Sort largest to smallest
    List<Indexed<Integer, Double>> sortedValues = CollectionUtils.sort(valueIndices);

    if (!asc) {
      sortedValues = CollectionUtils.reverse(sortedValues);
    }

    List<Integer> columns = new ArrayList<Integer>();

    for (Indexed<Integer, Double> valueIndex : sortedValues) {
      columns.add(valueIndex.getIndex());
    }

    output(m, columns);
  }

  /**
   * Sort by row within groups.
   *
   * @param m      the m
   * @param r      the r
   * @param groups the groups
   * @param asc    the asc
   */
  private void sortByRowWithinGroups(DataFrame m, int r, XYSeriesGroup groups, boolean asc) {
    // If a column belongs to more than one group, we
    // will just assign it to the first group we
    // encounter that it belongs to. The column will
    // then be marked as used so it cannot be put
    // in another group. This ensures consistent ordering.
    Set<Integer> used = new HashSet<Integer>();

    List<Integer> columns = new ArrayList<Integer>();

    for (MatrixGroup group : groups) {
      List<Integer> ids = MatrixGroup.findColumnIndices(m, group);

      List<Indexed<Integer, Double>> valueIndices = new ArrayList<Indexed<Integer, Double>>();

      for (int id : ids) {
        if (used.contains(id)) {
          continue;
        }

        valueIndices.add(new Indexed<Integer, Double>(id, m.getValue(r, id)));

        used.add(id);
      }

      // Sort largest to smallest
      List<Indexed<Integer, Double>> sortedValues = CollectionUtils.sort(valueIndices);

      if (!asc) {
        sortedValues = CollectionUtils.reverse(sortedValues);
      }

      for (Indexed<Integer, Double> valueIndex : sortedValues) {
        columns.add(valueIndex.getIndex());
      }
    }

    // deal with all the other columns that are not in groups

    for (int i = 0; i < m.getCols(); ++i) {
      if (!used.contains(i)) {
        columns.add(i);
      }
    }

    output(m, columns);
  }

  /**
   * Output.
   *
   * @param m       the m
   * @param columns the columns
   */
  private void output(DataFrame m, List<Integer> columns) {
    DataFrame ret = DataFrame.createDataFrame(m.getRows(), m.getCols());

    DataFrame.copyIndex(m, ret);

    DataFrame.copyColumns(m, columns, ret);

    mWindow.history().addToHistory("Sort matrix", ret);
  }
}
