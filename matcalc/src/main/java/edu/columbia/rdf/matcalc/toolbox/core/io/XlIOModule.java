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

import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.dialog.ModernDialogStatus;

import edu.columbia.rdf.matcalc.FileType;
import edu.columbia.rdf.matcalc.ImportDialog;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * Allow users to open and save Excel files.
 *
 * @author Antony Holmes
 */
public abstract class XlIOModule extends IOModule {

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.CalcModule#openFile(org.matcalc.MainMatCalcWindow,
   * java.nio.file.Path, boolean, int)
   */
  @Override
  public DataFrame open(final MainMatCalcWindow window, final Path file, FileType type, int headers, int indexCols,
      String delimiter, Collection<String> skipLines) throws IOException {

    ImportDialog dialog = new ImportDialog(window, headers, indexCols, true, TextUtils.TAB_DELIMITER, false);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return null;
    }

    headers = dialog.getHasHeader() ? 1 : 0;
    indexCols = dialog.getIndexCols();

    return super.open(window, file, type, headers, indexCols, delimiter, skipLines);
  }
}
