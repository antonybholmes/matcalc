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
package edu.columbia.rdf.matcalc.toolbox;

import java.util.List;

import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.table.ModernTableModel;

/**
 * Loads.
 *
 * @author Antony Holmes
 */
public class ModulesTableModel extends ModernTableModel {

  /**
   * The constant HEADER.
   */
  private static final String[] HEADER = { "Name", "Product", "Version", "Copyright", "Description" };

  /**
   * The member modules.
   */
  private List<Module> mModules;

  /**
   * Instantiates a new modules table model.
   *
   * @param modules the modules
   */
  public ModulesTableModel(List<Module> modules) {
    mModules = modules;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.dataview.ModernDataModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int row, int col) {
    switch (col) {
    case 0:
      return mModules.get(row).getName();
    case 1:
      return mModules.get(row).getModuleInfo().getName();
    case 2:
      return mModules.get(row).getModuleInfo().getVersion().toString();
    case 3:
      return mModules.get(row).getModuleInfo().getCopyright();
    case 4:
      return mModules.get(row).getModuleInfo().getDescription();
    default:
      return TextUtils.EMPTY_STRING;
    }
  }

  @Override
  public int getRowCount() {
    return mModules.size();
  }

  @Override
  public int getColCount() {
    return HEADER.length;
  }

  @Override
  public String getColumnName(int col) {
    return HEADER[col];
  }
}