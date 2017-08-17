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
package edu.columbia.rdf.matcalc.toolbox.core.shift;

import java.text.ParseException;

import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.MatrixOperations;
import org.jebtk.math.ui.matrix.MatrixTransforms;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.input.ModernInputDialog;
import org.jebtk.modern.ribbon.RibbonLargeButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;


// TODO: Auto-generated Javadoc
/**
 * Row name.
 *
 * @author Antony Holmes Holmes
 */
public class ShiftModule extends CalcModule implements ModernClickListener {
	/**
	 * The member window.
	 */
	private MainMatCalcWindow mWindow;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Shift";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mWindow = window;

		ModernButton button;

		button = new RibbonLargeButton("Min", 
				UIService.getInstance().loadIcon("min", 32),
				"Min Filter", 
				"Set all values below minimum to minimum.");
		button.addClickListener(this);
		mWindow.getRibbon().getToolbar("Transform").getSection("Shift").add(button);

		button = new RibbonLargeButton("Min/Max", 
				UIService.getInstance().loadIcon("min_max", 32),
				"Min/Max Filter", 
				"Set all values below minimum to minimum and all values above maximum to maximum.");
		button.addClickListener(this);
		mWindow.getRibbon().getToolbar("Transform").getSection("Shift").add(button);

		button = new RibbonLargeButton("Min Shift", 
				UIService.getInstance().loadIcon("min_shift", 32),
				"Min Shift", 
				"Shift all values so that the minimum is the greater of zero or itself.");
		button.addClickListener(this);
		mWindow.getRibbon().getToolbar("Transform").getSection("Shift").add(button);

	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		AnnotationMatrix m = mWindow.getCurrentMatrix();
		
		if (e.getMessage().equals("Min")) {
			mWindow.addToHistory("Minimum Threshold", 
					MatrixTransforms.minThreshold(mWindow, m, 1));
		} else if (e.getMessage().equals("Min/Max")) {
			mWindow.addToHistory("Minimum Threshold", 
					MatrixTransforms.minMaxThreshold(mWindow, m, 1, 10000)); //addFlowItem(new MinMaxMatrixTransform(this, getCurrentMatrix(), 1, 10000));
		} else if (e.getMessage().equals("Min Shift")) {
			mWindow.addToHistory("Min Shift", 
					MatrixTransforms.subtract(mWindow, m, 1));
		} else if (e.getMessage().equals("Median Shift")) {
			mWindow.addToHistory("Median Shift", 
					MatrixOperations.divide(m, MatrixOperations.median(m))); //.collapseMaxMedian(m, rowAnnotation)MatrixTransforms.medianShift(mWindow, m));
		}
	}
	
	/**
	 * Min shift.
	 *
	 * @throws ParseException the parse exception
	 */
	private void minShift() throws ParseException {
		AnnotationMatrix m = mWindow.getCurrentMatrix();
		
		double min = MatrixOperations.min(m);
		
		System.err.println("min " + min);
		
		ModernInputDialog dialog = new ModernInputDialog(mWindow, 
				"Minimum",
				"Minimum Expression", 
				"1");
		
		dialog.setVisible(true);
		
		if (dialog.isCancelled()) {
			return;
		}
		
		double add = TextUtils.parseDouble(dialog.getText());
		
		AnnotationMatrix ret = MatrixOperations.add(MatrixOperations.subtract(m, min), add);
		
		mWindow.addToHistory("Minimum Threshold", ret);
	}
}
