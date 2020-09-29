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
package edu.columbia.rdf.matcalc.toolbox.core.sort;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBoxAutoWidth;

/**
 * The Class ColumnSorters.
 */
public class ColumnSorters extends VBoxAutoWidth {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m M. */
  private DataFrame mM;

  /**
   * Instantiates a new column sorters.
   *
   * @param m the m
   */
  public ColumnSorters(DataFrame m) {
    mM = m;

    setBorder(BorderService.getInstance().createBorder(5));
  }

  /**
   * Adds the.
   *
   * @param column the column
   */
  public void add(int column) {
    ColumnSort sort;

    if (getComponentCount() == 0) {
      sort = new ColumnSort(mM, "Sort by", column);
      sort.disableDelete();
    } else {
      sort = new ColumnSort(mM, "Then by", column);
    }

    sort.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        remove((Component) e.getSource());
        revalidate();
        repaint();
      }
    });

    add(sort);

  }

  /**
   * Clear.
   */
  public void clear() {
    removeAll();

    add(-1);

    revalidate();
    repaint();
  }

  /**
   * Gets the sorters.
   *
   * @return the sorters
   */
  public List<ColumnSort> getSorters() {
    List<ColumnSort> sorters = new ArrayList<ColumnSort>();

    for (int i = 0; i < getComponentCount(); ++i) {
      Component c = getComponent(i);

      if (c instanceof ColumnSort) {
        sorters.add((ColumnSort) c);
      }
    }

    return sorters;
  }
}
