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
package edu.columbia.rdf.matcalc.toolbox.plot.heatmap.legacy;

import java.awt.Color;
import java.util.List;

import javax.swing.Box;

import org.jebtk.core.MinMax;
import org.jebtk.core.Properties;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationModel;
import org.jebtk.graphplot.figure.heatmap.ColorNormalizationType;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnLabelProperties;
import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelPosition;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelProperties;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxPanel;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.collapsepane.AbstractCollapsePane;
import org.jebtk.modern.collapsepane.ModernSubCollapsePane;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.colormap.ColorMap;
import org.jebtk.modern.graphics.colormap.ColorMapModel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.tabs.TabsModel;
import org.jebtk.modern.widget.ModernTwoStateWidget;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.jebtk.modern.zoom.ZoomModel;

import edu.columbia.rdf.matcalc.figure.BlockSizeControl;
import edu.columbia.rdf.matcalc.figure.CheckControl;
import edu.columbia.rdf.matcalc.figure.ColoredPlotControl;
import edu.columbia.rdf.matcalc.figure.ColumnLabelPositionPlotElement;
import edu.columbia.rdf.matcalc.figure.FormatPlotPane;
import edu.columbia.rdf.matcalc.figure.GroupsPlotControl;
import edu.columbia.rdf.matcalc.figure.MatrixGroupControl;
import edu.columbia.rdf.matcalc.figure.PlotConstants;
import edu.columbia.rdf.matcalc.figure.RowLabelControl;
import edu.columbia.rdf.matcalc.figure.ScaleControl;
import edu.columbia.rdf.matcalc.figure.TreeControl;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.ScaleModel;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.cluster.legacy.ClusterCanvas;

/**
 * The class HeatMapPanel.
 */
public class HeatMapPanel extends FormatPlotPane
    implements ModernClickListener, ChangeListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member canvas.
   */
  protected PlotBox mCanvas;

  /**
   * The member matrix.
   */
  protected DataFrame mMatrix;

  /**
   * The member zoom model.
   */
  private ZoomModel mZoomModel;

  // private StandardizationChooser standardizationChooser =
  // new StandardizationChooser();

  /**
   * The member grid element.
   */
  protected ColoredPlotControl mGridElement;

  /**
   * The member outline element.
   */
  private ColoredPlotControl mOutlineElement;

  /**
   * The member border element.
   */
  protected ColoredPlotControl mBorderElement;

  /**
   * The member rows element.
   */
  protected RowLabelControl mRowsElement;

  /**
   * The member columns element.
   */
  protected ColoredPlotControl mColumnsElement;


  /** The m check show. */
  private ModernTwoStateWidget mCheckShow;

  /**
   * The member aspect ratio element.
   */
  protected BlockSizeControl mAspectRatioElement;

  /**
   * The member column label position element.
   */
  protected ColumnLabelPositionPlotElement mColumnLabelPositionElement;

  /**
   * The member intensity element.
   */
  protected ScaleControl mScaleElement;

  /**
   * The member groups element.
   */
  protected GroupsPlotControl mGroupsElement;

  /**
   * The member history.
   */
  protected List<String> mHistory;

  /**
   * The member color standardization model.
   */
  protected ColorNormalizationModel mColorStandardizationModel;

  /**
   * The member properties.
   */
  protected Properties mProperties;

  /**
   * The member color map model.
   */
  protected ColorMapModel mColorMapModel;

  /** The m scale model. */
  protected ScaleModel mScaleModel;

  /** The m groups model. */
  protected XYSeriesModel mGroupsModel;

  /** The m group display control. */
  private MatrixGroupControl mGroupDisplayControl;

  /** The m count groups. */
  protected CountGroups mCountGroups;

  /** The m row groups model. */
  protected XYSeriesModel mRowGroupsModel;

  /** The m row cluster. */
  protected Cluster mRowCluster;

  /** The m column cluster. */
  protected Cluster mColumnCluster;

  private ModernRibbonWindow mParent;

  /**
   * Instantiates a new heat map panel.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param groupsModel the groups model
   * @param rowGroupsModel the row groups model
   * @param countGroups the count groups
   * @param history the history
   * @param zoomModel the zoom model
   * @param colorMapModel the color map model
   * @param colorStandardizationModel the color standardization model
   * @param scaleModel the scale model
   * @param contentModel the content model
   * @param properties the properties
   */
  public HeatMapPanel(ModernRibbonWindow parent, DataFrame matrix,
      XYSeriesModel groupsModel, XYSeriesModel rowGroupsModel,
      CountGroups countGroups, List<String> history, ZoomModel zoomModel,
      ColorMapModel colorMapModel,
      ColorNormalizationModel colorStandardizationModel, ScaleModel scaleModel,
      TabsModel contentModel, Properties properties) {
    this(parent, matrix, null, null, groupsModel, rowGroupsModel, countGroups,
        history, zoomModel, colorMapModel, colorStandardizationModel,
        scaleModel, contentModel, properties);
  }

  /**
   * Instantiates a new heat map panel.
   *
   * @param parent the parent
   * @param matrix the matrix
   * @param rowCluster the row cluster
   * @param columnCluster the column cluster
   * @param groupsModel the groups model
   * @param rowGroupsModel the row groups model
   * @param countGroups the count groups
   * @param history the history
   * @param zoomModel the zoom model
   * @param colorMapModel the color map model
   * @param colorStandardizationModel the color standardization model
   * @param scaleModel the scale model
   * @param contentModel the content model
   * @param properties the properties
   */
  public HeatMapPanel(ModernRibbonWindow parent, DataFrame matrix,
      Cluster rowCluster, Cluster columnCluster, XYSeriesModel groupsModel,
      XYSeriesModel rowGroupsModel, CountGroups countGroups,
      List<String> history, ZoomModel zoomModel, ColorMapModel colorMapModel,
      ColorNormalizationModel colorStandardizationModel, ScaleModel scaleModel,
      TabsModel contentModel, Properties properties) {
    mParent = parent;
    // mContent = contentModel;

    mRowCluster = rowCluster;
    mColumnCluster = columnCluster;
    mCountGroups = countGroups;
    mMatrix = matrix;
    mGroupsModel = groupsModel;
    mRowGroupsModel = rowGroupsModel;
    mHistory = history;
    mZoomModel = zoomModel;
    mColorMapModel = colorMapModel;
    mColorStandardizationModel = colorStandardizationModel;
    mScaleModel = scaleModel;
    mProperties = properties;

    //
    // Heat map
    //

    AbstractCollapsePane rightPanel;

    Box box;

    rightPanel = new ModernSubCollapsePane();

    box = VBox.create();

    mCheckShow = new ModernCheckSwitch("Show",
        properties.getAsBool("plot.heatmap.visible"));
    mCheckShow.addClickListener(this);
    mCheckShow.setAlignmentY(TOP_ALIGNMENT);
    box.add(mCheckShow);

    box.add(ModernPanel.createVGap());

    // Box box2 = VBox.create();
    // box2.setBorder(BorderService.getInstance().createLeftBorder(10));

    mGridElement = new ColoredPlotControl(parent, "Grid",
        properties.getAsColor("plot.grid-color"),
        properties.getAsBool("plot.show-grid-color"));
    mGridElement.addClickListener(this);
    box.add(mGridElement);

    box.add(ModernPanel.createVGap());

    mOutlineElement = new ColoredPlotControl(parent, "Outline",
        properties.getAsColor("plot.outline-color"),
        properties.getAsBool("plot.show-outline-color"));
    mOutlineElement.addClickListener(this);
    box.add(mOutlineElement);

    box.add(ModernPanel.createVGap());

    mBorderElement = new ColoredPlotControl(parent, "Border",
        properties.getAsColor("plot.border-color"),
        properties.getAsBool("plot.border-color-enabled"));
    mBorderElement.addClickListener(this);
    box.add(mBorderElement);

    // box.setAlignmentY(TOP_ALIGNMENT);
    // box.add(box2);

    box.setBorder(LARGE_BORDER);

    // box.add(ModernPanel.createVGap());
    // rightPanel.addTab("Heat Map", box, true);

    rightPanel.addTab(PlotConstants.LABEL_HEATMAP, box, true);

    box = VBox.create();
    mAspectRatioElement = new BlockSizeControl(
        (DoubleDim) properties.getProperty("plot.block-size"));
    mAspectRatioElement.addChangeListener(this);
    box.add(mAspectRatioElement);
    box.setBorder(LARGE_BORDER);
    rightPanel.addTab(PlotConstants.LABEL_BLOCK_SIZE, box, true);

    box = VBox.create();
    mScaleElement = new ScaleControl(scaleModel);
    box.add(mScaleElement);
    box.add(UI.createVGap(10));
    box.add(new CheckControl(parent, "Legend", properties,
        "plot.show-legend"));
    box.add(new CheckControl(parent, "Color Bar", properties,
        "plot.show-colorbar"));
    box.add(new CheckControl(parent, "Summary", properties,
        "plot.show-summary"));
    
    box.setBorder(LARGE_BORDER);
    rightPanel.addTab(PlotConstants.LABEL_LEGEND, box, true);

    mGroupTabsModel.addTab("Plot", rightPanel);

    // TEST
    // JScrollPane s = new JScrollPane(box);
    // s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    // mGroupTabsModel.addTab("Rows", s);

    //
    // Rows
    //

    rightPanel = new ModernSubCollapsePane();

    // Only show this option if we have clusters to show
    if (mRowCluster != null) {
      box = VBox.create();
      box.add(new TreeControl(parent, properties, "plot.tree.hoz"));
      box.add(UI.createVGap(5));
      box.setBorder(LARGE_BORDER);
      rightPanel.addTab("Row Tree", box, true);
    }

    box = VBox.create();

    mRowsElement = new RowLabelControl(parent, matrix, RowLabelPosition.RIGHT,
        properties.getAsBool("plot.show-feature-counts"),
        properties.getAsBool("plot.show-row-labels"));

    box.add(mRowsElement);

    box.setBorder(LARGE_BORDER);

    rightPanel.addTab("Row Labels", box, true);

    mGroupTabsModel.addTab("Rows", rightPanel); // new
                                                // ModernScrollPane(rightPanel).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER));

    //
    // Columns
    //

    rightPanel = new ModernSubCollapsePane();

    if (mColumnCluster != null) {
      box = VBox.create();
      box.add(new TreeControl(parent, properties, "plot.tree.vert"));
      box.add(UI.createVGap(5));

      box.setBorder(LARGE_BORDER);

      rightPanel.addTab("Tree", box, true);
    }

    box = VBox.create();

    mColumnsElement = new ColoredPlotControl(parent, "Show", Color.BLACK);
    box.add(mColumnsElement);

    // box2 = VBox.create();

    box.add(new CheckControl(parent, "Color by group", properties,
        "plot.labels.color-by-group"));

    // box.add(UI.createVGap(5));

    mColumnLabelPositionElement = new ColumnLabelPositionPlotElement();

    box.add(mColumnLabelPositionElement);

    // box2.setBorder(BorderService.getInstance().createLeftBorder(10));

    // box.add(box2);

    box.setBorder(LARGE_BORDER);

    rightPanel.addTab("Column Labels", box, true);

    box = VBox.create();

    mGroupsElement = new GroupsPlotControl(parent, Color.BLACK, properties);
    box.add(mGroupsElement);

    // Ability to switch on groups is only available to unclustered
    // data
    if (mColumnCluster == null && mGroupsModel.getTotalCount() > 0) {
      box.add(UI.createVGap(10));

      mGroupDisplayControl = new MatrixGroupControl(mGroupsModel);

      box.add(mGroupDisplayControl);

      // rightPanel.addTab("Group Display", mGroupDisplayControl, true);
    }

    box.setBorder(LARGE_BORDER);

    rightPanel.addTab("Groups", box, true);

    mGroupTabsModel.addTab("Columns", rightPanel); // new
                                                   // ModernScrollPane(rightPanel).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER));

    mGroupsElement.addClickListener(this);
    // standardizationChooser.addClickListener(this);
    // mIntensityElement.addClickListener(this);
    mColumnLabelPositionElement.addClickListener(this);
    mRowsElement.addClickListener(this);
    mColumnsElement.addClickListener(this);

    mColorMapModel.addChangeListener(this);
    mColorStandardizationModel.addChangeListener(this);
    mScaleModel.addChangeListener(this);
    mProperties.addChangeListener(this);

    if (mGroupsModel != null) {
      mGroupsModel.addChangeListener(this);
    }

    mGroupTabsModel.changeTab(0);
  }

  /**
   * Update.
   */
  @Override
  public void update() {
    // PlotConstants.MAX_STD; // / scale;

    MinMax norm = MinMax.create(-mScaleModel.get(), mScaleModel.get());

    // Create version of the matrix with only the groups of interest
    XYSeriesGroup seriesOfInterest = new XYSeriesGroup();

    for (XYSeries series : mGroupsModel) {
      if (mGroupsModel.getVisible(series)) {
        seriesOfInterest.add(series);
      }
    }

    XYSeriesGroup rowSeriesOfInterest = new XYSeriesGroup();

    for (XYSeries series : mRowGroupsModel) {
      if (mRowGroupsModel.getVisible(series)) {
        rowSeriesOfInterest.add(series);
      }
    }

    DataFrame m = createMatrix(mMatrix,
        seriesOfInterest,
        rowSeriesOfInterest,
        norm);

    display(m, seriesOfInterest, rowSeriesOfInterest, norm);
  }

  /**
   * Create a matrix (normalize for coloring etc) without filtering groups.
   *
   * @param m the m
   * @param min the min
   * @param max the max
   * @return the annotation matrix
   */
  public DataFrame createMatrix(DataFrame m, MinMax norm) {
    return createMatrix(m,
        XYSeriesGroup.EMPTY_GROUP,
        XYSeriesGroup.EMPTY_GROUP,
        norm);
  }

  /**
   * Creates the matrix.
   *
   * @param m the m
   * @param groupsOfInterest the groups of interest
   * @param rowGroupsOfInterest the row groups of interest
   * @param min the min
   * @param max the max
   * @return the annotation matrix
   */
  public DataFrame createMatrix(DataFrame m,
      XYSeriesGroup groupsOfInterest,
      XYSeriesGroup rowGroupsOfInterest,
      MinMax norm) {
    DataFrame ret = m;

    // double plotMin = Plot.MIN_STD;
    // double plotMax = Plot.MAX_STD;

    // For z-transforms default to being centered about 0
    // double scale = mIntensityModel.getScale(); //mIntensityModel.get(); //

    switch (mColorStandardizationModel.get().getType()) {
    case ZSCORE_MATRIX:
      ret = MatrixOperations.zscore(ret); // new MatrixZTransformView(mMatrix);
      break;
    case ZSCORE_ROW:
      ret = MatrixOperations.rowZscore(ret); // new
                                             // RowZTransformMatrixView(mMatrix);
      break;
    case ZSCORE_COLUMN:
      ret = MatrixOperations.columnZscore(ret); // new
                                                // ColumnZTransformMatrixView(mMatrix);
      break;
    case NORMALIZE:
      if (Double.isNaN(mColorStandardizationModel.get().getMax())) {
        // Auto use the min and max to normalize
        norm.setMin(MatrixOperations.min(ret));
        norm.setMax(MatrixOperations.max(ret));
      } else {
        // Use fixed bounds to normalize on.
        norm.setMin(mColorStandardizationModel.get().getMin());
        norm.setMax(mColorStandardizationModel.get().getMax());
      }

      break;
    default:
      // For the scale of no standardization, i.e we do not adjust
      // or normalize
      // min = MatrixOperations.min(ret);
      // max = MatrixOperations.max(ret);

      break;
    }

    if (groupsOfInterest.size() > 0) {
      ret = DataFrame.copyColumns(ret, groupsOfInterest);
    }

    if (mColorStandardizationModel.get()
        .getType() != ColorNormalizationType.NONE) {
      // min /= scale;
      // max /= scale;

      ret = MatrixOperations.normalize(ret, norm);
    }

    return ret;
  }

  /**
   * Display.
   *
   * @param m the m
   * @param groupsOfInterest the groups of interest
   * @param rowGroupsOfInterest the row groups of interest
   * @param min the min
   * @param max the max
   */
  public void display(DataFrame m,
      XYSeriesGroup groupsOfInterest,
      XYSeriesGroup rowGroupsOfInterest,
      MinMax norm) {
    ColorMap colorMap = mColorMapModel.get();

    RowLabelProperties rowLabelProperties = new RowLabelProperties();

    rowLabelProperties.showFeatureCounts = mRowsElement.getShowFeatureCount();

    rowLabelProperties.show = mRowsElement.isSelected();

    rowLabelProperties.color = mRowsElement.getSelectedColor();

    rowLabelProperties.position = mRowsElement.getPosition();

    mRowsElement.setShowAnnotations(rowLabelProperties.showAnnotations);

    ColumnLabelProperties columnLabelProperties = new ColumnLabelProperties();

    columnLabelProperties.show = mColumnsElement.isSelected();

    columnLabelProperties.color = mColumnsElement.getSelectedColor();

    columnLabelProperties.position = mColumnLabelPositionElement.getPosition();


    mProperties.updateProperty("plot.grid-color",
        mGridElement.getSelectedColor());
    mProperties.updateProperty("plot.show-grid-color",
        mGridElement.isSelected());

    mProperties.updateProperty("plot.outline-color",
        mOutlineElement.getSelectedColor());
    mProperties.updateProperty("plot.show-outline-color",
        mOutlineElement.isSelected());

    mProperties.updateProperty("plot.border-color",
        mBorderElement.getSelectedColor());
    mProperties.updateProperty("plot.show-border-color",
        mBorderElement.isSelected());

    // mProperties.updateProperty("plot.aspect-ratio",
    // mAspectRatioElement.getAspectRatio());

    mProperties.updateProperty("plot.block-size",
        mAspectRatioElement.getBlockSize());

    mProperties.updateProperty("plot.colormap", colorMap);

    mProperties.updateProperty("plot.heatmap.visible", mCheckShow.isSelected());

    mCanvas = createCanvas(m,
        groupsOfInterest,
        rowGroupsOfInterest,
        norm,
        rowLabelProperties,
        columnLabelProperties);

    display(mCanvas);
  }

  /**
   * Creates the canvas.
   *
   * @param m the m
   * @param groupsOfInterest the groups of interest
   * @param rowGroupsOfInterest the row groups of interest
   * @param min the min
   * @param max the max
   * @param rowLabelProperties the row label properties
   * @param columnLabelProperties the column label properties
   * @return the modern plot canvas
   */
  public PlotBox createCanvas(DataFrame m,
      XYSeriesGroup groupsOfInterest,
      XYSeriesGroup rowGroupsOfInterest,
      MinMax norm,
      RowLabelProperties rowLabelProperties,
      ColumnLabelProperties columnLabelProperties) {
    return new ClusterCanvas(m, mRowCluster, mColumnCluster, groupsOfInterest,
        rowGroupsOfInterest, mCountGroups, mHistory, norm.getMin(),
        norm.getMax(), rowLabelProperties, columnLabelProperties,
        mGroupsElement.getProperties(), mProperties);
  }

  /**
   * Display.
   *
   * @param canvas the canvas
   */
  public void display(PlotBox canvas) {
    PlotBoxPanel panel = new PlotBoxPanel(canvas);

    panel.setZoomModel(mZoomModel);

    // BackgroundCanvas backgroundCanvas = new BackgroundCanvas(zoomCanvas);

    ModernScrollPane scrollPane = new ModernScrollPane(panel)
        .setVScrollBarLocation(ScrollBarLocation.FLOATING)
        .setHScrollBarLocation(ScrollBarLocation.FLOATING);

    mParent.setCard(new ModernComponent(scrollPane, ModernWidget.DOUBLE_BORDER));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.ui.plot.CanvasPanel#getCanvas()
   */
  @Override
  public PlotBox getCanvas() {
    return mCanvas;
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
    update();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
   */
  @Override
  public void changed(ChangeEvent e) {
    update();
  }
}
