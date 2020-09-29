/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.matcalc.groups;

import java.awt.Graphics2D;

import org.jebtk.core.tree.TreeNode;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.tree.ModernTreeIconTextNodeRenderer;
import org.jebtk.modern.tree.ModernTreeNodeRenderer;
import org.jebtk.modern.tree.Tree;

// TODO: Auto-generated Javadoc
/**
 * Basic renderer for displaying directories and files within the map database.
 *
 * @author Antony Holmes
 */
public class XYSeriesTreeNodeRenderer extends ModernTreeIconTextNodeRenderer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member group.
   */
  private MatrixGroup mGroup;

  /** The Constant HEIGHT. */
  private static final int HEIGHT = 28;

  /**
   * public void drawBackgroundAA(Graphics2D g2) { IntRect rect = new IntRect(0,
   * 0, getWidth(), HEIGHT);
   * 
   * if (mNodeIsSelected) { getWidgetRenderer().drawButton(g2, rect,
   * RenderMode.SELECTED); } else { if (mNodeIsHighlighted) {
   * getWidgetRenderer().drawButton(g2, rect, RenderMode.SELECTED); }
   * 
   * if (mIsDragToNode) { getWidgetRenderer().drawButtonOutline(g2, rect,
   * RenderMode.SELECTED); } } }
   */

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.tree.ModernTreeNodeRenderer#drawForegroundAA(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawNodeBranch(Graphics2D g2) {
    if (mNode.isParent()) {
      // Always draw the branch at the top of the expansion
      int x = (mIconWidth - 16) / 2;
      int y = (HEIGHT - 16) / 2;

      super.drawNodeBranch(g2, x, y);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.tree.ModernTreeIconTextNodeRenderer#drawNodeIcon(java.
   * awt. Graphics2D)
   */
  @Override
  public void drawNodeIcon(Graphics2D g2) {
    if (mNode.isParent() && mGroup != null) {
      int x = (mIconWidth - 16) / 2;
      int y = (HEIGHT - 16) / 2;

      g2.setColor(mGroup.getColor());
      g2.fillRect(x, y, 16, 16);

      if (mNode.isExpanded()) {
        x += 8;

        g2.setFont(FONT);

        int p = getTextYPosCenter(g2, HEIGHT) + HEIGHT;

        for (TreeNode<?> child : mNode) {
          g2.drawString(getTruncatedText(g2, child.getName(), x, mRect.getW()), x, p);

          p += HEIGHT;
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.tree.ModernTreeIconTextNodeRenderer#drawNodeText(java.
   * awt. Graphics2D)
   */
  @Override
  public void drawNodeText(Graphics2D g2) {
    int p = getTextYPosCenter(g2, HEIGHT); // (g2, getRect(), mNode.getName());

    g2.setColor(mGroup.getColor());

    StringBuilder buffer = new StringBuilder(mNode.getName());

    if (mNode.isParent() && mGroup != null) {
      // buffer.append(" ").append(group.getSearch());

      buffer.append(" (").append(Integer.toString(mNode.getChildCount())).append(")");// group.getSearch());

      g2.setFont(BOLD_FONT);
    } else {
      g2.setFont(FONT);
    }

    g2.drawString(getTruncatedText(g2, buffer.toString(), 0, mRect.getW()), 0, p);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.tree.ModernTreeNodeRenderer#getRenderer(org.abh.
   * common. ui.ui.tree.Tree, org.abh.lib.tree.TreeNode, boolean, boolean,
   * boolean, boolean, int, int)
   */
  @Override
  public ModernTreeNodeRenderer getRenderer(Tree<?> tree, TreeNode<?> node, boolean nodeIsHighlighted,
      boolean nodeIsSelected, boolean hasFocus, boolean isDragToNode, int depth, int row) {

    super.getRenderer(tree, node, nodeIsHighlighted, nodeIsSelected, hasFocus, isDragToNode, depth, row);

    mGroup = (XYSeries) node.getValue();

    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.tree.ModernTreeNodeRenderer#setSize(org.abh.common.ui.
   * tree. Tree, org.abh.common.tree.TreeNode, int, int)
   */
  @Override
  protected void setSize(Tree<?> tree, TreeNode<?> node, int depth, int row) {
    // If the node is expanded, the size will be itself plus the number
    // of children, since we use the renderer to display hidden children
    setSize(tree.getInternalRect().getW(), HEIGHT * ((node.isExpanded() ? node.getChildCount() : 0) + 1));
  }
}