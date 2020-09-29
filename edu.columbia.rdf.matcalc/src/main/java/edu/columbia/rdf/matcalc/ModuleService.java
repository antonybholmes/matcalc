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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jebtk.core.collections.IterTreeMap;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Loads plugin class definitions from file so they can be instantiated. Plugins
 * must be in there own directory, for example annotations, within the main
 * plugin directory. Within each individual plugin folder, the classes must be
 * in the same package hierarchy as they were designed. The plugin loader will
 * scan the directories and build the packages from this.
 *
 * @author Antony Holmes
 *
 */
public class ModuleService implements Iterable<String> {

  private static class PluginServiceLoader {
    private static final ModuleService INSTANCE = new ModuleService();
  }

  /**
   * Gets the single instance of SettingsService.
   *
   * @return single instance of SettingsService
   */
  public static ModuleService getInstance() {
    return PluginServiceLoader.INSTANCE;
  }

  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);

  private Map<String, Class<? extends Module>> mPluginMap = new IterTreeMap<String, Class<? extends Module>>();

  private Map<String, Module> mInstanceMap = new IterTreeMap<String, Module>();

  private List<String> mNames = new UniqueArrayList<String>();

  /**
   * Instantiates a new plugin service.
   */
  private ModuleService() {
    // do nothing
  }

  /**
   * Adds the plugin.
   *
   * @param c the c
   */
  public void add(Class<? extends Module> m) {
    String name = m.getName().toLowerCase().replaceFirst("^.+\\.", TextUtils.EMPTY_STRING).replaceFirst("module",
        TextUtils.EMPTY_STRING);

    add(name, m);
  }

  public void add(String name, Class<? extends Module> m) {
    LOG.info("Adding module {}", name);

    name = name.toLowerCase();

    mPluginMap.put(name, m);

    // Iterate over plugins in the order added.
    mNames.add(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<String> iterator() {
    return mNames.iterator();
  }

  public Module instance(String name) throws InstantiationException, IllegalAccessException {
    String ln = name.toLowerCase();

    if (!mInstanceMap.containsKey(ln)) {
      mInstanceMap.put(ln, (Module) get(ln).newInstance());
    }

    return mInstanceMap.get(ln);
  }

  public Class<? extends Module> get(String name) {
    return mPluginMap.get(name.toLowerCase());
  }

  public void add(ModuleLoader loader) {
    for (Class<? extends Module> m : loader) {
      ModuleService.getInstance().add(m);
    }
  }
}