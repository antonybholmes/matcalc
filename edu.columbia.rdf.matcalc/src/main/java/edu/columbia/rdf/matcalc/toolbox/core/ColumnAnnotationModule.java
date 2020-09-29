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

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Allow user to change which columns are annotation or not.
 * 
 *
 * @author Antony Holmes
 *
 */
public class ColumnAnnotationModule extends Module implements ModernClickListener {

  /**
   * The member split button.
   */
  private RibbonLargeButton mButton = new RibbonLargeButton(AssetService.getInstance().loadIcon("annotation", 24));

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
    return "Column Annotation";
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

    mButton.setToolTip("Column Annotation", "Adjust which columns are annotation columns.");

    window.getRibbon().getToolbar("Data").getSection("Tools").add(mButton);

    mButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    annotate();
  }

  /**
   * Split.
   */
  private void annotate() {
    int c = mWindow.getSelectedColumn();

    if (c == Integer.MIN_VALUE) {
      ModernMessageDialog.createDialog(mWindow, "You must select a column.", MessageDialogType.WARNING);

      return;
    }

    DataFrame m = mWindow.getCurrentMatrix();

    int minC = -m.getIndex().getNames().size();

    // First copy the columns and turn them into annotations
    DataFrame ret;

    if (c > minC) {
      ret = DataFrame.createDataFrame(m.getRows(), m.getCols() - c);
    } else {
      // If there are no annotation columns, assume the matrix has
      // mixed content
      ret = DataFrame.createDataFrame(m.getRows(), m.getCols());
    }

    // Copy the other columns
    // Columns start with negative indices if they are part of the annotation
    DataFrame.copyColumns(m, c, ret);

    // Copy existing row annotations
    // DataFrame.copyIndex(m, ret);

    switch (m.getType()) {
    case NUMBER:
      for (int i = minC; i < c; ++i) {
        ret.getIndex().setAnnotation(m.getText(-1, i), m.columnToDouble(i));
      }

      break;
    case TEXT:
      for (int i = minC; i < c; ++i) {
        ret.getIndex().setAnnotation(m.getText(-1, i), m.columnToText(i));
      }

      break;
    default:
      for (int i = minC; i < c; ++i) {
        ret.getIndex().setAnnotation(m.getText(-1, i), m.columnToObject(i));
      }

      break;
    }

    mWindow.history().addToHistory("Adjust matrix", ret);
  }
}
