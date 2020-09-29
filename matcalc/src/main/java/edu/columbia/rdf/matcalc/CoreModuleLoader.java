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

import edu.columbia.rdf.matcalc.toolbox.core.HomeModule;
import edu.columbia.rdf.matcalc.toolbox.core.OpenFileModule;
import edu.columbia.rdf.matcalc.toolbox.core.ViewModule;
import edu.columbia.rdf.matcalc.toolbox.core.ZoomModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.CsvIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.ExcelIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.MatrixIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.TsvIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.TxtIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.XLSXIOModule;
import edu.columbia.rdf.matcalc.toolbox.core.io.XlsIOModule;

/**
 * The Class ModuleLoader.
 */
public class CoreModuleLoader extends ModuleLoader {

  /**
   * Instantiates a new module loader.
   */
  public CoreModuleLoader() {
    addModule(HomeModule.class);

    addModule(OpenFileModule.class);

    addModule(MatrixIOModule.class);
    addModule(ExcelIOModule.class);
    addModule(XLSXIOModule.class);
    addModule(XlsIOModule.class);
    addModule(TxtIOModule.class);
    addModule(TsvIOModule.class);
    addModule(CsvIOModule.class);

    addModule(ZoomModule.class);
    addModule(ViewModule.class);
  }
}
