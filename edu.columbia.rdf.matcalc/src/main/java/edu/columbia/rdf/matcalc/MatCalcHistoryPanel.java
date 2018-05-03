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
package edu.columbia.rdf.matcalc;

import org.jebtk.math.ui.matrix.transform.MatrixTransform;
import org.jebtk.modern.history.ModernHistoryPanel;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class MatCalcHistoryPanel.
 */
public class MatCalcHistoryPanel extends ModernHistoryPanel<MatrixTransform> {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new mat calc history panel.
   *
   * @param parent the parent
   */
  public MatCalcHistoryPanel(ModernWindow parent) {
    super(parent);

    // setBorder(LARGE_BORDER);
  }

  /**
   * Search.
   *
   * @param text the text
   * @return the int
   */
  public int search(String text) {
    String ls = text.toLowerCase();

    for (int i = 0; i < mHistoryModel.getItemCount(); ++i) {
      if (mHistoryModel.getValueAt(i).getName().toLowerCase().contains(ls)) {
        return i;
      }
    }

    return -1;
  }

  // @Override
  /**
   * public MatrixTransform addItem(MatrixTransform item) { if (item == null) {
   * return null; }
   * 
   * System.err.println(Math.max(1, mHistoryList.getSelectedIndex()) + " " +
   * (mHistoryModel.getItemCount() - 1));
   * 
   * for (int i = mHistoryModel.getItemCount() - 1; i > Math.max(1,
   * mHistoryList.getSelectedIndex()); --i) { mHistoryModel.removeValueAt(i); }
   * 
   * mHistoryModel.addValue(item);
   * 
   * mHistoryList.setSelectedIndex(mHistoryList.getItemCount() - 1);
   * 
   * return item; }
   **/
}
