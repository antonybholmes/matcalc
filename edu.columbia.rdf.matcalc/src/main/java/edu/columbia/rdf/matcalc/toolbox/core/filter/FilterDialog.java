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

import java.util.List;

import javax.swing.Box;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.graphics.icons.RedCrossIcon;
import org.jebtk.modern.panel.HSpacedBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * The class MatchDialog.
 */
public class FilterDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m add button. */
  private ModernButton mAddButton = new ModernButton("Add Filter",
      AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /** The m clear button. */
  private ModernButton mClearButton = new ModernButton(UI.MENU_CLEAR,
      AssetService.getInstance().loadIcon(RedCrossIcon.class, 16));

  /** The m content panel. */
  private ColumnFilters mContentPanel;

  /**
   * Instantiates a new match dialog.
   *
   * @param parent the parent
   * @param m      the m
   * @param c      the c
   */
  public FilterDialog(MainMatCalcWindow parent, DataFrame m, int c) {
    super(parent, "matcalc.filter.help.url");

    mContentPanel = new ColumnFilters(m);

    setTitle("Filter " + m.getColumnName(c) + " column");

    setup();

    createUi();

    mContentPanel.add(c);
  }

  /**
   * Setup.
   */
  private void setup() {
    mAddButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mContentPanel.add(-1);
      }

    });

    mClearButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        mContentPanel.clear();
      }

    });

    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(640, 300);

    setResizable(true);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    ModernComponent content = new ModernComponent();

    Box box = new HSpacedBox();

    box.add(mAddButton);
    box.add(mClearButton);

    box.setBorder(BorderService.getInstance().createBottomBorder(10));

    content.setHeader(box);

    ModernScrollPane scrollPane = new ModernScrollPane(mContentPanel)
        .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER).setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS);

    content.setBody(scrollPane);

    setCard(content);
  }

  /**
   * Gets the column filters.
   *
   * @return the column filters
   */
  public List<ColumnFilter> getColumnFilters() {
    return mContentPanel.getColumnFilters();
  }
}
