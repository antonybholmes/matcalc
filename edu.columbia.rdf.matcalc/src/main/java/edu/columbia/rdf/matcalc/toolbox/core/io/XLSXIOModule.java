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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.external.microsoft.ExcelMatrix;
import org.jebtk.math.external.microsoft.XLSXMetaData;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.ui.external.microsoft.XlsxGuiFileFilter;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.io.GuiFileExtFilter;

import edu.columbia.rdf.matcalc.FileType;
import edu.columbia.rdf.matcalc.ImportDialog;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.OpenFile;


// TODO: Auto-generated Javadoc
/**
 * Allow users to open and save Excel files.
 *
 * @author Antony Holmes Holmes
 */
public class XLSXIOModule extends XlIOModule  {

	/** The Constant FILTER. */
	private static final GuiFileExtFilter FILTER = new XlsxGuiFileFilter();

	/**
	 * Instantiates a new xlsx IO module.
	 */
	public XLSXIOModule() {
		registerFileOpenType(FILTER);
		registerFileSaveType(FILTER);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Excel XLSX IO";
	}
	
	@Override
	public DataFrame openFile(final MainMatCalcWindow window,
			final Path file,
			FileType type,
			int headers,
			int rowAnnotations,
			String delimiter,
			Collection<String> skipLines) throws IOException {
		
		XLSXMetaData metaData = new XLSXMetaData(file);
		
		rowAnnotations = metaData.estimateRowAnnotations();
		
		ImportDialog dialog = new ImportDialog(window, 
				rowAnnotations, 
				true, 
				delimiter, 
				metaData.isNumerical(rowAnnotations));

		dialog.setVisible(true);

		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return null;
		}

		headers = dialog.getHasHeader() ? 1 : 0;
		rowAnnotations = dialog.getRowAnnotations();

		return autoOpenFile(window, 
				file,
				type,
				headers, 
				rowAnnotations, 
				delimiter,
				skipLines);
	}

	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#openFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, boolean, int)
	 */
	@Override
	public DataFrame autoOpenFile(final MainMatCalcWindow window,
			final Path file,
			FileType type,
			int headers,
			int rowAnnotations,
			String delimiter,
			Collection<String> skipLines) throws IOException {
		try {
			//return Excel.convertXlsxToMatrix(file, 
			//		hasHeader, 
			//		rowAnnotations);
			return ExcelMatrix.xlsxAsMatrix(file, 
					headers > 0, 
					rowAnnotations);
		} catch (InvalidFormatException e) {
			throw new IOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#saveFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, org.abh.common.math.matrix.DataFrame)
	 */
	@Override
	public boolean saveFile(final MainMatCalcWindow window,
			final Path file, 
			final DataFrame m) throws IOException {
		Excel.writeXlsx(m, file);

		return true;
	}
}
