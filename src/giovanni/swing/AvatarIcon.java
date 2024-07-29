package giovanni.swing;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class AvatarIcon implements Icon {

    private String filename;

    private URL location;

    private Image image;

    private int round;

    private int border;

    private int width;

    private int height;

    private int imageWidth;

    private int imageHeight;

    private int oldBorder;

    public AvatarIcon(String filename, int width, int height, int round) {
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.round = round;
    }

    public AvatarIcon(URL location, int width, int height, int round) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.round = round;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        updateImage();
        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);
        g2.setColor(c.getBackground());
        FlatUIUtils.paintComponentBackground(g2, x, y, this.imageWidth, this.imageHeight, 0.0F, UIScale.scale(this.round));
        g2.drawImage(this.image, 0, 0, (ImageObserver) null);
        g2.dispose();
    }

    private void updateImage() {
        if ((this.filename != null || this.location != null) && (this.image == null || this.border != this.oldBorder)) {
            ImageIcon icon;
            this.imageWidth = UIScale.scale(this.width);
            this.imageHeight = UIScale.scale(this.height);
            this.oldBorder = this.border;
            int b = UIScale.scale(this.border);
            if (this.filename != null) {
                icon = new ImageIcon(this.filename);
            } else {
                icon = new ImageIcon(this.location);
            }
            this.image = resizeImage(icon.getImage(), this.imageWidth, this.imageHeight, b);
        }
    }

    private Image resizeImage(Image icon, int width, int height, int border) {
        Image img;
        width -= border * 2;
        height -= border * 2;
        int sw = width - icon.getWidth(null);
        int sh = height - icon.getHeight(null);
        if (sw > sh) {
            img = (new ImageIcon(icon.getScaledInstance(width, -1, 4))).getImage();
        } else {
            img = (new ImageIcon(icon.getScaledInstance(-1, height, 4))).getImage();
        }
        return (this.round > 0) ? roundImage(img, width, height, border, this.round) : img;
    }

    private Image roundImage(Image image, int width, int height, int border, int round) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int x = border + (width - w) / 2;
        int y = border + (height - h) / 2;
        BufferedImage buff = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics2D g = buff.createGraphics();
        FlatUIUtils.setRenderingHints(g);
        if (round == 999) {
            g.fill(new Ellipse2D.Double(border, border, width, height));
        } else {
            int r = UIScale.scale(round);
            g.fill(new RoundRectangle2D.Double(border, border, width, height, r, r));
        }
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(image, x, y, (ImageObserver) null);
        g.dispose();
        return buff;
    }

    public int getIconWidth() {
        updateImage();
        return this.imageWidth;
    }

    public int getIconHeight() {
        updateImage();
        return this.imageHeight;
    }

    public int getRound() {
        return this.round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getBorder() {
        return this.border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
