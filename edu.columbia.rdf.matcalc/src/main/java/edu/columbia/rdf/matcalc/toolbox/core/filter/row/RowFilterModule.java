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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jebtk.core.Range;
import org.jebtk.core.collections.IterHashMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.FilterVectorIcon;
import org.jebtk.modern.graphics.icons.Raster24Icon;
import org.jebtk.modern.graphics.icons.RotateVectorIcon;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.theme.ThemeService;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Row name.
 *
 * @author Antony Holmes
 */
public class RowFilterModule extends Module implements ModernClickListener {

  /**
   * The button.
   */
  private RibbonLargeButton button = new RibbonLargeButton(
      new Raster24Icon(new RotateVectorIcon(new FilterVectorIcon(ThemeService.getInstance().getColors().getGray(8),
          ThemeService.getInstance().getColors().getGray(6)), -90)),
      "Row Filter", "Filter rows matching a list of values.");

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow = null;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Row Filter";
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

    button.addClickListener(this);
    mWindow.getRibbon().getToolbar("Data").getSection("Filter").add(button);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    filter();
  }

  /**
   * Filter.
   */
  public void filter() {
    DataFrame m = mWindow.getCurrentMatrix();

    MatrixRowFilterDialog dialog = new MatrixRowFilterDialog(mWindow, m);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<Integer> rows = new ArrayList<Integer>(m.getRows());

    List<String> names = dialog.getNames();
    List<String> missingNames = new ArrayList<String>(names.size());

    int column = dialog.getColumn();

    getRows(m, dialog.getColumnText(), column, dialog.getExactMatch(), dialog.getInList(), dialog.getIncludeMissing(),
        dialog.getCaseSensitive(), names, rows, missingNames);

    if (rows.size() == 0) {
      ModernMessageDialog.createWarningDialog(mWindow, "There were no rows matching your filter criteria.");
      return;
    }

    DataFrame ret = null;

    if (missingNames.size() > 0) {
      // Need to insert blank rows

      ret = DataFrame.createDataFrame(rows.size() + missingNames.size(), m.getCols());

      DataFrame.copyColumnNames(m, ret);
      DataFrame.copyRows(m, rows, ret);

      // Insert some blank rows
      Matrix.setRowNA(rows.size(), ret.getRows() - 1, ret);

      for (int i = 0; i < missingNames.size(); ++i) {
        ret.set(i + rows.size(), column, missingNames.get(i));
      }

    } else {
      ret = DataFrame.copyRows(m, rows);
    }

    mWindow.history().addToHistory("Row Filter", ret);
  }

  /**
   * Gets the rows.
   *
   * @param m              The matrix to search.
   * @param columnText     The
   * @param column         The column to match on.
   * @param exactMatch     Whether to match names exactly (case insensitive).
   * @param inList         Matches rows to values in names if true, otherwise
   *                       returns rows not matching names.
   * @param includeMissing Update missingNames with the list of names not found in
   *                       the matrix. This option is ignored if inList == false.
   * @param caseSensitive  the case sensitive
   * @param names          The list of names to search for in the matrix.
   * @param rows           Will be populated with the rows matching the values in
   *                       names.
   * @param missingNames   Will be populated with those names that are not found
   *                       in the matrix. This list is only updated if
   *                       includeMissing == true and inList == true.
   * @return the rows
   */
  private static void getRows(final DataFrame m, final String columnText, int column, boolean exactMatch,
      boolean inList, boolean includeMissing, boolean caseSensitive, final List<String> names, List<Integer> rows,
      List<String> missingNames) {
    String[] ids;

    int numRowAnnotations = m.getIndex().getNames().size();

    if (column < numRowAnnotations) {
      // The ids we want to search are from row annotations rather than
      // the matrix itself
      ids = m.getIndex().getText(columnText);
    } else {
      ids = m.columnToText(column - numRowAnnotations);
    }

    if (!caseSensitive) {
      ids = TextUtils.toLowerCase(ids);
    }

    int rowCount = ids.length;

    //
    // A lookup set so we can quickly see if a row matches what we are
    // looking for

    IterMap<String, String> nameMap = new IterHashMap<String, String>();

    for (String name : names) {
      if (caseSensitive) {
        nameMap.put(name, name);
      } else {
        nameMap.put(name.toLowerCase(), name);
      }
    }

    // Keep track of which of the list of names we have found in the matrix
    Set<String> foundNameSet = new HashSet<String>();

    //
    // Matching
    //

    if (exactMatch) {
      if (inList) {
        for (int i = 0; i < rowCount; ++i) {
          String rowName = ids[i];

          if (rowName != null && nameMap.containsKey(rowName)) {
            rows.add(i);
            foundNameSet.add(nameMap.get(rowName));
          }
        }
      } else {
        for (int i = 0; i < rowCount; ++i) {
          String rowName = ids[i];

          if (rowName != null && !nameMap.containsKey(rowName)) {
            rows.add(i);
          }
        }
      }
    } else {
      // Partial matching

      if (inList) {
        for (int i = 0; i < rowCount; ++i) {
          String rowName = ids[i];

          if (rowName == null) {
            continue;
          }

          for (Entry<String, String> f : nameMap) {
            if (rowName.contains(f.getKey())) {
              rows.add(i);
              foundNameSet.add(nameMap.get(rowName));

              break;
            }
          }
        }
      } else {
        // for (int i = 0; i < rowCount; ++i) {
        for (int i : Range.create(rowCount)) {
          String rowName = ids[i];

          if (rowName == null) {
            continue;
          }

          for (Entry<String, String> f : nameMap) {
            if (!rowName.contains(f.getKey())) {
              rows.add(i);
            }
          }
        }
      }
    }

    if (includeMissing) {
      // Add those we did not find to the list of missing names.

      for (String name : names) {
        if (!foundNameSet.contains(name)) {
          missingNames.add(name);
        }
      }
    }
  }
}
