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

/**
 * The Class EqualsFilter.
 */
public class EqualsFilter implements Filter {

  /** The m V. */
  private double mV;

  /**
   * Instantiates a new equals filter.
   *
   * @param v the v
   */
  public EqualsFilter(double v) {
    mV = v;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.filter.Filter#test(java.lang.String, double)
   */
  @Override
  public boolean test(String text, double value) {
    return value == mV;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.filter.Filter#getName()
   */
  @Override
  public String getName() {
    return "Equals";
  }

}
