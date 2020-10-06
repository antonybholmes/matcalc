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

import org.jebtk.core.Props;

/**
 * The Class MatCalcProps controls the configuraration of the matcalc UI.
 */
public class MatCalcProps extends Props {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new mat calc properties.
   */
  public MatCalcProps() {
    set("matcalc.ui.groups.enabled", true);
    set("matcalc.ui.files.enabled", true);
    set("matcalc.ui.history.enabled", true);
    set("matcalc.ui.left-tabs.enabled", true);
    set("matcalc.ui.right-tabs.enabled", true);
    set("matcalc.ui.table.drop-shadow.enabled", true);
  }

}
