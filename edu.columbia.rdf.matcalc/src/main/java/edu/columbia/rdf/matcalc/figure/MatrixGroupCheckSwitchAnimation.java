package edu.columbia.rdf.matcalc.figure;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.modern.button.CheckSwitchAnimation;
import org.jebtk.modern.widget.ModernWidget;

public class MatrixGroupCheckSwitchAnimation extends CheckSwitchAnimation {

  private Color mColor;

  public MatrixGroupCheckSwitchAnimation(ModernWidget widget, Color color) {
    super(widget);

    mColor = color;
  }

  @Override
  public void setSelectedColor(Graphics2D g2) {
    g2.setColor(mColor);
  }

}
