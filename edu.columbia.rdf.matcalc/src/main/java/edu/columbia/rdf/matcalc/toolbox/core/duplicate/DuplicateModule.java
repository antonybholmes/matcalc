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
import java.util.List;

import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.text.Splitter;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.tooltip.ModernToolTip;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The Class DuplicateModule.
 */
public class DuplicateModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mDuplicateButton = new RibbonLargeButton(
      AssetService.getInstance().loadIcon("duplicate", 24));

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
    return "Match";
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

    mDuplicateButton.setToolTip(new ModernToolTip("Duplicate", "Duplicate rows."));

    window.getRibbon().getToolbar("Data").getSection("Tools").add(mDuplicateButton);

    mDuplicateButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mDuplicateButton)) {
      duplicate();
    }
  }

  /**
   * Match.
   */
  private void duplicate() {
    int c = mWindow.getSelectedColumn();

    if (c == Integer.MIN_VALUE) {
      ModernMessageDialog.createDialog(mWindow, "You must select a column of to match on.", MessageDialogType.WARNING);

      return;
    }

    DataFrame m = mWindow.getCurrentMatrix();

    DuplicateDialog dialog = new DuplicateDialog(mWindow, m);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    String delimiter = dialog.getDelimiter();

    List<Integer> rows = new ArrayList<Integer>(m.getRows());
    List<String> ids = new ArrayList<String>(m.getRows());

    Splitter splitter = Splitter.on(delimiter);

    for (int i = 0; i < m.getRows(); ++i) {
      String text = m.getText(i, c);

      List<String> tokens = new UniqueArrayList<String>(splitter.text(text));

      for (String id : tokens) {
        rows.add(i);
        ids.add(id.trim());
      }
    }

    DataFrame ret = DataFrame.createDataFrame(rows.size(), m.getCols());

    ret.setColumnNames(m.getColumnNames());

    // first copy the annotations

    for (String name : m.getIndex().getNames()) {
      for (int i = 0; i < rows.size(); ++i) {
        ret.getIndex().setAnnotation(name, i, m.getIndex().getAnnotation(name, rows.get(i)));
      }
    }

    for (int i = 0; i < rows.size(); ++i) {
      for (int j = 0; j < m.getCols(); ++j) {
        if (j == c) {
          // If we are on the column we selected, use the expanded
          // value rather than the original
          ret.set(i, j, ids.get(i));
        } else {
          ret.set(i, j, m.get(rows.get(i), j));
        }
      }
    }

    mWindow.history().addToHistory("Duplicate rows", ret);
  }
}
