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
package edu.columbia.rdf.matcalc.figure;

import java.util.HashMap;
import java.util.Map;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.modern.button.ModernClickWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;

/**
 * Allows users to select which groups are displayed.
 *
 * @author Antony Holmes
 */
public class MatrixGroupControl extends VBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member groups model.
   */
  private XYSeriesModel mGroupsModel;

  /**
   * The button map.
   */
  private Map<ModernClickWidget, XYSeries> mButtonMap = new HashMap<ModernClickWidget, XYSeries>();

  /**
   * The class ChangeEvents.
   */
  private class ChangeEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      refresh();
    }

  }

  /**
   * Instantiates a new matrix group ribbon section.
   *
   * @param groupsModel the groups model
   */
  public MatrixGroupControl(XYSeriesModel groupsModel) {
    mGroupsModel = groupsModel;

    groupsModel.addChangeListener(new ChangeEvents());

    ModernClickWidget button;

    for (XYSeries series : groupsModel) {
      button = new MatrixGroupCheckSwitch(series, groupsModel.getVisible(series));
      button.addClickListener(this);
      add(button);

      mButtonMap.put(button, series);
    }
  }

  /**
   * Refresh.
   */
  private void refresh() {
    for (ModernClickWidget button : mButtonMap.keySet()) {
      button.setSelected(mGroupsModel.getVisible(mButtonMap.get(button)));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui. ui.
   * event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    ModernClickWidget button = (ModernClickWidget) e.getSource();

    XYSeries series = mButtonMap.get(button);

    mGroupsModel.setVisible(series, button.isSelected());
  }
}
