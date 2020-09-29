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
package edu.columbia.rdf.matcalc.toolbox.core.duplicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
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
 * Collapse a file based on a repeated column. The selected column will be
 * reduced to a list of unique values whilst all other columns will be turned
 * into semi-colon separated lists of values.
 *
 * @author Antony Holmes
 *
 */
public class UniqueModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mCollapseButton = new RibbonLargeButton("Unique",
      AssetService.getInstance().loadIcon("collapse", 24));

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
    return "Unique";
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

    mCollapseButton.setToolTip(new ModernToolTip("Unique", "Collapse rows on a column."));

    window.getRibbon().getToolbar("Transform").getSection("Duplicate").add(mCollapseButton);

    mCollapseButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mCollapseButton)) {
      collapse();
    }
  }

  /**
   * Match.
   */
  private void collapse() {
    List<Integer> columns = mWindow.getSelectedColumns();

    if (columns == null || columns.size() == 0) {
      ModernMessageDialog.createDialog(mWindow, "You must select a column of to match on.", MessageDialogType.WARNING);
    }

    DataFrame m = mWindow.getCurrentMatrix();

    int c = columns.get(0);

    Map<String, List<Integer>> rows = new HashMap<String, List<Integer>>();
    List<String> ids = new UniqueArrayList<String>();

    for (int i = 0; i < m.getRows(); ++i) {
      String id = m.getText(i, c);

      ids.add(id);

      if (!rows.containsKey(id)) {
        rows.put(id, new ArrayList<Integer>());
      }

      rows.get(id).add(i);
    }

    DataFrame ret = DataFrame.createDataFrame(ids.size(), m.getCols());

    ret.setColumnNames(m.getColumnNames());

    // first copy the annotations

    for (String name : m.getIndex().getNames()) {
      for (int i = 0; i < ids.size(); ++i) {
        List<Integer> indices = rows.get(ids.get(i));

        List<String> items = new UniqueArrayList<String>(indices.size());

        for (int k : indices) {
          items.add(m.getIndex().getText(name, k));
        }

        String text = TextUtils.scJoin(items);

        ret.getIndex().setAnnotation(name, i, text);
      }
    }

    int max = 0;

    for (int row = 0; row < ids.size(); ++row) {
      List<Integer> indices = rows.get(ids.get(row));

      for (int column = 0; column < m.getCols(); ++column) {
        // StringBuilder buffer = new StringBuilder(m.getText(indices.get(0),
        // column));

        List<String> items = new UniqueArrayList<String>(indices.size());

        for (int k : indices) {
          items.add(m.getText(k, column));
        }

        String text = TextUtils.scJoin(items);

        System.err.println(row + " " + column + " " + text);

        ret.set(row, column, text);

        max = Math.max(max, text.length());
      }
    }

    // Set the collapse column values to their unique values

    for (int row = 0; row < ids.size(); ++row) {
      // System.err.println("c " + row + " " + c + " " + ids.get(row));

      ret.set(row, c, ids.get(row));
    }

    // System.err.println("size " + ret.getColumnCount() + " " +
    // ret.getRowCount());

    // mWindow.history().addToHistory("Duplicate rows", m);
    mWindow.history().addToHistory("Duplicate rows", ret);
  }
}
