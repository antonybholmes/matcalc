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
package edu.columbia.rdf.matcalc;

import org.jebtk.core.AppVersion;
import org.jebtk.modern.help.GuiAppInfo;

import edu.columbia.rdf.matcalc.icons.MatCalcIcon;

/**
 * The class MatCalcInfo.
 */
public class MatCalcInfo extends GuiAppInfo {

  /**
   * Instantiates a new mat calc info.
   */
  public MatCalcInfo() {
    super("MatCalc", new AppVersion(28),
        "Copyright (C) 2014-${year} Antony Holmes",
        new MatCalcIcon(),
        "Matrix calculations and plotting.");
  }

}
