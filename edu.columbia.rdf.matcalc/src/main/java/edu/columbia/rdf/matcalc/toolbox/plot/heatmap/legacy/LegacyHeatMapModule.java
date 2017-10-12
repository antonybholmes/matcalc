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

import java.io.IOException;
import java.util.List;

import org.jebtk.graphplot.figure.heatmap.legacy.CountGroups;
import org.jebtk.graphplot.figure.series.XYSeriesModel;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.UIService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.HeatMapProperties;

// TODO: Auto-generated Javadoc
/**
 * The class HeatMapModule.
 */
public class LegacyHeatMapModule extends CalcModule implements ModernClickListener {
	
	/**
	 * The member parent.
	 */
	private MainMatCalcWindow mParent;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Heat Map";
	}
	
	/* (non-Javadoc)
	 * @see org.matcalc.toolbox.CalcModule#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) {
		for (String arg : args) {
			if (arg.contains("plot")) {
				try {
					plot();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mParent = window;
		
		RibbonLargeButton button = new RibbonLargeButton("Heat Map", 
				UIService.getInstance().loadIcon("heatmap", 24),
				"Heat Map",
				"Generate a heat map.");
		button.addClickListener(this);

		mParent.getRibbon().getToolbar("Plot").getSection("Plot").add(button);
	}
	
	/**
	 * Creates the.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void plot() throws IOException {
		DataFrame m = mParent.getCurrentMatrix();

		XYSeriesModel groups = XYSeriesModel.create(mParent.getGroups());

		XYSeriesModel rowGroups = XYSeriesModel.create(mParent.getRowGroups());
		
		CountGroups countsGroup = CountGroups.defaultGroup(m);

		List<String> history = mParent.getTransformationHistory();
		
		mParent.addToHistory(new HeatMapPlotMatrixTransform(mParent, 
				m, 
				groups, 
				rowGroups, 
				countsGroup,
				history,
				new HeatMapProperties()));
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		try {
			plot();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	

}
