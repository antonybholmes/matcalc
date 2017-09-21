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

import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.CsvDynamicMatrixParser;
import org.jebtk.math.matrix.CsvMatrixParser;
import org.jebtk.math.matrix.DoubleMatrixParser;
import org.jebtk.math.matrix.DynamicMatrixParser;
import org.jebtk.math.matrix.MixedMatrixParser;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.io.GuiFileExtFilter;
import org.jebtk.modern.io.TxtGuiFileFilter;

import edu.columbia.rdf.matcalc.FileType;
import edu.columbia.rdf.matcalc.ImportDialog;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.OpenFile;


// TODO: Auto-generated Javadoc
/**
 * Allow users to open and save Text files.
 *
 * @author Antony Holmes Holmes
 */
public class TxtIOModule extends IOModule  {

	/** The Constant TXT_FILTER. */
	private static final GuiFileExtFilter TXT_FILTER = new TxtGuiFileFilter();


	/**
	 * Instantiates a new tsv IO module.
	 */
	public TxtIOModule() {
		registerFileOpenType(TXT_FILTER);
		registerFileSaveType(TXT_FILTER);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Txt IO";
	}

	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#openFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, boolean, int)
	 */
	@Override
	public AnnotationMatrix openFile(final MainMatCalcWindow window,
			final Path file,
			FileType type,
			int headers,
			int rowAnnotations,
			String delimiter,
			Collection<String> skipMatches) throws IOException {

		rowAnnotations = OpenFile.estimateRowAnnotations(file, headers, skipMatches);
		
		delimiter = OpenFile.guessDelimiter(file, skipMatches);
		
		boolean numerical = OpenFile.guessNumerical(file, headers, delimiter, rowAnnotations, skipMatches);
		
		System.err.println("aha " + rowAnnotations + " " + delimiter + " " + numerical);
		
		ImportDialog dialog = new ImportDialog(window,
				rowAnnotations, 
				false,
				delimiter,
				numerical);

		dialog.setVisible(true);

		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return null;
		}

		return autoOpenFile(window, 
				file,
				dialog.isNumerical() ? FileType.NUMERICAL : FileType.MIXED,
				dialog.getHasHeader() ? 1: 0,
				dialog.getRowAnnotations(),
				dialog.getDelimiter(),
				dialog.getSkipLines());
	}

	@Override
	public AnnotationMatrix autoOpenFile(final MainMatCalcWindow window,
			final Path file,
			FileType type,
			int headers,
			int rowAnnotations,
			String delimiter,
			Collection<String> skipLines) throws IOException {
		if (delimiter.equals(",")) {
			// Use the csv parser instead
			if (headers > 0) {
				return new CsvMatrixParser(true, rowAnnotations).parse(file);
			} else {
				return new CsvDynamicMatrixParser(rowAnnotations).parse(file);
			}
		} else {
			if (headers > 0) {
				if (type == FileType.NUMERICAL) {
					return new DoubleMatrixParser(true, 
							skipLines, 
							rowAnnotations, 
							delimiter)
							.parse(file);
				} else {
					return new MixedMatrixParser(true, 
							skipLines, 
							rowAnnotations, 
							delimiter)
							.parse(file);
				}
			} else {
				return new DynamicMatrixParser(skipLines, 
						rowAnnotations, 
						delimiter)
						.parse(file); //return AnnotationMatrix.parseDynamicMatrix(file, hasHeader, rowAnnotations, '\t');
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#saveFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, org.abh.common.math.matrix.AnnotationMatrix)
	 */
	@Override
	public boolean saveFile(final MainMatCalcWindow window,
			final Path file, 
			final AnnotationMatrix m) throws IOException {
		AnnotationMatrix.writeAnnotationMatrix(m, file);

		return true;
	}
}
