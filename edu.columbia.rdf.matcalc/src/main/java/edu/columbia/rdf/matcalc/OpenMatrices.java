package edu.columbia.rdf.matcalc;

import java.nio.file.Path;
import java.util.Collection;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.math.matrix.DataFrame;

public class OpenMatrices {

  private OpenMode mOpenMode = OpenMode.CURRENT_WINDOW;

  private MainMatCalcWindow mWindow;

  public OpenMatrices(MainMatCalcWindow window) {
    mWindow = window;
  }

  public OpenMatrices(OpenMatrices openMatrices) {
    mWindow = openMatrices.mWindow;
    mOpenMode = openMatrices.mOpenMode;
  }

  public OpenMatrices(OpenMatrices openMatrices, MainMatCalcWindow window) {
    this(openMatrices);

    mWindow = window;
  }

  public OpenMatrices openMode(OpenMode openMode) {
    OpenMatrices of = new OpenMatrices(this);
    of.mOpenMode = openMode;
    return of;
  }

  public OpenMatrices newWindow() {
    return openMode(OpenMode.NEW_WINDOW);
  }

  /*
   * public void openMatrixInNewWindow(DataFrame m) { MainMatCalcWindow window =
   * new MainMatCalcWindow(mWindow.getAppInfo());
   * 
   * window.openMatrix(m);
   * 
   * window.setVisible(true); }
   */

  /*
   * public void openMatricesInNewWindow(List<DataFrame> matrices) {
   * MainMatCalcWindow window = new MainMatCalcWindow(getAppInfo());
   * 
   * window.openMatrices(matrices);
   * 
   * window.setVisible(true); }
   */

  /*
   * public boolean openMatrix(Path file, DataFrame m) { boolean status = false;
   * 
   * if (m != null) { openMatrix(m); status = true; }
   * 
   * if (status) { mFilesModel.add(file);
   * 
   * setSubTitle(PathUtils.getName(file)); }
   * 
   * return status; }
   */

  /**
   * Load a matrix into the main window. If there is a currently loaded matrix, a
   * new window will be created. To add a matrix to the existing history of this
   * window, use addToHistory().
   *
   * @param m        the m
   * @param openMode the open mode
   * @return
   */
  public boolean open(DataFrame m) {
    return open(CollectionUtils.asList(m));
  }

  public boolean open(Path file, DataFrame m) {
    boolean ret = open(m);

    if (ret) {
      mWindow.mFilesModel.add(file);

      mWindow.setSubTitle(PathUtils.getName(file));
    }

    return ret;
  }

  public boolean open(Collection<DataFrame> matrices) {
    if (CollectionUtils.isNullOrEmpty(matrices)) {
      return false;
    }

    if (mOpenMode == OpenMode.NEW_WINDOW) {
      MainMatCalcWindow window = new MainMatCalcWindow(mWindow.getAppInfo());

      window.setVisible(true);

      new OpenMatrices(window).open(matrices);
    } else {
      for (DataFrame m : matrices) {

        mWindow.mMatrices.add(m);

        if (m.getName().length() > 0) {
          mWindow.setSubTitle(m.getName());
        } else {
          mWindow.setSubTitle("Load matrix");
        }

        mWindow.history().addToHistory(mWindow.getSubTitle(), m);
      }
    }

    return true;
  }

}
