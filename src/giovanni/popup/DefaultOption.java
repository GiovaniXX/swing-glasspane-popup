package giovanni.popup;

import com.formdev.flatlaf.FlatLaf;
import java.awt.Color;
import java.awt.Component;

public class DefaultOption implements Option {
  private float animate;
  
  public String getLayout(Component parent, float animate) {
    float an = 20.0F / parent.getHeight();
    float space = 0.5F + an - animate * an;
    return "pos 0.5al " + space + "al,height ::100%-20";
  }
  
  public boolean useSnapshot() {
    return true;
  }
  
  public boolean closeWhenPressedEsc() {
    return true;
  }
  
  public boolean closeWhenClickOutside() {
    return false;
  }
  
  public boolean blockBackground() {
    return true;
  }
  
  public Color background() {
    return FlatLaf.isLafDark() ? new Color(50, 50, 50) : new Color(20, 20, 20);
  }
  
  public float opacity() {
    return 0.5F;
  }
  
  public int duration() {
    return 300;
  }
  
  public float getAnimate() {
    return this.animate;
  }
  
  public void setAnimate(float animate) {
    this.animate = animate;
  }
}
