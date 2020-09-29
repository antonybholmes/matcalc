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
package edu.columbia.rdf.matcalc.toolbox.core.roworder;

import java.util.List;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.icons.OrderRows32VectorIcon;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Row name.
 *
 * @author Antony Holmes
 */
public class RowOrderModule extends Module implements ModernClickListener {

  /**
   * The button.
   */
  private RibbonLargeButton button = new RibbonLargeButton(new Raster32Icon(new OrderRows32VectorIcon()), "Row Order",
      "Order rows.");

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Row Order";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    button.addClickListener(this);
    mWindow.getRibbon().getToolbar("Data").getSection("Sort").add(button);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    filter();
  }

  /**
   * Filter.
   */
  public void filter() {
    DataFrame m = mWindow.getCurrentMatrix();

    RowOrderDialog dialog = new RowOrderDialog(mWindow, m);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<Integer> rows = dialog.getRows();

    DataFrame ret = DataFrame.copyRows(m, rows);

    mWindow.history().addToHistory("Row order", ret);
  }
}
