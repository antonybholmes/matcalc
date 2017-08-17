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
package edu.columbia.rdf.matcalc.toolbox.math;

import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.modern.UIService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;

// TODO: Auto-generated Javadoc
/**
 * The class LogModule.
 */
public class PowerModule extends CalcModule implements ModernClickListener {
	
	/**
	 * The member window.
	 */
	private MainMatCalcWindow mWindow;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Power";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mWindow = window;

		RibbonLargeButton button = new RibbonLargeButton("x^y",
				UIService.getInstance().loadIcon("log", 24),
				"x^y", 
				"Compute the yth power of each cell.");
		button.addClickListener(this);
		mWindow.getRibbon().getToolbar("Transform").getSection("Transform").add(button);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		if (e.getMessage().equals("x^y")) {
			mWindow.addToHistory("x^y", 
					power(mWindow, mWindow.getCurrentMatrix(), 2)); //new Log2MatrixTransform(this, getCurrentMatrix(), 1));
		}
	}
	
	/**
	 * Power.
	 *
	 * @param parent the parent
	 * @param matrix the matrix
	 * @param base the base
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix power(ModernWindow parent, 
			AnnotationMatrix matrix, 
			int base) {
		PowerDialog dialog = new PowerDialog(parent, base);
		
		dialog.setVisible(true);
		
		if (dialog.getStatus() == ModernDialogStatus.OK) {
			return MatrixOperations.power(matrix, dialog.getBase());
		} else {
			return null;
		}
	}
}
