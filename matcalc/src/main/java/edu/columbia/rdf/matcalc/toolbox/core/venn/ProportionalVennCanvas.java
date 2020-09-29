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
package edu.columbia.rdf.matcalc.toolbox.core.venn;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import org.jebtk.bioinformatics.ui.groups.GroupsModel;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The Class ProportionalVennCanvas.
 */
public class ProportionalVennCanvas extends VennCanvas {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private static final int MAX_DIAMETER = 300;
  /** The Constant PLOT_OFFSET. */
  // private static final int MAX_RADIUS = MAX_DIAMETER / 2;
  private static final Point PLOT_OFFSET = new Point(SIZE.width / 2, 300);
  // private static final double MAX_AREA = Math.PI * MAX_RADIUS * MAX_RADIUS;

  /** The Constant UNIFORM_THREE_ANGLE. */
  private static final double UNIFORM_THREE_ANGLE = Math.PI / 3.0;

  /** The Constant D_INC. */
  private static final double D_INC = 1;

  /** The Constant OVERLAP_TEXT_COLOR. */
  private static final Color OVERLAP_TEXT_COLOR = Color.WHITE;

  /** The m count. */
  private int mCount;

  /** The m intersection 12. */
  private List<String> mIntersection12;

  /** The m D 1. */
  private double mD1;

  /** The m D 2. */
  private double mD2;

  /** The m max. */
  private double mMax;

  /** The m P 12. */
  // The percentage of overlap in g1
  private double mP12;

  /** The m R 1. */
  private double mR1;

  /** The m R 2. */
  private double mR2;

  /** The m A 1. */
  private double mA1;

  /** The m OA 12. */
  private double mOA12;

  /** The m D 12. */
  private double mD12;

  /** The m A 2. */
  private double mA2;

  /** The m chord 12. */
  private double mChord12;

  /** The m half chord 12. */
  private double mHalfChord12;

  /** The m style. */
  private CircleStyle mStyle;

  /** The m intersection 13. */
  private List<String> mIntersection13;

  /** The m intersection 23. */
  private List<String> mIntersection23;

  /** The m intersection 123. */
  private List<String> mIntersection123;

  /** The m P 13. */
  private double mP13;

  /** The m P 23. */
  private double mP23;

  /** The m A 3. */
  private double mA3;

  /** The m R 3. */
  private double mR3;

  /** The m D 3. */
  private double mD3;

  /** The m D 13. */
  private double mD13;

  /** The m OA 13. */
  private double mOA13;

  /** The m OA 23. */
  private double mOA23;

  /** The m D 23. */
  private double mD23;

  /** The m P 1. */
  private Point mP1;

  /** The m P 2. */
  private Point mP2;

  /** The m P 3. */
  private Point mP3;

  /** The m intersection 1. */
  private List<String> mIntersection1;

  /** The m intersection 2. */
  private List<String> mIntersection2;

  /** The m intersection 3. */
  private List<String> mIntersection3;

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.venn.VennCanvas#setGroups(org.jebtk.
   * bioinformatics. ui.groups.GroupsModel,
   * org.matcalc.toolbox.core.venn.CircleStyle)
   */
  public void setGroups(GroupsModel groups, CircleStyle style) {
    if (groups == null || groups.size() == 0) {
      return;
    }

    mGroups = groups;
    mStyle = style;

    layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.matcalc.toolbox.core.venn.VennCanvas#layout()
   */
  @Override
  public void layout() {
    if (mGroups == null || mGroups.size() == 0) {
      return;
    }

    mP1 = null;
    mP2 = null;
    mP3 = null;

    int r = mProps.getInt("venn.reference.radius");

    double maxArea = Math.PI * r * r;

    mCount = Math.min(mGroups.size(), 3);

    mMax = maxSize(mGroups);

    if (mCount == 1) {
      mA1 = maxArea;
      mR1 = Math.sqrt(mA1 / Math.PI);

      mD1 = mR1 * 2;
    } else {
      mIntersection12 = CollectionUtils.iterIntersect(mGroups.get(0), mGroups.get(1));

      mP12 = (double) mIntersection12.size() / (double) mGroups.get(0).size();

      if (mCount == 3) {
        mIntersection13 = CollectionUtils.iterIntersect(mGroups.get(0), mGroups.get(2));
        mP13 = (double) mIntersection13.size() / (double) mGroups.get(0).size();

        mIntersection23 = CollectionUtils.iterIntersect(mGroups.get(1), mGroups.get(2));
        mP23 = (double) mIntersection23.size() / (double) mGroups.get(1).size();
      }

      mA1 = maxArea;

      if (mStyle == CircleStyle.PROPORTIONAL) {
        mA1 *= mGroups.get(0).size() / mMax;
        mR1 = Math.sqrt(mA1 / Math.PI);
        mOA12 = mA1 * mP12;

        mA2 = maxArea * mGroups.get(1).size() / mMax;
        mR2 = Math.sqrt(mA2 / Math.PI);
        mD2 = mR2 * 2;

        mD12 = solveD(mOA12, mR1, mR2);
        mChord12 = getChordSize(mD12, mR1, mR2);
        mHalfChord12 = mChord12 / 2;

        if (mCount == 3) {
          mOA13 = mA1 * mP13;
          mOA23 = mA2 * mP23;

          mA3 = maxArea * mGroups.get(2).size() / mMax;
          mR3 = Math.sqrt(mA3 / Math.PI);
          mD3 = mR3 * 2;

          mD13 = solveD(mOA13, mR1, mR3);

          mD23 = solveD(mOA23, mR2, mR3);
        }
      } else {
        mR1 = Math.sqrt(mA1 / Math.PI);
        mR2 = mR1;
        mR3 = mR1;
        mD12 = mR1 * 1.25;
      }

      mD1 = mR1 * 2;
    }

    if (mCount == 1) {
      mIntersection1 = CollectionUtils.iterToList(mGroups.get(0));
    }

    if (mCount == 2) {
      mIntersection1 = CollectionUtils.iterCompliment(mGroups.get(0), mGroups.get(1));
      mIntersection2 = CollectionUtils.iterCompliment(mGroups.get(1), mGroups.get(0));
    }

    if (mCount == 3) {
      mIntersection123 = CollectionUtils.iterIntersect(mGroups.get(0),
          CollectionUtils.iterIntersect(mGroups.get(1), mGroups.get(2)));

      mIntersection12 = CollectionUtils.compliment(mIntersection12, mIntersection123);
      mIntersection13 = CollectionUtils.compliment(mIntersection13, mIntersection123);
      mIntersection23 = CollectionUtils.compliment(mIntersection23, mIntersection123);

      mIntersection1 = CollectionUtils.compliment(CollectionUtils.iterToList(mGroups.get(0)),
          CollectionUtils.union(mIntersection12, CollectionUtils.union(mIntersection13, mIntersection123)));

      mIntersection2 = CollectionUtils.compliment(CollectionUtils.iterToList(mGroups.get(1)),
          CollectionUtils.union(mIntersection12, CollectionUtils.union(mIntersection23, mIntersection123)));

      mIntersection3 = CollectionUtils.compliment(CollectionUtils.iterToList(mGroups.get(2)),
          CollectionUtils.union(mIntersection13, CollectionUtils.union(mIntersection23, mIntersection123)));

      // System.err.println("i32 " + mIntersection13 + " " + mIntersection23 + "
      // " +
      // mIntersection123);
      // System.err.println("i3 " + mIntersection3 + " " +
      // CollectionUtils.iterToList(mGroups.get(2)).size());
    }

    // Now the positions

    int x = PLOT_OFFSET.x;
    int y = PLOT_OFFSET.y;

    if (mCount == 1) {
      mP1 = new Point(x, y);
    } else if (mCount == 2) {
      x -= (int) (mD12 / 2);

      mP1 = new Point(x, y);

      x += (int) mD12;

      mP2 = new Point(x, y);
    } else {
      if (mStyle == CircleStyle.PROPORTIONAL) {
        x -= (int) (mD12 / 2);

        mP1 = new Point(x, y);

        x += (int) mD12;

        mP2 = new Point(x, y);

        double angle = 0;
        double maxAngle = Math.PI;
        double inc = maxAngle / 100.0;

        while (angle < maxAngle) {
          double a = mD13 * Math.sin(angle);
          double b1 = mD13 * Math.cos(angle);
          double b2 = mD12 - b1;
          double h2 = Math.sqrt(a * a + b2 * b2);

          x = PLOT_OFFSET.x + (int) b1;
          y = PLOT_OFFSET.y + (int) a;

          if (h2 >= mD23) {
            break;
          }

          angle += inc;
        }

        // shift so correctly relative to circle 1
        x -= (int) (mD12 / 2);

        mP3 = new Point(x, y);
      } else {
        int r2 = (int) (mR1 * 0.75);

        x = PLOT_OFFSET.x - (int) (r2 * Math.sin(UNIFORM_THREE_ANGLE));
        y = PLOT_OFFSET.y - (int) (r2 * Math.cos(UNIFORM_THREE_ANGLE));

        mP1 = new Point(x, y);

        x = PLOT_OFFSET.x + (int) (r2 * Math.sin(UNIFORM_THREE_ANGLE));
        y = PLOT_OFFSET.y - (int) (r2 * Math.cos(UNIFORM_THREE_ANGLE));

        mP2 = new Point(x, y);

        x = PLOT_OFFSET.x;
        y = PLOT_OFFSET.y + r2;

        mP3 = new Point(x, y);
      }
    }

    fireCanvasChanged();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.ModernPlotCanvas#plot(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext)
   */
  @Override
  public void drawPlot(Graphics2D g2, DrawingContext context) {
    if (mGroups == null || mGroups.size() == 0) {
      return;
    }

    // first the radius

    if (mCount == 2) {
      plotTwoCircles(g2);
    } else if (mCount == 3) {
      plotThreeCircles(g2);
    } else {
      plotOneCircle(g2);
    }
  }

  /**
   * Plot one circle.
   *
   * @param g2 the g 2
   */
  private void plotOneCircle(Graphics2D g2) {
    g2.setFont(PLOT_FONT);

    int r = (int) (mR1);
    int d = (int) mD1;

    g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(0).getColor()));
    g2.fillOval(mP1.x - r, mP1.y - r, d, d);

    String text = Integer.toString(mGroups.get(0).size());

    int textX = ModernWidget.centerTextAboutX(g2, text, mP1.x);
    int textY = ModernWidget.centerTextAboutY(g2, mP1.y);

    int r4 = (int) (mR1 * 1.5);

    g2.drawString(text, textX, textY);

    g2.setColor(mGroups.get(0).getColor());
    text = mGroups.get(0).getName();
    textX = ModernWidget.centerTextAboutX(g2, text, mP1.x);
    textY = mP1.y - r4;
    g2.drawString(text, textX, textY);
    text = "(" + mGroups.get(0).size() + ")";
    textX = ModernWidget.centerTextAboutX(g2, text, mP1.x);
    textY += ModernWidget.getStringHeight(g2);
    g2.drawString(text, textX, textY);
  }

  /**
   * Plot two circles.
   *
   * @param g2 the g 2
   */
  private void plotTwoCircles(Graphics2D g2) {
    // overlap in terms of g1

    g2.setFont(PLOT_FONT);

    if (mStyle == CircleStyle.PROPORTIONAL) {
      int r = (int) (mR1);
      int d = (int) mD1;

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(0).getColor()));
      g2.fillOval(mP1.x - r, mP1.y - r, d, d);

      g2.setColor(mGroups.get(0).getColor());
      g2.drawOval(mP1.x - r, mP1.y - r, d, d);

      // g2.setColor(Color.BLACK);

      String text = Integer.toString(mGroups.get(0).size() - mIntersection12.size());

      int textX = ModernWidget.leftAlignTextX(g2, text, mP1.x);
      int textY = ModernWidget.centerTextAboutY(g2, mP1.y);

      if (mD12 - mR2 <= 0) {
        textX -= 2 * (int) (mR2 - mD12);
      }

      g2.drawString(text, textX, textY);

      //
      // Second circle
      //

      r = (int) mR2;
      d = (int) mD2;

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(1).getColor()));
      g2.fillOval(mP2.x - r, mP2.y - r, d, d);

      g2.setColor(mGroups.get(1).getColor());
      g2.drawOval(mP2.x - r, mP2.y - r, d, d);

      // g2.setColor(Color.BLACK);
      text = Integer.toString(mGroups.get(1).size() - mIntersection12.size());

      textX = mP2.x; // ModernWidget.centerTextAboutX(g2, text, x);

      if (mR1 >= mD12) {
        textX += 2 * (int) (mR1 - mD12);
      }

      textY = ModernWidget.centerTextAboutY(g2, mP2.y);
      g2.drawString(text, textX, textY);

      textX = Math.max(mP1.x, mP2.x - (int) (Math.sqrt(mR2 * mR2 - mHalfChord12 * mHalfChord12)));

      textX = ModernWidget.centerTextAboutX(g2, text, textX);
      textY = ModernWidget.centerTextAboutY(g2, mP2.y);
      text = Integer.toString(mIntersection12.size());

      g2.setColor(OVERLAP_TEXT_COLOR);
      g2.drawString(text, textX, textY);
    } else {
      // uniform

      int r = (int) (mR1);
      int d = (int) mD1;

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(0).getColor()));
      g2.fillOval(mP1.x - r, mP1.y - r, d, d);

      g2.setColor(mGroups.get(0).getColor());
      g2.drawOval(mP1.x - r, mP1.y - r, d, d);

      String text = Integer.toString(mGroups.get(0).size() - mIntersection12.size());

      int textX = ModernWidget.leftAlignTextX(g2, text, mP1.x);
      int textY = ModernWidget.centerTextAboutY(g2, mP1.y);

      g2.drawString(text, textX, textY);

      //
      // Second circle
      //

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(1).getColor()));
      g2.fillOval(mP2.x - r, mP2.y - r, d, d);

      g2.setColor(mGroups.get(1).getColor());
      g2.drawOval(mP2.x - r, mP2.y - r, d, d);

      // g2.setColor(Color.BLACK);
      text = Integer.toString(mGroups.get(1).size() - mIntersection12.size());

      textX = mP2.x; // ModernWidget.centerTextAboutX(g2, text, x);

      textY = ModernWidget.centerTextAboutY(g2, mP2.y);
      g2.drawString(text, textX, textY);

      textX -= (int) (mD12 / 2.0);

      text = Integer.toString(mIntersection12.size());

      textX = ModernWidget.centerTextAboutX(g2, text, textX);

      g2.setColor(OVERLAP_TEXT_COLOR);
      g2.drawString(text, textX, textY);

      //
      // Group labels
      //

      int r4 = (int) (mR1 * 1.5);

      // First circle

      g2.setColor(mGroups.get(0).getColor());
      text = mGroups.get(0).getName();
      textX = ModernWidget.centerTextAboutX(g2, text, mP1.x);
      textY = mP1.y - r4;
      g2.drawString(text, textX, textY);
      text = "(" + mGroups.get(0).size() + ")";
      textX = ModernWidget.centerTextAboutX(g2, text, mP1.x);
      textY += ModernWidget.getStringHeight(g2);
      g2.drawString(text, textX, textY);

      // Second circle

      g2.setColor(mGroups.get(1).getColor());
      text = mGroups.get(1).getName();
      textX = ModernWidget.centerTextAboutX(g2, text, mP2.x);
      textY = mP2.y - r4;
      g2.drawString(text, textX, textY);
      text = "(" + mGroups.get(1).size() + ")";
      textX = ModernWidget.centerTextAboutX(g2, text, mP2.x);
      textY += ModernWidget.getStringHeight(g2);
      g2.drawString(text, textX, textY);
    }
  }

  /**
   * Plot three circles.
   *
   * @param g2 the g 2
   */
  private void plotThreeCircles(Graphics2D g2) {

    if (mStyle == CircleStyle.PROPORTIONAL) {
      int r = (int) (mR1);
      int d = (int) mD1;

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(0).getColor()));
      g2.fillOval(mP1.x - r, mP1.y - r, d, d);

      g2.setColor(mGroups.get(0).getColor());
      g2.drawOval(mP1.x - r, mP1.y - r, d, d);

      String text = Integer
          .toString(mGroups.get(0).size() - mIntersection12.size() - mIntersection13.size() - mIntersection123.size());

      int textX = ModernWidget.leftAlignTextX(g2, text, mP1.x);
      int textY = ModernWidget.centerTextAboutY(g2, mP1.y);

      if (mR2 >= mD12) {
        textX -= 2 * (int) (mR2 - mD12);
      }

      g2.drawString(text, textX, textY);

      //
      // Second circle
      //

      r = (int) mR2;
      d = (int) mD2;

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(1).getColor()));
      g2.fillOval(mP2.x - r, mP2.y - r, d, d);

      g2.setColor(mGroups.get(1).getColor());
      g2.drawOval(mP2.x - r, mP2.y - r, d, d);

      // g2.setColor(Color.BLACK);
      text = Integer
          .toString(mGroups.get(1).size() - mIntersection12.size() - mIntersection23.size() - mIntersection123.size());

      textX = mP2.x; // ModernWidget.centerTextAboutX(g2, text, x);

      if (mR1 >= mD12) {
        textX += 2 * (int) (mR1 - mD12);
      }

      textY = ModernWidget.centerTextAboutY(g2, mP2.y);
      g2.drawString(text, textX, textY);

      //
      // third circle
      //

      r = (int) mR3;
      d = (int) mD3;

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(2).getColor()));
      g2.fillOval(mP3.x - r, mP3.y - r, d, d);

      g2.setColor(mGroups.get(2).getColor());
      g2.drawOval(mP3.x - r, mP3.y - r, d, d);

      text = Integer
          .toString(mGroups.get(2).size() - mIntersection13.size() - mIntersection23.size() - mIntersection123.size());

      textX = mP3.x; // ModernWidget.centerTextAboutX(g2, text, x);
      textY = ModernWidget.centerTextAboutY(g2, mP3.y);

      g2.drawString(text, textX, textY);

      // center
      if (mIntersection123.size() > 0) {
        g2.setColor(OVERLAP_TEXT_COLOR);
        textX = (Math.max(mP2.x, mP3.x) + mP1.x) / 2;
        textY = (Math.max(mP2.y, mP3.y) + mP1.y) / 2;

        textX = ModernWidget.centerTextAboutX(g2, text, textX);
        textY = ModernWidget.centerTextAboutY(g2, textY);

        text = Integer.toString(mIntersection123.size());

        g2.drawString(text, textX, textY);
      }
    } else {
      //
      // uniform
      //

      int r = (int) (mR1);
      int d = (int) mD1;

      int r3 = (int) (mR1 * 3.0 / 8.0);
      int r3sa = (int) (r3 * Math.sin(UNIFORM_THREE_ANGLE));
      int r3ca = (int) (r3 * Math.cos(UNIFORM_THREE_ANGLE));

      int r4 = (int) (mR1 * 2.0);
      int r4sa = (int) (r4 * Math.sin(UNIFORM_THREE_ANGLE));
      int r4ca = (int) (r4 * Math.cos(UNIFORM_THREE_ANGLE));

      // First circle

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(0).getColor()));
      g2.fillOval(mP1.x - r, mP1.y - r, d, d);

      g2.setColor(mGroups.get(0).getColor());
      g2.drawOval(mP1.x - r, mP1.y - r, d, d);

      // g2.setColor(Color.BLACK);

      String text = Integer
          .toString(mGroups.get(0).size() - mIntersection12.size() - mIntersection13.size() - mIntersection123.size());

      int textX = ModernWidget.centerTextAboutX(g2, text, mP1.x);
      int textY = ModernWidget.centerTextAboutY(g2, mP1.y);
      g2.drawString(text, textX, textY);

      // Second circle

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(1).getColor()));
      g2.fillOval(mP2.x - r, mP2.y - r, d, d);

      g2.setColor(mGroups.get(1).getColor());
      g2.drawOval(mP2.x - r, mP2.y - r, d, d);

      // g2.setColor(Color.BLACK);
      text = Integer
          .toString(mGroups.get(1).size() - mIntersection12.size() - mIntersection23.size() - mIntersection123.size());

      textX = ModernWidget.centerTextAboutX(g2, text, mP2.x);
      textY = ModernWidget.centerTextAboutY(g2, mP2.y);
      g2.drawString(text, textX, textY);

      // Third circle

      g2.setColor(ColorUtils.getTransparentColor60(mGroups.get(2).getColor()));
      g2.fillOval(mP3.x - r, mP3.y - r, d, d);

      g2.setColor(mGroups.get(2).getColor());
      g2.drawOval(mP3.x - r, mP3.y - r, d, d);

      // g2.setColor(Color.BLACK);
      text = Integer
          .toString(mGroups.get(2).size() - mIntersection13.size() - mIntersection23.size() - mIntersection123.size());

      textX = ModernWidget.centerTextAboutX(g2, text, mP3.x);
      textY = ModernWidget.centerTextAboutY(g2, mP3.y);
      g2.drawString(text, textX, textY);

      //
      // Number labels
      //

      g2.setColor(OVERLAP_TEXT_COLOR);

      // center label

      textX = PLOT_OFFSET.x;
      textY = PLOT_OFFSET.y;

      text = Integer.toString(mIntersection123.size());

      textX = ModernWidget.centerTextAboutX(g2, text, textX);
      textY = ModernWidget.centerTextAboutY(g2, textY);

      g2.drawString(text, textX, textY);

      // 12 label

      textX = PLOT_OFFSET.x;
      textY = PLOT_OFFSET.y - r3;

      text = Integer.toString(mIntersection12.size());
      textX = ModernWidget.centerTextAboutX(g2, text, textX);
      textY = ModernWidget.centerTextAboutY(g2, textY);
      g2.drawString(text, textX, textY);

      // 13 label

      textX = PLOT_OFFSET.x - r3sa;
      textY = PLOT_OFFSET.y + r3ca;

      text = Integer.toString(mIntersection13.size());
      textX = ModernWidget.centerTextAboutX(g2, text, textX);
      textY = ModernWidget.centerTextAboutY(g2, textY);
      g2.drawString(text, textX, textY);

      // 23 label

      textX = PLOT_OFFSET.x + r3sa;
      textY = PLOT_OFFSET.y + r3ca;

      text = Integer.toString(mIntersection23.size());
      textX = ModernWidget.centerTextAboutX(g2, text, textX);
      textY = ModernWidget.centerTextAboutY(g2, textY);
      g2.drawString(text, textX, textY);

      //
      // Group labels
      //

      // First circle

      g2.setColor(mGroups.get(0).getColor());
      text = mGroups.get(0).getName();
      int w = ModernWidget.getStringWidth(g2, text);
      textX = PLOT_OFFSET.x - r4sa - w;
      textY = PLOT_OFFSET.y - r4ca;
      g2.drawString(text, textX, textY);
      text = "(" + mGroups.get(0).size() + ")";
      textX += (w - ModernWidget.getStringWidth(g2, text)) / 2;
      textY = PLOT_OFFSET.y - r4ca + ModernWidget.getStringHeight(g2);
      g2.drawString(text, textX, textY);

      // Second circle

      g2.setColor(mGroups.get(1).getColor());
      text = mGroups.get(1).getName();
      w = ModernWidget.getStringWidth(g2, text);
      textX = PLOT_OFFSET.x + r4sa;
      textY = PLOT_OFFSET.y - r4ca;
      g2.drawString(text, textX, textY);
      text = "(" + mGroups.get(1).size() + ")";
      textX += (w - ModernWidget.getStringWidth(g2, text)) / 2;
      textY = PLOT_OFFSET.y - r4ca + ModernWidget.getStringHeight(g2);
      g2.drawString(text, textX, textY);

      // Third circle
      g2.setColor(mGroups.get(2).getColor());
      text = mGroups.get(2).getName();
      textX = ModernWidget.centerTextAboutX(g2, text, PLOT_OFFSET.x);
      textY = PLOT_OFFSET.y + r4;
      g2.drawString(text, textX, textY);
      text = "(" + mGroups.get(2).size() + ")";
      textX = ModernWidget.centerTextAboutX(g2, text, PLOT_OFFSET.x);
      textY += ModernWidget.getStringHeight(g2);
      g2.drawString(text, textX, textY);
    }
  }

  /**
   * The maximum size of any of the groups.
   *
   * @param groups the groups
   * @return the double
   */
  private static double maxSize(GroupsModel groups) {
    double max = 0;

    for (int i = 0; i < Math.min(3, groups.size()); ++i) {
      max = Math.max(max, groups.get(i).size());
    }

    return max;
  }

  /**
   * Determine the distance between the centers of the circles.
   *
   * @param ai Area of intersection.
   * @param r1 the r 1
   * @param r2 the r 2
   * @return the double
   */
  private static double solveD(double ai, double r1, double r2) {
    double d = r1 + r2;

    System.err.println("ai " + ai);

    if (ai == 0) {
      return d;
    }

    double dmin = Math.abs(r1 - r2);

    System.err.println("dmin " + dmin);

    double a;

    double r1sq = r1 * r1;
    double r2sq = r2 * r2;
    double d2sq;

    while (d > dmin) {
      d2sq = d * d;

      a = r2sq * Math.acos((d2sq + r2sq - r1sq) / (2 * d * r2)) + r1sq * Math.acos((d2sq + r1sq - r2sq) / (2 * d * r1))
          - 0.5 * Math.sqrt((-d + r2 + r1) * (d + r2 - r1) * (d - r2 + r1) * (d + r2 + r1));

      if (a >= ai) {
        return d;
      }

      d -= D_INC;

      // System.err.println("d " + d + " " + a + " " + ai + " " + r1 + " " +
      // r2);
    }

    // Favor the overlap being slightly too large rather than too small.
    return d + D_INC;
  }

  /**
   * Gets the chord size.
   *
   * @param d  the d
   * @param r1 the r 1
   * @param r2 the r 2
   * @return the chord size
   */
  private static double getChordSize(double d, double r1, double r2) {
    return 1.0 / d * Math.sqrt((-d + r2 - r1) * (-d - r2 + r1) * (-d + r2 + r1) * (d + r2 + r1));
  }

  /**
   * Gets the p1.
   *
   * @return the p1
   */
  public Point getP1() {
    return mP1;
  }

  /**
   * Gets the r1.
   *
   * @return the r1
   */
  public double getR1() {
    return mR1;
  }

  /**
   * Gets the p2.
   *
   * @return the p2
   */
  public Point getP2() {
    return mP2;
  }

  /**
   * Gets the r2.
   *
   * @return the r2
   */
  public double getR2() {
    return mR2;
  }

  /**
   * Gets the p3.
   *
   * @return the p3
   */
  public Point getP3() {
    return mP3;
  }

  /**
   * Gets the r3.
   *
   * @return the r3
   */
  public double getR3() {
    return mR3;
  }

  /**
   * Gets the i123.
   *
   * @return the i123
   */
  public List<String> getI123() {
    return mIntersection123;
  }

  /**
   * Gets the i12.
   *
   * @return the i12
   */
  public List<String> getI12() {
    return mIntersection12;
  }

  /**
   * Gets the i13.
   *
   * @return the i13
   */
  public List<String> getI13() {
    return mIntersection13;
  }

  /**
   * Gets the i23.
   *
   * @return the i23
   */
  public List<String> getI23() {
    return mIntersection23;
  }

  /**
   * Gets the i1.
   *
   * @return the i1
   */
  public List<String> getI1() {
    return mIntersection1;
  }

  /**
   * Gets the i2.
   *
   * @return the i2
   */
  public List<String> getI2() {
    return mIntersection2;
  }

  /**
   * Gets the i3.
   *
   * @return the i3
   */
  public List<String> getI3() {
    return mIntersection3;
  }
}
