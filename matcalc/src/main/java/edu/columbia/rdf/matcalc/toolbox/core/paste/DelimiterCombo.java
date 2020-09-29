package edu.columbia.rdf.matcalc.toolbox.core.paste;

import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.combobox.ModernComboBox;

public class DelimiterCombo extends ModernComboBox {

  private static final long serialVersionUID = 1L;

  public DelimiterCombo() {
    addMenuItem("Comma");
    addMenuItem("Semi-colon");
    addMenuItem("Tab");

    setSelectedIndex(2);
  }

  public String getDelimiter() {
    switch (getSelectedIndex()) {
    case 0:
      return TextUtils.COMMA_DELIMITER;
    case 1:
      return TextUtils.SEMI_COLON_DELIMITER;
    default:
      return TextUtils.TAB_DELIMITER;
    }
  }

}
