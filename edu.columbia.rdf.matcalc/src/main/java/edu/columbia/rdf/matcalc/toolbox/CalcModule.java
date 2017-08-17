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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.io.GuiFileExtFilter;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.MatCalcInfo;

// TODO: Auto-generated Javadoc
/**
 * The class CalcModule.
 */
public abstract class CalcModule implements Module {

	/**
	 * The constant LOAD_MATRIX_MESSAGE.
	 */
	private static final String LOAD_MATRIX_MESSAGE = 
			"You must load a file.";
	
	/** The Constant DEFAULT_INFO. */
	private static final GuiAppInfo DEFAULT_INFO = 
			new MatCalcInfo();
	
	/** The m open file filters. */
	private Set<GuiFileExtFilter> mOpenFileFilters =
			new TreeSet<GuiFileExtFilter>();
	
	/** The m save file filters. */
	private Set<GuiFileExtFilter> mSaveFileFilters =
			new TreeSet<GuiFileExtFilter>();

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#getModuleInfo()
	 */
	@Override
	public GuiAppInfo getModuleInfo() {
		return DEFAULT_INFO;
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.Module#init(org.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		// Do nothing
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) {
		// Do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.Module#getOpenFileFilters()
	 */
	@Override
	public Set<GuiFileExtFilter> getOpenFileFilters() {
		return mOpenFileFilters;	
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.Module#getSaveFileFilters()
	 */
	@Override
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
	
	
	@Override
	public AnnotationMatrix openFile(final MainMatCalcWindow window,
			final Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter) throws IOException {
		return autoOpenFile(window, file, hasHeader, skipMatches, rowAnnotations, delimiter);	
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.Module#openFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, boolean, int)
	 */
	@Override
	public AnnotationMatrix autoOpenFile(final MainMatCalcWindow window,
			final Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter) throws IOException {
		return null;	
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.Module#saveFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, org.abh.common.math.matrix.AnnotationMatrix)
	 */
	@Override
	public boolean saveFile(final MainMatCalcWindow window,
			final Path file, 
			final AnnotationMatrix m) throws IOException {
		return false;
	}
	
	/**
	 * Show a standard error telling user to load a file.
	 *
	 * @param parent the parent
	 */
	public static final void showLoadMatrixError(ModernWindow window) {
		ModernMessageDialog.createWarningDialog(window, LOAD_MATRIX_MESSAGE);
	}
	
	/**
	 * Find columns.
	 *
	 * @param w the w
	 * @param m the m
	 * @param terms the terms
	 * @return the map
	 */
	protected static Map<String, Integer> findColumns(MainMatCalcWindow w,
			AnnotationMatrix m, 
			String... terms) {
		
		Map<String, Integer> indexMap = 
				AnnotationMatrix.findColumns(m, terms);
		
		boolean found = true;
		
		for (String term : terms) {
			if (indexMap.get(term) == -1) {
				found = false;
				break;
			}
		}
		
		if (found) {
			return indexMap;
		} else {
			ModernMessageDialog.createWarningDialog(w,
					"The matrix must include columns named " +
					TextUtils.formattedList(CollectionUtils.sort(CollectionUtils.unique(terms))) + ".");
			
			return null;
		}
	}
}
