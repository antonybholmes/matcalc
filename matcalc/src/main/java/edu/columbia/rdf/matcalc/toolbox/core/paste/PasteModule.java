package edu.columbia.rdf.matcalc.toolbox.core.paste;

import java.nio.file.Path;
import java.util.List;

import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.tooltip.ModernToolTip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

public class PasteModule extends Module implements ModernClickListener {

  public final static Logger LOG = LoggerFactory.getLogger(PasteModule.class);

  private MainMatCalcWindow mWindow;

  @Override
  public String getName() {
    return "Paste";
  }

  @Override
  public void init(MainMatCalcWindow window) {
    mWindow = window;

    Ribbon ribbon = window.getRibbon();

    ModernButton button = ribbon.getToolbar("Data").getSection("Tools")
        .createButton(AssetService.getInstance().loadIcon("paste_files", 24));
    button.setToolTip(new ModernToolTip("Paste Files", "Paste multiple files column wise."));
    button.addClickListener(this);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    paste();
  }

  private void paste() {
    PasteDialog dialog = new PasteDialog(mWindow);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    List<Path> files = dialog.getFiles();

    if (files.size() == 0) {
      return;
    }

    PasteTask task = new PasteTask(mWindow, files, dialog.getDelimiter(), dialog.getCommonIndex());

    task.doInBackground();
  }
}
