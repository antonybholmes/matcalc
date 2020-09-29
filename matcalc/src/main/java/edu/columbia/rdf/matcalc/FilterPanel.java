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
package edu.columbia.rdf.matcalc;

import java.text.ParseException;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.panel.VBox;

/**
 * For choosing an FDR method.
 *
 * @author Antony Holmes
 */
public class FilterPanel extends VBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member check pos z.
   */
  private ModernTwoStateWidget mCheckPosZ = new ModernCheckSwitch("Positive z-scores", true);

  /**
   * The member check neg z.
   */
  private ModernTwoStateWidget mCheckNegZ = new ModernCheckSwitch("Negative z-scores", true);

  /**
   * The member min zscore.
   */
  private OptionalEntry mMinZscore = new OptionalEntry("Minimum absolute z-score", 1.0, false);

  /**
   * The member min fold.
   */
  private OptionalEntry mMinFold = new OptionalEntry("Minimum fold change", 1.0, false);

  /** The m top genes. */
  private OptionalEntry mTopGenes = new OptionalEntry("Show top genes only", 10, false);

  /**
   * Instantiates a new filter panel.
   */
  public FilterPanel() {
    add(mMinZscore);
    add(UI.createVGap(5));
    add(mCheckPosZ);
    add(UI.createVGap(5));
    add(mCheckNegZ);
    add(UI.createVGap(5));
    add(mMinFold);
    add(UI.createVGap(5));
    add(mTopGenes);
  }

  /**
   * Gets the min zscore.
   *
   * @return the min zscore
   */
  public double getMinZscore() {
    if (mMinZscore.getSelected()) {
      try {
        return mMinZscore.getValue();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    return 0;
  }

  /**
   * Gets the min fold change.
   *
   * @return the min fold change
   */
  public double getMinFoldChange() {
    if (mMinFold.getSelected()) {
      try {
        return mMinFold.getValue();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    return 0;
  }

  /**
   * Gets the top genes.
   *
   * @return the top genes
   */
  public int getTopGenes() {
    if (mTopGenes.getSelected()) {
      try {
        return mTopGenes.getIntValue();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    return -1;
  }

  /**
   * Gets the pos z.
   *
   * @return the pos z
   */
  public boolean getPosZ() {
    return mCheckPosZ.isSelected();
  }

  /**
   * Gets the neg z.
   *
   * @return the neg z
   */
  public boolean getNegZ() {
    return mCheckNegZ.isSelected();
  }
}
