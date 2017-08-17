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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.properties.LegendPosition;
import org.jebtk.graphplot.figure.properties.LegendProperties;
import org.jebtk.modern.UI;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonModeProperty;
import org.jebtk.modern.ribbon.RibbonSize;
import org.jebtk.modern.text.TextProperty;
import org.jebtk.modern.widget.ModernClickWidget;

// TODO: Auto-generated Javadoc
/**
 * The class LegendPositionPicker.
 */
public class LegendPositionPicker extends ModernClickWidget implements RibbonModeProperty, TextProperty {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The member position.
	 */
	private LegendPosition mPosition = LegendPosition.TOP_RIGHT;

	/**
	 * The member legend.
	 */
	private LegendProperties mLegend;
	
	/**
	 * The constant HEIGHT.
	 */
	private static final int HEIGHT = 8;
	
	/**
	 * The constant WIDTH.
	 */
	private static final int WIDTH = 2 * HEIGHT;

	/**
	 * The constant GAP.
	 */
	private static final int GAP = 2;
	
	/**
	 * The highlighted.
	 */
	private int highlighted = -1;
	
	/**
	 * The class LegendEvents.
	 */
	private class LegendEvents implements ChangeListener {

		/* (non-Javadoc)
		 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			setPosition(mLegend.getPosition());
		}
		
	}
	
	/**
	 * The class MouseEvents.
	 */
	private class MouseEvents extends MouseAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			switch (highlighted) {
			case 0:
				mPosition = LegendPosition.TOP_LEFT;
				break;
			case 1:
				mPosition = LegendPosition.TOP_MIDDLE;
				break;
			case 2:
				mPosition = LegendPosition.TOP_RIGHT;
				break;
			case 3:
				mPosition = LegendPosition.CENTER_LEFT;
				break;
			case 4:
				mPosition = LegendPosition.CENTER;
				break;
			case 5:
				mPosition = LegendPosition.CENTER_RIGHT;
				break;
			case 6:
				mPosition = LegendPosition.BOTTOM_LEFT;
				break;
			case 7:
				mPosition = LegendPosition.BOTTOM_MIDDLE;
				break;
			case 8:
				mPosition = LegendPosition.BOTTOM_RIGHT;
				break;
			}
			
			change();
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			highlighted = -1;
			
			repaint();
		}
	}
	
	/**
	 * The class MouseMotionEvents.
	 */
	private class MouseMotionEvents implements MouseMotionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			int r = (e.getY() - PADDING) / (HEIGHT + GAP);
			int c = (e.getX() - PADDING) / (WIDTH + GAP);
			
			int i = r * 3 + c;
			
			if (i == highlighted) {
				return;
			}
			
			highlighted = i;
			
			repaint();
		}
	}
	
	/**
	 * Instantiates a new legend position picker.
	 *
	 * @param legend the legend
	 */
	public LegendPositionPicker(LegendProperties legend) {
		mLegend = legend;
		
		legend.addChangeListener(new LegendEvents());
		addMouseListener(new MouseEvents());
		addMouseMotionListener(new MouseMotionEvents());
		
		UI.setSize(this, WIDTH * 3 + GAP * 2 + DOUBLE_PADDING, Ribbon.COMPACT_BUTTON_HEIGHT);
	}
	
	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	private void setPosition(LegendPosition position) {
		mPosition = position;
		
		repaint();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.ModernWidget#drawBackground(java.awt.Graphics2D)
	 */
	@Override
	public void drawBackground(Graphics2D g2) {
		//paintOutlined(g2, getRect());
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.ModernWidget#drawForegroundAA(java.awt.Graphics2D)
	 */
	public void drawForegroundAAText(Graphics2D g2) {
		int x = PADDING;
		int y = PADDING;
		
		int pc = 0;
		
		LegendPosition p;
		
		for (int i = 0; i < 3; ++i) {
			x = PADDING;
			
			for (int j = 0; j < 3; ++j) {
				switch (pc) {
				case 0:
					p = LegendPosition.TOP_LEFT;
					break;
				case 1:
					p = LegendPosition.TOP_MIDDLE;
					break;
				case 3:
					p = LegendPosition.CENTER_LEFT;
					break;
				case 4:
					p = LegendPosition.CENTER;
					break;
				case 5:
					p = LegendPosition.CENTER_RIGHT;
					break;
				case 6:
					p = LegendPosition.BOTTOM_LEFT;
					break;
				case 7:
					p = LegendPosition.BOTTOM_MIDDLE;
					break;
				case 8:
					p = LegendPosition.BOTTOM_RIGHT;
					break;
				default:
					p = LegendPosition.TOP_RIGHT;
					break;
				}
				
				if (p == mPosition) {
					paintHighlightedOutlined(g2, new Rectangle(x, y, WIDTH, HEIGHT));
				} else if (pc == highlighted) {
					paintOutlinedFocused(g2, new Rectangle(x, y, WIDTH, HEIGHT));
				} else {
					paintOutlined(g2, new Rectangle(x, y, WIDTH, HEIGHT));
				}
				
				x += WIDTH + GAP;
				
				++pc;
			}
			
			y += HEIGHT + GAP;
		}
	}
	
	/**
	 * Change.
	 */
	private void change() {
		repaint();
		
		mLegend.setPosition(mPosition);
		
		doClick();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.text.TextProperty#getText()
	 */
	@Override
	public String getText() {
		return mPosition.toString();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.text.TextProperty#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.abh.common.ui.ui.ribbon.RibbonModeProperty#setMode(org.abh.common.ui.ui.ribbon.RibbonMode)
	 */
	@Override
	public void setSize(RibbonSize mode) {
		// TODO Auto-generated method stub
		
	}
}
