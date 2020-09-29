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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.batik.transcoder.TranscoderException;
import org.jebtk.bioinformatics.ui.groups.Group;
import org.jebtk.bioinformatics.ui.groups.GroupsGuiFileFilter;
import org.jebtk.bioinformatics.ui.groups.GroupsModel;
import org.jebtk.bioinformatics.ui.groups.GroupsPanel;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.text.Join;
import org.jebtk.graphplot.Image;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonWidget;
import org.jebtk.modern.clipboard.ClipboardRibbonSection;
import org.jebtk.modern.contentpane.CenterTab;
import org.jebtk.modern.contentpane.HTab;
import org.jebtk.modern.contentpane.ModernHContentPane;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.CanvasMouseAdapter;
import org.jebtk.modern.graphics.CanvasMouseEvent;
import org.jebtk.modern.graphics.icons.QuickOpenVectorIcon;
import org.jebtk.modern.graphics.icons.QuickSaveVectorIcon;
import org.jebtk.modern.help.ModernAboutDialog;
import org.jebtk.modern.help.RibbonPanelProductInfo;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.JpgGuiFileFilter;
import org.jebtk.modern.io.OpenRibbonPanel;
import org.jebtk.modern.io.PdfGuiFileFilter;
import org.jebtk.modern.io.PngGuiFileFilter;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.io.SaveAsRibbonPanel;
import org.jebtk.modern.io.SvgGuiFileFilter;
import org.jebtk.modern.options.ModernOptionsRibbonPanel;
import org.jebtk.modern.panel.CardPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.ribbon.QuickAccessButton;
import org.jebtk.modern.ribbon.RibbonMenuItem;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.splitpane.ModernVSplitPaneLine;
import org.jebtk.modern.tabs.SizableTab;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextArea;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.tooltip.ModernToolTip;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.zoom.ModernStatusZoomSlider;
import org.jebtk.modern.zoom.ZoomModel;
import org.jebtk.modern.zoom.ZoomRibbonSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class MainVennWindow.
 */
public class MainVennWindow extends ModernRibbonWindow implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m open panel. */
  private OpenRibbonPanel mOpenPanel = new OpenRibbonPanel();

  /** The m save as panel. */
  private SaveAsRibbonPanel mSaveAsPanel = new SaveAsRibbonPanel();

  // private ModernStatusBar mStatusBar = new ModernStatusBar();

  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory.getLogger(MainVennWindow.class);

  /** The m content pane. */
  private ModernHContentPane mContentPane = new ModernHContentPane();

  /** The m groups panel. */
  private GroupsPanel mGroupsPanel;

  /** The m groups model. */
  private GroupsModel mGroupsModel = new GroupsModel();

  /** The m venn canvas. */
  private ProportionalVennCanvas mVennCanvas = new ProportionalVennCanvas();

  /** The m zoom model. */
  private ZoomModel mZoomModel = new ZoomModel();

  /** The m style model. */
  private StyleModel mStyleModel = new StyleModel();

  /** The m format pane. */
  private VBox mFormatPane;

  /** The m text values. */
  private ModernClipboardTextArea mTextValues = new ModernClipboardTextArea();

  /** The m overlap label. */
  private ModernLabel mOverlapLabel = new ModernAutoSizeLabel();

  /**
   * The Class ExportCallBack.
   */
  private class ExportCallBack implements DialogEventListener {

    /** The m file. */
    private Path mFile;

    /** The m pwd. */
    private Path mPwd;

    /**
     * Instantiates a new export call back.
     *
     * @param file the file
     * @param pwd  the pwd
     */
    public ExportCallBack(Path file, Path pwd) {
      mFile = file;
      mPwd = pwd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.dialog.DialogEventListener#statusChanged(org.abh.common
     * .ui. dialog.DialogEvent)
     */
    @Override
    public void statusChanged(DialogEvent e) {
      if (e.getStatus() == ModernDialogStatus.OK) {
        try {
          save(mFile);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        try {
          export(mPwd);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * The Class GroupEvents.
   */
  private class GroupEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.
     * ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      mVennCanvas.setGroups(mGroupsModel, mStyleModel.get());
    }
  }

  /**
   * The Class MouseEvents.
   */
  private class MouseEvents extends CanvasMouseAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseClicked(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseClicked(CanvasMouseEvent e) {
      IntPos2D p = e.getScaledPos();

      boolean in1 = mVennCanvas.getP1() != null
          ? Mathematics.getEuclidianD(mVennCanvas.getP1(), p) <= mVennCanvas.getR1()
          : false;
      boolean in2 = mVennCanvas.getP2() != null
          ? Mathematics.getEuclidianD(mVennCanvas.getP2(), p) <= mVennCanvas.getR2()
          : false;
      boolean in3 = mVennCanvas.getP3() != null
          ? Mathematics.getEuclidianD(mVennCanvas.getP3(), p) <= mVennCanvas.getR3()
          : false;

      // System.err.println(in1 + " " + in2 + " " + in3 + " " +
      // mVennCanvas.getP1() +
      // " " + p + " " + mVennCanvas.getR2());

      List<String> lines = new ArrayList<String>();

      if (in1 && in2 && in3) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI123()));

        mOverlapLabel.setText(
            Join.onSpace().values("Values in", mGroupsModel.get(0).getName(), "and", mGroupsModel.get(1).getName(),
                "and", mGroupsModel.get(2).getName(), "only (" + lines.size() + "):").toString());
      } else if (in1 && in2) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI12()));

        mOverlapLabel.setText(Join.onSpace().values("Values in", mGroupsModel.get(0).getName(), "and",
            mGroupsModel.get(1).getName(), "only (" + lines.size() + "):").toString());

      } else if (in1 && in3) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI13()));

        mOverlapLabel.setText(Join.onSpace().values("Values in", mGroupsModel.get(0).getName(), "and",
            mGroupsModel.get(2).getName(), "only (" + lines.size() + "):").toString());
      } else if (in2 && in3) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI23()));

        mOverlapLabel.setText(Join.onSpace().values("Values in", mGroupsModel.get(1).getName(), "and",
            mGroupsModel.get(2).getName(), "only (" + lines.size() + "):").toString());
      } else if (in1) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI1()));

        mOverlapLabel.setText(Join.onSpace()
            .values("Values in", mGroupsModel.get(0).getName(), "only (" + lines.size() + "):").toString());
      } else if (in2) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI2()));

        mOverlapLabel.setText(Join.onSpace()
            .values("Values in", mGroupsModel.get(1).getName(), "only (" + lines.size() + "):").toString());
      } else if (in3) {
        lines.addAll(CollectionUtils.toString(mVennCanvas.getI3()));

        mOverlapLabel.setText(Join.onSpace()
            .values("Values in", mGroupsModel.get(2).getName(), "only (" + lines.size() + "):").toString());
      } else {
        // Do nothing
      }

      mTextValues.setText(lines);
    }
  }

  /**
   * Instantiates a new main venn window.
   */
  public MainVennWindow() {
    super(new VennInfo());

    setup();
  }

  /**
   * Instantiates a new main venn window.
   *
   * @param group1 the group 1
   * @param group2 the group 2
   * @param style  the style
   */
  public MainVennWindow(Group group1, Group group2, CircleStyle style) {
    super(new VennInfo());

    setup();

    mGroupsPanel.addGroup(group1);
    mGroupsPanel.addGroup(group2);

    mStyleModel.set(style);
  }

  /**
   * Instantiates a new main venn window.
   *
   * @param group1 the group 1
   * @param group2 the group 2
   * @param group3 the group 3
   * @param style  the style
   */
  public MainVennWindow(Group group1, Group group2, Group group3, CircleStyle style) {
    super(new VennInfo());

    setup();

    mGroupsPanel.addGroup(group1);
    mGroupsPanel.addGroup(group2);
    mGroupsPanel.addGroup(group3);

    mStyleModel.set(style);
  }

  /**
   * Setup.
   */
  private void setup() {
    mGroupsPanel = new GroupsPanel(this, mGroupsModel);

    createRibbon();

    createUi();

    mGroupsModel.addChangeListener(new GroupEvents());

    mStyleModel.addChangeListener(new GroupEvents());

    setSize(1200, 900);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ribbon.
   */
  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.window.ModernRibbonWindow#createRibbon()
   */
  public final void createRibbon() {
    // RibbongetRibbonMenu() getRibbonMenu() = new RibbongetRibbonMenu()(0);
    RibbonMenuItem ribbonMenuItem;

    ribbonMenuItem = new RibbonMenuItem("Open");
    // menuItem.setToolTipText("Select an input file.");
    getRibbonMenu().addTabbedMenuItem(ribbonMenuItem, mOpenPanel);

    ribbonMenuItem = new RibbonMenuItem(UI.MENU_SAVE_AS);
    getRibbonMenu().addTabbedMenuItem(ribbonMenuItem, mSaveAsPanel);

    ribbonMenuItem = new RibbonMenuItem(UI.MENU_EXIT);
    getRibbonMenu().addTabbedMenuItem(ribbonMenuItem);

    getRibbonMenu().addSeparator();

    ribbonMenuItem = new RibbonMenuItem(UI.MENU_INFO);
    getRibbonMenu().addTabbedMenuItem(ribbonMenuItem, new RibbonPanelProductInfo(getAppInfo()));

    ribbonMenuItem = new RibbonMenuItem(UI.MENU_SETTINGS);
    getRibbonMenu().addTabbedMenuItem(ribbonMenuItem, new ModernOptionsRibbonPanel(getAppInfo()));

    getRibbonMenu().addClickListener(this);

    // Ribbon2 ribbon = new Ribbon2();
    getRibbon().setHelpButtonEnabled(getAppInfo());

    ModernButtonWidget button = new QuickAccessButton(
        AssetService.getInstance().loadIcon(QuickOpenVectorIcon.class, 16));
    button.setClickMessage("Open");
    button.setToolTip(new ModernToolTip("Open", "Open a gene list."));
    button.addClickListener(this);
    addQuickAccessButton(button);

    button = new QuickAccessButton(AssetService.getInstance().loadIcon(QuickSaveVectorIcon.class, 16));
    button.setClickMessage(UI.MENU_SAVE);
    button.setToolTip(new ModernToolTip("Save", "Save the current gene list."));
    button.addClickListener(this);
    addQuickAccessButton(button);

    // home

    getRibbon().getHomeToolbar().addSection(new ClipboardRibbonSection(getRibbon()));

    getRibbon().getHomeToolbar().addSection(new StyleRibbonSection(getRibbon(), mStyleModel));

    /*
     * toolbarSection = new RibbonSection("Data");
     * 
     * RibbonHItemsContainer toolbarContainer = new RibbonHItemsContainer();
     * 
     * ModernPopupMenu openProjectPopup = new
     * RecentFilesPopup(RecentFiles.getInstance());
     * 
     * ModernDropDownMenuButton openButton = new
     * RibbonLargeOptionalDropDownMenuButton("Open",
     * Resources.getInstance().loadIcon("open", Resources.ICON_SIZE_32),
     * openProjectPopup);
     * 
     * openButton.addClickListener(this); toolbarContainer.add(openButton);
     * 
     * ModernButtonWidget button = new RibbonLargeButton("Export",
     * Resources.getInstance().loadIcon("export", Resources.ICON_SIZE_32));
     * button.addClickListener(this); toolbarContainer.add(button);
     * 
     * toolbarSection.setContent(toolbarContainer); toolbar.add(toolbarSection);
     */

    // pick a chip

    getRibbon().getToolbar("View").add(new ZoomRibbonSection(this, mZoomModel));

    /*
     * toolbar = new RibbonToolbar("View");
     * 
     * toolbarSection = new RibbonSection("Show");
     * 
     * toolbarContainer = new RibbonHItemsContainer();
     * 
     * toolbarSection.setContent(toolbarContainer);
     * 
     * toolbar.add(toolbarSection);
     * 
     * toolbar.add(new WindowRibbonSection(this, ribbon));
     * 
     * getRibbon().addToolbar(toolbar);
     */

    // tabbedBar.setCanvasSize(new Dimension(Short.MAX_VALUE, 130));

    // setRibbon(ribbon, getRibbonMenu());

    getRibbon().setSelectedIndex(1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.window.ModernWindow#createUi()
   */
  public final void createUi() {
    setBody(mContentPane);

    mStatusBar.addRight(new ModernStatusZoomSlider(mZoomModel));

    // ZoomCanvas zoomCanvas = new ZoomCanvas(mVennCanvas);

    mVennCanvas.addCanvasMouseListener(new MouseEvents());

    // ZoomCanvas zoomCanvas = new ZoomCanvas(canvas);

    mVennCanvas.setZoomModel(mZoomModel);

    // BackgroundCanvas backgroundCanvas = new BackgroundCanvas(zoomCanvas);

    ModernScrollPane scrollPane1 = new ModernScrollPane(mVennCanvas).setScrollBarLocation(ScrollBarLocation.FLOATING);

    ModernComponent panel = new ModernComponent();

    panel.setHeader(mOverlapLabel);

    ModernScrollPane scrollPane2 = new ModernScrollPane(mTextValues)
        .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    panel.setBody(new ModernComponent(scrollPane1, BorderService.getInstance().createTopBorder(10)));

    ModernVSplitPaneLine splitPane = new ModernVSplitPaneLine();

    splitPane.addComponent(
        new ModernComponent(new CardPanel(new ModernPanel(panel, ModernWidget.BORDER)), ModernWidget.DOUBLE_BORDER),
        0.7);
    splitPane.addComponent(new ModernComponent(new CardPanel(new ModernPanel(scrollPane2, ModernWidget.BORDER)),
        ModernWidget.DOUBLE_BORDER), 0.3);

    // splitPane.setDividerLocation(600);

    mContentPane.tabs().setCenterTab(new CenterTab(splitPane));

    addGroupsPanel();

    mFormatPane = VBox.create();

    mFormatPane.add(new RadiusControl(mVennCanvas.getProperties()));

    // addFormatPane();
  }

  /**
   * Adds the groups panel.
   */
  private void addGroupsPanel() {
    if (mContentPane.tabs().left().contains("Groups")) {
      return;
    }

    SizableTab sizePane = new SizableTab("Groups", mGroupsPanel, 300, 300, 500);

    mContentPane.tabs().left().add(sizePane);
  }

  /**
   * Adds the history pane to the layout if it is not already showing.
   */
  private void addFormatPane() {
    if (mContentPane.tabs().right().contains("Format Plot")) {
      return;
    }

    mContentPane.tabs().right().add(new SizableTab("Format Plot", new HTab("Format Plot", mFormatPane), 300, 200, 500));

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    if (e.getMessage().equals(UI.MENU_OPEN) || e.getMessage().equals(UI.MENU_BROWSE)
        || e.getMessage().startsWith("Other...")) {
      try {
        browseForFile();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(OpenRibbonPanel.FILE_SELECTED)) {
      try {
        openFile(mOpenPanel.getSelectedFile());
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(OpenRibbonPanel.DIRECTORY_SELECTED)) {

      try {
        browseForFile(mOpenPanel.getSelectedDirectory());
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(UI.MENU_SAVE)) {
      try {
        export();
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (TranscoderException e1) {
        e1.printStackTrace();
      } catch (TransformerException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(SaveAsRibbonPanel.DIRECTORY_SELECTED)) {
      try {
        export(mSaveAsPanel.getSelectedDirectory());
      } catch (IOException e1) {
        e1.printStackTrace();
      } catch (TranscoderException e1) {
        e1.printStackTrace();
      } catch (TransformerException e1) {
        e1.printStackTrace();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      }
    } else if (e.getMessage().equals(UI.MENU_ABOUT)) {
      ModernAboutDialog.show(this, getAppInfo());
    } else if (e.getMessage().equals(UI.MENU_EXIT)) {
      close();
    } else {
      // do nothing
    }
  }

  /**
   * Browse for file.
   *
   * @throws Exception the exception
   */
  private void browseForFile() throws Exception {
    browseForFile(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Browse for file.
   *
   * @param workingDirectory the working directory
   * @throws Exception the exception
   */
  private void browseForFile(Path workingDirectory) throws Exception {
    openFile(FileDialog.openFile(this, workingDirectory, new GroupsGuiFileFilter()));
  }

  /**
   * Open file.
   *
   * @param file the file
   * @throws Exception the exception
   */
  private void openFile(Path file) throws Exception {
    if (file == null) {
      return;
    }

    LOG.info("Open file {}...", file);

    if (!FileUtils.exists(file)) {
      ModernMessageDialog.createFileDoesNotExistDialog(this, getAppInfo().getName(), file);

      return;
    }

    if (PathUtils.getFileExt(file).equals("mgrpx")) {
      List<Group> groups = Group.loadGroups(file);

      mGroupsPanel.addGroups(groups);
    } else {

    }

    RecentFilesService.getInstance().add(file);
  }

  /**
   * Export.
   *
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   * @throws TranscoderException          the transcoder exception
   * @throws TransformerException         the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void export() throws IOException, TranscoderException, TransformerException, ParserConfigurationException {
    export(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Export.
   *
   * @param pwd the pwd
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   * @throws TranscoderException          the transcoder exception
   * @throws TransformerException         the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void export(Path pwd)
      throws IOException, TranscoderException, TransformerException, ParserConfigurationException {
    Path file = FileDialog.saveFile(this, pwd, new SvgGuiFileFilter(), new PngGuiFileFilter(), new PdfGuiFileFilter(),
        new JpgGuiFileFilter(), new GroupsGuiFileFilter());

    if (file == null) {
      return;
    }

    if (FileUtils.exists(file)) {
      ModernMessageDialog.createFileReplaceDialog(this, file, new ExportCallBack(file, pwd));
    } else {
      save(file);
    }
  }

  /**
   * Save.
   *
   * @param file the file
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   * @throws TranscoderException          the transcoder exception
   * @throws TransformerException         the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  private void save(Path file)
      throws IOException, TranscoderException, TransformerException, ParserConfigurationException {
    if (file == null) {
      return;
    }

    if (PathUtils.getFileExt(file).equals("mgrpx")) {
      mGroupsPanel.saveGroupsFile(file);
    } else {
      Image.write(mVennCanvas, file);
    }

    RecentFilesService.getInstance().setPwd(file.getParent());

    ModernMessageDialog.createFileSavedDialog(this, file);
  }
}
