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

import org.jebtk.core.search.SearchStackOperator;

/**
 * The Class FilterStackElement.
 */
public class FilterStackElement {

  /** The op. */
  public SearchStackOperator op = SearchStackOperator.RESULT;

  /** The result. */
  public boolean result = true;

  /** The filter. */
  public ColumnFilter filter = null;

  /**
   * Instantiates a new filter stack element.
   *
   * @param result the result
   */
  public FilterStackElement(boolean result) {
    this.op = SearchStackOperator.RESULT;
    this.result = result;
  }

  /**
   * Instantiates a new filter stack element.
   *
   * @param filter the filter
   */
  public FilterStackElement(ColumnFilter filter) {
    this.filter = filter;
    this.op = SearchStackOperator.MATCH;
  }

  /**
   * Instantiates a new filter stack element.
   *
   * @param op the op
   */
  public FilterStackElement(SearchStackOperator op) {
    this.op = op;
  }
}
