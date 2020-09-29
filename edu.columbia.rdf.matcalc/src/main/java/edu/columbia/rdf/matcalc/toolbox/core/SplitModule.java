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
package edu.columbia.rdf.matcalc.toolbox.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.Function;
import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.TmpService;
import org.jebtk.core.stream.Stream;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.ModernIcon;
import org.jebtk.modern.help.ModernMenuHelpItem;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.menu.ModernPopupMenu2;
import org.jebtk.modern.menu.ModernTwoLineMenuItem;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton2;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Split table into multiple tables by grouping column values and then create a
 * zip of the results.
 *
 * @author Antony Holmes
 *
 */
public class SplitModule extends Module implements ModernClickListener {

  /** The Constant ICON. */
  private static final ModernIcon ICON = AssetService.getInstance().loadIcon("split", 24);

  /** The m button. */
  private RibbonLargeDropDownButton2 mButton;

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Split";
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

    ModernPopupMenu2 popup = new ModernPopupMenu2();

    popup.addMenuItem(
        new ModernTwoLineMenuItem("Split on column values", "Split matrix by grouping rows in a column.", ICON));
    popup.addMenuItem(new ModernTwoLineMenuItem("Split by group", "Split matrix based on column groups.", ICON));

    // popup.addMenuItem(new ModernMenuSeparator());

    popup.addMenuItem(
        new ModernMenuHelpItem("Help with splitting a matrix...", "matcalc.split.help.url").setTextOffset(48));

    mButton = new RibbonLargeDropDownButton2("Split", ICON, popup);
    mButton.setChangeText(false);
    mButton.setToolTip("Split", "Split matrix into sub-matrices.");

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
    if (e.getMessage().equals("Split on column values")) {
      try {
        splitByColumn();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals("Split by group")) {
      try {
        splitByGroup();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } else {
      // Do nothing
    }
  }

  /**
   * Split a matrix by grouping rows with the same value in a given column.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void splitByColumn() throws IOException {
    List<Integer> columns = mWindow.getSelectedColumns();

    if (columns == null || columns.size() == 0) {
      ModernMessageDialog.createDialog(mWindow, "You must select a column to split on.", MessageDialogType.WARNING);

      return;
    }

    SplitDialog dialog = new SplitDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    boolean openMatrices = dialog.getLoad();
    boolean createZip = dialog.getCreateZip();

    DataFrame current = mWindow.getCurrentMatrix();

    ListMultiMap<String, Integer> idMap = ArrayListMultiMap.create();

    int c = columns.get(0);

    // first the list of ids

    for (int i = 0; i < current.getRows(); ++i) {
      idMap.get(current.getText(i, c)).add(i);
    }

    // Clean up the ids
    List<String> ids = CollectionUtils.sort(idMap.keySet());

    List<String> cleanedIds = Stream.of(ids).map(new Function<String, String>() {
      @Override
      public String apply(String item) {
        return item.toLowerCase().replaceAll("\\.[a-z]+$", "").replaceAll("[^a-z0-9\\_\\-]", "_").replaceAll("_+", "_");
      }
    }).toList();

    List<DataFrame> matrices = new ArrayList<DataFrame>(ids.size());

    for (String id : ids) {
      System.err.println("split id " + id);
      matrices.add(DataFrame.createAnnotatableMatrixFromRows(current, idMap.get(id)));
    }

    splitByGroup(cleanedIds, matrices, openMatrices, createZip);
  }

  /**
   * Split by group.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void splitByGroup() throws IOException {
    List<XYSeries> groups = mWindow.getGroups();

    if (groups.size() == 0) {
      MainMatCalcWindow.createGroupWarningDialog(mWindow);

      return;
    }

    SplitDialog dialog = new SplitDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    boolean openMatrices = dialog.getLoad();
    boolean createZip = dialog.getCreateZip();

    DataFrame current = mWindow.getCurrentMatrix();

    List<List<Integer>> allIndices = MatrixGroup.findColumnIndices(current, groups);

    List<DataFrame> matrices = new ArrayList<DataFrame>(groups.size());

    for (List<Integer> indices : allIndices) {
      matrices.add(DataFrame.createAnnotatableMatrixFromCols(current, indices));
    }

    List<String> ids = new ArrayList<String>(groups.size());

    for (XYSeries group : groups) {
      ids.add(group.getName().toLowerCase().replaceAll("\\.[a-z]+$", "").replaceAll("[^a-z0-9\\_\\-]", "_")
          .replaceAll("_+", "_"));
    }

    splitByGroup(ids, matrices, openMatrices, createZip);
  }

  /**
   * Split by group.
   *
   * @param ids          the ids
   * @param matrices     the matrices
   * @param openMatrices the open matrices
   * @param createZip    the create zip
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void splitByGroup(List<String> ids, List<DataFrame> matrices, boolean openMatrices, boolean createZip)
      throws IOException {

    if (createZip) {
      Path file = FileDialog.saveZipFile(mWindow, RecentFilesService.getInstance().getPwd());

      if (file != null) {
        boolean write = true;

        if (FileUtils.exists(file)) {
          ModernDialogStatus status = ModernMessageDialog.createFileReplaceDialog(mWindow, file);

          write = status == ModernDialogStatus.OK;
        }

        if (write) {
          List<Path> files = new ArrayList<Path>(ids.size());

          for (int i = 0; i < ids.size(); ++i) {
            String id = ids.get(i);

            DataFrame m = matrices.get(i);

            Path tmp = TmpService.getInstance().newTmpFile(id + ".txt");

            DataFrame.writeDataFrame(m, tmp);

            files.add(tmp);
          }

          FileUtils.zip(file, files);

          ModernMessageDialog.createFileSavedDialog(mWindow, file);
        }
      }
    }

    if (openMatrices) {
      // We elect to open the matrices into matcalc
      mWindow.openMatrices().open(matrices);
    }
  }
}
