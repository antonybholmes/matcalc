package edu.columbia.rdf.matcalc.toolbox.supervised;

import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;
import org.jebtk.modern.widget.ModernWidget;

public class TestCombo extends ModernComboBox {

  private static final long serialVersionUID = 1L;

  public TestCombo() {
    addMenuItem("T-Test Equal Variance");
    addMenuItem("T-Test Unequal Variance");
    addMenuItem("Mann Whitney");

    setSelectedIndex(1);

    UI.setSize(this, 200, ModernWidget.WIDGET_HEIGHT);
  }

  public TestType getTest() {
    switch (getSelectedIndex()) {
    case 0:
      return TestType.TTEST_EQUAL_VARIANCE;
    case 2:
      return TestType.MANN_WHITNEY;
    default:
      return TestType.TTEST_UNEQUAL_VARIANCE;
    }
  }
}
