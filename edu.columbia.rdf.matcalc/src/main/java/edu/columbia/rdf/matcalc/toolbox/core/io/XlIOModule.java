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
import java.util.List;

import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.dialog.ModernDialogStatus;

import edu.columbia.rdf.matcalc.ImportDialog;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;


// TODO: Auto-generated Javadoc
/**
 * Allow users to open and save Excel files.
 *
 * @author Antony Holmes Holmes
 */
public abstract class XlIOModule extends IOModule  {

	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#openFile(org.matcalc.MainMatCalcWindow, java.nio.file.Path, boolean, int)
	 */
	@Override
	public AnnotationMatrix openFile(final MainMatCalcWindow window,
			final Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter) throws IOException {
		ImportDialog dialog = 
				new ImportDialog(window, 0, true, TextUtils.TAB_DELIMITER);

		dialog.setVisible(true);

		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return null;
		}

		hasHeader = dialog.getHasHeader();
		rowAnnotations = dialog.getRowAnnotations();

		return super.openFile(window, 
				file, 
				hasHeader, 
				skipMatches, 
				rowAnnotations, 
				delimiter);
	}
}
