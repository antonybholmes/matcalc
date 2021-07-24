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
import java.util.Map;

import org.jebtk.core.NameGetter;
import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.cli.Args;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.MatCalcInfo;

/**
 * The class CalcModule.
 */
public abstract class Module implements NameGetter {

  /**
   * The constant LOAD_MATRIX_MESSAGE.
   */
  private static final String LOAD_MATRIX_MESSAGE = "You must load a file.";

  /** The Constant DEFAULT_INFO. */
  private static final GuiAppInfo DEFAULT_INFO = new MatCalcInfo();

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#getModuleInfo()
   */
  public GuiAppInfo getModuleInfo() {
    return DEFAULT_INFO;
  }

  public void init(MainMatCalcWindow window) {
    // Do nothing
  }

  public Args args() {
    return null;
  }

  /**
   * Should run itself.
   *
   * @param args the args
   * @throws IOException
   */
  public final void run(String... args) throws IOException {
    Args pa = args();

    ArgParser ap = new ArgParser(pa).parse(args);
    
    System.err.println("run mod");
    
    run(ap);
  }

  public void run(ArgParser ap) throws IOException {
    // Do nothing
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
   * @param w     the w
   * @param m     the m
   * @param terms the terms
   * @return the map
   */
  protected static Map<String, Integer> findColumns(MainMatCalcWindow w, DataFrame m, String... terms) {

    Map<String, Integer> indexMap = DataFrame.findColumns(m, terms);

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
      ModernMessageDialog.createWarningDialog(w, "The matrix must include columns named "
          + TextUtils.formattedList(CollectionUtils.sort(CollectionUtils.unique(terms))) + ".");

      return null;
    }
  }

  public Args getArgs() {
    // TODO Auto-generated method stub
    return null;
  }
}
