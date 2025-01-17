package giovanni.popup.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;

public class ComponentImageUtils {

    public static VolatileImage createImage(Component component) {
        int width = component.getWidth();
        int height = component.getHeight();
        if (width > 0 && height > 0) {
            VolatileImage image = component.createVolatileImage(width, height);
            component.paint(image.createGraphics());
            return image;
        }
        return null;
    }

    public static Image createImage(Component component, int round) {
        int width = component.getWidth();
        int height = component.getHeight();
        if (width > 0 && height > 0) {
            VolatileImage image = createImage(component);
            if (image != null) {
                component.paint(image.createGraphics());
                BufferedImage buffImage = new BufferedImage(width, height, 2);
                Graphics2D g = buffImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = UIScale.scale(round);
                FlatUIUtils.paintComponentBackground(g, 0, 0, width, height, 0.0F, arc);
                g.setComposite(AlphaComposite.SrcIn);
                g.drawImage(image, 0, 0, (ImageObserver) null);
                image.flush();
                return buffImage;
            }
        }
        return null;
    }
}
