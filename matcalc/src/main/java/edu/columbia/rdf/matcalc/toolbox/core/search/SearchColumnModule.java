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
package edu.columbia.rdf.matcalc.toolbox.core.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.tooltip.ModernToolTip;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Annotate a table as to whether some values can be found in a particular
 * column.
 *
 * @author Antony Holmes
 *
 */
public class SearchColumnModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mSearchButton = new RibbonLargeButton(
      AssetService.getInstance().loadIcon("search_column", 24));

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
    return "Search Column";
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

    mSearchButton.setToolTip(new ModernToolTip("Search Column", "Search column for values."));

    window.getRibbon().getHomeToolbar().getSection("Search").add(mSearchButton);

    mSearchButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    search();
  }

  /**
   * Match.
   */
  private void search() {
    int column = mWindow.getSelectedColumn();

    if (column == Integer.MIN_VALUE) {
      ModernMessageDialog.createWarningDialog(mWindow, "You must select a column to match on.");

      return;
    }

    DataFrame m = mWindow.getCurrentMatrix();

    SearchColumnDialog dialog = new SearchColumnDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<String> items = dialog.getLines();

    // for case insensitive searching
    Map<String, String> lcMap;

    if (dialog.caseSensitive()) {
      lcMap = CollectionUtils.createMap(items, items);
    } else {
      lcMap = CollectionUtils.createMap(items, TextUtils.toLowerCase(items));
    }

    List<Set<String>> matchMap = new ArrayList<Set<String>>(m.getRows());

    for (int i = 0; i < m.getRows(); ++i) {
      matchMap.add(new TreeSet<String>());

      String lc = m.getText(i, column);

      if (!dialog.caseSensitive()) {
        lc = lc.toLowerCase();
      }

      for (String item : items) {
        boolean match = false;

        if (dialog.getInList()) {
          if (dialog.getExact()) {
            match = lc.equals(lcMap.get(item));
          } else {
            match = lc.contains(lcMap.get(item));
          }
        } else {
          if (dialog.getExact()) {
            match = !lc.equals(lcMap.get(item));
          } else {
            match = !lc.contains(lcMap.get(item));
          }
        }

        if (match) {
          matchMap.get(i).add(item);
        }
      }
    }

    DataFrame ret = DataFrame.createDataFrame(m.getRows(), m.getCols() + 2);

    // Copy before column
    DataFrame.copyColumns(m, 0, column - 1, ret);

    // Shift the rest by one column so we can insert the results
    DataFrame.copyColumns(m, column, ret, column + 2);

    // ret.setColumnName(c + 1, "Match In " + window.getSubTitle() + " - " +
    // copyM.getColumnName(inputDialog.getReplaceColumn()));
    ret.setColumnName(column, "Number Of Matches");
    ret.setColumnName(column + 1, "Matches");

    for (int i = 0; i < m.getRows(); ++i) {
      int s = matchMap.get(i).size();

      ret.set(i, column, s);

      if (s == 0) {
        ret.set(i, column + 1, TextUtils.NA);
      } else {
        ret.set(i, column + 1, TextUtils.scJoin(CollectionUtils.sort(matchMap.get(i))));
      }
    }

    mWindow.history().addToHistory("Search Column	", ret);
  }
}
