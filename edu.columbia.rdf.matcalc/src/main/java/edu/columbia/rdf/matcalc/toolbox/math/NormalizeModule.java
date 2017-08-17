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

import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.modern.UIService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcWinModule;
import edu.columbia.rdf.matcalc.toolbox.plot.heatmap.NormalizeDialog;

// TODO: Auto-generated Javadoc
/**
 * The class ZScoreModule.
 */
public class NormalizeModule extends CalcWinModule implements ModernClickListener {
	
	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Normalize";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		super.init(window);

		RibbonLargeButton button = new RibbonLargeButton("Normalize", 
				UIService.getInstance().loadIcon("normalize", 32),
				"Normalize",
				"Normalize values between 0 and 1.");
		button.addClickListener(this);
		window.getRibbon().getToolbar("Transform").getSection("Transform").add(button);

	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		NormalizeDialog dialog = new NormalizeDialog(mWindow);
		
		dialog.setVisible(true);
		
		if (dialog.getStatus() == ModernDialogStatus.OK) {
			if (dialog.getAuto()) {
				mWindow.addToHistory("Normalize", MatrixOperations.normalize(mWindow.getCurrentMatrix()));
			} else {
				mWindow.addToHistory("Normalize", MatrixOperations.normalize(mWindow.getCurrentMatrix(), dialog.getMin(), dialog.getMax()));
			}
		}
	}
}
