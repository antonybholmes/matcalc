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

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.panel.HExpandBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;

/**
 * For choosing an FDR method.
 *
 * @author Antony Holmes
 */
public class DuplicatePanel extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member text delimiter.
   */
  private ModernTextField mTextDelimiter = new ModernTextField(" /// ");

  /**
   * The member header combo.
   *
   * @param matrix the matrix
   */
  // private MatrixRowAnnotationCombo mHeaderCombo;

  /**
   * Instantiates a new duplicate panel.
   *
   * @param matrix the matrix
   */
  public DuplicatePanel(DataFrame matrix) {
    // ModernLabel label = new ModernLabel("ID", ModernWidget.STANDARD_SIZE);

    // box2.add(label);

    // mHeaderCombo = new MatrixRowAnnotationCombo(matrix);

    // Ui.setSize(mHeaderCombo, new Dimension(180, ModernWidget.WIDGET_HEIGHT));

    // box2.add(mHeaderCombo);

    // add(box2);

    // add(Ui.createVGap(20));

    add(new HExpandBox("Delimiter", new ModernTextBorderPanel(mTextDelimiter, ModernWidget.STANDARD_SIZE)));

  }

  /**
   * Gets the delimiter.
   *
   * @return the delimiter
   */
  public String getDelimiter() {
    return mTextDelimiter.getText();
  }
}
