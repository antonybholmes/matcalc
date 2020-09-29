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
package edu.columbia.rdf.matcalc.groups;

import org.jebtk.modern.table.ModernRowTable;
import org.jebtk.modern.table.ModernTableCheckboxCellEditor;
import org.jebtk.modern.table.ModernTableCheckboxCellRenderer;
import org.jebtk.modern.table.ModernTableColorCellEditor;
import org.jebtk.modern.table.ModernTableColorCellRenderer;
import org.jebtk.modern.window.ModernWindow;

/**
 * The Class XYSeriesTable.
 */
public class XYSeriesTable extends ModernRowTable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new XY series table.
   *
   * @param parent the parent
   */
  public XYSeriesTable(ModernWindow parent) {
    getRendererModel().setCol(0, new ModernTableCheckboxCellRenderer());
    getEditorModel().setCol(0, new ModernTableCheckboxCellEditor());

    getRendererModel().setCol(2, new ModernTableColorCellRenderer());
    getEditorModel().setCol(2, new ModernTableColorCellEditor(parent));

  }

}
