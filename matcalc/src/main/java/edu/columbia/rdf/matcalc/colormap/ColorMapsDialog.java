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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Box;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernDialogWindow;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapService;
import org.jebtk.modern.graphics.colormap.ColorMapTable;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.panel.CardPanel;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class ColorDialog.
 */
public class ColorMapsDialog extends ModernDialogHelpWindow implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The m table. */
  private ColorMapTable mTable = new ColorMapTable();

  // private ModernButton mSelectAllButton =
  // new ModernButton("Select All");

  /** The m add button. */
  private ModernButton mAddButton = new ModernButton("New...",
      AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /** The m delete button. */
  private ModernButton mDeleteButton = new ModernButton(UI.MENU_DELETE,
      AssetService.getInstance().loadIcon("delete", 16));

  /** The m color map. */
  private ColorMap mColorMap;

  /**
   * Instantiates a new color dialog.
   *
   * @param parent   the parent
   * @param colorMap the color map
   */
  public ColorMapsDialog(ModernWindow parent, ColorMap colorMap) {
    super(parent, "matcalc.color-maps.help.url");

    mColorMap = colorMap;

    setup();
  }

  /**
   * Sets the up.
   */
  private void setup() {
    setResizable(true);
    setBackground(ModernDialogWindow.DIALOG_BACKGROUND);
    setSize(640, 480);
    setTitle("Color Maps");

    ModernComponent box1 = new ModernComponent();

    Box box2 = HBox.create();

    box2.add(mAddButton);
    box2.add(UI.createHGap(5));
    box2.add(mDeleteButton);
    box2.setBorder(BorderService.getInstance().createBottomBorder(10));

    box1.setHeader(box2);

    ModernScrollPane scrollPane = new ModernScrollPane(mTable);

    // UI.setSize(scrollPane, 500, 200);

    box1.setBody(scrollPane);

    setContent(new CardPanel(new ModernComponent(box1, ModernWidget.QUAD_BORDER)));

    mAddButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        add();
      }
    });

    mDeleteButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        delete();
      }
    });

    mTable.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          edit();
        }
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

      }
    });

    UI.centerWindowToScreen(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {
      mColorMap = (ColorMap) mTable.getValueAt(mTable.getSelectedRow(), 0);

      if (mColorMap == null) {
        ModernMessageDialog.createWarningDialog(mParent, "You must select a color map.");

        return;
      }
    }

    super.clicked(e);
  }

  /**
   * Delete.
   */
  private void delete() {
    List<Integer> indices = CollectionUtils
        .reverse(CollectionUtils.sort(CollectionUtils.toList(mTable.getRowModel().getSelectionModel())));

    if (indices.size() > 0) {
      ModernDialogStatus status = ModernMessageDialog.createDialog(mParent,
          "Are you sure you want to delete the selected color maps?", MessageDialogType.WARNING_OK_CANCEL);

      if (status == ModernDialogStatus.OK) {
        for (int i : indices) {
          ColorMapService.getInstance().remove((String) mTable.getValueAt(i, 1));
        }
      }

    }

  }

  /**
   * Adds the.
   */
  private void add() {
    ColorMapDialog dialog = new ColorMapDialog(mParent);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.OK) {

      ColorMap colorMap = dialog.getColorMap();

      if (!ColorMapService.getInstance().contains(colorMap.getName())) {
        ColorMapService.getInstance().add(dialog.getColorMap());
      } else {
        ModernMessageDialog.createWarningDialog(mParent,
            "'" + colorMap.getName() + "' is already in use. Please choose another name.");
      }
    }
  }

  /**
   * Edits the.
   */
  private void edit() {
    ColorMap colorMap = (ColorMap) mTable.getValueAt(mTable.getSelectedRow(), 0);

    if (colorMap != null) {
      ColorMapDialog dialog = new ColorMapDialog(mParent, colorMap);

      dialog.setVisible(true);

      if (dialog.getStatus() == ModernDialogStatus.OK) {

        colorMap = dialog.getColorMap();

        if (!ColorMapService.getInstance().contains(colorMap.getName())) {
          ColorMapService.getInstance().add(dialog.getColorMap());
        } else {
          ModernMessageDialog.createWarningDialog(mParent,
              "'" + colorMap.getName() + "' is already in use. Please choose another name.");
        }
      }
    }
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public ColorMap getColorMap() {
    return mColorMap;// new ColorMap(mTextName.getText(),
                     // mColorMapEditor.getColorMap(), false);
  }
}
