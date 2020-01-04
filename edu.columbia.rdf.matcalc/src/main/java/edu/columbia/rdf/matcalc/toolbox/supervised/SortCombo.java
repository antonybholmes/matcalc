package edu.columbia.rdf.matcalc.toolbox.supervised;

import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;

public class SortCombo extends ModernComboBox {

  private static final long serialVersionUID = 1L;

  public SortCombo() {
    addMenuItem("Fold Change");
    addMenuItem("Z-score");

    setSelectedIndex(1);

    UI.setSize(this, 200, ModernWidget.WIDGET_HEIGHT);
  }

  public SortType getSortType() {
    switch (getSelectedIndex()) {
    case 0:
      return SortType.FOLD_CHANGE;
    default:
      return SortType.Z_SCORE;
    }
  }
}
