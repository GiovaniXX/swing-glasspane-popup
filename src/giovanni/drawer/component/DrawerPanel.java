package giovanni.drawer.component;

import java.awt.LayoutManager;
import net.miginfocom.swing.MigLayout;
import giovanni.popup.component.GlassPaneChild;

public class DrawerPanel extends GlassPaneChild {

    private final DrawerBuilder drawerBuilder;

    public DrawerPanel(DrawerBuilder drawerBuilder) {
        this.drawerBuilder = drawerBuilder;
        init();
    }

    private void init() {
        String layoutRow = "";
        if (this.drawerBuilder.getHeader() != null) {
            layoutRow = "[grow 0]";
            add(this.drawerBuilder.getHeader());
        }
        if (this.drawerBuilder.getHeaderSeparator() != null) {
            layoutRow = layoutRow + "[grow 0,2::]";
            add(this.drawerBuilder.getHeaderSeparator());
        }
        if (this.drawerBuilder.getMenu() != null) {
            layoutRow = layoutRow + "[fill]";
            add(this.drawerBuilder.getMenu());
        }
        if (this.drawerBuilder.getFooter() != null) {
            layoutRow = layoutRow + "[grow 0]";
            add(this.drawerBuilder.getFooter());
        }
        setLayout((LayoutManager) new MigLayout("wrap,insets 0,fill", "fill", layoutRow));
    }

    public DrawerBuilder getDrawerBuilder() {
        return this.drawerBuilder;
    }
}
