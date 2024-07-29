package giovanni.drawer;

import java.awt.Component;
import giovanni.popup.DefaultOption;

public class DrawerOption extends DefaultOption {

    private final int width;

    public DrawerOption() {
        this(275);
    }

    public DrawerOption(int width) {
        this.width = width;
    }

    @Override
    public String getLayout(Component parent, float animate) {
        String l;
        if (parent.getComponentOrientation().isLeftToRight()) {
            float x = animate * this.width - this.width;
            l = "pos " + x + " 0,height 100%,width " + this.width;
        } else {
            float x = animate * this.width;
            l = "pos 100%-" + x + " 0,height 100%,width " + this.width;
        }
        return l;
    }

    @Override
    public boolean closeWhenClickOutside() {
        return true;
    }

    @Override
    public boolean closeWhenPressedEsc() {
        return true;
    }
}
