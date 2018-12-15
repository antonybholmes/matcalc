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

import org.jebtk.math.matrix.CSVWorksheetParser;
import org.jebtk.math.matrix.CsvMatrixParser;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.DoubleMatrixParser;
import org.jebtk.math.matrix.MixedMatrixParser;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.io.FileFilterService;
import org.jebtk.modern.io.GuiFileExtFilter;

import edu.columbia.rdf.matcalc.FileType;
import edu.columbia.rdf.matcalc.ImportDialog;
import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.OpenFiles;

/**
 * Allow users to open and save Text files.
 *
 * @author Antony Holmes Holmes
 */
public class TxtIOModule extends IOModule {

  /** The Constant TXT_FILTER. */
  private static final GuiFileExtFilter TXT_FILTER = 
      FileFilterService.getInstance().getFilter("txt");

  /**
   * Instantiates a new tsv IO module.
   */
  public TxtIOModule() {
    registerFileOpenType(TXT_FILTER);
    registerFileSaveType(TXT_FILTER);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Txt IO";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.CalcModule#openFile(org.matcalc.MainMatCalcWindow,
   * java.nio.file.Path, boolean, int)
   */
  @Override
  public DataFrame open(final MainMatCalcWindow window,
      final Path file,
      FileType type,
      int headers,
      int rowAnnotations,
      String delimiter,
      Collection<String> skipMatches) throws IOException {

    rowAnnotations = OpenFiles
        .estimateRowAnnotations(file, headers, skipMatches);

    delimiter = OpenFiles.guessDelimiter(file, skipMatches);

    boolean numerical = OpenFiles
        .guessNumerical(file, headers, delimiter, rowAnnotations, skipMatches);

    ImportDialog dialog = new ImportDialog(window, headers, rowAnnotations, false,
        delimiter, numerical);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return null;
    }

    return read(window,
        file,
        dialog.isNumerical() ? FileType.NUMERICAL : FileType.MIXED,
            dialog.getHasHeader() ? 1 : 0,
                dialog.getRowAnnotations(),
                dialog.getDelimiter(),
                dialog.getSkipLines());
  }

  @Override
  public DataFrame read(final MainMatCalcWindow window,
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
        return new CSVWorksheetParser(rowAnnotations).parse(file);
      }
    } else {
      //if (headers > 0) {
      if (type == FileType.NUMERICAL) {
        return new DoubleMatrixParser(headers, skipLines, rowAnnotations,
            delimiter).parse(file);
      } else {
        return new MixedMatrixParser(headers, skipLines, rowAnnotations,
            delimiter).parse(file);
        
        //return new MixedWorksheetParser(headers, skipLines, rowAnnotations,
        //    delimiter).parse(file);
      }
      //} else {
      //  return new DynamicMixedMatrixParser(skipLines, rowAnnotations,
      //      delimiter).parse(file);
      //}
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.CalcModule#saveFile(org.matcalc.MainMatCalcWindow,
   * java.nio.file.Path, org.abh.common.math.matrix.DataFrame)
   */
  @Override
  public boolean write(final MainMatCalcWindow window,
      final Path file,
      final DataFrame m) throws IOException {
    DataFrame.writeDataFrame(m, file);

    return true;
  }
}
