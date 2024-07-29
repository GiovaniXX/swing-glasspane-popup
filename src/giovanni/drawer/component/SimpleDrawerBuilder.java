package giovanni.drawer.component;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import giovanni.drawer.component.footer.SimpleFooter;
import giovanni.drawer.component.footer.SimpleFooterData;
import giovanni.drawer.component.header.SimpleHeader;
import giovanni.drawer.component.header.SimpleHeaderData;
import giovanni.drawer.component.menu.SimpleMenu;
import giovanni.drawer.component.menu.SimpleMenuOption;
import giovanni.utils.FlatLafStyleUtils;

public abstract class SimpleDrawerBuilder implements DrawerBuilder {

    protected SimpleHeader header = new SimpleHeader(getSimpleHeaderData());

    protected JSeparator headerSeparator = new JSeparator();

    protected JScrollPane menuScroll;

    protected SimpleMenu menu;

    protected SimpleFooter footer;

    public SimpleDrawerBuilder() {
        SimpleMenuOption simpleMenuOption = getSimpleMenuOption();
        this.menu = new SimpleMenu(simpleMenuOption);
        this.menuScroll = createScroll((JComponent) this.menu);
        this.footer = new SimpleFooter(getSimpleFooterData());
    }

    protected JScrollPane createScroll(JComponent component) {
        JScrollPane scroll = new JScrollPane(component);
        scroll.setHorizontalScrollBarPolicy(31);
        String background = FlatLafStyleUtils.getStyleValue(component, "background", "null");
        scroll.putClientProperty("FlatLaf.style", "background:" + background);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        scroll.getHorizontalScrollBar().setUnitIncrement(10);
        scroll.getVerticalScrollBar().putClientProperty("FlatLaf.style", "width:9;trackArc:999;thumbInsets:0,3,0,3;trackInsets:0,3,0,3;background:" + background);
        if (!background.equals("null")) {
            FlatLafStyleUtils.appendStyleIfAbsent(scroll.getVerticalScrollBar(), "track:" + background);
        }
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    public Component getHeader() {
        return (Component) this.header;
    }

    public Component getHeaderSeparator() {
        return this.headerSeparator;
    }

    public Component getMenu() {
        return this.menuScroll;
    }

    public Component getFooter() {
        return (Component) this.footer;
    }

    public int getDrawerWidth() {
        return 275;
    }

    public void build(DrawerPanel drawerPanel) {
    }

    public void rebuildMenu() {
        if (this.menu != null) {
            this.menu.rebuildMenu();
        }
    }

    public abstract SimpleHeaderData getSimpleHeaderData();

    public abstract SimpleMenuOption getSimpleMenuOption();

    public abstract SimpleFooterData getSimpleFooterData();
}
