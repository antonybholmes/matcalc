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
import java.awt.Point;

import org.jebtk.core.tree.TreeNode;
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
public class MatrixGroupTreeNodeRenderer extends ModernTreeIconTextNodeRenderer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member group.
   */
  private MatrixGroup mGroup;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.tree.ModernTreeNodeRenderer#drawForegroundAA(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawNodeIcon(Graphics2D g2) {
    if (mNode.isParent() && mGroup != null) {
      g2.setColor(mGroup.getColor());
      g2.fillRect(0, (mRect.getH() - 16) / 2, 16, 16);
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
    Point p = getStringCenterPlotCoordinates(g2, getRect(), mNode.getName());

    g2.setColor(TEXT_COLOR);

    StringBuilder buffer = new StringBuilder(mNode.getName());

    if (mNode.isParent() && mGroup != null) {
      // buffer.append(" ").append(group.getSearch());

      buffer.append(" (").append(Integer.toString(mNode.getChildCount())).append(")");// group.getSearch());

      g2.setFont(BOLD_FONT);
    } else {
      g2.setFont(FONT);
    }

    g2.drawString(getTruncatedText(g2, buffer.toString(), 0, mRect.getW()), 0, p.y);
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

    mGroup = (MatrixGroup) node.getValue();

    return this;
  }
}