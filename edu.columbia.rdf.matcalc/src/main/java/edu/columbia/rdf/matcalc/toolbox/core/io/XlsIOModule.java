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
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.ui.external.microsoft.XlsGuiFileFilter;
import org.jebtk.modern.io.GuiFileExtFilter;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;


// TODO: Auto-generated Javadoc
/**
 * Allow users to open and save Excel files.
 *
 * @author Antony Holmes Holmes
 */
public class XlsIOModule extends XlIOModule  {
	
	/** The Constant FILTER. */
	private static final GuiFileExtFilter FILTER = new XlsGuiFileFilter();

	/**
	 * Instantiates a new xlsx IO module.
	 */
	public XlsIOModule() {
		registerFileOpenType(FILTER);
		registerFileSaveType(FILTER);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Excel Xls IO";
	}		

	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#openFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, boolean, int)
	 */
	@Override
	public AnnotationMatrix autoOpenFile(final MainMatCalcWindow window,
			final Path file,
			int headers,
			int rowAnnotations,
			String delimiter,
			Collection<String> skipLines) throws IOException {
		try {
			return Excel.convertXlsToMatrix(file, 
					headers > 0, 
					rowAnnotations);
		} catch (InvalidFormatException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#saveFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, org.abh.common.math.matrix.AnnotationMatrix)
	 */
	@Override
	public boolean saveFile(final MainMatCalcWindow window,
			final Path file, 
			final AnnotationMatrix m) throws IOException {
		Excel.writeXls(m, file);
		
		return true;
	}
}