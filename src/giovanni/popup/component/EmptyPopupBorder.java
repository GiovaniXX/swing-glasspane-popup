package giovanni.popup.component;

import java.awt.BorderLayout;
import java.awt.Component;

public class EmptyPopupBorder extends GlassPaneChild {

    public EmptyPopupBorder(Component component) {
        setLayout(new BorderLayout());
        add(component);
    }

    public EmptyPopupBorder(GlassPaneChild glassPaneChild) {
    }
}
