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

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.event.ModernClickListeners;
import org.jebtk.modern.graphics.icons.RedCrossIcon;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;

import edu.columbia.rdf.matcalc.toolbox.ColumnsCombo;

/**
 * The Class ColumnSort.
 */
public class ColumnSort extends HBox implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m columns combo. */
  private ColumnsCombo mColumnsCombo;

  /** The m sort combo. */
  private SortOrderCombo mSortCombo = new SortOrderCombo();

  /** The m delete button. */
  private ModernButton mDeleteButton = new ModernButton(AssetService.getInstance().loadIcon(RedCrossIcon.class, 16));

  /** The m listeners. */
  private ModernClickListeners mListeners = new ModernClickListeners();

  /**
   * Instantiates a new column sort.
   *
   * @param m     the m
   * @param label the label
   */
  public ColumnSort(DataFrame m, String label) {
    add(new ModernAutoSizeLabel(label, 60));

    mColumnsCombo = new ColumnsCombo(m);
    UI.setSize(mColumnsCombo, 200, ModernWidget.WIDGET_HEIGHT);
    add(mColumnsCombo);

    // add(Ui.createHGap(30));

    // add(new ModernLabel("Order"));
    add(UI.createHGap(5));
    UI.setSize(mSortCombo, 150, ModernWidget.WIDGET_HEIGHT);
    add(mSortCombo);

    add(UI.createHGap(5));

    add(mDeleteButton);

    setBorder(ModernWidget.BOTTOM_BORDER);

    mDeleteButton.addClickListener(this);
  }

  /**
   * Instantiates a new column sort.
   *
   * @param m the m
   * @param c the c
   */
  public ColumnSort(DataFrame m, int c) {
    this(m, c, true);
  }

  /**
   * Instantiates a new column sort.
   *
   * @param m   the m
   * @param c   the c
   * @param asc the asc
   */
  public ColumnSort(DataFrame m, int c, boolean asc) {
    this(m, "Sort by", c, asc);
  }

  /**
   * Instantiates a new column sort.
   *
   * @param m     the m
   * @param label the label
   * @param c     the c
   */
  public ColumnSort(DataFrame m, String label, int c) {
    this(m, label, c, true);
  }

  /**
   * Instantiates a new column sort.
   *
   * @param m     the m
   * @param label the label
   * @param c     the c
   * @param asc   the asc
   */
  public ColumnSort(DataFrame m, String label, int c, boolean asc) {
    this(m, label);

    if (c != -1) {
      mColumnsCombo.setSelectedIndex(c);
    }

    mSortCombo.setSelectedIndex(asc ? 0 : 1);
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mListeners.addClickListener(l);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    mListeners.fireClicked(new ModernClickEvent(this, "delete"));
  }

  /**
   * Gets the column.
   *
   * @return the column
   */
  public int getColumn() {
    return mColumnsCombo.getSelectedIndex();
  }

  /**
   * Gets the sort asc.
   *
   * @return the sort asc
   */
  public boolean getSortAsc() {
    return mSortCombo.getSelectedIndex() == 0;
  }

  /**
   * Disable delete.
   */
  public void disableDelete() {
    mDeleteButton.setVisible(false);
  }
}
