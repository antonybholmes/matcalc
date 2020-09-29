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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.heatmap.ColorNormalization;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeCheckButton;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allows user to select a how the colors are standardized on a heat map.
 *
 * @author Antony Holmes
 */
public class ColorStandardizationRibbonSection extends RibbonSection implements ModernClickListener, ChangeListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member model.
   */
  private ColorNormalizationModel mModel;

  /**
   * The member matrix button.
   */
  private RibbonLargeCheckButton mMatrixButton;

  /**
   * The member row button.
   */
  private RibbonLargeCheckButton mRowButton;

  /**
   * The member column button.
   */
  private RibbonLargeCheckButton mColumnButton;

  /**
   * The member none button.
   */
  private RibbonLargeCheckButton mNoneButton;

  /**
   * The member none button.
   */
  private RibbonLargeCheckButton mNormButton;

  private ModernWindow mParent;

  /**
   * Instantiates a new color standardization ribbon section.
   *
   * @param ribbon the ribbon
   * @param model  the model
   */
  public ColorStandardizationRibbonSection(ModernWindow parent, Ribbon ribbon, ColorNormalizationModel model) {
    super(ribbon, "Normalization");

    mParent = parent;

    mModel = model;

    mModel.addChangeListener(this);

    ModernButtonGroup group = new ModernButtonGroup();

    mMatrixButton = new RibbonLargeCheckButton("Z-Score");
    mMatrixButton.setToolTip("Z-Score Matrix", "Z-Score entire matrix.");
    mMatrixButton.addClickListener(this);
    mMatrixButton.setSelected(model.get().getType() == ColorNormalizationType.ZSCORE_MATRIX);
    group.add(mMatrixButton);
    add(mMatrixButton);

    mRowButton = new RibbonLargeCheckButton("Row Z-Score"); // new
                                                            // Raster32Icon(new
                                                            // TableRowVectorIcon()),
                                                            // "Row");
    mRowButton.setToolTip("Row Z-Score", "Z-Score matrix row wise.");
    mRowButton.addClickListener(this);
    mRowButton.setSelected(model.get().getType() == ColorNormalizationType.ZSCORE_ROW);
    group.add(mRowButton);
    add(mRowButton);

    mColumnButton = new RibbonLargeCheckButton("Column Z-Score"); // new
                                                                  // Raster32Icon(new
                                                                  // TableColumnVectorIcon()),
                                                                  // "Column");
    mColumnButton.setToolTip("Column Z-Score", "Z-Score matrix column wise.");
    mColumnButton.addClickListener(this);
    mColumnButton.setSelected(model.get().getType() == ColorNormalizationType.ZSCORE_COLUMN);
    group.add(mColumnButton);
    add(mColumnButton);

    mNormButton = new RibbonLargeCheckButton("Normalize"); // new
                                                           // Raster32Icon(new
                                                           // TableVectorIcon()),
                                                           // "Normalize");
    mNormButton.setToolTip("Normalize", "Normalize matrix entire matrix.");
    mNormButton.addClickListener(this);
    mNormButton.setSelected(model.get().getType() == ColorNormalizationType.NORMALIZE);
    group.add(mNormButton);
    add(mNormButton);

    mNoneButton = new RibbonLargeCheckButton("None"); // new Raster32Icon(new
                                                      // TableVectorIcon()),
                                                      // "None");
    mNoneButton.setToolTip("No Normalization", "Do not normalize matrix.");
    mNoneButton.addClickListener(this);
    mNoneButton.setSelected(model.get().getType() == ColorNormalizationType.NONE);
    group.add(mNoneButton);
    add(mNoneButton);

    // add(Ui.createHGap(10));
    // add(mCheckBetween);
    // mCheckBetween.addClickListener(this);
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
    if (e.getSource().equals(mMatrixButton)) {
      mModel.set(ColorNormalization.ZSCORE_MATRIX);
    } else if (e.getSource().equals(mRowButton)) {
      mModel.set(ColorNormalization.ZSCORE_ROW);
    } else if (e.getSource().equals(mColumnButton)) {
      mModel.set(ColorNormalization.ZSCORE_COLUMN);
    } else if (e.getSource().equals(mNormButton)) {

      NormalizeDialog dialog = new NormalizeDialog(mParent);

      dialog.setVisible(true);

      if (dialog.getStatus() == ModernDialogStatus.OK) {
        if (dialog.getAuto()) {
          mModel.set(ColorNormalization.NORMALIZE);
        } else {
          mModel.set(new ColorNormalization(ColorNormalizationType.NORMALIZE, dialog.getMin(), dialog.getMax()));
        }
      }
    } else {
      mModel.set(ColorNormalization.NONE);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
   */
  @Override
  public void changed(ChangeEvent e) {
    switch (mModel.get().getType()) {
    case ZSCORE_MATRIX:
      mMatrixButton.setSelected(true);
      break;
    case ZSCORE_ROW:
      mRowButton.setSelected(true);
      break;
    case ZSCORE_COLUMN:
      mColumnButton.setSelected(true);
      break;
    case NORMALIZE:
      mNormButton.setSelected(true);
      break;
    default:
      mNoneButton.setSelected(true);
      break;
    }
  }
}
