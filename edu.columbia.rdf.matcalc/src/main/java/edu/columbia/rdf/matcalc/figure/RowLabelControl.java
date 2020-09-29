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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.jebtk.graphplot.figure.heatmap.legacy.AnnotationProps;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelPosition;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class RowLabelControl.
 */
public class RowLabelControl extends VBox implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member show element.
   */
  private ColoredPlotControl mShowElement;

  /**
   * The member position element.
   */
  private RowLabelPositionControl mPositionElement;

  /**
   * The member annotation map.
   */
  private Map<String, ModernTwoStateWidget> mAnnotationMap = new HashMap<String, ModernTwoStateWidget>();

  /** The m check features. */
  private ModernTwoStateWidget mCheckFeatures;

  /**
   * Instantiates a new row label control.
   *
   * @param parent           the parent
   * @param matrix           the matrix
   * @param rowLabelPosition the row label position
   */
  public RowLabelControl(ModernWindow parent, DataFrame matrix, RowLabelPosition rowLabelPosition) {
    this(parent, matrix, rowLabelPosition, true, true);
  }

  /**
   * Instantiates a new row label control.
   *
   * @param parent           the parent
   * @param matrix           the matrix
   * @param rowLabelPosition the row label position
   * @param show             the show
   */
  public RowLabelControl(ModernWindow parent, DataFrame matrix, RowLabelPosition rowLabelPosition,
      boolean featureCounts, boolean show) {

    mCheckFeatures = new ModernCheckSwitch("Feature Count", featureCounts);
    add(mCheckFeatures);

    add(UI.createVGap(5));

    mShowElement = new ColoredPlotControl(parent, "Show", Color.BLACK, show);

    add(mShowElement);

    add(UI.createVGap(5));

    for (String name : matrix.getIndex().getNames()) {
      ModernTwoStateWidget checkAnnotation = new ModernCheckSwitch(name, true);

      add(checkAnnotation);
      add(UI.createVGap(5));

      mAnnotationMap.put(name, checkAnnotation);
    }

    // box.setBorder(BorderService.getInstance().createLeftBorder(10));

    // add(box);

    add(UI.createVGap(10));

    mPositionElement = new RowLabelPositionControl(rowLabelPosition);

    // mPositionElement.setBorder(BorderService.getInstance().createLeftBorder(10));

    add(mPositionElement);

    mShowElement.addClickListener(this);
    mCheckFeatures.addClickListener(this);

    update();
  }

  /**
   * Adds the click listener.
   *
   * @param l the l
   */
  public void addClickListener(ModernClickListener l) {
    mShowElement.addClickListener(l);
    mCheckFeatures.addClickListener(l);
    mPositionElement.addClickListener(l);

    for (ModernTwoStateWidget c : mAnnotationMap.values()) {
      c.addClickListener(l);
    }
  }

  /**
   * Gets the position.
   *
   * @return the position
   */
  public RowLabelPosition getPosition() {
    return mPositionElement.getPosition();
  }

  /**
   * Gets the selected color.
   *
   * @return the selected color
   */
  public Color getSelectedColor() {
    return mShowElement.getSelectedColor();
  }

  /**
   * Checks if is selected.
   *
   * @return true, if is selected
   */
  public boolean isSelected() {
    return mShowElement.isSelected();
  }

  /**
   * Gets the show annotation.
   *
   * @param name the name
   * @return the show annotation
   */
  public boolean getShowAnnotation(String name) {
    return mAnnotationMap.get(name).isSelected();
  }

  /**
   * Gets the show feature count.
   *
   * @return the show feature count
   */
  public boolean getShowFeatureCount() {
    return mCheckFeatures.isSelected();
  }

  /**
   * Determines which annotations to show.
   *
   * @param showAnnotations the show annotations
   */
  public void setShowAnnotations(AnnotationProps showAnnotations) {
    for (String name : mAnnotationMap.keySet()) {
      showAnnotations.set(name, mAnnotationMap.get(name).isSelected());
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
    update();
  }

  /**
   * Update.
   */
  public void update() {
    // mCheckFeatures.setEnabled(mShowElement.isSelected());

    for (ModernTwoStateWidget c : mAnnotationMap.values()) {
      c.setEnabled(mShowElement.isSelected());
    }
  }
}
