package giovanni.drawer.component;

import java.awt.Component;

public interface DrawerBuilder {

    void build(DrawerPanel paramDrawerPanel);

    Component getHeader();

    Component getHeaderSeparator();

    Component getMenu();

    Component getFooter();

    int getDrawerWidth();
}
