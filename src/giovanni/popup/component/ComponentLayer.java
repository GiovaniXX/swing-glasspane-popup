package giovanni.popup.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Stack;
import javax.swing.JPanel;

public class ComponentLayer extends JPanel {

    private GlassPaneChild component;

    private GlassPaneChild nextComponent;

    private Animator animator;

    private float animate;

    private boolean showSnapshot;

    private boolean simpleSnapshot;

    private Image image;

    private Image nextImage;

    private PopupLayout popupLayout;

    private Stack<GlassPaneChild> componentStack;

    private boolean push;

    private void pushStack(GlassPaneChild component) {
        if (this.componentStack == null) {
            this.componentStack = new Stack<>();
        }
        this.componentStack.push(component);
    }

    public ComponentLayer(GlassPaneChild component) {
        this.component = component;
        init();
    }

    private void init() {
        this.popupLayout = new PopupLayout();
        setLayout(this.popupLayout);
        if (this.component.getRoundBorder() > 0) {
            setOpaque(false);
            this.component.setOpaque(false);
        }
        add(this.component);
        this.component.setVisible(false);
    }

    private void initAnimator() {
        this.animator = new Animator(250, new Animator.TimingTarget() {
            private boolean layoutChang;

            public void timingEvent(float v) {
                ComponentLayer.this.animate = v;
                if (this.layoutChang) {
                    ComponentLayer.this.revalidate();
                    ComponentLayer.this.repaint();
                } else {
                    ComponentLayer.this.repaint();
                }
            }

            public void begin() {
                ComponentLayer.this.nextImage = ComponentImageUtils.createImage(ComponentLayer.this.nextComponent);
                ComponentLayer.this.popupLayout.snapshotLayout(false);
                Dimension from = ComponentLayer.this.component.getPreferredSize();
                Dimension target = ComponentLayer.this.nextComponent.getPreferredSize();
                this.layoutChang = (from.width != target.width || from.height != target.height);
                ComponentLayer.this.popupLayout.set(from, target);
            }

            public void end() {
                ComponentLayer.this.showSnapshot = false;
                ComponentLayer.this.component = ComponentLayer.this.nextComponent;
                ComponentLayer.this.component.popupShow();
                ComponentLayer.this.nextComponent = null;
                ComponentLayer.this.animate = 0.0F;
                ComponentLayer.this.component.setVisible(true);
                if (ComponentLayer.this.nextImage != null) {
                    ComponentLayer.this.nextImage.flush();
                }
            }
        });
        this.animator.setInterpolator((Animator.Interpolator) CubicBezierEasing.EASE_IN_OUT);
    }

    private void startAnimate() {
        if (this.animator == null) {
            initAnimator();
        }
        if (this.animator.isRunning()) {
            this.animator.stop();
        }
        this.animate = 0.0F;
        this.animator.start();
    }

    public void pushComponent(GlassPaneChild component) {
        component.onPush();
        pushStack(this.component);
        this.push = true;
        showComponent(component);
    }

    public void popComponent() {
        if (!this.componentStack.isEmpty()) {
            GlassPaneChild component = this.componentStack.pop();
            component.onPop();
            this.push = false;
            showComponent(component);
        }
    }

    private void showComponent(GlassPaneChild component) {
        this.showSnapshot = true;
        this.nextComponent = component;
        this.image = ComponentImageUtils.createImage(this.component);
        if (component.getRoundBorder() > 0) {
            setOpaque(false);
            component.setOpaque(false);
        }
        component.setVisible(false);
        remove(this.component);
        add(component);
        this.popupLayout.snapshotLayout(true);
        startAnimate();
    }

    public void showSnapshot() {
        if (this.animator != null && this.animator.isRunning()) {
            this.animator.stop();
        }
        this.simpleSnapshot = true;
        revalidate();
        if (this.component.isVisible()) {
            this.image = ComponentImageUtils.createImage(this.component, this.component.getRoundBorder());
            this.component.setVisible(false);
        } else {
            EventQueue.invokeLater(() -> {
                this.image = ComponentImageUtils.createImage(this.component, this.component.getRoundBorder());
                this.component.setVisible(false);
            });
        }
    }

    public void hideSnapshot() {
        this.showSnapshot = false;
        this.simpleSnapshot = false;
        this.component.setVisible(true);
        if (this.image != null) {
            this.image.flush();
        }
    }

    protected void paintComponent(Graphics g) {
        if (!isOpaque() && this.component.getRoundBorder() > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(getBackground());
            int arc = UIScale.scale(this.component.getRoundBorder());
            FlatUIUtils.paintComponentBackground(g2, 0, 0, getWidth(), getHeight(), 0.0F, arc);
            g2.dispose();
        }
        super.paintComponent(g);
    }

    public void paint(Graphics g) {
        if (this.simpleSnapshot) {
            g.drawImage(this.image, 0, 0, null);
        } else if (this.showSnapshot) {
            int width = getWidth();
            int height = getHeight();
            if (width > 0 && height > 0) {
                BufferedImage bufferedImage = new BufferedImage(width, height, 2);
                Graphics2D g2 = bufferedImage.createGraphics();
                FlatUIUtils.setRenderingHints(g2);
                int arc = UIScale.scale(this.component.getRoundBorder());
                g2.setColor(getBackground());
                FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, 0.0F, arc);
                if (this.image != null) {
                    double x;
                    int w = this.image.getWidth(null);
                    if (this.push) {
                        x = (-w * this.animate);
                    } else {
                        x = (w * this.animate);
                    }
                    g2.setComposite(AlphaComposite.SrcAtop.derive(1.0F - this.animate));
                    g2.drawImage(this.image, (int) x, 0, (ImageObserver) null);
                }
                if (this.nextImage != null) {
                    double x;
                    int w = this.nextImage.getWidth(null);
                    if (this.push) {
                        x = (getWidth() - w * this.animate);
                    } else {
                        x = (-getWidth() + w * this.animate);
                    }
                    g2.setComposite(AlphaComposite.SrcAtop.derive(this.animate));
                    g2.drawImage(this.nextImage, (int) x, 0, (ImageObserver) null);
                }
                g2.dispose();
                g.drawImage(bufferedImage, 0, 0, null);
            }
        } else {
            super.paint(g);
        }
    }

    public GlassPaneChild getComponent() {
        return this.component;
    }

    private class PopupLayout implements LayoutManager {

        private Dimension from;

        private Dimension target;

        private boolean snapshotLayout;

        private PopupLayout() {
        }

        public void snapshotLayout(boolean snapshotLayout) {
            this.snapshotLayout = snapshotLayout;
            ComponentLayer.this.revalidate();
        }

        public void set(Dimension from, Dimension target) {
            this.from = from;
            this.target = target;
        }

        private Dimension getSize() {
            double width = (this.from.width + (this.target.width - this.from.width) * ComponentLayer.this.animate);
            double height = (this.from.height + (this.target.height - this.from.height) * ComponentLayer.this.animate);
            return new Dimension((int) width, (int) height);
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                if (ComponentLayer.this.animate != 0.0F) {
                    return getSize();
                }
                return ComponentLayer.this.component.getPreferredSize();
            }
        }

        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                int width = parent.getWidth();
                int height = parent.getHeight();
                int count = parent.getComponentCount();
                for (int i = 0; i < count; i++) {
                    Component component = parent.getComponent(i);
                    component.setBounds(0, 0, width, height);
                }
            }
        }
    }
}
