/**
 * Copyright (C) 2016, Antony Holmes
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
package edu.columbia.rdf.matcalc.groups;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.Box;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.Join;
import org.jebtk.core.text.RegexUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.ui.external.microsoft.ExcelDialog;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.input.ModernInputExtPanel;
import org.jebtk.modern.input.ModernInputPanel;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allows a matrix group to be edited.
 *
 * @author Antony Holmes
 */
public class XYSeriesDialog extends ModernDialogHelpWindow {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The Constant HELP_URL. */
  private static final String HELP_URL = "matcalc.groups.help.url";

  /**
   * The member regex check.
   */
  private CheckBox mRegexCheck = new ModernCheckSwitch("Regular expression");

  /** The m check case sensitive. */
  private CheckBox mCheckCaseSensitive = new ModernCheckSwitch(
      "Case sensitive");

  /**
   * The member color button.
   */
  private ColorSwatchButton mColorButton;

  /**
   * The member name field.
   */
  private ModernInputPanel mNameField = new ModernInputPanel("Group");

  /**
   * The member search field.
   */
  private ModernInputExtPanel mSearchField;

  /**
   * The member group.
   */
  private XYSeries mGroup;

  /**
   * The member load button.
   */
  private ModernButton mLoadButton = new RibbonButton(
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

  /**
   * The class KeyEvents.
   */
  private class KeyEvents implements KeyListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent arg0) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent arg0) {
      setTitle("Group Editor", mNameField.getText());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent arg0) {

    }
  }

  /**
   * Instantiates a new XY series dialog.
   *
   * @param parent the parent
   */
  public XYSeriesDialog(ModernWindow parent) {
    this(parent, new XYSeries("New Group", ColorUtils.randomColor()));
  }

  /**
   * Instantiates a new XY series dialog.
   *
   * @param parent the parent
   * @param names the names
   */
  public XYSeriesDialog(ModernWindow parent, List<String> names) {
    super(parent, HELP_URL);

    mGroup = new XYSeries("New Group", ColorUtils.randomColor());

    setup(names);

    createUi();
  }

  /**
   * Instantiates a new XY series dialog.
   *
   * @param parent the parent
   * @param group the group
   */
  public XYSeriesDialog(ModernWindow parent, XYSeries group) {
    super(parent, HELP_URL);

    mGroup = group;

    mCheckCaseSensitive.setSelected(mGroup.getCaseSensitive());

    setup(CollectionUtils.toString(mGroup));

    createUi();
  }

  /**
   * Setup.
   *
   * @param names the new up
   */
  private void setup(List<String> names) {
    mSearchField = new ModernInputExtPanel(mParent, "", ",");

    mNameField.setText(mGroup.getName());
    mSearchField.setText(TextUtils.join(names, TextUtils.COMMA_DELIMITER));

    setTitle("Group Editor", mGroup.getName());

    mNameField.addKeyListener(new KeyEvents());

    mLoadButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        try {
          load();
        } catch (InvalidFormatException e1) {
          e1.printStackTrace();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });

    setSize(520, 300);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    mColorButton = new ColorSwatchButton(getParentWindow(), mGroup.getColor());

    Box box = VBox.create();

    Box box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Name", 100));
    
    UI.setSize(mNameField, 300, ModernWidget.WIDGET_HEIGHT);
    box2.add(mNameField);
    box.add(box2);
    box.add(UI.createVGap(5));
    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Search", 100));

    UI.setSize(mSearchField, 300, ModernWidget.WIDGET_HEIGHT);
    box2.add(mSearchField); // new ModernTextBorderPanel(mSearchField, 400));
    box2.add(UI.createHGap(5));
    box2.add(mLoadButton);
    box.add(box2);
    box.add(UI.createVGap(5));
    box2 = HBox.create();
    box2.add(UI.createHGap(100));
    box2.add(mRegexCheck);
    box.add(box2);
    box2 = HBox.create();
    box2.add(UI.createHGap(100));
    box2.add(mCheckCaseSensitive);
    box.add(box2);
    box.add(UI.createVGap(5));
    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Color", 100));
    box2.add(mColorButton);
    box.add(box2);

    // JPanel buttonPanel = new Panel(new FlowLayout(FlowLayout.LEFT));

    // importButton.setCanvasSize(new Dimension(100,
    // ModernTheme.getInstance().getClass("widget").getInt("height")));
    // exportButton.setCanvasSize(new Dimension(100,
    // ModernTheme.getInstance().getClass("widget").getInt("height")));

    // buttonPanel.add(importButton);
    // buttonPanel.add(exportButton);

    // panel.add(buttonPanel, BorderLayout.PAGE_END);

    setCard(box);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * ui. event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {
      editGroup();
    }

    super.clicked(e);
  }

  /**
   * Edits the group.
   */
  private void editGroup() {
    String search = mSearchField.getText();

    List<String> terms = TextUtils.commaSplit(search);

    List<Pattern> regexes = new ArrayList<Pattern>(terms.size());

    for (String t : terms) {
      Pattern regex;

      if (mRegexCheck.isSelected()) {
        regex = RegexUtils.compile(t, mCheckCaseSensitive.isSelected());
      } else {
        regex = RegexUtils.literal(t, mCheckCaseSensitive.isSelected()); // =
                                                                          // ".*"
                                                                          // + t
                                                                          // +
                                                                          // ".*";
      }

      regexes.add(regex);
    }

    System.err
        .println("change group g " + mNameField.getText() + " " + regexes);

    mGroup.setName(mNameField.getText());
    mGroup.setColor(mColorButton.getSelectedColor());
    mGroup.setRegexes(regexes);
    mGroup.setCaseSensitive(mCheckCaseSensitive.isSelected());
    System.err.println("after " + mGroup.getName());
  }

  /**
   * Load.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws InvalidFormatException the invalid format exception
   */
  private void load() throws IOException, InvalidFormatException {
    Path file = ExcelDialog.open(getParentWindow()).xlsx()
        .getFile(RecentFilesService.getInstance().getPwd());

    if (file == null) {
      return;
    }

    String[] ids = Excel.getTextFromFile(file, true);

    String text = Join.onComma().values(ids).toString();

    text = text.replaceAll("\\(", "\\\\(");
    text = text.replaceAll("\\)", "\\\\)");

    mSearchField.setText(text);
  }

  /**
   * Gets the group.
   *
   * @return the group
   */
  public XYSeries getGroup() {
    return mGroup;
  }
}
