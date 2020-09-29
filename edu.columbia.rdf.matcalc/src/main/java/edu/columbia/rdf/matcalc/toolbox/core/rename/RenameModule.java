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
package edu.columbia.rdf.matcalc.toolbox.core.rename;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.collections.ArrayListCreator;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.RunVectorIcon;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Row name.
 *
 * @author Antony Holmes
 */
public class RenameModule extends Module implements ModernClickListener {

  /**
   * The button.
   */
  private RibbonLargeButton button = new RibbonLargeButton(AssetService.getInstance().loadIcon(RunVectorIcon.class, 24),
      "Rename", "Rename and sort within a column.");

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
    return "Rename";
  }

  @Override
  public void run(ArgParser ap) {
    rename();
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
    rename();
  }

  /**
   * Filter.
   */
  private void rename() {
    DataFrame m = mWindow.getCurrentMatrix();

    List<Integer> columns = mWindow.getSelectedColumns();

    if (columns == null || columns.size() == 0) {
      ModernMessageDialog.createDialog(mWindow, "Please select a column.", MessageDialogType.WARNING);
      return;
    }

    int column = columns.get(0);

    RenameDialog dialog = new RenameDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<String> names = new ArrayList<String>();
    Map<String, String> nameToSearchTermMap = new HashMap<String, String>();
    Map<String, String> searchTermToNameMap = new HashMap<String, String>();
    Map<String, String> searchTermToReplacementMap = new HashMap<String, String>();

    dialog.getNames(names, nameToSearchTermMap, searchTermToNameMap, searchTermToReplacementMap);

    String[] ids = new String[m.getRows()];

    // for (int column : columns) {
    m.columnToText(column, ids);

    String id;
    String id2 = null;

    if (dialog.getExactMatch()) {
      if (dialog.getSort()) {
        Map<String, List<Integer>> sortMap = DefaultHashMap.create(new ArrayListCreator<Integer>());

        Map<Integer, String> replacementMap = new HashMap<Integer, String>();

        for (int i = 0; i < ids.length; ++i) {
          id = ids[i];

          if (!dialog.getCaseSensitive()) {
            id = id.toLowerCase();
          }

          String name = searchTermToNameMap.get(id);
          String replace = searchTermToReplacementMap.get(id);

          if (replace == null) {
            // Also test if id is number such as int that is being
            // stored as 1.0 when user wants to match on 1

            if (TextUtils.isDouble(id)) {
              id2 = Integer.toString((int) Double.parseDouble(id));

              name = searchTermToNameMap.get(id2);
              replace = searchTermToReplacementMap.get(id2);
            }
          }

          if (replace != null) {
            ids[i] = replace;
          } else {
            name = TextUtils.NA;
            replace = id;

          }

          sortMap.get(name).add(i);
          replacementMap.put(i, replace);
        }

        int[] rowOrder = new int[ids.length];

        int i = 0;

        for (String name : names) {
          for (int r : sortMap.get(name)) {
            rowOrder[i++] = r;
          }
        }

        // Add the unsorted
        for (int r : sortMap.get(TextUtils.NA)) {
          rowOrder[i++] = r;
        }

        // First replace the ids

        DataFrame m2 = new DataFrame(m);
        m2.setColumn(column, ids);
        mWindow.history().addToHistory("Renamed", m2);

        // Now reorder

        DataFrame m3 = m2.copyRows(rowOrder);
        mWindow.history().addToHistory("Sorted", m3);
      }
    }
  }
}