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

package edu.columbia.rdf.matcalc.colormap;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapModel;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.matcalc.figure.ScaleSpinner;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ScaleModel;

/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes
 *
 */
public class ColorMapRibbonSection extends RibbonSection {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member model.
   */
  private ColorMapModel mColorMapModel;

  /**
   * The invert button.
   */
  // private CheckBox mInvertButton =
  // new ModernCheckBox("Invert");

  // private RibbonLargeButton mInvertButton =
  // new RibbonLargeButton("Invert", UIService.getInstance().loadIcon("invert",
  // 24));

  private CheckBox mInvertButton = new ModernCheckSwitch("Invert");

  /** The m scale spinner. */
  private ModernCompactSpinner mScaleSpinner;

  /** The m scale model. */
  private ScaleModel mScaleModel;

  /** The m color map button. */
  private RibbonColorMapButton2 mColorMapButton;

  /**
   * Instantiates a new color map ribbon section.
   *
   * @param parent        the parent
   * @param colorMapModel the model
   * @param scaleModel    the scale model
   */
  public ColorMapRibbonSection(ModernRibbonWindow parent, ColorMapModel colorMapModel, ScaleModel scaleModel) {
    super(parent.getRibbon(), "Color Map");

    mColorMapModel = colorMapModel;
    mScaleModel = scaleModel;

    mScaleSpinner = new ScaleSpinner(scaleModel.get());

    mColorMapButton = new RibbonColorMapButton2(parent);

    add(mColorMapButton);

    /*
     * ModernButtonGroup group = new ModernButtonGroup();
     * 
     * ModernComponent choicePanel = new RibbonMultiItemPanel();
     * 
     * mJetButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("jet"))), "Jet");
     * mJetButton.addClickListener(this); choicePanel.add(mJetButton);
     * group.add(mJetButton);
     * 
     * mBlueRedButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("blue_white_red"))),
     * "Blue White Red"); mBlueRedButton.addClickListener(this);
     * choicePanel.add(mBlueRedButton); group.add(mBlueRedButton);
     * 
     * mGreenRedButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("green_black_red"))),
     * "Green Red"); mGreenRedButton.addClickListener(this);
     * choicePanel.add(mGreenRedButton); group.add(mGreenRedButton);
     * 
     * mBlueYellowButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("blue_yellow"))),
     * "Blue Yellow"); mBlueYellowButton.addClickListener(this);
     * choicePanel.add(mBlueYellowButton); group.add(mBlueYellowButton);
     * 
     * mWhiteRedButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("white_red"))), "White Red");
     * mWhiteRedButton.addClickListener(this); choicePanel.add(mWhiteRedButton);
     * group.add(mWhiteRedButton);
     * 
     * mWhiteBlueButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("white_blue"))),
     * "White Blue"); mWhiteBlueButton.addClickListener(this);
     * choicePanel.add(mWhiteBlueButton); group.add(mWhiteBlueButton);
     * 
     * mWhiteGreenButton = new RibbonChoiceButton(new Raster48Icon(new
     * ColorMapIcon(ColorMapService.getInstance().get("white_green"))),
     * "White Green"); mWhiteGreenButton.addClickListener(this);
     * choicePanel.add(mWhiteGreenButton); group.add(mWhiteGreenButton);
     * 
     * add(choicePanel);
     */

    add(UI.createHGap(5));
    add(new ModernAutoSizeLabel("Scale"));
    add(UI.createHGap(5));
    add(mScaleSpinner);
    add(UI.createHGap(5));
    add(mInvertButton);
    // add(box);

    //
    // Events
    //

    mColorMapButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mColorMapModel.set(mColorMapButton.getSelectedColorMap());
      }
    });

    mColorMapModel.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mColorMapButton.setSelectedColorMap(mColorMapModel.get());
      }
    });

    mInvertButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mColorMapModel.set(new ColorMap(mColorMapButton.getSelectedColorMap(), true));
      }
    });

    mScaleSpinner.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mScaleModel.set(mScaleSpinner.getValue()); // Math.pow(2,
                                                   // mScaleSpinner.getValue()));
      }
    });

    mScaleModel.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mScaleSpinner.updateValue(mScaleModel.get());
      }
    });

    mColorMapButton.setSelectedColorMap(colorMapModel.get());
  }
}
