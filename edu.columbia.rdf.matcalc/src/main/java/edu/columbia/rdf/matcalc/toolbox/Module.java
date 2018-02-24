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
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.jebtk.core.NameProperty;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.io.GuiFileExtFilter;

import edu.columbia.rdf.matcalc.FileType;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;

// TODO: Auto-generated Javadoc
/**
 * The module interface dictates the function of a MatCalc module that can
 * extend the functionality of the base MatCalc application. Modules can access
 * the current matrix and add new matrices to the work flow as well as provide
 */
public abstract class Module implements NameProperty {

  //private List<IOModule> mFileModules = new ArrayList<IOModule>();
  
  /** The m open file filters. */
  private Set<GuiFileExtFilter> mOpenFileFilters = 
      new TreeSet<GuiFileExtFilter>();

  /** The m save file filters. */
  private Set<GuiFileExtFilter> mSaveFileFilters = 
      new TreeSet<GuiFileExtFilter>();

  ///public void registerFileModule(IOModule module) {
  //  mFileModules.add(module);
  //}

  //@Override
  //public Iterator<IOModule> iterator() {
  //  return mFileModules.iterator();
 // }

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
  public void init(MainMatCalcWindow window) {
    // Do nothing
  }

  /**
   * Should run itself.
   *
   * @param args the args
   * @throws IOException 
   */
  public void run(String... args) throws IOException {
    // Do nothing
  }
  
  /**
   * Open a file prompting user for options.
   * 
   * @param window
   * @param file
   * @param hasHeader
   * @param skipMatches
   * @param rowAnnotations
   * @param delimiter
   * @return
   * @throws IOException
   */
  public DataFrame openFile(final MainMatCalcWindow window,
      final Path file,
      FileType type,
      int headers,
      int rowAnnotations,
      String delimiter,
      Collection<String> skipLines) throws IOException {
    return autoOpenFile(window,
        file,
        type,
        headers,
        rowAnnotations,
        delimiter,
        skipLines);
  }

  /**
   * Open a file without prompting user for options.
   * 
   * @param window
   * @param file
   * @param mFileType
   * @param headers How many lines represent a header.
   * @param rowAnnotations
   * @param delimiter
   * @param skipLines Skip lines beginning with
   * @return
   * @throws IOException
   */
  public DataFrame autoOpenFile(final MainMatCalcWindow window,
      final Path file,
      FileType type,
      int headers,
      int rowAnnotations,
      String delimiter,
      Collection<String> skipLines) throws IOException {
    return null;
  }

  /**
   * Save file.
   *
   * @param window the window
   * @param file the file
   * @param m the m
   * @return true if the file was saved or false otherwise.
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean saveFile(final MainMatCalcWindow window,
      final Path file,
      final DataFrame m) throws IOException {
    return false;
  }

  /**
   * If this module can open a certain file type, return an ext filter so that
   * this module is registered for the files with a given set of extensions.
   * There can only be one module associated with a file extension. Modules are
   * loaded in order, so if there are two modules registered for a given file
   * type, the second will ultimately be bound to that file type.
   * 
   * @return A file filter or null if the module does not open a file.
   */
  public Set<GuiFileExtFilter> getOpenFileFilters() {
    return mOpenFileFilters;
  }

  public Set<GuiFileExtFilter> getSaveFileFilters() {
    return mSaveFileFilters;
  }

  /**
   * Register file type.
   *
   * @param filter the filter
   */
  public void registerFileOpenType(GuiFileExtFilter filter) {
    mOpenFileFilters.add(filter);
  }

  /**
   * Register file save type.
   *
   * @param filter the filter
   */
  public void registerFileSaveType(GuiFileExtFilter filter) {
    mSaveFileFilters.add(filter);
  }
  
  /**
   * Returns true if module implements IO operations to open/save files that
   * require the UI to be updated so user can select file type etc. Returning
   * false implies module is a file listener - it can respond to file events
   * only.
   * 
   * @return
   */
  public boolean showIOFilterUI() {
    return true;
  }
}
