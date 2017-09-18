/**
normalize() * Copyright 2016 Antony Holmes
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
package edu.columbia.rdf.matcalc.toolbox.math;

import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;

// TODO: Auto-generated Javadoc
/**
 * The class ZScoreModule.
 */
public class ZScoreModule extends CalcModule implements ModernClickListener {
	
	/**
	 * The member window.
	 */
	private MainMatCalcWindow mWindow;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Z-score";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mWindow = window;

		ModernButton button = new RibbonLargeButton("z-score", 
				UIService.getInstance().loadIcon("z_score", UIService.ICON_SIZE_32),
				"z-score", 
				"z-score values.");
		button.addClickListener(this);
		mWindow.getRibbon().getToolbar("Transform").getSection("Z-score").add(button);
		
		button = new RibbonLargeButton("Row", 
				UIService.getInstance().loadIcon("z_score", UIService.ICON_SIZE_32),
				"Row z-score", 
				"z-score rows.");
		button.setClickMessage("Row z-score");
		button.addClickListener(this);
		mWindow.getRibbon().getToolbar("Transform").getSection("Z-score").add(button);

	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		if (e.getMessage().equals("z-score")) {
			mWindow.addToHistory("z-score", "z-score", MatrixOperations.zscore(mWindow.getCurrentMatrix())); //new ZScoreMatrixTransform(this, getCurrentMatrix()));
		} else if (e.getMessage().equals("Row z-score")) {
			mWindow.addToHistory("Row z-score", "Row z-score", MatrixOperations.rowZscore(mWindow.getCurrentMatrix())); //addFlowItem(new ZScoreRowsMatrixTransform(this, getCurrentMatrix()));
		} else {
			// do nothing
		}
	}
}
