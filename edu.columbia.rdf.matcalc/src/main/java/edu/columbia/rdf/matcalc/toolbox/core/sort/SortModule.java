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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.TreeSetCreator;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
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
public class SortModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mSortButton = new RibbonLargeButton(AssetService.getInstance().loadIcon("sort", 24));

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
    return "Sort";
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

    window.getRibbon().getToolbar("Data").getSection("Sort", true).add(mSortButton);

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

    List<Integer> columns = mWindow.getSelectedColumns();

    int c = -1;

    if (columns.size() > 0) {
      c = columns.get(0);
    }

    SortDialog dialog = new SortDialog(mWindow, m, c);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<List<Integer>> sortedIds = new ArrayList<List<Integer>>();

    // Seed the sorter with an ordered list of all the rows
    sortedIds.add(Mathematics.sequence(0, m.getRows() - 1));

    // List<ColumnSort> sorters = new ArrayList<ColumnSort>();

    // sorters.add(new ColumnSort(m, 1, false));
    // sorters.add(new ColumnSort(m, 2));

    List<ColumnSort> sorters = dialog.getSorters();

    for (ColumnSort sorter : sorters) {
      c = sorter.getColumn();

      List<List<Integer>> newSortedIds = new ArrayList<List<Integer>>();

      for (List<Integer> ids : sortedIds) {
        // Sort the lists by key

        Map<String, Set<Integer>> sortMap = DefaultTreeMap.create(new TreeSetCreator<Integer>());

        for (int id : ids) {
          sortMap.get(m.getText(id, c)).add(id);
        }

        List<String> sortedKeys = CollectionUtils.sort(sortMap.keySet(), NaturalSorter.INSTANCE);

        if (!sorter.getSortAsc()) {
          sortedKeys = CollectionUtils.reverse(sortedKeys);
        }

        // Create new list of sorted lists

        for (String key : sortedKeys) {
          newSortedIds.add(CollectionUtils.sort(sortMap.get(key)));
        }
      }

      // Swap the new ids for the old so that now we sort sub lists
      // on each loop
      sortedIds = newSortedIds;
    }

    // Create a master list of rows in sorted order
    List<Integer> rows = new ArrayList<Integer>(m.getRows());

    for (List<Integer> ids : sortedIds) {
      for (int id : ids) {
        rows.add(id);
      }
    }

    DataFrame ret = DataFrame.createDataFrame(m.getRows(), m.getCols());

    DataFrame.copyColumnHeaders(m, ret);

    DataFrame.copyRows(m, rows, ret);

    mWindow.history().addToHistory("Sort matrix", ret);
  }
}
