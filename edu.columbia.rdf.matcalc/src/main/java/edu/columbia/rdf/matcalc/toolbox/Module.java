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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jebtk.modern.help.GuiAppInfo;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

// TODO: Auto-generated Javadoc
/**
 * The module interface dictates the function of a MatCalc module that can
 * extend the functionality of the base MatCalc application. Modules can access
 * the current matrix and add new matrices to the work flow as well as provide
 */
public abstract class Module extends FileModule
    implements Iterable<FileModule> {

  private List<FileModule> mFileModules = new ArrayList<FileModule>();

  public void registerFileModule(FileModule module) {
    mFileModules.add(module);
  }

  @Override
  public Iterator<FileModule> iterator() {
    return mFileModules.iterator();
  }

  /**
   * Gets the module info.
   *
   * @return the module info
   */
  public abstract GuiAppInfo getModuleInfo();

  /**
   * Each module is given access to the app so that it can manipulate the UI and
   * add new functions.
   *
   * @param window the window
   */
  public abstract void init(MainMatCalcWindow window);

  /**
   * Should run itself.
   *
   * @param args the args
   * @throws IOException 
   */
  public abstract void run(String... args) throws IOException;
}
