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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.Affymetrix;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.input.ModernTextInputDialog;
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
public class DiffModule extends Module implements ModernClickListener {
  /**
   * The constant NO_MATCH.
   */
  private static final String NO_MATCH = "no_match";

  /**
   * The member diff button.
   */
  private RibbonLargeButton mDiffButton = new RibbonLargeButton("Diff",
      AssetService.getInstance().loadIcon("diff", 24));

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
    return "Diff";
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

    mDiffButton.setToolTip(new ModernToolTip("Diff", "Find differences in a column."));
    mDiffButton.setClickMessage("Diff");

    window.getRibbon().getToolbar("Data").getSection("Match").add(mDiffButton);

    mDiffButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getMessage().equals("Diff")) {
      diff();
    }
  }

  /**
   * Diff.
   */
  private void diff() {
    List<Integer> columns = mWindow.getSelectedColumns();

    if (columns == null || columns.size() == 0) {
      mWindow.createDialog("You must select a column of ids to match on.", MessageDialogType.WARNING);

      return;
    }

    ModernTextInputDialog inputDialog = new ModernTextInputDialog(mWindow, "Diff");

    inputDialog.setVisible(true);

    if (inputDialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    DataFrame m = mWindow.getCurrentMatrix();

    List<String> ids = getIds(inputDialog.getText());

    List<String> lids = TextUtils.toLowerCase(ids);

    Map<String, String> idMap = CollectionUtils.createMap(lids, ids);

    int c = columns.get(0);
    // first clone the matrix

    DataFrame ret = DataFrame.createDataFrame(m.getRows(), m.getCols() + 1);

    // Copy before column
    DataFrame.copyColumns(m, 0, c, ret);

    // Shift the rest by one column so we can insert the results
    DataFrame.copyColumns(m, c + 1, ret, c + 2);

    ret.setColumnName(c + 1, "Diff Match");

    for (int i = 0; i < m.getRows(); ++i) {
      String id = m.getText(i, c).toLowerCase();

      if (idMap.containsKey(id)) {
        ret.set(i, c + 1, idMap.get(id));
      } else {
        ret.set(i, c + 1, NO_MATCH);
      }
    }

    mWindow.history().addToHistory("Diff", ret);
  }

  /**
   * Gets the ids.
   *
   * @param text the text
   * @return the ids
   */
  private static List<String> getIds(String text) {
    List<String> lines = TextUtils.fastSplit(text.trim(), TextUtils.NEW_LINE_DELIMITER);

    List<String> ret = new ArrayList<String>();

    for (String line : lines) {
      line = line.replaceAll(TextUtils.COMMA_DELIMITER, TextUtils.SEMI_COLON_DELIMITER);
      line = line.replaceAll(Affymetrix.GENE_DELIMITER, TextUtils.SEMI_COLON_DELIMITER);

      if (line.length() > 0) {
        for (String id : TextUtils.scSplit(line)) {
          ret.add(id);
        }
      }
    }

    return ret;
  }

}
