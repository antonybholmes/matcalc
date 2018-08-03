package edu.columbia.rdf.matcalc.figure;

import java.awt.Color;

import org.jebtk.modern.button.CheckSwitchAnimation;
import org.jebtk.modern.widget.ModernWidget;

public class MatrixGroupCheckSwitchAnimation extends CheckSwitchAnimation {

  public MatrixGroupCheckSwitchAnimation(ModernWidget widget, Color color) {
    super(widget, widget.getFromKeyFrame().getColor("background-color"), color);
  }
}
