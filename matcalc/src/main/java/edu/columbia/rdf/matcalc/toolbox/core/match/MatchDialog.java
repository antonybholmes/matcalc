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
package edu.columbia.rdf.matcalc.toolbox.core.match;

import javax.swing.Box;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernLabelBold;
import org.jebtk.modern.window.WindowService;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.MatCalcWindowCombo;
import edu.columbia.rdf.matcalc.toolbox.ColumnsCombo;

/**
 * The class MatchDialog.
 */
public class MatchDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member match matrix.
   */
  private DataFrame mMatchMatrix = null;

  /**
   * The member m.
   */
  private DataFrame mM;

  /**
   * The member c.
   */
  private int mC;

  /**
   * The member match combo.
   */
  private ModernComboBox mMatchCombo = new MatCalcWindowCombo();

  /**
   * The member match column combo.
   */
  private ColumnsCombo mMatchColumnCombo = new ColumnsCombo();

  /**
   * The member replace column combo.
   */
  private ColumnsCombo mReplaceColumnCombo = new ColumnsCombo();

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow = null;

  /**
   * The class MatchEvents.
   */
  private class MatchEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mWindow = (MainMatCalcWindow) WindowService.getInstance().findByName(mMatchCombo.getText());
      mMatchMatrix = mWindow.getCurrentMatrix();

      mMatchColumnCombo.setMatrix(mMatchMatrix);
      mReplaceColumnCombo.setMatrix(mMatchMatrix);
    }

  }

  /**
   * Instantiates a new match dialog.
   *
   * @param parent the parent
   * @param m      the m
   * @param c      the c
   */
  public MatchDialog(MainMatCalcWindow parent, DataFrame m, int c) {
    super(parent, "matcalc.match-in-files.help.url");

    mWindow = parent;
    mM = m;
    mC = c;

    setTitle("Match In Files");

    setup();

    createUi();

  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(640, 320);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    Box content = VBox.create();

    int[] rows = { ModernWidget.WIDGET_HEIGHT };
    int[] cols = { 150, 380 };

    MatrixPanel matrixPanel;

    matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING, ModernWidget.PADDING);

    matrixPanel.add(new ModernAutoSizeLabel("Source Table"));
    matrixPanel.add(new ModernLabelBold(mWindow.getSubTitle()));

    matrixPanel.add(new ModernAutoSizeLabel("Source Column"));
    matrixPanel.add(new ModernLabelBold(mM.getColumnName(mC)));

    matrixPanel.add(new ModernAutoSizeLabel("Match Table"));
    matrixPanel.add(mMatchCombo);

    matrixPanel.add(new ModernAutoSizeLabel("Match Column"));
    matrixPanel.add(mMatchColumnCombo);

    matrixPanel.add(new ModernAutoSizeLabel("Insert Column"));
    matrixPanel.add(mReplaceColumnCombo);

    // matrixPanel.setBorder(ModernWidget.LARGE_BORDER);
    content.add(matrixPanel);

    setCard(content);

    mMatchCombo.addClickListener(new MatchEvents());

    if (mMatchCombo.getItemCount() > 0) {
      mMatchCombo.setSelectedIndex(0);
    }
  }

  /**
   * Gets the window name.
   *
   * @return the window name
   */
  public String getWindowName() {
    return mMatchCombo.getText();
  }

  /**
   * Gets the match column.
   *
   * @return the match column
   */
  public int getMatchColumn() {
    return mMatchColumnCombo.getSelectedIndex();
  }

  /**
   * Gets the replace column.
   *
   * @return the replace column
   */
  public int getReplaceColumn() {
    return mReplaceColumnCombo.getSelectedIndex();
  }
}
