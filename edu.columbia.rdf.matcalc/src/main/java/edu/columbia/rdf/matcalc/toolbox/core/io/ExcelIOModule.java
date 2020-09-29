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
package edu.columbia.rdf.matcalc.toolbox.core.io;

import org.jebtk.modern.io.FileFilterService;
import org.jebtk.modern.io.GuiFileExtFilter;

/**
 * Allow users to open and save Excel files. This module provides file types,
 * but expects other modules to handle opening and saving.
 *
 * @author Antony Holmes
 */
public class ExcelIOModule extends IOModule {

  /** The Constant FILTER. */
  private static final GuiFileExtFilter FILTER = FileFilterService.getInstance().getFilter("excel"); // new
                                                                                                     // ExcelGuiFileFilter();

  /**
   * Instantiates a new xlsx IO module.
   */
  public ExcelIOModule() {
    registerFileOpenType(FILTER);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Excel IO";
  }
}
