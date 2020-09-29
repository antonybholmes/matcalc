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
 * The Class DoesNotContainFilter.
 */
public class DoesNotContainFilter implements Filter {

  /** The m text. */
  private String mText;

  /**
   * Instantiates a new does not contain filter.
   *
   * @param text the text
   */
  public DoesNotContainFilter(String text) {
    mText = text.toLowerCase();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.filter.Filter#test(java.lang.String, double)
   */
  @Override
  public boolean test(String text, double value) {
    if (text != null) {
      return !text.toLowerCase().contains(mText);
    } else {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.filter.Filter#getName()
   */
  @Override
  public String getName() {
    return "Does not contain";
  }

}
