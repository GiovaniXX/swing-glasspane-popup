package giovanni.drawer;

import java.awt.Component;
import giovanni.drawer.component.DrawerBuilder;
import giovanni.drawer.component.DrawerPanel;
import giovanni.popup.GlassPanePopup;
import giovanni.popup.Option;
import giovanni.popup.component.GlassPaneChild;

public class Drawer {

    private static Drawer instance;

    private DrawerPanel drawerPanel;

    private DrawerOption option;

    public static Drawer getInstance() {
        if (instance == null) {
            instance = new Drawer();
        }
        return instance;
    }

    public static Drawer newInstance() {
        return new Drawer();
    }

    public void setDrawerBuilder(DrawerBuilder drawerBuilder) {
        this.drawerPanel = new DrawerPanel(drawerBuilder);
        this.option = new DrawerOption(drawerBuilder.getDrawerWidth());
        drawerBuilder.build(this.drawerPanel);
    }

    public void showDrawer() {
        if (this.drawerPanel == null) {
            throw new NullPointerException("Drawer builder has not initialize");
        }
        if (!isShowing()) {
            GlassPanePopup.showPopup((GlassPaneChild) this.drawerPanel, (Option) this.option, "drawer");
        }
    }

    public void closeDrawer() {
        GlassPanePopup.closePopup("drawer");
    }

    public boolean isShowing() {
        return GlassPanePopup.isShowing((Component) this.drawerPanel);
    }

    public DrawerPanel getDrawerPanel() {
        return this.drawerPanel;
    }
}
