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
package edu.columbia.rdf.matcalc.figure;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.batik.transcoder.TranscoderException;
import org.jebtk.core.io.FileUtils;
import org.jebtk.graphplot.Image;
import org.jebtk.graphplot.MatrixGroupModel;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Graph2dStyleModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.icons.FormatPlot32VectorIcon;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.clipboard.ClipboardRibbonSection;
import org.jebtk.modern.contentpane.CloseableHTab;
import org.jebtk.modern.contentpane.ModernHContentPane;
import org.jebtk.modern.contentpane.SizableContentPane;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMapModel;
import org.jebtk.modern.graphics.icons.QuickSaveVectorIcon;
import org.jebtk.modern.graphics.icons.Raster32Icon;
import org.jebtk.modern.help.ModernAboutDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.io.SaveAsRibbonPanel;
import org.jebtk.modern.ribbon.QuickAccessButton;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.ribbon.RibbonMenuItem;
import org.jebtk.modern.widget.ModernClickWidget;
import org.jebtk.modern.widget.tooltip.ModernToolTip;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.ModernWindowConstructor;
import org.jebtk.modern.window.WindowRibbonSection;
import org.jebtk.modern.zoom.ModernStatusZoomSlider;
import org.jebtk.modern.zoom.ZoomModel;
import org.jebtk.modern.zoom.ZoomRibbonSection;

import edu.columbia.rdf.matcalc.colormap.ColorMapRibbonSection;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dStyleRibbonSection;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ColorStandardizationRibbonSection;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ScaleModel;

// TODO: Auto-generated Javadoc
/**
 * Window for showing 2D graphs such as a scatter plot.
 * 
 * @author Antony Holmes Holmes
 *
 */
public abstract class FigureWindow extends ModernRibbonWindow implements ModernWindowConstructor, ModernClickListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member save as panel.
	 */
	private SaveAsRibbonPanel mSaveAsPanel = new SaveAsRibbonPanel();

	/**
	 * The member zoom model.
	 */
	protected ZoomModel mZoomModel = new ZoomModel();

	/**
	 * The member color map model.
	 */
	protected ColorMapModel mColorMapModel = 
			new ColorMapModel();

	/**
	 * The member groups model.
	 */
	protected MatrixGroupModel mGroupsModel = 
			new MatrixGroupModel();


	//protected MultiPlotCanvas mCanvas = new MultiPlotCanvas();

	/**
	 * The content pane.
	 */
	protected ModernHContentPane mContentPane = 
			new ModernHContentPane();

	/**
	 * The member format pane.
	 */
	protected FormatPlotPane mFormatPane = null;

	/**
	 * The member color model.
	 */
	protected ColorNormalizationModel mColorModel =
			new ColorNormalizationModel();

	/** The m style model. */
	protected Graph2dStyleModel mStyleModel = new Graph2dStyleModel();

	/** The m window. */
	protected ModernWindow mWindow;

	/** The m scale model. */
	protected ScaleModel mScaleModel = new ScaleModel();

	/**
	 * The member figure.
	 */
	protected Figure mFigure = null;
	
	/**
	 * The constant NEXT_ID.
	 */
	private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
	
	/**
	 * The member id.
	 */
	private final int mId = NEXT_ID.getAndIncrement();

	/** The m allow style. */
	private boolean mAllowStyle;
	
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
		 * @see org.abh.common.ui.ui.dialog.DialogEventListener#statusChanged(org.abh.common.ui.ui.dialog.DialogEvent)
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
	 * Instantiates a new figure window.
	 *
	 * @param window the window
	 */
	public FigureWindow(ModernWindow window) {
		this(window, null, true);
	}

	/**
	 * Instantiates a new figure window.
	 *
	 * @param window the window
	 * @param figure the figure
	 * @param allowStyle the allow style
	 */
	public FigureWindow(ModernWindow window, Figure figure, boolean allowStyle) {
		super(window.getAppInfo());

		mWindow = window;
		mFigure = figure;
		mAllowStyle = allowStyle;

		setSubTitle("Figure " + mId);

		setup();
	}

	/**
	 * Setup.
	 */
	private void setup() {
		createRibbon();

		createUi();

		setSize(1280, 768);

		UI.centerWindowToScreen(this);
	}



	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.window.ModernWindowConstructor#createRibbon()
	 */
	public void createRibbon() {
		//RibbongetRibbonMenu() getRibbonMenu() = new RibbongetRibbonMenu()(0);
		RibbonMenuItem menuItem;

		menuItem = new RibbonMenuItem(UI.MENU_SAVE_AS);
		getRibbonMenu().addTabbedMenuItem(menuItem, mSaveAsPanel);

		getRibbonMenu().addDefaultItems(getAppInfo(), false);


		getRibbonMenu().addClickListener(this);


		ModernClickWidget button;

		//Ribbon2 ribbon = new Ribbon2();
		getRibbon().setHelpButtonEnabled(getAppInfo());

		button = new QuickAccessButton(UIService.getInstance().loadIcon(QuickSaveVectorIcon.class, 16));
		button.setClickMessage(UI.MENU_SAVE);
		button.setToolTip(new ModernToolTip("Save", 
				"Save the current image."));
		button.addClickListener(this);
		addQuickAccessButton(button);

		getRibbon().getToolbar("Plot").add(new ClipboardRibbonSection(getRibbon()));
		
		if (mAllowStyle) {
			getRibbon().getToolbar("Plot").add(new Graph2dStyleRibbonSection(getRibbon(), mStyleModel));
		}
		
		getRibbon().getToolbar("Color").addSection(new ColorMapRibbonSection(this, mColorMapModel, mScaleModel));
		getRibbon().getToolbar("Color").addSection(new ColorStandardizationRibbonSection(this, getRibbon(), mColorModel));


		button = new RibbonLargeButton("Format", 
				new Raster32Icon(new FormatPlot32VectorIcon()));
		button.addClickListener(this);
		getRibbon().getToolbar("View").getSection("Show").add(button);

		getRibbon().getToolbar("View").add(new ZoomRibbonSection(this, mZoomModel));
		getRibbon().getToolbar("View").add(new WindowRibbonSection(this, getRibbon()));

		getRibbon().setSelectedIndex(0);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.window.ModernWindow#createUi()
	 */
	public void createUi() {
		setBody(mContentPane);
		
		mStatusBar.addRight(new ModernStatusZoomSlider(mZoomModel));
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public final void clicked(ModernClickEvent e) {
		if (e.getMessage().equals(UI.MENU_SAVE)) {
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
		} else if (e.getMessage().equals("Format")) {
			addFormatPane();
		} else if (e.getMessage().equals(UI.MENU_ABOUT)) {
			ModernAboutDialog.show(this, getAppInfo());
		} else if (e.getMessage().equals(UI.MENU_CLOSE)) {
			close();
		} else {

		}
	}

	/**
	 * Adds the history pane to the layout if it is not already showing.
	 */
	private void addFormatPane() {
		if (mContentPane.getModel().getRightTabs().containsTab("Format")) {
			return;
		}

		mContentPane.getModel().getRightTabs().addTab(new SizableContentPane("Format", 
				new CloseableHTab("Format", mFormatPane, mContentPane), 300, 200, 500));
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
	 * @param pwd the pwd
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	private void export(Path pwd) throws IOException, TranscoderException {
		Path file = Image.saveFile(this, pwd);
		
		if (file == null) {
			return;
		}
		
		if (FileUtils.exists(file)) {
			//createFileExistsDialog(file, new ExportCallBack(file, pwd));
			
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
	 * @throws TranscoderException the transcoder exception
	 */
	private void save(Path file) throws IOException, TranscoderException {
		Image.write(getCanvas(), file);
		
		RecentFilesService.getInstance().setPwd(file.getParent());
		
		//createFileSavedDialog(file);
		
		ModernMessageDialog.createFileSavedDialog(this, file);
	}

	/**
	 * Gets the canvas.
	 *
	 * @return the canvas
	 */
	public abstract ModernPlotCanvas getCanvas();

	/**
	 * Gets the color normalization model.
	 *
	 * @return the color normalization model
	 */
	public ColorNormalizationModel getColorNormalizationModel() {
		return mColorModel;
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public Graph2dStyleModel getStyle() {
		return mStyleModel;
	}
}
