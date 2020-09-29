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
package edu.columbia.rdf.matcalc.toolbox.plot.barchart;

import javax.swing.Box;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.MatrixRowAnnotationCombo;

/**
 * The class BarChartDialog.
 */
public class BarChartDialog extends ModernDialogTaskWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member row combo.
   */
  private MatrixRowAnnotationCombo mRowCombo;

  /**
   * Instantiates a new bar chart dialog.
   *
   * @param parent the parent
   * @param m      the m
   */
  public BarChartDialog(ModernWindow parent, DataFrame m) {
    super(parent);

    mRowCombo = new MatrixRowAnnotationCombo(m);

    setTitle("Plot Bar Chart");

    setup();

    createUi();

  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setSize(320, 240);

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
    int[] cols = { 150, 120 };

    MatrixPanel matrixPanel;

    matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING, ModernWidget.PADDING);

    matrixPanel.add(new ModernAutoSizeLabel("Row Annotation"));
    matrixPanel.add(mRowCombo);

    matrixPanel.setBorder(ModernWidget.DOUBLE_BORDER);
    content.add(matrixPanel);

    setContent(content);
  }

  /**
   * Gets the annotation name.
   *
   * @return the annotation name
   */
  public String getAnnotationName() {
    return mRowCombo.getText();
  }
}
