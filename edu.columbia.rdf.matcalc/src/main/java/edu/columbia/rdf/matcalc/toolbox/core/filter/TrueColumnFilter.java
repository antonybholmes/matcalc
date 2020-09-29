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
package edu.columbia.rdf.matcalc.toolbox.core.filter;

import org.jebtk.math.matrix.DataFrame;

/**
 * The Class TrueColumnFilter.
 */
public class TrueColumnFilter extends ColumnFilter {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new true column filter.
   *
   * @param m       the m
   * @param isFirst the is first
   */
  public TrueColumnFilter(DataFrame m, boolean isFirst) {
    super(m, isFirst);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.filter.ColumnFilter#getFilter()
   */
  @Override
  public Filter getFilter() {
    return TrueFilter.INSTANCE;
  }

}
