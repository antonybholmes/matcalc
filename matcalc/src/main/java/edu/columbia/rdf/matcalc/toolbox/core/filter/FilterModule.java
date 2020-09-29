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
package edu.columbia.rdf.matcalc.toolbox.core.filter;

import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.jebtk.core.collections.BooleanFixedStack;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.search.SearchStackOperator;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.FilterVectorIcon;
import org.jebtk.modern.graphics.icons.Raster24Icon;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.jebtk.modern.theme.ThemeService;
import org.jebtk.modern.tooltip.ModernToolTip;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * Can compare a column of values to another list to see what is common and
 * record this in a new column next to the reference column. Useful for doing
 * overlaps and keeping data in a specific order in a table.
 *
 * @author Antony Holmes
 *
 */
public class FilterModule extends Module implements ModernClickListener {

  /**
   * The member match button.
   */
  private RibbonLargeButton mFilterButton = new RibbonLargeButton(new Raster24Icon(new FilterVectorIcon(
      ThemeService.getInstance().getColors().getTheme(8), ThemeService.getInstance().getColors().getTheme(6))));

  /**
   * The member window.
   */
  private MainMatCalcWindow mWindow;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "Filter";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    mFilterButton.setToolTip(new ModernToolTip("Filter", "Filter rows in columns."));

    window.getRibbon().getToolbar("Data").getSection("Filter", true).add(mFilterButton);

    mFilterButton.addClickListener(this);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {
    try {
      filter();
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Match.
   *
   * @throws ParseException the parse exception
   */
  private void filter() throws ParseException {
    DataFrame m = mWindow.getCurrentMatrix();

    List<Integer> columns = mWindow.getSelectedColumns();

    if (columns.size() == 0) {
      ModernMessageDialog.createDialog(mWindow, "You must select a column to match on.", MessageDialogType.WARNING);
      return;
    }

    int c = columns.get(0);

    FilterDialog dialog = new FilterDialog(mWindow, m, c);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<ColumnFilter> columnFilters = dialog.getColumnFilters();

    Deque<FilterStackElement> stack = new ArrayDeque<FilterStackElement>();

    // True for and false for or
    Deque<Boolean> opStack = new ArrayDeque<Boolean>();

    // dummy true since all expression begin with true so that the
    // first filter is ANDed with true so that the infix expression
    // is built correctly
    stack.push(new FilterStackElement(true));

    for (ColumnFilter columnFilter : columnFilters) {

      // Process ORs since they have a lower precedence than AND.
      if (!columnFilter.getLogical()) {
        // OR so remove any ands
        while (!opStack.isEmpty()) {
          // System.err.println("peek " + opStack.peek());

          if (opStack.peek()) {
            stack.push(new FilterStackElement(SearchStackOperator.AND));
            opStack.pop();
          } else {
            // Stop once we encounter ORs
            break;
          }
        }
      }

      stack.push(new FilterStackElement(columnFilter));

      opStack.push(columnFilter.getLogical());
    }

    while (!opStack.isEmpty()) {
      boolean op = opStack.pop();

      if (op) {
        stack.push(new FilterStackElement(SearchStackOperator.AND));
      } else {
        stack.push(new FilterStackElement(SearchStackOperator.OR));
      }
    }

    // Reverse the stack to get the evaluation order
    List<FilterStackElement> operators = CollectionUtils.reverse(CollectionUtils.toList(stack));

    // Create a master list of rows in sorted order
    List<Integer> rows = new ArrayList<Integer>(m.getRows());

    BooleanFixedStack resultStack = new BooleanFixedStack();

    for (int i = 0; i < m.getRows(); ++i) {
      String text = m.getText(i, c);

      double value = m.getValue(i, c);

      for (int j = 0; j < operators.size(); ++j) {
        FilterStackElement op = operators.get(j);

        // System.err.println("op " + op.op);

        switch (op.op) {
        case MATCH:
          boolean match = op.filter.getFilter().test(text, value);

          resultStack.push(match);
          break;
        case RESULT:
          resultStack.push(op.result);
          break;
        case AND:
          // We must strict AND rather than lazy and since we
          // need both operands to be removed from the stack
          resultStack.push(resultStack.pop() & resultStack.pop());
          break;
        case OR:
          resultStack.push(resultStack.pop() | resultStack.pop());
          break;
        default:
          // Do nothing
          break;
        }
      }

      // The last entry contains the result of the filter booleans

      if (resultStack.pop()) {
        rows.add(i);
      }
    }

    DataFrame ret = DataFrame.createDataFrame(rows.size(), m.getCols());

    DataFrame.copyColumnHeaders(m, ret);

    DataFrame.copyRows(m, rows, ret);

    mWindow.history().addToHistory("Filter matrix", ret);
  }
}
