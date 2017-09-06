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
package edu.columbia.rdf.matcalc;

import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.bioinformatics.file.BioPathUtils;
import org.jebtk.core.Plugin;
import org.jebtk.core.PluginService;
import org.jebtk.core.collections.ArrayListCreator;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.io.ReaderUtils;
import org.jebtk.core.io.Temp;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.DoubleMatrixParser;
import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.math.matrix.MixedMatrixParser;
import org.jebtk.math.ui.matrix.EditableMatrixTableModel;
import org.jebtk.math.ui.matrix.MatrixTable;
import org.jebtk.math.ui.matrix.MatrixTableModel;
import org.jebtk.math.ui.matrix.transform.MatrixTransform;
import org.jebtk.math.ui.matrix.transform.MatrixTransformCellRenderer;
import org.jebtk.math.ui.matrix.transform.MatrixTransformListener;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButtonWidget;
import org.jebtk.modern.clipboard.ClipboardRibbonSection;
import org.jebtk.modern.contentpane.HTab;
import org.jebtk.modern.contentpane.ModernHContentPane;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.event.ModernSelectionListener;
import org.jebtk.modern.graphics.icons.QuickOpenVectorIcon;
import org.jebtk.modern.graphics.icons.QuickSaveVectorIcon;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.help.ModernAboutDialog;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.GuiFileExtFilter;
import org.jebtk.modern.io.OpenRibbonPanel;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.io.SaveAsRibbonPanel;
import org.jebtk.modern.ribbon.QuickAccessButton;
import org.jebtk.modern.ribbon.RibbonMenuItem;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.splitpane.ModernVSplitPaneLine;
import org.jebtk.modern.table.ModernSpreadsheetBar;
import org.jebtk.modern.tabs.SegmentTabsPanel;
import org.jebtk.modern.tabs.TabsModel;
import org.jebtk.modern.widget.tooltip.ModernToolTip;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.ModernWindowConstructor;
import org.jebtk.modern.window.WindowService;
import org.jebtk.modern.zoom.ModernStatusZoomSlider;
import org.jebtk.modern.zoom.ZoomModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dWindow;
import edu.columbia.rdf.matcalc.groups.ColumnGroupTreePanel;
import edu.columbia.rdf.matcalc.groups.RowGroupTreePanel;
import edu.columbia.rdf.matcalc.toolbox.FileModule;
import edu.columbia.rdf.matcalc.toolbox.Module;

// TODO: Auto-generated Javadoc
/**
 * Merges designated segments together using the merge column. Consecutive rows with the same
 * merge id will be merged together. Coordinates and copy number will be adjusted but
 * genes, cytobands etc are not.
 *
 * @author Antony Holmes Holmes
 *
 */
public class MainMatCalcWindow extends ModernRibbonWindow implements ModernWindowConstructor, ModernClickListener, ModernSelectionListener, MatrixTransformListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The constant CREATE_GROUPS_MESSAGE.
	 */
	public static final String CREATE_GROUPS_MESSAGE = 
			"You must load or create some groups.";


	/**
	 * The member input file.
	 */
	private List<Path> mInputFiles = new ArrayList<Path>();
	//private Path outputFile;

	/**
	 * The member open panel.
	 */
	private OpenRibbonPanel mOpenPanel = new OpenRibbonPanel();

	/**
	 * The member save as panel.
	 */
	private SaveAsRibbonPanel mSaveAsPanel = new SaveAsRibbonPanel();

	/** The m matrices. */
	private List<AnnotationMatrix> mMatrices = 
			new ArrayList<AnnotationMatrix>();


	//private ModernListModel<MatrixTransform> transformModel = 
	//		new ModernListModel<MatrixTransform>();

	//private ModernHistoryList<MatrixTransform> mTransformList = 
	//		new ModernHistoryList<MatrixTransform>();

	/**
	 * The member history panel.
	 */
	private MatCalcHistoryPanel mHistoryPanel;

	//private MatrixTable mMatrixTable = new MatrixTable();

	/**
	 * The member column groups panel.
	 */
	private ColumnGroupTreePanel mColumnGroupsPanel;

	/**
	 * The member row groups panel.
	 */
	private RowGroupTreePanel mRowGroupsPanel;


	/**
	 * The member group panel.
	 */
	private ModernComponent mGroupPanel;


	/**
	 * The member matrix table.
	 */
	private MatrixTable mMatrixTable;


	/**
	 * The member modules.
	 */
	private List<Module> mModules = new ArrayList<Module>();

	/**
	 * The member module map.
	 */
	private Map<String, Module> mModuleMap = new HashMap<String, Module>();

	/** The m open file filters. */
	private List<GuiFileExtFilter> mOpenFileFilters =
			new ArrayList<GuiFileExtFilter>();

	/** Modules associated with opening files with a given extension. */
	private Map<String, List<FileModule>> mOpenFileModuleMap =
			DefaultHashMap.create(new ArrayListCreator<FileModule>());

	/** The m save file filters. */
	private List<GuiFileExtFilter> mSaveFileFilters =
			new ArrayList<GuiFileExtFilter>();

	/** The m save file module map. */
	private Map<String, List<FileModule>> mSaveFileModuleMap =
			DefaultHashMap.create(new ArrayListCreator<FileModule>());


	//private ModernScrollPane mTableScrollPane;

	/**
	 * The constant LOG.
	 */
	private static final Logger LOG = 
			LoggerFactory.getLogger(MainMatCalcWindow.class);


	/** The Constant INVALID_COLUMN. */
	public static final int INVALID_COLUMN = Integer.MIN_VALUE;


	public static final List<String> EMPTY_SKIP = TextUtils.EMPTY_LIST;

	/**
	 * The member find dialog.
	 */
	private FindReplaceDialog mFindDialog;


	/** The m zoom model. */
	private ZoomModel mZoomModel = new ZoomModel();

	/**
	 * The class MouseEvents.
	 */
	private class MouseEvents extends MouseAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				MatrixTransform transform = mHistoryPanel.getSelectedItem();

				if (transform != null) {
					transform.apply();
				}
			}
		}
	}

	/**
	 * The class ExportCallBack.
	 */
	private class ExportCallBack implements DialogEventListener {

		/**
		 * The member file.
		 */
		private Path mFile;

		/**
		 * The member pwd.
		 */
		private Path mPwd;

		/**
		 * Instantiates a new export call back.
		 *
		 * @param file the file
		 * @param pwd the pwd
		 */
		public ExportCallBack(Path file, Path pwd) {
			mFile = file;
			mPwd = pwd;
		}

		/* (non-Javadoc)
		 * @see org.abh.lib.ui.modern.dialog.DialogEventListener#statusChanged(org.abh.lib.ui.modern.dialog.DialogEvent)
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
	 * The class SaveAction.
	 */
	private class SaveAction extends AbstractAction {

		/**
		 * The constant serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				export();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (TranscoderException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * The class OpenAction.
	 */
	private class OpenAction extends AbstractAction {

		/**
		 * The constant serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				browseForFile();
			} catch (InvalidFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * The class FindAction.
	 */
	private class FindAction extends AbstractAction {

		/**
		 * The constant serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			find();
		}
	}

	/**
	 * Instantiates a new main mat calc window.
	 */
	public MainMatCalcWindow() {
		this(new MatCalcInfo());
	}

	/**
	 * Instantiates a new main mat calc window.
	 *
	 * @param appInfo the app info
	 */
	public MainMatCalcWindow(GuiAppInfo appInfo) {
		super(appInfo);

		try {
			setup();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new main mat calc window.
	 *
	 * @param appInfo the app info
	 * @param m the m
	 */
	public MainMatCalcWindow(GuiAppInfo appInfo, AnnotationMatrix m) {
		super(appInfo);

		try {
			setup();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		openMatrix(m);
	}

	/**
	 * Setup.
	 *
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void setup() throws InstantiationException, IllegalAccessException {
		loadModules();

		mFindDialog = new FindReplaceDialog(this);

		mHistoryPanel = new MatCalcHistoryPanel(this);

		createRibbon();

		createUi();

		addModulesUI();

		mHistoryPanel.setRowHeight(48);
		mHistoryPanel.setCellRenderer(new MatrixTransformCellRenderer());
		//mHistoryPanel.setModel(transformModel);
		mHistoryPanel.addMouseListener(new MouseEvents());

		mHistoryPanel.addSelectionListener(this);

		JComponent content = (JComponent)getContentPane();

		content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), "open");
		content.getActionMap().put("open", new OpenAction());

		content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "save");
		content.getActionMap().put("save", new SaveAction());

		content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), "find");
		content.getActionMap().put("find", new FindAction());

		setSize(1440, 900);

		UI.centerWindowToScreen(this);
	}

	/**
	 * Load external modules.
	 *
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private void loadModules() throws InstantiationException, IllegalAccessException {
		Module module;

		for (Plugin plugin : PluginService.getInstance()) {

			System.err.println("Loading plugin " + plugin.getName());
			module = (Module)plugin.getPluginClass().newInstance();

			//System.err.println("Loading module " + module.getName());

			mModules.add(module);

			mModuleMap.put(module.getName(), module);
		}
	}

	/**
	 * Run module.
	 *
	 * @param module the module
	 * @param args the args
	 * @return true, if successful
	 */
	public boolean runModule(String module, String... args) {
		System.err.println("run module " + module + " " + args);

		if (mModuleMap.containsKey(module)) {
			mModuleMap.get(module).run(args);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Allow modules to initialize and customize the UI.
	 */
	private void addModulesUI() {
		for (Module module : mModules) {
			module.init(this);

			addFileModule(module);
			
			for (FileModule fileModule : module) {
				addFileModule(fileModule);
			}
		}
	}
	
	/**
	 * Register modules that deal with opening and saving files.
	 * 
	 * @param module		A file module.
	 */
	private void addFileModule(FileModule module) {
		for (GuiFileExtFilter filter : module.getOpenFileFilters()) {
			mOpenFileFilters.add(filter);

			// Track what this module can 
			for (String ext : filter.getExtensions()) {
				mOpenFileModuleMap.get(ext).add(module);
			}
		}

		for (GuiFileExtFilter filter : module.getSaveFileFilters()) {
			mSaveFileFilters.add(filter);

			// Track what this module can 
			for (String ext : filter.getExtensions()) {
				mSaveFileModuleMap.get(ext).add(module);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.window.ModernWindowConstructor#createRibbon()
	 */
	public final void createRibbon() {
		//RibbongetRibbonMenu() getRibbonMenu() = new RibbongetRibbonMenu()(0);
		RibbonMenuItem ribbonMenuItem;

		ribbonMenuItem = new RibbonMenuItem("Open");
		//menuItem.setToolTipText("Select an input file.");
		getRibbonMenu().addTabbedMenuItem(ribbonMenuItem, mOpenPanel);


		ribbonMenuItem = new RibbonMenuItem(UI.MENU_SAVE_AS);
		getRibbonMenu().addTabbedMenuItem(ribbonMenuItem, mSaveAsPanel);

		getRibbonMenu().addDefaultItems(getAppInfo());

		getRibbonMenu().addClickListener(this);
		


		//Ribbon2 ribbon = new Ribbon2();

		getRibbon().setHelpButtonEnabled(getAppInfo());

		//public static final ModernIcon QUICK_SAVE_16_ICON = new Raster16Icon(new QuickSave16VectorIcon());
		//public static final ModernIcon QUICK_OPEN_16_ICON = new Raster16Icon(new QuickOpen16VectorIcon());

		ModernButtonWidget button = new QuickAccessButton(UIService.getInstance().loadIcon(QuickOpenVectorIcon.class, 16));
		button.setClickMessage("Open");
		button.setToolTip(new ModernToolTip("Open", 
				"Open an expression matrix."));
		button.addClickListener(this);
		addQuickAccessButton(button);

		button = new QuickAccessButton(UIService.getInstance().loadIcon(QuickSaveVectorIcon.class, 16));
		button.setClickMessage(UI.MENU_SAVE);
		button.setToolTip(new ModernToolTip("Save", 
				"Save the current table."));
		button.addClickListener(this);
		addQuickAccessButton(button);


		getRibbon().getToolbar("Home").add(new ClipboardRibbonSection(getRibbon()));



		//
		// Plot
		//

		/*
		button = new RibbonCompactIconButton("Heat", "Map", 
				UIResources.getInstance().loadIcon("heatmap", 32),
				"Heat Map",
				"Generate a heat map from the matrix.");
		button.addClickListener(this);
		getRibbon().getToolbar("Plot").getSection("Plot").add(button);
		 */

		//
		// Order
		//

		/*
		button = new RibbonButtonIconText2("Rows", 
				new Raster32Icon(new OrderRows32VectorIcon()),
				"Order Rows", 
				"Order rows");
		button.setClickMessage("Order Rows");
		button.addClickListener(this);
		getRibbon().getToolbar("Data").getSection("Sort").add(button);

		button = new RibbonButtonIconText2("Columns", 
				new Raster32Icon(new OrderColumns32VectorIcon()),
				"Order Columns", 
				"Order columns");
		button.setClickMessage("Order Columns");
		button.addClickListener(this);
		getRibbon().getToolbar("Data").getSection("Sort").add(button);
		 */

		//getRibbon().setSelectedIndex(1);
	}



	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.window.ModernWindow#createUi()
	 */
	public final void createUi() {
		setCard(new ModernComponent());
		
		ModernStatusZoomSlider slider = new ModernStatusZoomSlider(mZoomModel);
		
		mStatusBar.addRight(slider);

		createGroupsPanel();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public final void clicked(ModernClickEvent e) {
		if (e.getMessage().equals(UI.MENU_OPEN) ||
				e.getMessage().equals(UI.MENU_BROWSE) ||
				e.getMessage().startsWith("Other...")) {
			try {
				browseForFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (InvalidFormatException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (e.getMessage().equals(OpenRibbonPanel.FILE_SELECTED)) {
			try {
				openFile(mOpenPanel.getSelectedFile(), OpenMode.NEW_WINDOW);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (InvalidFormatException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (e.getMessage().equals(OpenRibbonPanel.DIRECTORY_SELECTED)) {
			try {
				browseForFile(mOpenPanel.getSelectedDirectory());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (InvalidFormatException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (e.getMessage().equals(UI.MENU_SAVE)) {
			try {
				export();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (TranscoderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getMessage().equals(SaveAsRibbonPanel.DIRECTORY_SELECTED)) {
			try {
				export(mSaveAsPanel.getSelectedDirectory());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (TranscoderException e1) {
				e1.printStackTrace();
			}
		} else if (e.getMessage().equals("Groups Pane")) {
			//addGroupsPane();
		} else if (e.getMessage().equals("History Pane")) {
			addHistoryPane();
		} else if (e.getMessage().equals("z-score")) {
			addToHistory("z-score", 
					"z-score", 
					MatrixOperations.zscore(getCurrentMatrix())); //new ZScoreMatrixTransform(this, getCurrentMatrix()));
		} else if (e.getMessage().equals("Row z-score")) {
			addToHistory("Row z-score", "Row z-score", MatrixOperations.rowZscore(getCurrentMatrix())); //addFlowItem(new ZScoreRowsMatrixTransform(this, getCurrentMatrix()));
			//new StdDevFilterMatrixTransform(this, getCurrentMatrix(), 1.5));
		} else if (e.getMessage().equals(UI.MENU_ABOUT)) {
			ModernAboutDialog.show(this, getAppInfo());
		} else if (e.getMessage().equals(UI.MENU_EXIT)) {
			ModernWindow.close(this);
		} else {
			// do nothing
		}
	}

	/**
	 * Find.
	 */
	private void find() {
		AnnotationMatrix m = getCurrentMatrix();

		if (m != null) {
			mFindDialog.setVisible(m, mMatrixTable);
		}

	}

	/**
	 * Reset history.
	 */
	public void resetHistory() {
		mHistoryPanel.resetHistory();
	}

	/**
	 * Return the current matrix visible.
	 *
	 * @return the current matrix
	 */
	public AnnotationMatrix getCurrentMatrix() {
		if (mHistoryPanel.getItemCount() == 0) {
			return null;
		}

		MatrixTransform transform = mHistoryPanel.getSelectedItem();

		if (transform == null) {
			return null;
		}

		return transform.getMatrix();
	}

	/**
	 * Gets the matrices.
	 *
	 * @return the matrices
	 */
	public List<AnnotationMatrix> getMatrices() {
		return Collections.unmodifiableList(mMatrices);
	}

	/**
	 * Gets the history index.
	 *
	 * @return the history index
	 */
	public int getHistoryIndex() {
		return mHistoryPanel.getSelectedIndex();
	}

	/**
	 * Search history.
	 *
	 * @param text the text
	 * @return the int
	 */
	public int searchHistory(String text) {
		return mHistoryPanel.search(text);
	}

	/**
	 * Adds the to history.
	 *
	 * @param name the name
	 * @param matrix the matrix
	 * @return the annotation matrix
	 */
	public AnnotationMatrix addToHistory(String name,
			AnnotationMatrix matrix) {
		if (matrix == null) {
			return null;
		}

		return addToHistory(name, name, matrix);
	}

	/**
	 * Adds the to history.
	 *
	 * @param name the name
	 * @param description the description
	 * @param matrix the matrix
	 * @return the annotation matrix
	 */
	public AnnotationMatrix addToHistory(String name,
			String description,
			AnnotationMatrix matrix) {
		if (matrix == null) {
			return null;
		}

		return addToHistory(new MatrixTransform(this, name, description, matrix));
	}

	/**
	 * Adds the to history.
	 *
	 * @param name the name
	 * @param matrix the matrix
	 * @param selectedIndex the selected index
	 * @return the annotation matrix
	 */
	public AnnotationMatrix addToHistory(String name,
			AnnotationMatrix matrix,
			int selectedIndex) {
		if (matrix == null) {
			return null;
		}

		return addToHistory(name, name, matrix, selectedIndex);
	}

	/**
	 * Adds the to history.
	 *
	 * @param name the name
	 * @param description the description
	 * @param matrix the matrix
	 * @param selectedIndex the selected index
	 * @return the annotation matrix
	 */
	public AnnotationMatrix addToHistory(String name,
			String description,
			AnnotationMatrix matrix,
			int selectedIndex) {
		if (matrix == null) {
			return null;
		}

		return addToHistory(new MatrixTransform(this, name, description, matrix), selectedIndex);
	}

	/**
	 * Add a pipeline step to the workflow.
	 *
	 * @param transform the transform
	 * @return the annotation matrix
	 */
	public AnnotationMatrix addToHistory(MatrixTransform transform) {
		return addToHistory(transform, getHistoryIndex());
	}

	/**
	 * Adds the to history.
	 *
	 * @param transform the transform
	 * @param selectedIndex the selected index
	 * @return the annotation matrix
	 */
	public AnnotationMatrix addToHistory(MatrixTransform transform, int selectedIndex) {
		if (transform == null) {
			return null;
		}

		transform.addMatrixTransformListener(this);

		mHistoryPanel.addItem(transform, selectedIndex).apply();

		return transform.getMatrix();
	}

	/**
	 * Returns a string list of the transformations 
	 * performed on the matrix.
	 *
	 * @return the transformation history
	 */
	public List<String> getTransformationHistory() {
		// The first and last elements of the history list are excluded
		// since there is no transformation of the initial matrix, nor
		// when it is plotted

		List<String> history = new ArrayList<String>();

		for (int i = 1; i < mHistoryPanel.getItemCount(); ++i) {
			StringBuilder buffer = new StringBuilder(mHistoryPanel.getValueAt(i).getName());

			if (!mHistoryPanel.getValueAt(i).getDescription().equals(mHistoryPanel.getValueAt(i).getName()) &&
					!mHistoryPanel.getValueAt(i).getName().contains("Plot")) {
				buffer.append(" (").append(mHistoryPanel.getValueAt(i).getDescription()).append(")");
			}

			history.add(buffer.toString());
		}

		return history;
	}

	/**
	 * Open column groups.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void openColumnGroups() throws IOException {
		if (mInputFiles.size() == 0) {
			ModernMessageDialog.createWarningDialog(this, 
					"Please open an expression file.");

			return;
		}

		Path file = FileDialog.open(this).all().getFile(RecentFilesService.getInstance().getPwd());

		if (file == null) {
			return;
		}

		mColumnGroupsPanel.loadGroups(file);
	}

	/**
	 * Open row groups.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void openRowGroups() throws IOException {
		if (mInputFiles.size() == 0) {
			ModernMessageDialog.createWarningDialog(this, 
					"Please open an expression file.");

			return;
		}

		Path file = FileDialog.open(this).all().getFile(RecentFilesService.getInstance().getPwd());

		if (file == null) {
			return;
		}

		mRowGroupsPanel.loadGroups(file);
	}

	/**
	 * Browse for file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ParseException the parse exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	private void browseForFile() throws IOException, SAXException, ParserConfigurationException, InvalidFormatException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, UnsupportedLookAndFeelException {
		browseForFile(RecentFilesService.getInstance().getPwd());
	}

	/**
	 * Browse for file.
	 *
	 * @param pwd the working directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ParseException the parse exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	private void browseForFile(Path pwd) throws IOException, SAXException, ParserConfigurationException, InvalidFormatException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, UnsupportedLookAndFeelException {
		//openFile(BioInfDialog.openMatrixFile(this, pwd));

		openFiles(FileDialog.openFiles(this, pwd, mOpenFileFilters), OpenMode.NEW_WINDOW);
	}
	
	public boolean openFile(Path file) throws InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException, ParseException, FontFormatException, UnsupportedLookAndFeelException {
		return openFile(file, OpenMode.NEW_WINDOW);
	}
	
	/**
	 * Open file.
	 *
	 * @param file the file
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws ParseException the parse exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean openFile(Path file, OpenMode openMode) throws InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException, ParseException, FontFormatException, UnsupportedLookAndFeelException {
		return openFile(file, true, EMPTY_SKIP, -1, TextUtils.TAB_DELIMITER, openMode);
	}

	/**
	 * Open a single file in the window. If there is already a file loaded,
	 * a new window will be created.
	 *
	 * @param file 			The file to open.
	 * @param hasHeader 		Whether the file has a header.
	 * @param skipMatches the skip matches
	 * @param rowAnnotations How may of the first n columns are row
	 * 							annotations.
	 * @param delimiter the delimiter
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean openFile(Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter,
			OpenMode openMode) throws InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException, FontFormatException, UnsupportedLookAndFeelException {
		if (mInputFiles.size() > 0 && openMode == OpenMode.NEW_WINDOW) {
			MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());

			window.openFile(file, 
					hasHeader,
					skipMatches,
					rowAnnotations,
					delimiter,
					OpenMode.NEW_WINDOW);
			
			window.setVisible(true);

			return false;
		} else {
			boolean status = false;

			String ext = PathUtils.getFileExt(file);

			for (FileModule module : mOpenFileModuleMap.get(ext)) {
				AnnotationMatrix m = module.openFile(this, 
						file, 
						hasHeader,
						skipMatches,
						rowAnnotations,
						delimiter);

				if (m != null) {
					openMatrix(m);
					status = true;
				}
			}

			if (status) {
				mInputFiles.add(file);

				RecentFilesService.getInstance().add(file);

				setSubTitle(PathUtils.getName(file));
			}

			return status;
		}
	}
	


	/**
	 * Open file. Returns true if the file was opened in the current window.
	 *
	 * @param files the files
	 * @param hasHeader 		Whether the file has a header row or not.
	 * @param skipMatches the skip matches
	 * @param rowAnnotations  	how many row annotations there are. Setting to
	 * 						  	-1 will prompt user to enter number.
	 * @param delimiter the delimiter
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ParseException the parse exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean openFiles(List<Path> files,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter,
			OpenMode openMode) throws IOException, SAXException, ParserConfigurationException, InvalidFormatException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, UnsupportedLookAndFeelException {
		if (files.size() == 0) {
			return false;
		}

		boolean status = true;

		for (Path file : files) {
			status &= openFile(file, hasHeader, skipMatches, rowAnnotations, delimiter, openMode);
		}

		return status;
	}
	
	/**
	 * Open files.
	 *
	 * @param files the files
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ParseException the parse exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean openFiles(List<Path> files,
			OpenMode openMode) throws IOException, SAXException, ParserConfigurationException, InvalidFormatException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, UnsupportedLookAndFeelException {
		if (files.size() == 0) {
			return false;
		}

		boolean status = true;

		for (Path file : files) {
			status &= openFile(file, openMode);
		}

		return status;
	}
	
	//
	// Auto open
	//
	
	public boolean autoOpenFile(Path file) throws InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException, ParseException, FontFormatException, UnsupportedLookAndFeelException {
		return autoOpenFile(file, OpenMode.NEW_WINDOW);
	}
	
	/**
	 * Open file.
	 *
	 * @param file the file
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws ParseException the parse exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean autoOpenFile(Path file, OpenMode openMode) throws InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException, ParseException, FontFormatException, UnsupportedLookAndFeelException {
		return autoOpenFile(file, true, EMPTY_SKIP, -1, TextUtils.TAB_DELIMITER, openMode);
	}

	/**
	 * Open a single file in the window. If there is already a file loaded,
	 * a new window will be created. Users are not prompted for options.
	 *
	 * @param file 			The file to open.
	 * @param hasHeader 		Whether the file has a header.
	 * @param skipMatches the skip matches
	 * @param rowAnnotations How may of the first n columns are row
	 * 							annotations.
	 * @param delimiter the delimiter
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean autoOpenFile(Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter,
			OpenMode openMode) throws InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException, FontFormatException, UnsupportedLookAndFeelException {
		if (mInputFiles.size() > 0 && openMode == OpenMode.NEW_WINDOW) {
			MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());

			window.autoOpenFile(file, 
					hasHeader,
					skipMatches,
					rowAnnotations,
					delimiter,
					OpenMode.NEW_WINDOW);
			
			window.setVisible(true);

			return false;
		} else {
			boolean status = false;

			String ext = PathUtils.getFileExt(file);

			for (FileModule module : mOpenFileModuleMap.get(ext)) {
				AnnotationMatrix m = module.autoOpenFile(this, 
						file, 
						hasHeader,
						skipMatches,
						rowAnnotations,
						delimiter);

				if (m != null) {
					openMatrix(m);
					status = true;
				}
			}

			if (status) {
				mInputFiles.add(file);

				RecentFilesService.getInstance().add(file);

				setSubTitle(PathUtils.getName(file));
			}

			return status;
		}
	}
	


	/**
	 * Open file. Returns true if the file was opened in the current window.
	 *
	 * @param files the files
	 * @param hasHeader 		Whether the file has a header row or not.
	 * @param skipMatches the skip matches
	 * @param rowAnnotations  	how many row annotations there are. Setting to
	 * 						  	-1 will prompt user to enter number.
	 * @param delimiter the delimiter
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ParseException the parse exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean autoOpenFiles(List<Path> files,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations,
			String delimiter,
			OpenMode openMode) throws IOException, SAXException, ParserConfigurationException, InvalidFormatException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, UnsupportedLookAndFeelException {
		if (files.size() == 0) {
			return false;
		}

		boolean status = true;

		for (Path file : files) {
			status &= autoOpenFile(file, hasHeader, skipMatches, rowAnnotations, delimiter, openMode);
		}

		return status;
	}
	
	/**
	 * Open files.
	 *
	 * @param files the files
	 * @param openMode the open mode
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ParseException the parse exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws FontFormatException the font format exception
	 * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
	 */
	public boolean autoOpenFiles(List<Path> files,
			OpenMode openMode) throws IOException, SAXException, ParserConfigurationException, InvalidFormatException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, FontFormatException, UnsupportedLookAndFeelException {
		if (files.size() == 0) {
			return false;
		}

		boolean status = true;

		for (Path file : files) {
			status &= autoOpenFile(file, openMode);
		}

		return status;
	}
	
	//
	//
	//

	/**
	 * Creates a new MatCalc window and opens the matrix in it.
	 *
	 * @param m the m
	 */
	public void openMatrixInNewWindow(AnnotationMatrix m) {
		MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());

		window.openMatrix(m);

		window.setVisible(true);

	}

	/**
	 * Create a new MatCalc window and open the matrices in it.
	 *
	 * @param matrices the matrices
	 */
	public void openMatricesInNewWindow(List<AnnotationMatrix> matrices) {
		MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());

		window.openMatrices(matrices);

		window.setVisible(true);

	}

	/**
	 * Open matrix.
	 *
	 * @param m the m
	 */
	public void openMatrix(AnnotationMatrix m) {
		openMatrix(m, OpenMode.CURRENT_WINDOW);
	}

	/**
	 * Load a matrix into the main window. If there is a currently loaded
	 * matrix, a new window will be created. To add a matrix to the 
	 * existing history of this window, use addToHistory().
	 *
	 * @param m the m
	 * @param openMode the open mode
	 */
	public void openMatrix(AnnotationMatrix m, OpenMode openMode) {
		if (mMatrices.size() > 0 && openMode == OpenMode.NEW_WINDOW) {
			MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());

			window.openMatrix(m);

			window.setVisible(true);
		} else {
			mMatrices.add(m);

			if (m.getName().length() > 0) {
				setSubTitle(m.getName());
			} else {
				setSubTitle("Load matrix");
			}

			//createGroupsPanel(m);

			addToHistory(getSubTitle(), m);
		}
	}

	/**
	 * Open matrices.
	 *
	 * @param matrices the matrices
	 */
	public void openMatrices(List<AnnotationMatrix> matrices) {
		openMatrices(matrices, OpenMode.CURRENT_WINDOW);
	}

	/**
	 * Open matrices.
	 *
	 * @param matrices the matrices
	 * @param openMode the open mode
	 */
	public void openMatrices(List<AnnotationMatrix> matrices, 
			OpenMode openMode) {
		if (mMatrices.size() > 0 && openMode == OpenMode.NEW_WINDOW) {
			MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());

			window.openMatrices(matrices, openMode);

			window.setVisible(true);
		} else {
			
			for (AnnotationMatrix matrix : matrices) {
				openMatrix(matrix, openMode);
			}
			
			/*
			mMatrices.addAll(matrices);

			AnnotationMatrix m = matrices.get(0);

			if (m.getName().length() > 0) {
				setSubTitle(m.getName());
			} else {

				if (matrices.size() > 1) {
					setSubTitle("Load matrices");
				} else {
					setSubTitle("Load matrix");
				}
			}
			*/

			//createGroupsPanel(m);

			//addToHistory(getSubTitle(), m);
		}
	}

	/**
	 * Creates the groups panel.
	 */
	private void createGroupsPanel() {
		//AnnotationMatrix m = getCurrentMatrix();

		mColumnGroupsPanel = new ColumnGroupTreePanel(this);
		mRowGroupsPanel = new RowGroupTreePanel(this);

		TabsModel groupTabsModel = new TabsModel();
		groupTabsModel.addTab("ROWS", mRowGroupsPanel);
		groupTabsModel.addTab("COLUMNS", mColumnGroupsPanel);


		mGroupPanel = new SegmentTabsPanel(groupTabsModel, 80, 20);

		// Show the column groups by default
		groupTabsModel.changeTab(1);

		//addGroupsPane();

		addHistoryPane();
	}

	/**
	 * Estimate how many annotation columns are in the matrix.
	 *
	 * @param file the file
	 * @param hasHeader the has header
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int estimateRowAnnotations(Path file, boolean hasHeader) throws IOException {
		if (BioPathUtils.ext().xlsx().test(file) || BioPathUtils.ext().xls().test(file)) {
			return 0;
		}
		
		BufferedReader reader = FileUtils.newBufferedReader(file);

		int ret = 0;

		try {
			if (hasHeader) {
				reader.readLine();
			}

			List<String> tokens = Splitter.onTab().text(reader.readLine());

			for (int i = 0; i < tokens.size(); ++i) {
				if (TextUtils.isNumber(tokens.get(i))) {
					ret = i;
					break;
				}
			}
		} finally {
			reader.close();
		}

		return ret;
	}
	
	/**
	 * Guess the file delimiter.
	 *
	 * @param file the file
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String guessDelimiter(Path file) throws IOException {
		//
		// Work out if we need to skip annotation rows that should be
		// ignored
		
		int skipLines = FileUtils.countHeaderLines(file);

		BufferedReader reader = FileUtils.newBufferedReader(file);
		
		String ret = TextUtils.TAB_DELIMITER;
		
		try {
			// Skip past any header
			ReaderUtils.skipLines(reader, skipLines);
			
			char[] chars = reader.readLine().toCharArray();
			
			for (char c : chars) {
				//System.err.println("test " + c + " " + (c == '\t'));
				
				boolean found = false;
				
				switch (c) {
				case '\t':
					ret = TextUtils.TAB_DELIMITER;
					found = true;
					break;
				case ';':
					ret = TextUtils.SEMI_COLON_DELIMITER;
					found = true;
					break;
				case ',':
					ret = TextUtils.COMMA_DELIMITER;
					found = true;
					break;
				default:
					found = false;
					break;
				}
				
				if (found) {
					break;
				}
			}
		} finally {
			reader.close();
		}
		
		return ret;
	}

	/**
	 * Parses the txt matrix.
	 *
	 * @param file the file
	 * @param hasHeader the has header
	 * @param skipMatches the skip matches
	 * @param rowAnnotations the row annotations
	 * @param delimiter the delimiter
	 * @return the annotation matrix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AnnotationMatrix parseTxtMatrix(Path file, 
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations, 
			String delimiter) throws IOException {
		if (hasHeader) {
			if (rowAnnotations > 0) {
				return new DoubleMatrixParser(hasHeader, skipMatches, rowAnnotations, delimiter).parse(file);
			} else {
				return new MixedMatrixParser(hasHeader, skipMatches, rowAnnotations, delimiter).parse(file);
			}
		} else {
			return AnnotationMatrix.parseDynamicMatrix(file, hasHeader, skipMatches, rowAnnotations, TextUtils.TAB_DELIMITER);
		}
	}

	/**
	 * Adds the group pane to the layout if it is not already showing.
	 */
	/*
	private void addGroupsPane() {
		if (mInputFiles.size() == 0) {
			return;
		}

		if (mContentPane.getModel().getLeftTabs().containsTab("Groups")) {
			return;
		}

		mContentPane.getModel().addLeft(new SizableContentPane("Groups", 
				mGroupPanel, 
				250, 
				200, 
				500));
	}
	 */

	/**
	 * Adds the history pane to the layout if it is not already showing.
	 */
	private void addHistoryPane() {
		if (getTabsPane().getModel().getRightTabs().containsTab("History")) {
			return;
		}

		ModernVSplitPaneLine splitPane = new ModernVSplitPaneLine();

		splitPane.addComponent(mGroupPanel, 0.4);
		splitPane.addComponent(mHistoryPanel, 0.5);

		getTabsPane()
			.getModel()
			.addRightTab("History", new HTab("Groups", splitPane), 250, 200, 500);
	}

	/**
	 * Export.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	private void export() throws IOException, TranscoderException {
		export(RecentFilesService.getInstance().getPwd());
	}

	/**
	 * Export.
	 *
	 * @param pwd the working directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	private void export(Path pwd) throws IOException, TranscoderException {
		AnnotationMatrix matrix = getCurrentMatrix();

		if (matrix == null) {
			ModernMessageDialog.createWarningDialog(this, 
					"Please open an expression file.");

			return;
		}

		exportMatrix(pwd);
	}

	/**
	 * Export matrix.
	 *
	 * @param pwd the pwd
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	private void exportMatrix(Path pwd) throws IOException, TranscoderException {
		AnnotationMatrix matrix = getCurrentMatrix();

		if (matrix == null) {
			return;
		}

		Path file = null;

		/*
		if (mInputFiles.size() > 0) {
			file = FileDialog.save(this).filter(new XlsxGuiFileFilter(),
					new TsvGuiFileFilter(),
					new GctGuiFileFilter(),
					new EstGuiFileFilter()).suggested(PathUtils.getNameNoExt(mInputFile)).getFile(pwd);
		} else {
			file = FileDialog.save(this).filter(new XlsxGuiFileFilter(),
					new TsvGuiFileFilter(),
					new GctGuiFileFilter(),
					new EstGuiFileFilter()).getFile(pwd);
		}
		 */

		if (mInputFiles.size() > 0) {
			file = FileDialog
					.save(this)
					.filter(mSaveFileFilters)
					.setDefaultFilter("txt") //PathUtils.getFileExt(mInputFile)) //"txt")
					.suggested(PathUtils.getNameNoExt(getInputFile()))
					.getFile(pwd);
		} else {
			file = FileDialog
					.save(this)
					.filter(mSaveFileFilters)
					.setDefaultFilter("txt")
					.getFile(pwd);
		}

		if (file == null) {
			return;
		}

		if (FileUtils.exists(file)) {
			ModernMessageDialog.createFileReplaceDialog(this, 
					file, 
					new ExportCallBack(file, pwd));
		} else {
			save(file);
		}
	}

	/**
	 * Save.
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void save(Path file) throws IOException {
		AnnotationMatrix matrix = getCurrentMatrix();

		String ext = PathUtils.getFileExt(file);

		boolean status = false;

		for (FileModule module : mSaveFileModuleMap.get(ext)) {
			status |= module.saveFile(this, 
					file, 
					matrix);
		}



		/*
		if (ext.equals("gct")) {
			GctMatrix.writeGctMatrix(matrix, file);
			RecentFilesService.getInstance().add(file);
		} else if (ext.equals("est")) {
			// Use version 2 as more flexible
			AnnotationMatrix.writeEstMatrixV2(matrix, file);
			RecentFilesService.getInstance().add(file);
		} else if (ext.equals("xlsx")) {
			Excel.writeXlsx(matrix, file);
			RecentFilesService.getInstance().add(file);
		} else {
			// txt
			AnnotationMatrix.writeDataMatrix(matrix, file);
			RecentFilesService.getInstance().add(file);
		}
		 */

		// Set the sub title to indicate to the user that they saved the
		// file

		if (status) {
			setSubTitle(PathUtils.getName(file));

			ModernMessageDialog.createFileSavedDialog(this, file);
		}
	}

	/**
	 * Fold change.
	 */
	/*
	private void foldChange(Properties properties) throws IOException {
		if (mInputFiles.size() == 0) {
			ModernMessageDialog.createSpecifyInputDialog(this, 
					getAppInfo().getName());

			return;
		}

		XYSeriesGroup groups = mColumnGroupsPanel.getGroups();


		if (groups.getCount() == 0) {
			ModernMessageDialog.createDialog(this, 
					getAppInfo().getName(), 
					CREATE_GROUPS_MESSAGE, 
					MessageDialogType.WARNING);

			return;
		}

		AnnotationMatrix m = getCurrentMatrix();

		TTestDialog dialog = new TTestDialog(this, m, groups);

		dialog.setVisible(true);

		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return;
		}

		// We are only interested in the opened matrix
		// without transformations.

		if (dialog.getReset()) {
			resetHistory();
		}

		if (m == null) {
			return;
		}

		XYSeries g1 = dialog.getGroup1(); // new Group("g1");
		XYSeries g2 = dialog.getGroup2(); //new Group("g2");

		groups = new XYSeriesGroup();
		groups.add(g1);
		groups.add(g2);

		double minExp = 1;


		boolean isLog2Data = false;
		boolean log2Data = false; //dialog.getLog2Transform();
		boolean equalVariance = false; //dialog.getEqualVariance();
		boolean plot = false; //dialog.getCreatePlot();


		foldChange(m, 
				minExp,
				g1, 
				g2,
				isLog2Data,
				log2Data, 
				equalVariance, 
				plot,
				properties);
	}
	 */

	/**
	 * Fold change.
	 *
	 * @param m the m
	 * @param minExp the min exp
	 * @param g1 the g1
	 * @param g2 the g2
	 * @param isLog2Data the is log2 data
	 * @param log2Data the log2 data
	 * @param equalVariance the equal variance
	 * @param plot the plot
	 * @param properties the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/*
	public void foldChange(AnnotationMatrix m,
			double minExp,
			XYSeries g1, 
			XYSeries g2,
			boolean isLog2Data,
			boolean log2Data,
			boolean equalVariance,
			boolean plot,
			Properties properties) throws IOException {

		XYSeriesGroup groups = new XYSeriesGroup();
		groups.add(g1);
		groups.add(g2);

		AnnotationMatrix mColumnFiltered = addToHistory("Extract grouped columns", 
				AnnotatableMatrix.copyInnerColumns(m, groups));


		AnnotationMatrix mlog2;

		if (isLog2Data || log2Data) {
			mlog2 = MatrixOperations.log2(MatrixOperations.min(mColumnFiltered,  minExp));
			addToHistory("Log 2", mlog2);
		} else {
			mlog2 = mColumnFiltered;
		}

		List<Double> foldChanges;

		if (isLog2Data|| log2Data) {
			foldChanges = NumericalMatrix.logFoldChange(mlog2, g1, g2);
		} else {
			foldChanges = NumericalMatrix.foldChange(mlog2, g1, g2);
		}

		// filter by fold changes
		// filter by fdr

		String name = isLog2Data || log2Data ? "Log2 Fold Change" : "Fold Change";

		AnnotationMatrix mfoldchanges = new AnnotatableMatrix(mlog2);
		mfoldchanges.setRowAnnotations(name, foldChanges);
		addToHistory(name, mfoldchanges);

		//AnnotationMatrix mfoldchanges = addFlowItem(isLog2Data || log2Data ? "Log2 Fold Change" : "Fold Change", 
		//		new RowAnnotationMatrixView(mlog2, 
		//				isLog2Data|| log2Data ? "Log2 Fold Change" : "Fold Change", 
		//						ArrayUtils.toObjects(foldChanges)));

		List<Double> zscores = 
				NumericalMatrix.diffGroupZScores(mfoldchanges, g1, g2);

		AnnotationMatrix mzscores = new AnnotatableMatrix(mfoldchanges);
		mzscores.setRowAnnotations("Z-score", zscores);

		addToHistory("Add row z-scores", mzscores);

		//AnnotationMatrix mzscores = addFlowItem("Add row z-scores", 
		//		new RowAnnotationMatrixView(mfoldchanges, 
		//				"Z-score", 
		//				ArrayUtils.toObjects(zscores)));


		List<IndexedValue<Integer, Double>> zscoresIndexed = 
				IndexedValueInt.index(zscores);

		List<IndexedValue<Integer, Double>> posZScores;

		posZScores = CollectionUtils.reverseSort(CollectionUtils.subList(zscoresIndexed, 
				IndexedValueInt.ge(zscoresIndexed, 0)));

		List<IndexedValue<Integer, Double>> negZScores;

		negZScores = CollectionUtils.sort(CollectionUtils.subList(zscoresIndexed, 
				IndexedValueInt.lt(zscoresIndexed, 0)));

		// Now make a list of the new zscores in the correct order,
		// positive decreasing, negative, decreasing
		List<IndexedValue<Integer, Double>> sortedZscores = 
				CollectionUtils.append(posZScores, negZScores);

		// Put the zscores in order

		List<Integer> indices = IndexedValue.indices(sortedZscores);

		AnnotationMatrix mDeltaSorted = 
				AnnotatableMatrix.copyInnerRows(mzscores, indices);
		addToHistory("Sort by row z-score", mDeltaSorted); //new RowFilterMatrixView(mzscores, indices));


		AnnotationMatrix mNormalized = 
				MatrixOperations.groupZScore(mDeltaSorted, groups);
		addToHistory("Normalize expression within groups", mNormalized); //new GroupZScoreMatrixView(mDeltaSorted, groups));



		//AnnotationMatrix mMinMax = addFlowItem("Min/max threshold", 
		//		"min: " + Plot.MIN_STD + ", max: "+ Plot.MAX_STD,
		//		new MinMaxBoundedMatrixView(mNormalized, 
		//				Plot.MIN_STD, 
		//				Plot.MAX_STD));

		//AnnotationMatrix mStandardized = 
		//		addFlowItem("Row normalize", new RowNormalizedMatrixView(mMinMax));

		if (!plot) {
			return;
		}

		List<String> history = getTransformationHistory();

		addToHistory(new TTestPlotMatrixTransform(this, mNormalized, groups, history, properties));
	}
	 */








	/**
	 * Generate a heatmap from the current matrix.
	 *
	 * @param properties the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/*
	public void heatMap(Properties properties) throws IOException {
		if (mInputFiles.size() == 0) {
			ModernMessageDialog.createSpecifyInputDialog(this, 
					getAppInfo().getName());

			return;
		}

		AnnotationMatrix m = getCurrentMatrix();

		if (m == null) {
			return;
		}

		if (m.getRowCount() > MAX_DISPLAY_ROWS) {
			ModernMessageDialog.createDialog(this, 
					getAppInfo().getName(), 
					"Matrices with more than " + Formatter.number().format(MAX_DISPLAY_ROWS) + " rows are too large to be plotted.",
					MessageDialogType.WARNING);

			return;
		}

		if (m.getColumnCount() > MAX_DISPLAY_ROWS) {
			ModernMessageDialog.createDialog(this, 
					getAppInfo().getName(), 
					"Matrices with more than " + Formatter.number().format(MAX_DISPLAY_ROWS) + " columns are too large to be plotted.",
					MessageDialogType.WARNING);

			return;
		}

		XYSeriesGroup groups = mColumnGroupsPanel.getGroups();

		XYSeriesGroup rowGroups = mRowGroupsPanel.getGroups();

		List<String> history = getTransformationHistory();

		addToHistory(new HeatMapPlotMatrixTransform(this, 
				m, 
				groups, 
				rowGroups, 
				history,
				properties));
	}
	 */

	/*
	private void pieChart() throws IOException {
		AnnotationMatrix m = getCurrentMatrix();

		pieChart(m, "title", false);
	}

	private void pieChart(AnnotationMatrix m,
			String rowAnnotationName,
			boolean group) throws IOException {

		XYSeriesCollection allSeries = new XYSeriesCollection();

		ColorCycle colorCycle = new ColorCycle();

		for (int i = 0; i < m.getRowCount(); ++i) {
			XYSeries series = new XYSeries(m.getRowAnnotationText(rowAnnotationName, i));

			series.getFillStyle().setColor(colorCycle.next());

			for (int j = 0; j < m.getColumnCount(); ++j) {
				series.add(m.get(i, j));

				// skip additional values if we are not grouping
				if (!group) {
					break;
				}
			}

			allSeries.add(series);
		}

		Graph2dLayerCanvas canvas = PlotFactory.createPiePlot(allSeries);

		GraphProperties gp = canvas.getGraphSpace();

		//gp.getPlotLayout().setPlotSize(new Dimension(800, 800));


		// How big to make the x axis
		//double min = Mathematics.min(log2FoldChanges);
		//double max = Mathematics.max(log2FoldChanges);


		//gp.getXAxis().autoSetLimits(min, max);
		//gp.getXAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
		//gp.getXAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min, max, inc));
		gp.getXAxis().getTitle().setText("Series");


		//min = Mathematics.min(minusLog10PValues);
		//max = Mathematics.max(minusLog10PValues);

		//System.err.println("my " + min + " " + max);

		//gp.getYAxis().autoSetLimits(min, max);
		//gp.getYAxis().getMajorTicks().set(Linspace.evenlySpaced(min, max, inc));
		//gp.getYAxis().getMajorTickMarks().setNumbers(Linspace.evenlySpaced(min, max, inc));
		gp.getYAxis().getTitle().setText("Count");

		addToHistory(new PieChartMatrixTransform(this, 
				m, 
				canvas));
	}
	 */

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.window.ModernWindow#close()
	 */
	@Override
	public void close() {
		Temp.deleteTempFiles();

		super.close();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernSelectionListener#selectionChanged(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void selectionChanged(ChangeEvent e) {
		if (mHistoryPanel.getSelectedIndex() == -1) {
			return;
		}

		//System.err.println("selected " + transformList.getSelectedIndex());

		AnnotationMatrix matrix = getCurrentMatrix();

		if (matrix == null) {
			return;
		}

			mColumnGroupsPanel.setMatrix(matrix);
			mRowGroupsPanel.setMatrix(matrix);

			mMatrixTable = new MatrixTable(mZoomModel);

			MatrixTableModel model = new EditableMatrixTableModel(matrix);

			mMatrixTable.setModel(model);

			//ContainerCanvas zoomCanvas = 
			//		new ModernTableFrameCanvas(new ZoomCanvas(mMatrixTable, mZoomModel));

			ModernScrollPane mTableScrollPane = new ModernScrollPane(mMatrixTable);
			mTableScrollPane.getVScrollBar().setPadding(5, 0);
			mTableScrollPane.getHScrollBar().setPadding(5, 0);
			//mTableScrollPane.configureTable(mMatrixTable);

			ModernSpreadsheetBar bar = new ModernSpreadsheetBar(mMatrixTable);

			ModernComponent c = new ModernComponent();

			c.setHeader(bar);
			c.setBody(mTableScrollPane);
			//c.setBorder(ModernWidget.DOUBLE_BORDER);

			setCard(c);

			// Highlight A1
			mMatrixTable.getCellSelectionModel().setSelection(0, 0);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.math.matrix.transform.MatrixTransformListener#matrixTransformChanged(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void matrixTransformChanged(ChangeEvent e) {
		mHistoryPanel.repaint();
	}

	/**
	 * Gets the groups.
	 *
	 * @return the groups
	 */
	public XYSeriesGroup getGroups() {
		return mColumnGroupsPanel.getGroups();
	}

	/**
	 * Gets the row groups.
	 *
	 * @return the row groups
	 */
	public XYSeriesGroup getRowGroups() {
		return mRowGroupsPanel.getGroups();
	}

	/**
	 * Gets the input file.
	 *
	 * @return the input file
	 */
	public Path getInputFile() {
		if (mInputFiles.size() > 0) {
			return mInputFiles.get(mInputFiles.size() - 1);
		} else {
			return null;
		}
	}

	/**
	 * Gets the history panel.
	 *
	 * @return the history panel
	 */
	public MatCalcHistoryPanel getHistoryPanel() {
		return mHistoryPanel;
	}

	/**
	 * Gets the center pane.
	 *
	 * @return the center pane
	 */
	public ModernHContentPane getCenterPane() {
		return getTabsPane();
	}

	/**
	 * Gets the selected column. If no column is selected,
	 * Integer.MIN_VALUE is returned.
	 *
	 * @return the selected column
	 */
	public int getSelectedColumn() {
		List<Integer> columns = getSelectedColumns();

		if (columns != null && columns.size() > 0) {
			return columns.get(0);
		} else {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * Gets the selected columns.
	 *
	 * @return the selected columns
	 */
	public List<Integer> getSelectedColumns() {
		System.err.println("selected columns " + CollectionUtils.toList(mMatrixTable.getColumnModel().getSelectionModel()));

		if (mMatrixTable == null) {
			return Collections.emptyList();
		}

		return CollectionUtils.toList(mMatrixTable.getSelectedColumns());
	}

	/**
	 * Gets the selected row.
	 *
	 * @return the selected row
	 */
	public int getSelectedRow() {
		List<Integer> rows = getSelectedRows();

		if (rows != null && rows.size() > 0) {
			return rows.get(0);
		} else {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * Gets the selected columns.
	 *
	 * @return the selected columns
	 */
	public List<Integer> getSelectedRows() {
		if (mMatrixTable == null) {
			return Collections.emptyList();
		}

		return CollectionUtils.toList(mMatrixTable.getSelectedRows());
	}


	/**
	 * Gets the current plot window.
	 *
	 * @return the current plot window
	 */
	public Graph2dWindow getCurrentPlotWindow() {
		Graph2dWindow currentPlotWindow = null;

		for (ModernWindow window : WindowService.getInstance()) {
			if (window instanceof Graph2dWindow) {
				currentPlotWindow = (Graph2dWindow)window;
			}
		}

		return currentPlotWindow;
	}

	/**
	 * Creates the group warning dialog.
	 *
	 * @param window the window
	 */
	public static void createGroupWarningDialog(ModernWindow window) {
		ModernMessageDialog.createDialog(window, 
				MainMatCalcWindow.CREATE_GROUPS_MESSAGE, 
				MessageDialogType.WARNING);
	}

	/**
	 * Gets the zoom model.
	 *
	 * @return the zoom model
	 */
	public ZoomModel getZoomModel() {
		return mZoomModel;
	}


}
