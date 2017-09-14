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

import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.ui.matrix.transform.MatrixTransform;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowService;

// TODO: Auto-generated Javadoc
/**
 * Create a plot from a matrix.
 * 
 * @author Antony Holmes Holmes
 *
 */
public abstract class PlotMatrixTransform extends MatrixTransform {
	
	/**
	 * The member window reference.
	 */
	private String mWindowReference;

	/**
	 * Instantiates a new plot matrix transform.
	 *
	 * @param parent the parent
	 * @param name the name
	 * @param inputMatrix the input matrix
	 */
	public PlotMatrixTransform(ModernWindow parent,
			String name,
			AnnotationMatrix inputMatrix) {
		super(parent, name, inputMatrix);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.math.matrix.transform.MatrixTransform#apply()
	 */
	@Override
	public void apply() {
		ModernWindow window = WindowService.getInstance().findByName(mWindowReference);
		
		System.err.println("search for plot window " + mWindowReference + " " + (window == null));
		
		if (window != null) {
			// if it does, focus it
			WindowService.setFocus(window);
		} else {
			// else create a new window
			window = createWindow();

			mWindowReference = window.getTitle();
			
			window.setVisible(true);
		}
		
		super.apply();
	}
	
	/**
	 * Creates the window.
	 *
	 * @return the modern window
	 */
	protected abstract ModernWindow createWindow();
}