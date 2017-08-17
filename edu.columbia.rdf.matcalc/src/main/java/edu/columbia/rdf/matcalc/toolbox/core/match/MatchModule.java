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
package edu.columbia.rdf.matcalc.toolbox.core.match;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.modern.UIService;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.widget.tooltip.ModernToolTip;
import org.jebtk.modern.window.WindowService;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.CalcModule;


// TODO: Auto-generated Javadoc
/**
 * Can compare a column of values to another list to see what is common and
 * record this in a new column next to the reference column. Useful for
 * doing overlaps and keeping data in a specific order in a table.
 *
 * @author Antony Holmes Holmes
 *
 */
public class MatchModule extends CalcModule implements ModernClickListener  {

	/**
	 * The constant NO_MATCH.
	 */
	private static final String NO_MATCH = "no_match";

	/**
	 * The member match button.
	 */
	private RibbonLargeButton mMatchButton = 
			new RibbonLargeButton("Match In Files", UIService.getInstance().loadIcon("match", 24));

	/**
	 * The member window.
	 */
	private MainMatCalcWindow mWindow;

	/* (non-Javadoc)
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return "Match";
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.matcalc.MainMatCalcWindow)
	 */
	@Override
	public void init(MainMatCalcWindow window) {
		mWindow = window;

		mMatchButton.setToolTip(new ModernToolTip("Match", 
				"Find matches between columns in two files."), mWindow.getRibbon().getToolTipModel());

		window.getRibbon().getHomeToolbar().getSection("Search").add(mMatchButton);

		mMatchButton.addClickListener(this);

	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.modern.event.ModernClickEvent)
	 */
	@Override
	public final void clicked(ModernClickEvent e) {
		if (e.getSource().equals(mMatchButton)) {
			match();
		}
	}

	/**
	 * Match.
	 */
	private void match() {
		List<Integer> columns = mWindow.getSelectedColumns();


		if (columns == null || columns.size() == 0) {
			ModernMessageDialog.createDialog(mWindow, 
					"You must select a column to match on.", 
					MessageDialogType.WARNING);
			return;
		}
		
		AnnotationMatrix m = mWindow.getCurrentMatrix();
		
		int c = columns.get(0);
		
		MatchDialog inputDialog = new MatchDialog(mWindow, m, c);

		inputDialog.setVisible(true);

		if (inputDialog.getStatus() == ModernDialogStatus.CANCEL) {
			return;
		}
		
		MainMatCalcWindow window = 
				(MainMatCalcWindow)WindowService.getInstance().findByName(inputDialog.getWindowName());
		
		AnnotationMatrix copyM = window.getCurrentMatrix();
		
		List<String> ids = 
				copyM.columnAsText(inputDialog.getMatchColumn());
		
		List<String> values = 
				copyM.columnAsText(inputDialog.getReplaceColumn());
		
		Map<String, Set<String>> idMap = 
				CollectionUtils.createMapSet(TextUtils.toLowerCase(ids), values);

	
		AnnotationMatrix ret = 
				AnnotatableMatrix.createAnnotatableMatrix(m.getRowCount(), m.getColumnCount() + 2);

		// Copy before column
		AnnotationMatrix.copyColumns(m, 0, c, ret);
		
		// Shift the rest by one column so we can insert the results
		AnnotationMatrix.copyColumns(m, c + 1, ret, c + 3);
		
		//ret.setColumnName(c + 1, "Match In " + window.getSubTitle()  + " - " + copyM.getColumnName(inputDialog.getReplaceColumn()));
		ret.setColumnName(c + 1, "Number Of Matches");
		ret.setColumnName(c + 2, "Matches From " + window.getSubTitle()  + " - " + copyM.getColumnName(inputDialog.getReplaceColumn()));
		
		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getText(i, c).toLowerCase();
			
			if (idMap.containsKey(id)) {
				ret.set(i, c + 1, idMap.get(id).size());
				ret.set(i, c + 2, TextUtils.scJoin(CollectionUtils.sort(idMap.get(id))));
			} else {
				ret.set(i, c + 1, 0);
				ret.set(i, c + 2, NO_MATCH);
			}
		}
		
		mWindow.addToHistory("Match", ret);
	}
}
