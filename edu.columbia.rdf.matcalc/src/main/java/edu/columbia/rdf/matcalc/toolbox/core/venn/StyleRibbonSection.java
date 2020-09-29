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
package edu.columbia.rdf.matcalc.toolbox.core.venn;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeRadioButton;
import org.jebtk.modern.ribbon.RibbonSection;

/**
 * The Class StyleRibbonSection.
 */
public class StyleRibbonSection extends RibbonSection implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m proportional button. */
  private ModernCheckButton mProportionalButton = new RibbonLargeRadioButton("Proportional",
      AssetService.getInstance().loadIcon("proportional", 24), "Proportional",
      "Draw circles proportional to their group size.", Ribbon.DEFAULT_BUTTON_SIZE);

  /** The m uniform button. */
  private ModernCheckButton mUniformButton = new RibbonLargeRadioButton("Uniform",
      AssetService.getInstance().loadIcon("uniform", 24), "Uniform", "Draw all circles the same size.",
      Ribbon.DEFAULT_BUTTON_SIZE);

  /** The m model. */
  private StyleModel mModel;

  /**
   * The Class ModelEvents.
   */
  private class ModelEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.
     * ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      update();
    }

  }

  /**
   * Instantiates a new style ribbon section.
   *
   * @param ribbon the ribbon
   * @param model  the model
   */
  public StyleRibbonSection(Ribbon ribbon, StyleModel model) {
    super(ribbon, "Style");

    mModel = model;

    mModel.addChangeListener(new ModelEvents());

    // Box box = new RibbonStripContainer2();

    add(mUniformButton);
    add(mProportionalButton);

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mUniformButton);
    group.add(mProportionalButton);

    mUniformButton.addClickListener(this);
    mProportionalButton.addClickListener(this);

    update();
  }

  /**
   * Update.
   */
  private void update() {
    switch (mModel.get()) {
    case UNIFORM:
      mUniformButton.setSelected(true);
      break;
    default:
      mProportionalButton.setSelected(true);
      break;
    }
  }

  /**
   * Change style.
   *
   * @param e the e
   */
  private void changeStyle(ModernClickEvent e) {
    if (mUniformButton.isSelected()) {
      mModel.set(CircleStyle.UNIFORM);
    } else {
      mModel.set(CircleStyle.PROPORTIONAL);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    changeStyle(e);
  }
}
