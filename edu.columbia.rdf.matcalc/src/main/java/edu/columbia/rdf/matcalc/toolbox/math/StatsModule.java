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

import java.text.ParseException;

import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernPopupMenu;
import org.jebtk.modern.ribbon.RibbonLargeDropDownButton;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;


// TODO: Auto-generated Javadoc
/**
 * Row name.
 *
 * @author Antony Holmes Holmes
 */
public class StatsModule extends CalcModule implements ModernClickListener {
	/**
	 * The member window.
	 */
	private MainMatCalcWindow mWindow;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Statistics";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mWindow = window;

		ModernPopupMenu popup = new ModernPopupMenu();

		popup.addMenuItem(new ModernIconMenuItem("Matrix sum"));
		popup.addMenuItem(new ModernIconMenuItem("Matrix mean"));
		popup.addMenuItem(new ModernIconMenuItem("Matrix median"));
		popup.addMenuItem(new ModernIconMenuItem("Matrix mode"));
		popup.addSeparator();
		popup.addMenuItem(new ModernIconMenuItem("Row sums"));
		popup.addMenuItem(new ModernIconMenuItem("Row means"));
		popup.addMenuItem(new ModernIconMenuItem("Row medians"));
		popup.addMenuItem(new ModernIconMenuItem("Row modes"));


		// The default behaviour is to do a log2 transform.
		RibbonLargeDropDownButton button = new RibbonLargeDropDownButton("Statistics", popup);
		button.setChangeText(false);
		button.setToolTip("Statistics", "Statistical functions.");
		mWindow.getRibbon().getToolbar("Formulas").getSection("Functions").add(button);
		button.addClickListener(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		if (e.getMessage().equals("Matrix sum")) {
			sum();
		} else if (e.getMessage().equals("Matrix mean")) {
			mean();
		} else if (e.getMessage().equals("Matrix median")) {
			median();
		} else if (e.getMessage().equals("Matrix mode")) {
			mode();
		} else if (e.getMessage().equals("Row sums")) {
			rowSum();
		} else if (e.getMessage().equals("Row means")) {
			rowMean();
		} else if (e.getMessage().equals("Row medians")) {
			rowMedian();
		} else if (e.getMessage().equals("Row modes")) {
			rowMode();
		} else {

		}
	}

	private void sum() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		double mean = MatrixOperations.sum(m);

		StatsDialog dialog = new StatsDialog(mWindow, 
				"Sum",
				mean);

		dialog.setVisible(true);
	}
	
	/**
	 * Min shift.
	 *
	 * @throws ParseException the parse exception
	 */
	private void mean() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		double mean = MatrixOperations.mean(m);

		StatsDialog dialog = new StatsDialog(mWindow, 
				"Mean",
				mean);

		dialog.setVisible(true);
	}

	private void median() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		double mean = MatrixOperations.median(m);

		StatsDialog dialog = new StatsDialog(mWindow, 
				"Median",
				mean);

		dialog.setVisible(true);
	}

	private void mode() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		double mean = MatrixOperations.mode(m);

		StatsDialog dialog = new StatsDialog(mWindow, 
				"Mode",
				mean);

		dialog.setVisible(true);
	}

	private void rowSum() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		if (m != null) {
			mWindow.addToHistory("Row sums", MatrixOperations.addRowSums(m));
		}
	}

	private void rowMean() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		if (m != null) {
			mWindow.addToHistory("Row means", MatrixOperations.addRowMeans(m));
		}
	}

	private void rowMedian() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		if (m != null) {
			mWindow.addToHistory("Row medians", MatrixOperations.addRowMedians(m));
		}
	}

	private void rowMode() {
		AnnotationMatrix m = mWindow.getCurrentMatrix();

		if (m != null) {
			mWindow.addToHistory("Row modes", MatrixOperations.addRowModes(m));
		}
	}
}