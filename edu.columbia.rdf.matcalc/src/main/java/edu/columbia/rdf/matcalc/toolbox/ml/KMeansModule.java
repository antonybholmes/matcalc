/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.matcalc.toolbox.ml;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.jebtk.core.collections.ArrayUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.utils.MatrixOperations;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.RibbonLargeButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;
import edu.columbia.rdf.matcalc.toolbox.Module;

/**
 * The class OneWayAnovaModule.
 */
public class KMeansModule extends Module implements ModernClickListener {

  /**
   * The member parent.
   */
  private MainMatCalcWindow mParent;

  private static final Logger LOG = LoggerFactory.getLogger(KMeansModule.class);

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "kmeans";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.matcalc.modules.Module#init(edu.columbia.rdf.apps.
   * matcalc.MainMatCalcWindow)
   */
  @Override
  public void init(MainMatCalcWindow window) {
    mParent = window;

    RibbonLargeButton button = new RibbonLargeButton("K-means", AssetService.getInstance().loadIcon("kmeans", 24),
        "K-means", "K-means clustering.");
    button.addClickListener(this);

    mParent.getRibbon().getToolbar("Classification").getSection("Classifier").add(button);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.ui.modern.event.ModernClickListener#clicked(org.abh.lib.ui.
   * modern .event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    try {
      kmeans();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Ttest.
   *
   * @param Props the properties
   * @throws IOException    Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void kmeans() throws IOException {
    DataFrame m = mParent.getCurrentMatrix();

    if (m == null) {
      return;
    }

    KMeansDialog dialog = new KMeansDialog(mParent);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    int k = dialog.getK();
    boolean zscore = dialog.getZScore();
    boolean sort = dialog.getSort();

    DataFrame zm;

    if (zscore) {
      zm = MatrixOperations.rowZscore(m);
      mParent.history().addToHistory("Z-score", zm);
    } else {
      zm = m;
    }

    int rows = m.getRows();
    int cols = m.getCols();

    List<RowPoints> points = new ArrayList<RowPoints>(rows);

    for (int i = 0; i < rows; ++i) {
      RowPoints c = new RowPoints(i, cols);

      zm.rowToDouble(i, c.mData);

      points.add(c);
    }

    KMeansPlusPlusClusterer<RowPoints> kmeans = new KMeansPlusPlusClusterer<RowPoints>(k);

    List<CentroidCluster<RowPoints>> clusters = kmeans.cluster(points);

    String[] cids = new String[rows];

    // Assign an arbitrary number to the cluster
    int cid = 1;

    for (CentroidCluster<RowPoints> cluster : clusters) {
      for (RowPoints p : cluster.getPoints()) {
        cids[p.mId] = "C" + cid;
      }

      ++cid;
    }

    DataFrame cm = new DataFrame(zm);
    cm.getIndex().setAnnotation("Cluster", cids);
    mParent.history().addToHistory("K-means", cm);

    if (dialog.getRename()) {
      // Allow user to rename/order clusters

      // Need to select column
      mParent.getMatrixTable().getColumnModel().getSelectionModel().set(cm.getIndex().size() - 1);

      mParent.runModule("Rename");
    } else {
      if (sort) {
        int[] indices = ArrayUtils.argsort(cids);

        DataFrame sm = DataFrame.copyRows(cm, indices);
        mParent.history().addToHistory("Sorted K-means", sm);
      }
    }
  }
}
