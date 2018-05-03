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
package edu.columbia.rdf.matcalc.toolbox.core.collapse;

/**
 * The enum CollapseType.
 */
public enum CollapseType {

  /**
   * The none.
   */
  NONE,

  /**
   * The max.
   */
  MAX,

  /**
   * The min.
   */
  MIN,

  /**
   * The max stdev.
   */
  MAX_STDEV,

  /**
   * The max mean.
   */
  MAX_MEAN,

  /**
   * The max median.
   */
  MAX_MEDIAN,

  /**
   * The max tstat.
   */
  MAX_TSTAT,

  /**
   * The max interquartile range.
   */
  MAX_IQR,

  /** The max quart coeff disp. */
  MAX_QUART_COEFF_DISP
}
