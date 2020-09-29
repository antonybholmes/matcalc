package edu.columbia.rdf.matcalc;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.ui.matrix.transform.MatrixTransform;

/**
 * The Class OpenFile.
 */
public class History {

  /** The m window. */
  private MainMatCalcWindow mWindow;

  private boolean mKeep = true;

  /**
   * Instantiates a new open file.
   *
   * @param window the window
   * @param file   the file
   */
  public History(MainMatCalcWindow window) {
    mWindow = window;
  }

  public History(History history) {
    mWindow = history.mWindow;
    mKeep = history.mKeep;
  }

  public History keep(boolean keep) {
    History h = new History(this);
    h.mKeep = keep;
    return h;
  }

  public int getHistoryIndex() {
    return mWindow.getHistoryIndex();
  }

  /**
   * Adds the to history.
   *
   * @param name   the name
   * @param matrix the matrix
   * @return the annotation matrix
   */
  public DataFrame addToHistory(String name, DataFrame matrix) {
    if (matrix == null) {
      return null;
    }

    return addToHistory(name, name, matrix);
  }

  /**
   * Adds the to history.
   *
   * @param name        the name
   * @param description the description
   * @param matrix      the matrix
   * @return the annotation matrix
   */
  public DataFrame addToHistory(String name, String description, DataFrame matrix) {
    if (matrix == null) {
      return null;
    }

    return addToHistory(new MatrixTransform(mWindow, name, description, matrix));
  }

  /**
   * Adds the to history.
   *
   * @param name          the name
   * @param matrix        the matrix
   * @param selectedIndex the selected index
   * @return the annotation matrix
   */
  public DataFrame addToHistory(String name, DataFrame matrix, int selectedIndex) {
    if (matrix == null) {
      return null;
    }

    return addToHistory(name, name, matrix, selectedIndex);
  }

  public DataFrame addToHistory(String name, String description, DataFrame matrix, int selectedIndex) {
    if (matrix == null) {
      return null;
    }

    return addToHistory(selectedIndex, new MatrixTransform(mWindow, name, description, matrix));
  }

  public DataFrame addToHistory(MatrixTransform transform) {
    return addToHistory(getHistoryIndex(), transform);
  }

  public DataFrame addToHistory(int selectedIndex, MatrixTransform transform) {
    if (transform == null) {
      return null;
    }

    if (mKeep) {
      // mWindow.history().addToHistory(selectedIndex, transform);
      mWindow.getHistoryPanel().addItem(transform, selectedIndex).apply();
    }

    return transform.getMatrix();
  }

  /*
   * public DataFrame addToHistory(String name, DataFrame matrix) { if (matrix ==
   * null) { return null; }
   * 
   * return addToHistory(name, name, matrix); }
   * 
   * public DataFrame addToHistory(String name, String description, DataFrame
   * matrix) { if (matrix == null) { return null; }
   * 
   * return addToHistory(new MatrixTransform(this, name, description, matrix)); }
   * 
   * public DataFrame addToHistory(String name, DataFrame matrix, int
   * selectedIndex) { if (matrix == null) { return null; }
   * 
   * return addToHistory(name, name, matrix, selectedIndex); }
   * 
   * public DataFrame addToHistory(String name, String description, DataFrame
   * matrix, int selectedIndex) { if (matrix == null) { return null; }
   * 
   * return addToHistory(selectedIndex, new MatrixTransform(this, name,
   * description, matrix)); }
   * 
   * public DataFrame addToHistory(MatrixTransform transform) { return
   * addToHistory(getHistoryIndex(), transform); }
   * 
   * public DataFrame addToHistory(int selectedIndex, MatrixTransform transform) {
   * if (transform == null) { return null; }
   * 
   * transform.addMatrixTransformListener(this);
   * 
   * mHistoryPanel.addItem(transform, selectedIndex).apply();
   * 
   * return transform.getMatrix(); }
   */
}
