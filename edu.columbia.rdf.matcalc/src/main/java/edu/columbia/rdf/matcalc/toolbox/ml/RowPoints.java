package edu.columbia.rdf.matcalc.toolbox.ml;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.jebtk.core.IdProperty;

public class RowPoints implements Clusterable, IdProperty {

  public final double[] mData;
  public final int mId;

  public RowPoints(int id, int l) {
    mId = id;

    mData = new double[l];
  }

  @Override
  public double[] getPoint() {
    return mData;
  }

  @Override
  public int getId() {
    return mId;
  }

}
