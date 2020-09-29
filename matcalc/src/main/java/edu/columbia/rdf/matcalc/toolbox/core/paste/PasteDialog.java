package edu.columbia.rdf.matcalc.toolbox.core.paste;

import java.nio.file.Path;
import java.util.List;

import javax.swing.Box;

import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.io.AllGuiFilesFilter;
import org.jebtk.modern.io.ChooseFilesPanel;
import org.jebtk.modern.io.TsvGuiFileFilter;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * Control which conservation scores are shown.
 * 
 * @author Antony Holmes
 *
 */
public class PasteDialog extends ModernDialogHelpWindow implements ModernClickListener {
  private static final long serialVersionUID = 1L;

  /** Assume first column is common between files */
  private CheckBox mCheckIndex = new ModernCheckSwitch("Header", true);

  // private DelimiterCombo mDelimiterCombo = new DelimiterCombo();

  private ChooseFilesPanel mChooseFilesPanel;

  public PasteDialog(MainMatCalcWindow parent) {
    super(parent, "matcalc.toolbox.paste.help.url");

    setTitle("Paste Files");

    mChooseFilesPanel = new ChooseFilesPanel(parent, AllGuiFilesFilter.INSTANCE, TsvGuiFileFilter.INSTANCE);

    setup();

    createUi();
  }

  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    mCheckIndex.setSelected(SettingsService.getInstance().getBool("org.matcalc.toolbox.paste.common-index", true));

    setSize(500, 420);

    UI.centerWindowToScreen(this);
  }

  private final void createUi() {
    ModernComponent content = new ModernComponent();

    Box box = VBox.create();
    box.add(UI.createVGap(10));
    box.add(mCheckIndex);

    content.setFooter(box);

    content.setBody(mChooseFilesPanel);

    setCard(content);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {
      if (getFiles().size() == 0) {
        ModernMessageDialog.createWarningDialog(mParent, "You must choose at least one file.");

        return;
      }

      SettingsService.getInstance().update("org.matcalc.toolbox.paste.common-index", mCheckIndex.isSelected());
    }

    super.clicked(e);
  }

  public List<Path> getFiles() {
    return mChooseFilesPanel.getFiles();
  }

  /**
   * Returns true if one way overlap is selected.
   * 
   * @return
   */
  public boolean getCommonIndex() {
    return mCheckIndex.isSelected();
  }

  public String getDelimiter() {
    return TextUtils.TAB_DELIMITER;
  }
}
