package giovanni.popup;

import java.awt.Color;
import java.awt.Component;

public interface Option {

    String getLayout(Component paramComponent, float paramFloat);

    boolean useSnapshot();

    boolean closeWhenPressedEsc();

    boolean closeWhenClickOutside();

    boolean blockBackground();

    Color background();

    float opacity();

    int duration();

    float getAnimate();

    void setAnimate(float paramFloat);
}
