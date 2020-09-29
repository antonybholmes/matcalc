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
package edu.columbia.rdf.matcalc.toolbox.core.filter.column;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.FilterVectorIcon;
import org.jebtk.modern.graphics.icons.Raster24Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.theme.ThemeService;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Row name.
 *
 * @author Antony Holmes
 */
public class ColumnFilterModule extends Module implements ModernClickListener {

  /**
   * The button.
   */
  private RibbonLargeButton mButton = new RibbonLargeButton(
      new Raster24Icon(new FilterVectorIcon(ThemeService.getInstance().getColors().getGray(8),
          ThemeService.getInstance().getColors().getGray(6))),
      "Filter Columns", "Filter columns.");

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
    return "Filter Columns";
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

    mButton.addClickListener(this);

    mWindow.getRibbon().getToolbar("Data").getSection("Filter").add(mButton);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    order();
  }

  /**
   * Order.
   */
  private void order() {
    DataFrame m = mWindow.getCurrentMatrix();

    ColumnFilterDialog dialog = new ColumnFilterDialog(mWindow, m);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    DataFrame ret = DataFrame.copyInnerColumns(m, dialog.getColumns());

    mWindow.history().addToHistory("Filter columns", ret);
  }
}
