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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jebtk.core.PluginService;

import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The Class ModuleLoader.
 */
public class ModuleLoader implements Iterable<Class<? extends Module>> {

  /** The m modules list. */
  private List<Class<? extends Module>> mModulesList = new ArrayList<Class<? extends Module>>();

  /**
   * Adds the module.
   *
   * @param c the c
   * @return the module loader
   */
  public ModuleLoader addModule(Class<? extends Module> m) {
    // PluginService.getInstance().addPlugin(c);

    mModulesList.add(m);

    ModuleService.getInstance().add(m);

    return this;
  }

  /**
   * Load modules.
   */
  public void loadModules() {
    for (Class<?> c : mModulesList) {
      PluginService.getInstance().addPlugin(c);
    }
  }

  @Override
  public Iterator<Class<? extends Module>> iterator() {
    return mModulesList.iterator();
  }
}
