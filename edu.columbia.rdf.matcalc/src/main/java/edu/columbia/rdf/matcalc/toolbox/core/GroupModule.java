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
package edu.columbia.rdf.matcalc.toolbox.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.TreeSetCreator;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Group items by an index column and return the counts for each unique index.
 * For example if there are two columns: gender and name, if gender is selected
 * as the index, it will create two groups, male and female and track now many
 * names occur in each group.
 *
 * @author Antony Holmes
 *
 */
public class GroupModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mMatchButton = new RibbonLargeButton("Group",
      AssetService.getInstance().loadIcon("group", 24));

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
    return "Group";
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

    mMatchButton.setToolTip("Group", "Group data.");

    window.getRibbon().getToolbar("Data").getSection("Tools").add(mMatchButton);

    mMatchButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getMessage().equals("Group")) {
      match();
    }
  }

  /**
   * Match.
   */
  private void match() {
    DataFrame m = mWindow.getCurrentMatrix();

    if (m == null) {
      return;
    }

    List<Integer> columns = mWindow.getSelectedColumns();

    if (columns.size() < 2) {
      ModernMessageDialog.createWarningDialog(mWindow, "You must select an index column and a values column.");

      return;
    }

    Map<String, Set<String>> idMap = DefaultTreeMap.create(new TreeSetCreator<String>()); // new TreeMap<String,
                                                                                          // Set<String>>();

    for (int i = 0; i < m.getRows(); ++i) {
      String id = m.getText(i, columns.get(0));
      String item = m.getText(i, columns.get(1));

      idMap.get(id).add(item);
    }

    DataFrame ret = DataFrame.createDataFrame(idMap.size(), 3);

    ret.setName("Group");

    ret.setColumnName(0, m.getColumnName(columns.get(0)));
    ret.setColumnName(1, "Count");
    ret.setColumnName(2, m.getColumnName(columns.get(1)));

    List<String> ids = CollectionUtils.toList(idMap.keySet());

    for (int i = 0; i < ids.size(); ++i) {
      String id = ids.get(i);

      ret.set(i, 0, id);
      ret.set(i, 1, idMap.get(id).size());
      ret.set(i, 2, TextUtils.scJoin(CollectionUtils.toList(idMap.get(id))));
    }

    mWindow.openMatrices().open(ret);
  }
}
