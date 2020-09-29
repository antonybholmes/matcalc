package edu.columbia.rdf.matcalc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.io.ModernFileList;
import org.jebtk.modern.io.PwdModel;
import org.jebtk.modern.list.ModernList;
import org.jebtk.modern.list.ModernListModel;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;

public class FilesPanel extends ModernComponent implements ChangeListener {
  private static final long serialVersionUID = 1L;

  private ModernListModel<Path> mListModel = new ModernListModel<Path>();

  private ModernList<Path> mList = new ModernFileList();

  private PwdModel mModel;

  private FileModel mFileModel;

  public FilesPanel(PwdModel model, FileModel fileModel) {
    mModel = model;
    mFileModel = fileModel;

    mList.setCellRenderer(new FilesListRenderer());
    mList.setModel(mListModel);

    setBody(new ModernScrollPane(mList).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
        .setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW).setVScrollBarLocation(ScrollBarLocation.FLOATING));

    // setBorder(BORDER);

    model.addChangeListener(this);

    mList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          Path file = mList.getSelectedItem();

          if (PathUtils.getName(file).equals("..")) {
            file = mModel.getPwd();

            file = file.getParent();

            if (file != null) {
              mModel.setPwd(file);
            }
          } else if (FileUtils.isDirectory(file)) {
            mModel.setPwd(file);
          } else {
            if (FileUtils.exists(file)) {
              mFileModel.set(file);
            }
          }
        }
      }
    });

    try {
      updateFiles();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void changed(ChangeEvent e) {
    try {
      updateFiles();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private void updateFiles() throws IOException {
    Path pwd = mModel.getPwd();

    List<Path> files = new ArrayList<Path>(100);

    files.add(PathUtils.getPath(".."));

    files.addAll(FileUtils.lsdir(pwd));
    files.addAll(FileUtils.ls(pwd, false));

    mListModel.setValues(files);
  }
}
