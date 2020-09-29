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

import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.help.RibbonPanelProductInfo;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.table.ModernRowTable;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernSubTitleLabel;

/**
 * Display the program settings tree inline within the Ribbon menu.
 * 
 * @author Antony Holmes
 *
 */
public class ModulesRibbonPanel extends RibbonPanelProductInfo {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member table.
   */
  private ModernRowTable mTable;

  /**
   * The member modules.
   */
  private List<Module> mModules;

  /**
   * Instantiates a new modules ribbon panel.
   *
   * @param product the product
   * @param modules the modules
   */
  public ModulesRibbonPanel(GuiAppInfo product, List<Module> modules) {
    super(product);

    mModules = modules;

    createUi();
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    ModernComponent content = new ModernComponent();

    ModernAutoSizeLabel heading = new ModernSubTitleLabel("Installed Modules");

    heading.setBorder(BorderService.getInstance().createTopBottomBorder(40, 10));

    content.setHeader(heading);

    mTable = new ModernRowTable(new ModulesTableModel(mModules));
    mTable.setShowRowHeader(false);
    mTable.getColumnModel().setWidth(0, 200);
    mTable.getColumnModel().setWidth(1, 100);
    mTable.getColumnModel().setWidth(2, 100);
    mTable.getColumnModel().setWidth(3, 200);
    mTable.getColumnModel().setWidth(4, 200);

    ModernScrollPane scrollPane = new ModernScrollPane(mTable);

    content.setBody(scrollPane); // new ModernContentPanel(scrollPane));

    // setBorder(BorderService.getInstance().createTopBorder(40));

    setBody(content);
  }
}
