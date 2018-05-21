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
package edu.columbia.rdf.matcalc.groups;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.text.RegexUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.ui.external.microsoft.ExcelUI;
import org.jebtk.math.ui.matrix.AllMatrixGroupGuiFileFilter;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.contentpane.VTabToolbar;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.FolderBwVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.graphics.icons.SaveBwVectorIcon;
import org.jebtk.modern.io.AllGuiFilesFilter;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.tree.ModernTreeEvent;
import org.jebtk.modern.tree.TreeEventListener;
import org.xml.sax.SAXException;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * The class ColumnGroupTreePanel.
 */
public class ColumnGroupTreePanel extends ModernComponent {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member tree.
   */
  protected ModernTree<XYSeries> mTree = new ModernTree<XYSeries>();

  /**
   * The member load button.
   */
  private ModernButton mLoadButton = new ModernButton(
      AssetService.getInstance().loadIcon(FolderBwVectorIcon.class, 16));

  /**
   * The member save button.
   */
  private ModernButton mSaveButton = new ModernButton(
      AssetService.getInstance().loadIcon(SaveBwVectorIcon.class, 16));

  /**
   * The member add button.
   */
  private ModernButton mAddButton = new ModernButton(
      AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /** The m import button. */
  private ModernButton mImportButton = new ModernButton("Import");

  /**
   * The member remove button.
   */
  private ModernButton mRemoveButton = new ModernButton(
      AssetService.getInstance().loadIcon("trash_bw", 16));

  // private ModernButton clearButton =
  // new ModernButton(UIResources.getInstance().loadIcon("clear", 16));
  // //Ui.MENU_CLEAR);

  /**
   * The member parent.
   */
  protected MainMatCalcWindow mParent;

  /**
   * The member matrix.
   */
  protected DataFrame mMatrix;

  /**
   * The class LoadEvents.
   */
  private class LoadEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      try {
        loadGroups();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (SAXException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class SaveEvents.
   */
  private class SaveEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      try {
        saveGroups();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (TransformerException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class AddEvents.
   */
  private class AddEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      addGroup();
    }
  }

  /**
   * The Class ImportEvents.
   */
  private class ImportEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      try {
        importGroups();
      } catch (InvalidFormatException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class RemoveEvents.
   */
  private class RemoveEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      remove();
    }
  }

  /**
   * The class ClearEvents.
   */
  private class ClearEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
     * modern .event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      clear();
    }
  }

  /**
   * The class TreeEvents.
   */
  private class TreeEvents implements TreeEventListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.tree.TreeEventListener#treeNodeDragged(org.abh.lib.
     * ui. modern.tree.ModernTreeEvent)
     */
    @Override
    public void treeNodeDragged(ModernTreeEvent e) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.tree.TreeEventListener#treeNodeDoubleClicked(org.
     * abh. lib.ui.modern.tree.ModernTreeEvent)
     */
    @Override
    public void treeNodeDoubleClicked(ModernTreeEvent e) {
      editGroup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.ui.modern.tree.TreeEventListener#treeNodeClicked(org.abh.lib.
     * ui. modern.tree.ModernTreeEvent)
     */
    @Override
    public void treeNodeClicked(ModernTreeEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * Instantiates a new column group tree panel.
   *
   * @param parent the parent
   */
  public ColumnGroupTreePanel(MainMatCalcWindow parent, String title) {
    mParent = parent;

    setup(title);

    // setGroups(matrix.getColumnGroups());
  }

  /**
   * Sets the matrix.
   *
   * @param matrix the new matrix
   */
  public void setMatrix(DataFrame matrix) {
    mMatrix = matrix;
  }

  /**
   * Setup.
   */
  private void setup(String title) {
    mTree.setDragEnabled(true);

    mTree.setNodeRenderer(new XYSeriesTreeNodeRenderer());

    // tree.setNodeSelectionPolicy(SelectionPolicy.SINGLE);
    // tree.addMouseListener(this);
    ModernScrollPane scrollPane = new ModernScrollPane(mTree);
    // scrollPane.setBorder(BORDER);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);

    // scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
    // scrollPane.getViewport().setBackground(Color.WHITE);

    setBody(scrollPane); // new ModernLineBorderPanel(new
                         // ModernComponent(scrollPane, BORDER)));

    VTabToolbar toolbar = new VTabToolbar(title);

    mLoadButton.setToolTip("Load groups", "Load groups from file.");
    toolbar.add(mLoadButton);
    //toolbar.add(ModernPanel.createHGap());
    mSaveButton.setToolTip("Save groups", "Save groups to file.");
    toolbar.add(mSaveButton);
    //toolbar.add(ModernPanel.createHGap());
    mAddButton.setToolTip("Add group", "Add a new group.");
    toolbar.add(mAddButton);
    //toolbar.add(ModernPanel.createHGap());
    mRemoveButton.setToolTip("Remove groups", "Remove selected groups.");
    toolbar.add(mRemoveButton);
    
    setHeader(toolbar);

    setBorder(BORDER);

    mLoadButton.addClickListener(new LoadEvents());
    mSaveButton.addClickListener(new SaveEvents());
    mAddButton.addClickListener(new AddEvents());
    mImportButton.addClickListener(new ImportEvents());
    mRemoveButton.addClickListener(new RemoveEvents());
    // clearButton.addClickListener(new ClearEvents());

    mTree.addTreeListener(new TreeEvents());
  }

  /**
   * Adds the group.
   */
  public void addGroup() {
    // See if there are some columns selected

    List<String> names = getSelectedNames();

    XYSeriesDialog dialog = new XYSeriesDialog(mParent, names);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    addGroup(dialog.getGroup());
  }

  /**
   * Gets the selected names.
   *
   * @return the selected names
   */
  public List<String> getSelectedNames() {
    // See if there are some columns selected

    List<Integer> columns = mParent.getSelectedColumns();

    List<String> names = DataFrame.columnNames(mMatrix, columns);

    return names;
  }

  /**
   * Load groups.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws SAXException the SAX exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws ParseException the parse exception
   */
  public void loadGroups() throws IOException, SAXException,
      ParserConfigurationException, ParseException {
    Path file = FileDialog.open(mParent)
        .filter(new AllMatrixGroupGuiFileFilter(),
            new MatrixGrpjGuiFileFilter(),
            new MatrixGrpxGuiFileFilter(),
            new MatrixGroupGuiFileFilter(),
            new MatrixGrp2GuiFileFilter(),
            new AllGuiFilesFilter())
        .getFile(RecentFilesService.getInstance().getPwd());

    if (file == null) {
      return;
    }

    if (PathUtils.getFileExt(file).equals(MatrixGrpxGuiFileFilter.EXT)) {
      loadXml(file);
    } else if (PathUtils.getFileExt(file).equals(MatrixGrpjGuiFileFilter.EXT)) {
      loadJson(file);
    } else {
      loadGroups(file);
    }
  }

  /**
   * Load grpx files.
   *
   * @param file the file
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParserConfigurationException the parser configuration exception
   */
  protected synchronized void loadXml(Path file)
      throws SAXException, IOException, ParserConfigurationException {
    List<XYSeries> groups = new ArrayList<XYSeries>();

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    XYSeriesXmlHandler handler = new XYSeriesXmlHandler(groups);

    saxParser.parse(file.toFile(), handler);

    addGroups(groups);
  }

  /**
   * Load json.
   *
   * @param file the file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  protected synchronized void loadJson(Path file) throws IOException {
    addGroups(XYSeries.loadJson(file));
  }

  /**
   * Load groups.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void loadGroups(Path file) throws IOException {
    List<XYSeries> groups = XYSeries.loadSeries(file, mMatrix);

    // setGroups(groups);
    // Append groups rather than loading a new set
    addGroups(groups);
  }

  /**
   * Save groups.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws TransformerException the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public void saveGroups()
      throws IOException, TransformerException, ParserConfigurationException {
    Path file = FileDialog.save(mParent).filter(new MatrixGrpjGuiFileFilter())
        .getFile(RecentFilesService.getInstance().getPwd());

    if (file == null) {
      return;
    }

    if (FileUtils.exists(file)) {
      ModernDialogStatus status = ModernMessageDialog
          .createFileReplaceDialog(mParent, file);

      if (status == ModernDialogStatus.CANCEL) {
        saveGroups();

        return;
      }
    }

    // MatrixGroup.writeXml(file, getGroups());
    MatrixGroup.writeJson(file, getGroups());

    ModernMessageDialog.createFileSavedDialog(mParent, file);
  }

  /**
   * Sets the groups.
   */
  public void setGroups() {
    setGroups(getGroups());
  }

  /**
   * Sets the groups.
   *
   * @param groups the new groups
   */
  public void setGroups(Collection<XYSeries> groups) {
    createTree(groups, true);
  }

  /**
   * Adds the groups.
   *
   * @param groups the groups
   */
  public void addGroups(Collection<XYSeries> groups) {
    createTree(groups, false);
  }

  /**
   * Creates the tree.
   *
   * @param groups the groups
   * @param clear the clear
   */
  protected void createTree(Collection<XYSeries> groups, boolean clear) {

    TreeRootNode<XYSeries> root;

    if (clear) {
      root = new TreeRootNode<XYSeries>();
      mTree.setRoot(root);
    } else {
      root = mTree.getRoot();
    }

    for (XYSeries group : groups) {
      TreeNode<XYSeries> groupNode = new TreeNode<XYSeries>(group.getName(),
          group);

      for (int i : XYSeries.findColumnIndices(mMatrix, group)) {
        TreeNode<XYSeries> childNode = new TreeNode<XYSeries>(
            mMatrix.getColumnName(i));

        // Don't render the children, the parent will. This is
        // because we allow dragging and we don't want users to
        // drag the children around
        childNode.setVisible(false);

        groupNode.addChild(childNode);
      }

      // Cannot inherit children
      groupNode.setExpanded(false);

      root.addChild(groupNode);
    }

  }

  /**
   * Creates the tree.
   *
   * @param group the group
   * @param clear the clear
   */
  protected void createTree(XYSeries group, boolean clear) {

    TreeRootNode<XYSeries> root;

    if (clear) {
      root = new TreeRootNode<XYSeries>();
      mTree.setRoot(root);
    } else {
      root = mTree.getRoot();
    }

    TreeNode<XYSeries> groupNode = new TreeNode<XYSeries>(group.getName(),
        group);

    for (int i : XYSeries.findColumnIndices(mMatrix, group)) {

      TreeNode<XYSeries> childNode = new TreeNode<XYSeries>(
          mMatrix.getColumnName(i));

      // Don't render the children, the parent will. This is
      // because we allow dragging and we don't want users to
      // drag the children around
      childNode.setVisible(false);

      groupNode.addChild(childNode);
    }

    // Cannot inherit children
    groupNode.setExpanded(false);

    root.addChild(groupNode);

  }

  /**
   * Gets the groups.
   *
   * @return the groups
   */
  public XYSeriesGroup getGroups() {
    XYSeriesGroup groups = new XYSeriesGroup();

    for (TreeNode<XYSeries> node : mTree) {
      if (node.getValue() != null) {
        groups.add(node.getValue());
      }
    }

    return groups;
  }

  /**
   * Adds the group.
   *
   * @param group the group
   */
  protected void addGroup(XYSeries group) {
    createTree(group, false);
  }

  /**
   * Import groups.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws InvalidFormatException the invalid format exception
   */
  private void importGroups() throws IOException, InvalidFormatException {
    Path file = ExcelUI.openExcelFileDialog(mParent,
        RecentFilesService.getInstance().getPwd());

    if (file == null) {
      return;
    }

    DataFrame groupsMatrix = Excel.convertToMatrix(file,
        true,
        TextUtils.emptyList(),
        0,
        TextUtils.TAB_DELIMITER);

    XYSeriesImportDialog dialog = new XYSeriesImportDialog(mParent, mMatrix,
        groupsMatrix);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    addGroups(dialog.getGroups());
  }

  /**
   * Edits the group.
   */
  private void editGroup() {
    if (mTree.getSelectedNode() == null) {
      return;
    }

    if (mTree.getSelectedNode().getValue() == null) {
      return;
    }

    XYSeriesDialog dialog = new XYSeriesDialog(mParent,
        mTree.getSelectedNode().getValue());

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setGroups(); // mTree.fireCanvasChanged();
  }

  /**
   * Clear.
   */
  private void clear() {
    ModernDialogStatus status = ModernMessageDialog.createDialog(mParent,
        mParent.getAppInfo().getName(),
        "Are you sure you want to clear all groups?",
        MessageDialogType.WARNING_OK_CANCEL);

    if (status == ModernDialogStatus.OK) {
      mTree.getRoot().clear();
    }
  }

  /**
   * Removes the.
   */
  private void remove() {
    ModernDialogStatus status = ModernMessageDialog.createDialog(mParent,
        mParent.getAppInfo().getName(),
        "Are you sure you want to remove the selected groups?",
        MessageDialogType.WARNING_OK_CANCEL);

    if (status == ModernDialogStatus.OK) {
      for (TreeNode<XYSeries> node : mTree.getSelectedNodes()) {
        if (node.getValue() != null) {
          node.removeSelf();
        }
      }
    }
  }
}
