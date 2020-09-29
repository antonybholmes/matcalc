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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.io.GuiFileExtFilter;

import edu.columbia.rdf.matcalc.FileType;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Designed for modules that can read/write files as well as perform some
 * function.
 */
public abstract class IOModule extends Module {

  // private List<IOModule> mFileModules = new ArrayList<IOModule>();

  /** The m open file filters. */
  private Set<GuiFileExtFilter> mOpenFileFilters = new HashSet<GuiFileExtFilter>();

  /** The m save file filters. */
  private Set<GuiFileExtFilter> mSaveFileFilters = new HashSet<GuiFileExtFilter>();

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
  public DataFrame open(final MainMatCalcWindow window, final Path file, FileType type, int headers, int rowAnnotations,
      String delimiter, Collection<String> skipLines) throws IOException {
    return read(window, file, type, headers, rowAnnotations, delimiter, skipLines);
  }

  /**
   * Open a file without prompting user for options.
   * 
   * @param window
   * @param file
   * @param mFileType
   * @param headers        How many lines represent a header.
   * @param rowAnnotations
   * @param delimiter
   * @param skipLines      Skip lines beginning with
   * @return
   * @throws IOException
   */
  public DataFrame read(final MainMatCalcWindow window, final Path file, FileType type, int headers, int rowAnnotations,
      String delimiter, Collection<String> skipLines) throws IOException {
    return null;
  }

  /**
   * Save file.
   *
   * @param window the window
   * @param file   the file
   * @param m      the m
   * @return true if the file was saved or false otherwise.
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean write(final MainMatCalcWindow window, final Path file, final DataFrame m) throws IOException {
    return false;
  }

  /**
   * If this module can open a certain file type, return an ext filter so that
   * this module is registered for the files with a given set of extensions. There
   * can only be one module associated with a file extension. Modules are loaded
   * in order, so if there are two modules registered for a given file type, the
   * second will ultimately be bound to that file type.
   * 
   * @return A file filter or null if the module does not open a file.
   */
  public Set<GuiFileExtFilter> getOpenFileFilters() {
    return mOpenFileFilters;
  }

  public Set<GuiFileExtFilter> getSaveFileFilters() {
    return mSaveFileFilters;
  }

  public void registerFileType(GuiFileExtFilter filter) {
    registerFileOpenType(filter);
    registerFileSaveType(filter);
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
}
