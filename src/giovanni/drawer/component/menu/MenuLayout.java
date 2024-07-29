package giovanni.drawer.component.menu;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class MenuLayout implements LayoutManager {

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = parent.getParent().getWidth();
            int height = insets.top + insets.bottom;
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    height += (com.getPreferredSize()).height;
                    width = Math.max(width, (com.getPreferredSize()).width);
                }
            }
            return new Dimension(width, height);
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension(0, 0);
        }
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = parent.getWidth() - insets.left + insets.right;
            int x = insets.left;
            int y = insets.top;
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    int h = (com.getPreferredSize()).height;
                    com.setBounds(x, y, width, h);
                    y += h;
                }
            }
        }
    }
}
