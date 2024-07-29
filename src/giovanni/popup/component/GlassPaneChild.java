package giovanni.popup.component;

import javax.swing.JPanel;
import giovanni.popup.GlassPanePopup;

public class GlassPaneChild extends JPanel {

    protected PopupController controller;

    protected PopupCallbackAction callbackAction;

    public int getRoundBorder() {
        return 0;
    }

    public void onPush() {
    }

    public void onPop() {
    }

    public void popupShow() {
    }

    protected PopupController createController() {
        return new PopupController() {
            public void closePopup() {
                GlassPanePopup.closePopup(GlassPaneChild.this);
            }
        };
    }
}
