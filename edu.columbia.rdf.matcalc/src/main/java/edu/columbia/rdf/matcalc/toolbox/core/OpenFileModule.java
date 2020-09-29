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
package edu.columbia.rdf.matcalc.toolbox.core;

import java.io.IOException;

import org.jebtk.core.cli.ArgParser;
import org.jebtk.core.cli.Args;
import org.jebtk.core.io.PathUtils;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.OpenFiles;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class HeatMapModule.
 */
public class OpenFileModule extends Module {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  private static final Args ARGS = new Args();

  static {
    ARGS.add('h', "headers", true);
    ARGS.add('i', "indexes", true);
    ARGS.add('s', "skip", true);
    ARGS.add('f', "file", true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Open File";
  }

  @Override
  public Args getArgs() {
    return ARGS;
  }

  @Override
  public void run(ArgParser ap) throws IOException {
    OpenFiles open = new OpenFiles(mParent);

    if (ap.contains("headers")) {
      open = open.headers(ap.getInt("headers"));
    }

    if (ap.contains("indexes")) {
      open = open.indexes(ap.getInt("indexes"));
    }

    if (ap.contains("skip")) {
      open = open.skipLines(ap.getArgs("skip"));
    }

    for (String path : ap.getArgs("file")) {
      open.open(PathUtils.getPath(path));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mParent = window;
  }
}
