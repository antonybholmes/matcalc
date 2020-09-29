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

import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.graphics.color.ColorPopupMenu;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapBlockPicker;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernScrollPopupMenu2;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class ColorPopupMenu.
 */
public class ColorMapPopupMenu2 extends ModernScrollPopupMenu2 {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m color map. */
  private ColorMap mColorMap = null;

  /** The m color map picker. */
  private ColorMapBlockPicker mColorMapPicker;

  /** The m parent. */
  private ModernWindow mParent;

  /**
   * Instantiates a new color popup menu.
   *
   * @param parent the parent
   */
  public ColorMapPopupMenu2(ModernWindow parent) {
    mParent = parent;

    setBackground(ColorPopupMenu.COLOR_PICKER_BACKGROUND);

    // setBorder(BorderFactory.createCompoundBorder(ModernWidget.LINE_BORDER,
    // BorderService.getInstance().createBorder(4)));

    add(new ModernLabel("Color Maps"));

    mColorMapPicker = new ColorMapBlockPicker(5); // new ColorMapPicker(4);

    add(mColorMapPicker);

    // addScrollMenuItem(new
    // ColorMapMenuItem(ColorMap.createWhiteToColorMap("red",
    // Color.RED), "Red"));
    // addScrollMenuItem(new
    // ColorMapMenuItem(ColorMap.createWhiteToColorMap("green", Color.GREEN),
    // "Green"));
    // addScrollMenuItem(new
    // ColorMapMenuItem(ColorMap.createWhiteToColorMap("blue",
    // Color.BLUE), "Blue"));
    // addScrollMenuItem(new
    // ColorMapMenuItem(ColorMap.createWhiteToColorMap("orange", Color.ORANGE),
    // "Orange"));
    // addScrollMenuItem(new
    // ColorMapMenuItem(ColorMap.createWhiteToColorMap("pink",
    // Color.PINK), "Pink"));

    // add(new ModernMenuDivider());

    add(new ModernIconMenuItem("More Color Maps...", AssetService.getInstance().loadIcon("color_wheel", 16)));

    update();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.ui.modern.menu.ModernPopup#clicked(org.abh.lib.ui.modern.event.
   * ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mColorMapPicker)) {
      mColorMap = mColorMapPicker.getSelectedColorMap();

      super.clicked(e);
    } else if (e.getMessage().equals("More Color Maps...")) {
      setVisible(false);

      ColorMapsDialog dialog = new ColorMapsDialog(mParent, mColorMap);

      dialog.setVisible(true);

      if (dialog.getStatus() == ModernDialogStatus.OK) {
        mColorMap = dialog.getColorMap();

        ColorMapService.getInstance().add(mColorMap);

        update();
      }

      super.clicked(e);
    }
  }

  /**
   * Update.
   */
  private void update() {
    /*
     * List<ColorMap> colorMaps = new ArrayList<ColorMap>();
     * 
     * colorMaps.add(ColorMapService.getInstance().get("Jet"));
     * colorMaps.add(ColorMapService.getInstance().get("Hot"));
     * colorMaps.add(ColorMapService.getInstance().get("Cool"));
     * colorMaps.add(ColorMapService.getInstance().get("Spring"));
     * colorMaps.add(ColorMapService.getInstance().get("Summer"));
     * colorMaps.add(ColorMapService.getInstance().get("Autumn"));
     * colorMaps.add(ColorMapService.getInstance().get("Winter"));
     * colorMaps.add(ColorMapService.getInstance().get("Gray"));
     * colorMaps.add(ColorMapService.getInstance().get("Blue White Red"));
     * colorMaps.add(ColorMapService.getInstance().get("Green White Red"));
     * colorMaps.add(ColorMapService.getInstance().get("Green Black Red"));
     * colorMaps.add(ColorMapService.getInstance().get("Blue Yellow"));
     * colorMaps.add(ColorMapService.getInstance().get("White Red"));
     * colorMaps.add(ColorMapService.getInstance().get("White Green"));
     * colorMaps.add(ColorMapService.getInstance().get("White Blue"));
     * colorMaps.add(ColorMap.createWhiteToColorMap("White Purple",
     * ColorMap.PURPLE));
     * colorMaps.add(ColorMap.createWhiteToColorMap("White Orange",
     * ColorMap.ORANGE));
     * colorMaps.add(ColorMap.createWhiteToColorMap("White Yellow",
     * ColorMap.YELLOW)); colorMaps.add(ColorMap.createWhiteToColorMap("White Pink",
     * ColorMap.PINK));
     * 
     * // Add anything the user created
     * colorMaps.addAll(ColorMapService.getInstance().getUserMaps().toList());
     */

    mColorMapPicker.update(ColorMapService.getInstance().toList()); // colorMaps);
  }

  /**
   * Gets the selected color.
   *
   * @return the selected color
   */
  public ColorMap getSelectedColorMap() {
    return mColorMap;
  }
}
