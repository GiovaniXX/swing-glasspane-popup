package giovanni.drawer.component.menu;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import giovanni.utils.FlatLafStyleUtils;

public class SimpleMenu extends JPanel {

    private final SimpleMenuOption simpleMenuOption;

    public SimpleMenu(SimpleMenuOption simpleMenuOption) {
        this.simpleMenuOption = simpleMenuOption;
        init();
    }

    public void rebuildMenu() {
        removeAll();
        buildMenu();
    }

    private void init() {
        setLayout(new MenuLayout());
        if (this.simpleMenuOption.simpleMenuStyle != null) {
            this.simpleMenuOption.simpleMenuStyle.styleMenu(this);
        }
        buildMenu();
    }

    private void buildMenu() {
        String[][] menus = this.simpleMenuOption.menus;
        if (menus != null) {
            int index = 0;
            int validationIndex = -1;
            for (int i = 0; i < menus.length; i++) {
                String[] menu = menus[i];
                if (menu.length > 0) {
                    String label = checkLabel(menu);
                    if (label != null) {
                        if (checkLabelValidation(i, index)) {
                            add(createLabel(label));
                        }
                    } else {
                        boolean validation = this.simpleMenuOption.menuValidation.menuValidation(++validationIndex, 0);
                        if (validation) {
                            if (menu.length == 1) {
                                JButton button = createMenuItem(menu[0], index);
                                applyMenuEvent(button, index, 0);
                                add(button);
                            } else {
                                add(createSubmenuItem(menu, index, validationIndex));
                            }
                        }
                        if (validation || this.simpleMenuOption.menuValidation.keepMenuValidationIndex) {
                            index++;
                        }
                    }
                }
            }
        }
    }

    private String getBasePath() {
        if (this.simpleMenuOption.baseIconPath == null) {
            return "";
        }
        if (this.simpleMenuOption.baseIconPath.endsWith("/")) {
            return this.simpleMenuOption.baseIconPath;
        }
        return this.simpleMenuOption.baseIconPath + "/";
    }

    protected JButton createMenuItem(String name, int index) {
        JButton button = new JButton(name);
        if (this.simpleMenuOption.icons != null && index < this.simpleMenuOption.icons.length) {
            String path = getBasePath();
            Icon icon = this.simpleMenuOption.buildMenuIcon(path + this.simpleMenuOption.icons[index], this.simpleMenuOption.iconScale);
            if (icon != null) {
                button.setIcon(icon);
            }
        }
        button.setHorizontalAlignment(10);
        if (this.simpleMenuOption.simpleMenuStyle != null) {
            this.simpleMenuOption.simpleMenuStyle.styleMenuItem(button, index);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(button, "arc:0;margin:6,20,6,20;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;iconTextGap:5;");
        return button;
    }

    protected void applyMenuEvent(JButton button, int index, int subIndex) {
        button.addActionListener(e -> {
            MenuAction action = runEvent(index, subIndex);
            if (action != null);
        });
    }

    private MenuAction runEvent(int index, int subIndex) {
        if (!this.simpleMenuOption.events.isEmpty()) {
            MenuAction action = new MenuAction();
            if (this.simpleMenuOption.menuItemAutoSelect) {
                action.selected();
            }
            for (MenuEvent event : this.simpleMenuOption.events) {
                event.selected(action, index, subIndex);
            }
            return action;
        }
        return null;
    }

    protected Component createSubmenuItem(String[] menu, int index, int validationIndex) {
        JPanel panelItem = new SubMenuItem(menu, index, validationIndex);
        return panelItem;
    }

    protected String checkLabel(String[] menu) {
        String label = menu[0];
        if (label.startsWith("~") && label.endsWith("~")) {
            return label.substring(1, label.length() - 1);
        }
        return null;
    }

    protected boolean checkLabelValidation(int labelIndex, int menuIndex) {
        if (this.simpleMenuOption.menuValidation.labelValidation(labelIndex)) {
            if (this.simpleMenuOption.menuValidation.removeLabelWhenEmptyMenu) {
                boolean fondMenu = false;
                for (int i = labelIndex + 1; i < this.simpleMenuOption.menus.length;) {
                    String label = checkLabel(this.simpleMenuOption.menus[i]);
                    if (label == null) {
                        if (this.simpleMenuOption.menuValidation.menuValidation(menuIndex, 0)) {
                            fondMenu = true;
                            break;
                        }
                        menuIndex++;
                        i++;
                    }
                }
                return fondMenu;
            }
            return true;
        }
        return false;
    }

    protected Component createLabel(String name) {
        JLabel label = new JLabel(name);
        if (this.simpleMenuOption.simpleMenuStyle != null) {
            this.simpleMenuOption.simpleMenuStyle.styleLabel(label);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(label, "border:8,10,8,10;[light]foreground:lighten($Label.foreground,30%);[dark]foreground:darken($Label.foreground,30%)");
        return label;
    }

    public SimpleMenuOption getSimpleMenuOption() {
        return this.simpleMenuOption;
    }

    protected class SubMenuItem extends JPanel {

        private SubmenuLayout menuLayout;

        private boolean menuShow;

        private final String[] menu;

        private final int index;

        private final int validationIndex;

        private int iconWidth;

        public void setAnimate(float animate) {
            this.menuLayout.setAnimate(animate);
        }

        public SubMenuItem(String[] menu, int index, int validationIndex) {
            this.menu = menu;
            this.index = index;
            this.validationIndex = validationIndex;
            init();
        }

        private void init() {
            this.menuLayout = new SubmenuLayout();
            setLayout(this.menuLayout);
            putClientProperty("FlatLaf.style", "background:null");
            this.iconWidth = 22;
            int index = 0;
            int validationIndex = -1;
            for (int i = 0; i < this.menu.length; i++) {
                boolean validation = SimpleMenu.this.simpleMenuOption.menuValidation.menuValidation(this.validationIndex, ++validationIndex);
                if (validation) {
                    if (i == 0) {
                        JButton button = SimpleMenu.this.createMenuItem(this.menu[i], this.index);
                        if (button.getIcon() != null) {
                            this.iconWidth = UIScale.unscale(button.getIcon().getIconWidth());
                        }
                        createMainMenuEvent(button);
                        SimpleMenu.this.applyMenuEvent(button, this.index, index);
                        add(button);
                    } else {
                        JButton button = createSubMenuItem(this.menu[i], index, this.iconWidth);
                        SimpleMenu.this.applyMenuEvent(button, this.index, index);
                        add(button);
                    }
                }
                if (validation || SimpleMenu.this.simpleMenuOption.menuValidation.keepMenuValidationIndex) {
                    index++;
                }
            }
        }

        private void createMainMenuEvent(JButton button) {
            button.addActionListener(e -> {
                this.menuShow = !this.menuShow;
                (new MenuAnimation(this)).run(this.menuShow);
            });
        }

        protected JButton createSubMenuItem(String name, int index, int gap) {
            JButton button = new JButton(name);
            button.setHorizontalAlignment(10);
            if (SimpleMenu.this.simpleMenuOption.simpleMenuStyle != null) {
                SimpleMenu.this.simpleMenuOption.simpleMenuStyle.styleSubMenuItem(button, this.index, index);
            }
            FlatLafStyleUtils.appendStyleIfAbsent(button, "arc:0;margin:7," + (gap + 25) + ",7," + (gap + 25) + ";borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null");
            return button;
        }

        protected void paintChildren(Graphics g) {
            super.paintChildren(g);
            if (getComponentCount() > 0) {
                boolean ltr = getComponentOrientation().isLeftToRight();
                Color foreground = getComponent(0).getForeground();
                int menuHeight = getComponent(0).getHeight();
                int width = getWidth();
                int height = getHeight();
                Graphics2D g2 = (Graphics2D) g.create();
                FlatUIUtils.setRenderingHints(g2);
                int last = getLastLocation();
                int round = UIScale.scale(8);
                int gap = UIScale.scale(20 + this.iconWidth / 2);
                Path2D.Double p = new Path2D.Double();
                int x = ltr ? gap : (width - gap);
                p.moveTo(x, menuHeight);
                p.lineTo(x, (last - round));
                int count = getComponentCount();
                for (int i = 1; i < count; i++) {
                    Component com = getComponent(i);
                    int y = com.getY() + com.getHeight() / 2;
                    p.append(createCurve(round, x, y, ltr), false);
                }
                Color color = ColorFunctions.mix(getBackground(), foreground, 0.7F);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(UIScale.scale(1.0F)));
                g2.draw(p);
                paintArrow(g2, width, menuHeight, this.menuLayout.getAnimate(), ltr);
                g2.dispose();
            }
        }

        private int getLastLocation() {
            Component com = getComponent(getComponentCount() - 1);
            return com.getY() + com.getHeight() / 2;
        }

        private Shape createCurve(int round, int x, int y, boolean ltr) {
            Path2D p2 = new Path2D.Double();
            p2.moveTo(x, (y - round));
            p2.curveTo(x, (y - round), x, y, (x + (ltr ? round : -round)), y);
            return p2;
        }

        private void paintArrow(Graphics2D g2, int width, int height, float animate, boolean ltr) {
            int arrowWidth = UIScale.scale(10);
            int arrowHeight = UIScale.scale(4);
            int gap = UIScale.scale(15);
            int x = ltr ? (width - arrowWidth - gap) : gap;
            int y = (height - arrowHeight) / 2;
            Path2D p = new Path2D.Double();
            p.moveTo(0.0D, (animate * arrowHeight));
            p.lineTo((arrowWidth / 2), ((1.0F - animate) * arrowHeight));
            p.lineTo(arrowWidth, (animate * arrowHeight));
            g2.translate(x, y);
            g2.draw(p);
        }

        protected class SubmenuLayout implements LayoutManager {

            private float animate;

            public void setAnimate(float animate) {
                this.animate = animate;
                SimpleMenu.SubMenuItem.this.revalidate();
            }

            public float getAnimate() {
                return this.animate;
            }

            public void addLayoutComponent(String name, Component comp) {
            }

            public void removeLayoutComponent(Component comp) {
            }

            public Dimension preferredLayoutSize(Container parent) {
                synchronized (parent.getTreeLock()) {
                    Insets insets = parent.getInsets();
                    int width = insets.left + insets.right;
                    int height = insets.top + insets.bottom;
                    int count = parent.getComponentCount();
                    int first = -1;
                    for (int i = 0; i < count; i++) {
                        Component com = parent.getComponent(i);
                        if (com.isVisible()) {
                            if (first == -1) {
                                first = (com.getPreferredSize()).height;
                            }
                            height += (com.getPreferredSize()).height;
                        }
                    }
                    int space = height - first;
                    height = (int) (first + space * this.animate);
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
                    int x = insets.left;
                    int y = insets.top;
                    int width = parent.getWidth() - insets.left + insets.right;
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
    }
}
