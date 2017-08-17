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

import org.jebtk.graphplot.figure.properties.LegendPosition;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.widget.ModernWidget;

// TODO: Auto-generated Javadoc
/**
 * The Class LegendPositionCombo.
 */
public class LegendPositionCombo extends ModernComboBox {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Instantiates a new legend position combo.
	 */
	public LegendPositionCombo() {
		addScrollMenuItem("Top Left");
		addScrollMenuItem("Top Middle");
		addScrollMenuItem("Top Right");
		addScrollMenuItem("Center Left");
		addScrollMenuItem("Center");
		addScrollMenuItem("Center Right");
		addScrollMenuItem("Bottom Left");
		addScrollMenuItem("Bottom Middle");
		addScrollMenuItem("Bottom Right");
		
		UI.setSize(this, ModernWidget.BIG_SIZE);
	}
	
	/**
	 * Sets the position.
	 *
	 * @param p the new position
	 */
	public void setPosition(LegendPosition p) {
		// Change the position, but do not trigger click event
		switch(p) {
		case TOP_LEFT:
			changeSelectedIndex(0);
			break;
		case TOP_MIDDLE:
			changeSelectedIndex(1);
			break;
		case TOP_RIGHT:
			changeSelectedIndex(2);
			break;
		case CENTER_LEFT:
			changeSelectedIndex(3);
			break;
		case CENTER:
			changeSelectedIndex(4);
			break;
		case CENTER_RIGHT:
			changeSelectedIndex(5);
			break;
		case BOTTOM_LEFT:
			changeSelectedIndex(6);
			break;
		case BOTTOM_MIDDLE:
			changeSelectedIndex(7);
			break;
		case BOTTOM_RIGHT:
			changeSelectedIndex(8);
			break;
		default:
			changeSelectedIndex(2);
			break;
		}
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public LegendPosition getPosition() {
		switch (getSelectedIndex()) {
		case 0:
			return LegendPosition.TOP_LEFT;
		case 1:
			return LegendPosition.TOP_MIDDLE;
		case 2:
			return LegendPosition.TOP_RIGHT;
		case 3:
			return LegendPosition.CENTER_LEFT;
		case 4:
			return LegendPosition.CENTER;
		case 5:
			return LegendPosition.CENTER_RIGHT;
		case 6:
			return LegendPosition.BOTTOM_LEFT;
		case 7:
			return LegendPosition.BOTTOM_MIDDLE;
		case 8:
			return LegendPosition.BOTTOM_RIGHT;
		default:
			return LegendPosition.TOP_RIGHT;
		}
	}
}
