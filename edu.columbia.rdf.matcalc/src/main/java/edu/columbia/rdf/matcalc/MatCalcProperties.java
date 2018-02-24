/**
 * Copyright 2018 Antony Holmes
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
package edu.columbia.rdf.matcalc;

import org.jebtk.core.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class MatCalcProperties controls the configuraration of the matcalc UI.
 */
public class MatCalcProperties extends Properties {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /**
   * Instantiates a new mat calc properties.
   */
  public MatCalcProperties() {
    setProperty("matcalc.ui.groups.enabled", true);
    setProperty("matcalc.ui.files.enabled", true);
    setProperty("matcalc.ui.history.enabled", true);
    setProperty("matcalc.ui.left-tabs.enabled", true);
    setProperty("matcalc.ui.right-tabs.enabled", true);
    setProperty("matcalc.ui.table.drop-shadow.enabled", true);
  }
  

}
