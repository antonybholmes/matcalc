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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.graphplot.figure.PlotLayer;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.ModernCanvasListener;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.window.ModernWindow;

/**
 * The class AxisVisibleControl.
 */
public class PlotLayerVisibleControl extends HBox implements ModernClickListener, ModernCanvasListener {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member check box.
	 */
	private ModernCheckSwitch mCheckBox;

	/**
	 * The member axis.
	 */
	private PlotLayer mLayer;
	
	
	/**
	 * Instantiates a new axis visible control.
	 *
	 * @param parent the parent
	 * @param layer the layer
	 */
	public PlotLayerVisibleControl(ModernWindow parent,
			PlotLayer layer) {
		mLayer = layer;
		
		mCheckBox = new ModernCheckSwitch(layer.getName());
		mCheckBox.setSelected(layer.getVisible());
		
		add(mCheckBox);
		add(ModernPanel.createHGap());
		
		mCheckBox.addClickListener(this);
		
		mLayer.addCanvasListener(this);
	}
	

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return mCheckBox.isSelected();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.ui.event.ModernClickEvent)
	 */
	@Override
	public void clicked(ModernClickEvent e) {
		mLayer.setVisible(mCheckBox.isSelected());
	}


	/* (non-Javadoc)
	 * @see org.abh.common.ui.graphics.ModernCanvasListener#canvasChanged(org.abh.common.event.ChangeEvent)
	 */
	@Override
	public void canvasChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.abh.common.ui.graphics.ModernCanvasListener#redrawCanvas(org.abh.common.event.ChangeEvent)
	 */
	@Override
	public void redrawCanvas(ChangeEvent e) {
		mCheckBox.setSelected(mLayer.getVisible());
	}


	/* (non-Javadoc)
	 * @see org.abh.common.ui.graphics.ModernCanvasListener#canvasScrolled(org.abh.common.event.ChangeEvent)
	 */
	@Override
	public void canvasScrolled(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.abh.common.ui.graphics.ModernCanvasListener#canvasResized(org.abh.common.event.ChangeEvent)
	 */
	@Override
	public void canvasResized(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

}
