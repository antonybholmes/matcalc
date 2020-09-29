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
package edu.columbia.rdf.matcalc.toolbox.core.venn;

import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.jebtk.modern.theme.ThemeService;
import org.xml.sax.SAXException;

/**
 * The Class MainVenn.
 */
public class MainVenn {

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws FontFormatException             the font format exception
   * @throws IOException                     Signals that an I/O exception has
   *                                         occurred.
   * @throws SAXException                    the SAX exception
   * @throws ParserConfigurationException    the parser configuration exception
   * @throws ClassNotFoundException          the class not found exception
   * @throws InstantiationException          the instantiation exception
   * @throws IllegalAccessException          the illegal access exception
   * @throws UnsupportedLookAndFeelException the unsupported look and feel
   *                                         exception
   */
  public static final void main(String[] args)
      throws FontFormatException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    ThemeService.getInstance().setTheme();

    main();
  }

  /**
   * Main.
   *
   * @throws SAXException                 the SAX exception
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static void main() throws SAXException, IOException, ParserConfigurationException {
    // VennSplashScreen window = new VennSplashScreen(new VennInfo());

    // window.setVisible(true);

    MainVennWindow window = new MainVennWindow();

    window.setVisible(true);
  }
}
