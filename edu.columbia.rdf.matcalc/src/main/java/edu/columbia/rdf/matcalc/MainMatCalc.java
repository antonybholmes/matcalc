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
package edu.columbia.rdf.matcalc;

import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import org.jebtk.core.sys.SysUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.help.GuiAppInfo;

/**
 * The class MainMatCalc.
 */
public class MainMatCalc {

  /**
   * Sets the up.
   *
   * @param moduleLoader the new up
   * @throws ClassNotFoundException          the class not found exception
   * @throws InstantiationException          the instantiation exception
   * @throws IllegalAccessException          the illegal access exception
   * @throws FontFormatException             the font format exception
   * @throws IOException                     Signals that an I/O exception has
   *                                         occurred.
   * @throws UnsupportedLookAndFeelException the unsupported look and feel
   *                                         exception
   */
  private static void setup(ModuleLoader moduleLoader) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, FontFormatException, IOException, UnsupportedLookAndFeelException {
    moduleLoader.loadModules();
  }

  /**
   * Main.
   *
   * @param moduleLoader the module loader
   * @return the main mat calc window
   * @throws ClassNotFoundException          the class not found exception
   * @throws InstantiationException          the instantiation exception
   * @throws IllegalAccessException          the illegal access exception
   * @throws FontFormatException             the font format exception
   * @throws IOException                     Signals that an I/O exception has
   *                                         occurred.
   * @throws UnsupportedLookAndFeelException the unsupported look and feel
   *                                         exception
   */
  public static MainMatCalcWindow main(BasicModuleLoader moduleLoader)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, IOException,
      UnsupportedLookAndFeelException {
    return main(new MatCalcInfo(), moduleLoader);
  }

  /**
   * Main.
   *
   * @param appInfo      the app info
   * @param moduleLoader the module loader
   * @return the main mat calc window
   * @throws ClassNotFoundException          the class not found exception
   * @throws InstantiationException          the instantiation exception
   * @throws IllegalAccessException          the illegal access exception
   * @throws FontFormatException             the font format exception
   * @throws IOException                     Signals that an I/O exception has
   *                                         occurred.
   * @throws UnsupportedLookAndFeelException the unsupported look and feel
   *                                         exception
   */
  public static MainMatCalcWindow main(GuiAppInfo appInfo, ModuleLoader moduleLoader)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, IOException,
      UnsupportedLookAndFeelException {
    setup(moduleLoader);

    MainMatCalcWindow window = new MainMatCalcWindow(appInfo);

    window.setVisible(true);

    return window;
  }

  /**
   * Main.
   *
   * @param appInfo      the app info
   * @param moduleLoader the module loader
   * @param m            the m
   * @return the main mat calc window
   * @throws ClassNotFoundException          the class not found exception
   * @throws InstantiationException          the instantiation exception
   * @throws IllegalAccessException          the illegal access exception
   * @throws FontFormatException             the font format exception
   * @throws IOException                     Signals that an I/O exception has
   *                                         occurred.
   * @throws UnsupportedLookAndFeelException the unsupported look and feel
   *                                         exception
   */
  public static MainMatCalcWindow main(GuiAppInfo appInfo, BasicModuleLoader moduleLoader, DataFrame m)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, IOException,
      UnsupportedLookAndFeelException {
    // AppService.getInstance().setAppName("matcalc");

    setup(moduleLoader);

    MainMatCalcWindow window = new MainMatCalcWindow(appInfo, m);

    window.setVisible(true);

    return window;
  }

  public static MainMatCalcWindow main(GuiAppInfo appInfo, BasicModuleLoader moduleLoader, MatCalcProps props)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, IOException,
      UnsupportedLookAndFeelException {
    // AppService.getInstance().setAppName("matcalc");

    setup(moduleLoader);

    MainMatCalcWindow window = new MainMatCalcWindow(appInfo, props);

    window.setVisible(true);

    return window;
  }

  public static MainMatCalcWindow main(GuiAppInfo appInfo, MatCalcProps props)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, IOException,
      UnsupportedLookAndFeelException {
    // AppService.getInstance().setAppName("matcalc");

    MainMatCalcWindow window = new MainMatCalcWindow(appInfo, props);

    window.setVisible(true);

    return window;
  }

  public static void main(GuiAppInfo appInfo, String args[])
      throws InstantiationException, IllegalAccessException, IOException {
    String[] modArgs = new String[args.length - 1];
    SysUtils.arraycopy(args, 1, modArgs);

    String modName = args[0];

    ModuleService.getInstance().instance(modName).run(modArgs);
  }
}
