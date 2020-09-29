/**
false * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.matcalc.toolbox.core;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.help.ModernMenuHelpItem;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Merges designated segments together using the merge column. Consecutive rows
 * with the same merge id will be merged together. Coordinates and copy number
 * will be adjusted but genes, cytobands etc are not.
 *
 * @author Antony Holmes
 *
 */
public class ExtractDataModule extends Module implements ModernClickListener {

  /** The Constant ICON. */
  private static final ModernIcon ICON = AssetService.getInstance().loadIcon("extract", 24);

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /** The m button. */
  private RibbonLargeDropDownButton2 mButton;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Extract Data";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    // mStvDevButton.addClickListener(this);
    // mWindow.getRibbon().getToolbar("Bioinformatics").getSection("Expression").add(mStvDevButton);
    // mMinExpButton.addClickListener(this);
    // mWindow.getRibbon().getToolbar("Bioinformatics").getSection("Expression").add(mMinExpButton);

    ModernPopupMenu2 popup = new ModernPopupMenu2();

    popup.addMenuItem(new ModernTwoLineMenuItem("Numerical", "Extract numerical data from matrix.", ICON));
    popup.addMenuItem(new ModernTwoLineMenuItem("Text", "Extract text data from matrix.", ICON));

    // popup.addMenuItem(new ModernMenuSeparator());

    popup.addMenuItem(
        new ModernMenuHelpItem("Help with extracting numbers or text...", "matcalc.data-extraction.help.url")
            .setTextOffset(48));

    mButton = new RibbonLargeDropDownButton2("Extract", ICON, popup);
    mButton.setChangeText(false);
    mButton.setToolTip("Extract Data", "Extract numbers or text from a matrix.");

    mWindow.getRibbon().getToolbar("Data").getSection("Tools").add(mButton);

    mButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getMessage().equals("Numerical")) {
      mWindow.history().addToHistory("Extract numerical data", extractNumerical());
    } else if (e.getMessage().equals("Text")) {
      mWindow.history().addToHistory("Extract text data", extractText());
    } else {
      // Do nothing
    }
  }

  /**
   * Extract numerical.
   *
   * @return the annotation matrix
   */
  private DataFrame extractNumerical() {
    DataFrame m = mWindow.getCurrentMatrix();

    return m.extractNumbers();
  }

  /**
   * Extract text.
   *
   * @return the annotation matrix
   */
  private DataFrame extractText() {
    DataFrame m = mWindow.getCurrentMatrix();

    return m.extractText();
  }
}
