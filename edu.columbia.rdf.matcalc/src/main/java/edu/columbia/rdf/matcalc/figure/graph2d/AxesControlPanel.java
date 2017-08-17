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
package edu.columbia.rdf.matcalc.figure.graph2d;

import javax.swing.JComponent;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.Layer;
import org.jebtk.graphplot.figure.MovableLayer;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.collapsepane.AbstractCollapsePane;
import org.jebtk.modern.collapsepane.ModernSubCollapsePane;
import org.jebtk.modern.graphics.ModernCanvasListener;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.matcalc.figure.AxesControl;
import edu.columbia.rdf.matcalc.figure.AxisControl;
import edu.columbia.rdf.matcalc.figure.FormatPlotPane;
import edu.columbia.rdf.matcalc.figure.PlotControl;


// TODO: Auto-generated Javadoc
/**
 * The class AxesControlPanel.
 */
public class AxesControlPanel extends FormatPlotPane {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m figure. */
	private Figure mFigure;

	/** The m parent. */
	private ModernRibbonWindow mParent;

	/**
	 * Instantiates a new axes control panel.
	 *
	 * @param parent the parent
	 * @param figure the figure
	 */
	public AxesControlPanel(ModernRibbonWindow parent, Figure figure) {
		mParent = parent;
		mFigure = figure;

		refresh();

		figure.getSubFigureZModel().addCanvasListener(new ModernCanvasListener() {

			@Override
			public void canvasChanged(ChangeEvent e) {
				refresh();
			}

			@Override
			public void redrawCanvas(ChangeEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void canvasScrolled(ChangeEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void canvasResized(ChangeEvent e) {
				//refresh();
			}});
	}

	/**
	 * Refresh.
	 */
	private void refresh() {
		//
		// Figures
		//

		JComponent c;

		AbstractCollapsePane figuresCollapsePane = new ModernSubCollapsePane();

		for (int fz : mFigure.getSubFigureZModel()) {
			SubFigure subFigure = mFigure.getSubFigureZModel().getAtZ(fz);

			AbstractCollapsePane axesCollapsePane = new ModernSubCollapsePane();

			for (GridLocation l : subFigure.getGridLocations()) {
				for (int z : subFigure.getGridLocations().get(l)) {
					Layer layer = subFigure.getGridLocations().get(l).getAtZ(z);

					if (layer instanceof Axes) {
						Axes axes = (Axes)layer;

						//TabsModel axisTabsModel = new TabsModel();

						//
						// Axes
						//
						
						AbstractCollapsePane axisCollapsePane = 
								new ModernSubCollapsePane();

						c = new AxesControl(mParent, axes);
						axisCollapsePane.addTab("Options", c, true);

						c = new AxisControl(mParent, axes.getX1Axis(), true);
						axisCollapsePane.addTab("X Axis", c, true);

						//c = new AxisControl(mParent, axes.getX2Axis(), true);
						//axisCollapsePane.addTab("X Axis 2", c);

						c = new AxisControl(mParent, axes.getY1Axis(), true);
						axisCollapsePane.addTab("Y Axis", c, true);

						//c = new AxisControl(mParent, axes.getY2Axis(), true);
						//axisCollapsePane.addTab("Y Axis 2", c);
						

						//TabsModel plotsTabsModel = new TabsModel();
						AbstractCollapsePane plotsCollapsePane = 
								new ModernSubCollapsePane();

						for (GridLocation bl : axes.getPlotZModel()) {
							for (int pz : axes.getPlotZModel().get(bl)) {
								MovableLayer al = axes.getPlotZModel().get(bl).getAtZ(pz);

								if (al instanceof Plot) {
									Plot plot = (Plot)al;

									plotsCollapsePane.addTab(plot.getName(), 
											new PlotControl(mParent, plot));
								}
							}
						}
						
						axisCollapsePane.addTab("Plots", plotsCollapsePane, true);
						
						axesCollapsePane.addTab(axes.getName(), axisCollapsePane, true);
					}
				}
			}
			
			figuresCollapsePane.addTab(subFigure.getName(), axesCollapsePane, true);
		}
		
		figuresCollapsePane.setExpanded(true);
		
		//figuresCollapsePane.setBorder(RIGHT_BORDER);
		ModernScrollPane scrollPane = new ModernScrollPane(figuresCollapsePane);
		scrollPane.setScrollBarPolicy(ScrollBarPolicy.NEVER, 
				ScrollBarPolicy.AUTO_SHOW);
		scrollPane.setBorder(BORDER);
		setBody(scrollPane); 
	}

	/* (non-Javadoc)
	 * @see org.matcalc.figure.FormatPlotPane#getCanvas()
	 */
	@Override
	public ModernPlotCanvas getCanvas() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.matcalc.figure.FormatPlotPane#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}
