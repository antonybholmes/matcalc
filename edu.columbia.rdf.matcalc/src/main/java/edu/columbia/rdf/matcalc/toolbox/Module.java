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
import java.util.List;
import java.util.Set;

import org.jebtk.core.NameProperty;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.io.GuiFileExtFilter;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

// TODO: Auto-generated Javadoc
/**
 * The module interface dictates the function of a MatCalc module that can
 * extend the functionality of the base MatCalc application. Modules can
 * access the current matrix and add new matrices to the work flow as well
 * as provide
 */
public interface Module extends NameProperty {
	
	/**
	 * Gets the module info.
	 *
	 * @return the module info
	 */
	public GuiAppInfo getModuleInfo();
	
	/**
	 * Each module is given access to the app so that it can manipulate
	 * the UI and add new functions.
	 *
	 * @param window the window
	 */
	public void init(MainMatCalcWindow window);
	
	/**
	 * Should run itself.
	 *
	 * @param args the args
	 */
	public void run(String... args);
	
	/**
	 * If this module can open a certain file type, return an ext filter
	 * so that this module is registered for the files with a given set
	 * of extensions. There can only be one module associated with a file
	 * extension. Modules are loaded in order, so if there are two modules
	 * registered for a given file type, the second will ultimately be
	 * bound to that file type.
	 * 
	 * @return		A file filter or null if the module does not open 
	 * 				a file.
	 */
	public Set<GuiFileExtFilter> getOpenFileFilters();
	
	
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
	public AnnotationMatrix openFile(final MainMatCalcWindow window,
			final Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter) throws IOException;
	
	/**
	 * Open a file without prompting user for options.
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
	public AnnotationMatrix autoOpenFile(final MainMatCalcWindow window,
			final Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter) throws IOException;
	
	/**
	 * Gets the save file filter.
	 *
	 * @return the save file filter
	 */
	//public GuiFileExtFilter getSaveFileFilter();
	
	public Set<GuiFileExtFilter> getSaveFileFilters();

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
			final AnnotationMatrix m) throws IOException;
}
