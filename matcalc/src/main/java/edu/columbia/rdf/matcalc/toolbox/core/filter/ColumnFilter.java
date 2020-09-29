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
package edu.columbia.rdf.matcalc.toolbox.core.filter;

import java.text.ParseException;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.combobox.AndOrLogicalComboBox;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.event.ModernClickListeners;
import org.jebtk.modern.graphics.icons.RedCrossIcon;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;

/**
 * The Class ColumnSort.
 */
public class ColumnFilter extends HBox implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m columns combo. */
  // private ColumnsCombo mColumnsCombo;

  private AndOrLogicalComboBox mLogicalCombo = new AndOrLogicalComboBox();

  /** The m sort combo. */
  private FilterCombo mFilterCombo = new FilterCombo();

  /** The m text. */
  private ModernTextField mText = new ModernTextField();

  /** The m delete button. */
  private ModernButton mDeleteButton = new ModernButton(AssetService.getInstance().loadIcon(RedCrossIcon.class, 16));

  /** The m listeners. */
  private ModernClickListeners mListeners = new ModernClickListeners();

  /**
   * Instantiates a new column sort.
   *
   * @param m       the m
   * @param isFirst the is first
   */
  public ColumnFilter(DataFrame m, boolean isFirst) {
    if (isFirst) {
      add(UI.createHGap(80));
    } else {
      UI.setSize(mLogicalCombo, 80, ModernWidget.WIDGET_HEIGHT);
      add(mLogicalCombo);
    }

    add(UI.createHGap(5));

    // mColumnsCombo = new ColumnsCombo(m);
    UI.setSize(mFilterCombo, 200, ModernWidget.WIDGET_HEIGHT);
    add(mFilterCombo);

    add(UI.createHGap(5));
    add(new ModernTextBorderPanel(mText, 200));

    add(UI.createHGap(5));

    add(mDeleteButton);

    setBorder(ModernWidget.BOTTOM_BORDER);

    mDeleteButton.addClickListener(this);

    mLogicalCombo.setVisible(!isFirst);
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
   * Disable delete.
   */
  public void disableDelete() {
    mDeleteButton.setVisible(false);
  }

  /**
   * Gets the filter.
   *
   * @return the filter
   * @throws ParseException the parse exception
   */
  public Filter getFilter() throws ParseException {
    switch (mFilterCombo.getSelectedIndex()) {
    case 0:
      return new EqualsFilter(mText.getDouble());
    case 1:
      return new DoesNotEqualFilter(mText.getDouble());
    case 2:
      return new GTFilter(mText.getDouble());
    case 3:
      return new GEFilter(mText.getDouble());
    case 4:
      return new LTFilter(mText.getDouble());
    case 5:
      return new LEFilter(mText.getDouble());
    case 6:
      return new StartsFilter(mText.getText());
    case 7:
      return new DoesNotStartFilter(mText.getText());
    case 8:
      return new EndsFilter(mText.getText());
    case 9:
      return new DoesNotEndFilter(mText.getText());
    case 10:
      return new ContainsFilter(mText.getText());
    case 11:
      return new DoesNotContainFilter(mText.getText());
    default:
      return new ContainsFilter(mText.getText());
    }
  }

  /**
   * Gets the logical.
   *
   * @return the logical
   */
  public boolean getLogical() {
    return mLogicalCombo.getSelectedIndex() == 0;
  }
}
