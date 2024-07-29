package giovanni.popup;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import giovanni.popup.component.GlassPaneChild;

public class GlassPanePopup {

    private static GlassPanePopup instance;

    protected WindowSnapshots windowSnapshots;

    protected JLayeredPane layerPane;

    protected Container contentPane;

    protected Option defaultOption;

    private GlassPanePopup() {
        init();
    }

    private void init() {
        this.layerPane = new JLayeredPane();
        this.layerPane.setLayout(new CardLayout());
    }

    protected void addAndShowPopup(GlassPaneChild component, Option option, String name) {
        GlassPopup popup = new GlassPopup(this, component, option);
        instance.layerPane.applyComponentOrientation(instance.contentPane.getComponentOrientation());
        popup.applyComponentOrientation(instance.contentPane.getComponentOrientation());
        if (name != null) {
            popup.setName(name);
        }
        this.layerPane.setLayer(popup, JLayeredPane.DRAG_LAYER.intValue());
        this.layerPane.add(popup, 0);
        popup.setVisible(true);
        popup.setShowPopup(true);
        if (!this.layerPane.isVisible()) {
            this.layerPane.setVisible(true);
        }
        this.layerPane.grabFocus();
    }

    private void updateLayout() {
        for (Component com : this.layerPane.getComponents()) {
            com.revalidate();
        }
    }

    public static void install(JFrame frame) {
        instance = new GlassPanePopup();
        instance.windowSnapshots = new WindowSnapshots(frame);
        instance.contentPane = frame.getContentPane();
        frame.setGlassPane(instance.layerPane);
        frame.addWindowStateListener(new WindowAdapter() {
            public void windowStateChanged(WindowEvent e) {
                SwingUtilities.invokeLater(() -> GlassPanePopup.instance.updateLayout());
            }
        });
    }

    public static void setDefaultOption(Option option) {
        instance.defaultOption = option;
    }

    public static void showPopup(GlassPaneChild component, Option option, String name) {
        if ((component.getMouseListeners()).length == 0) {
            component.addMouseListener(new MouseAdapter() {

            });
        }
        instance.addAndShowPopup(component, option, name);
    }

    public static void showPopup(GlassPaneChild component, Option option) {
        showPopup(component, option, null);
    }

    public static void showPopup(GlassPaneChild component, String name) {
        Option option = (instance.defaultOption == null) ? new DefaultOption() : instance.defaultOption;
        showPopup(component, option, name);
    }

    public static void showPopup(GlassPaneChild component) {
        showPopup(component, new DefaultOption(), null);
    }

    public static void push(GlassPaneChild component, String name) {
        for (Component com : instance.layerPane.getComponents()) {
            if (com.getName() != null && com.getName().equals(name)
                    && com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                popup.getComponentLayer().pushComponent(component);
                break;
            }
        }
    }

    public static void pop(String name) {
        for (Component com : instance.layerPane.getComponents()) {
            if (com.getName() != null && com.getName().equals(name)
                    && com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                popup.getComponentLayer().popComponent();
                break;
            }
        }
    }

    public static void pop(Component component) {
        for (Component com : instance.layerPane.getComponents()) {
            if (com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                if (popup.getComponent() == component) {
                    popup.getComponentLayer().popComponent();
                }
            }
        }
    }

    public static void closePopup(int index) {
        index = instance.layerPane.getComponentCount() - 1 - index;
        if (index >= 0 && index < instance.layerPane.getComponentCount()
                && instance.layerPane.getComponent(index) instanceof GlassPopup) {
            GlassPopup popup = (GlassPopup) instance.layerPane.getComponent(index);
            popup.setShowPopup(false);
        }
    }

    public static void closePopup(String name) {
        for (Component com : instance.layerPane.getComponents()) {
            if (com.getName() != null && com.getName().equals(name)
                    && com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                popup.setShowPopup(false);
            }
        }
    }

    public static void closePopup(Component component) {
        for (Component com : instance.layerPane.getComponents()) {
            if (com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                if (popup.getComponent() == component) {
                    popup.setShowPopup(false);
                }
            }
        }
    }

    public static void closePopupLast() {
        closePopup(getPopupCount() - 1);
    }

    public static void closePopupAll() {
        for (Component com : instance.layerPane.getComponents()) {
            if (com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                popup.setShowPopup(false);
            }
        }
    }

    public static boolean isShowing(String name) {
        boolean act = false;
        for (Component com : instance.layerPane.getComponents()) {
            if (com.getName() != null && com.getName().equals(name)) {
                act = true;
                break;
            }
        }
        return act;
    }

    public static boolean isShowing(Component component) {
        boolean act = false;
        for (Component com : instance.layerPane.getComponents()) {
            if (com instanceof GlassPopup) {
                GlassPopup popup = (GlassPopup) com;
                if (popup.getComponent() == component) {
                    act = true;
                    break;
                }
            }
        }
        return act;
    }

    public static int getPopupCount() {
        return instance.layerPane.getComponentCount();
    }

    protected synchronized void removePopup(Component popup) {
        this.layerPane.remove(popup);
        if (this.layerPane.getComponentCount() == 0) {
            this.layerPane.setVisible(false);
        }
    }
}
