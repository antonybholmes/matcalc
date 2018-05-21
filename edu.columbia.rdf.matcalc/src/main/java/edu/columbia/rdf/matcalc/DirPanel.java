package edu.columbia.rdf.matcalc;

import java.nio.file.Path;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.FolderVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.ModernFileCrumb;
import org.jebtk.modern.io.PwdModel;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.window.ModernWindow;

public class DirPanel extends ModernComponent {
  private static final long serialVersionUID = 1L;

  private ModernFileCrumb mFileCrumb = new ModernFileCrumb(
      RecentFilesService.getInstance().getPwd());

  private ModernButton mDirButton = new ModernButton(
      AssetService.getInstance().loadIcon(FolderVectorIcon.class, 16));

  private PwdModel mModel;

  private ModernWindow mParent;

  public DirPanel(ModernWindow parent, PwdModel model) {
    mParent = parent;
    mModel = model;

    mFileCrumb = new ModernFileCrumb(model.getPwd());

    setLeft(mDirButton);
    setBody(mFileCrumb);

    setBorder(BORDER);

    mDirButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        setDir();
      }
    });

    model.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mFileCrumb.updateDir(mModel.getPwd());
      }
    });

    mFileCrumb.addChangeListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        mModel.setPwd(mFileCrumb.getDir());
      }
    });
  }

  protected void setDir() {
    Path dir = FileDialog.openDir(mParent, mFileCrumb.getDir());

    if (dir != null) {
      mFileCrumb.setDir(dir);
    }
  }
}
