package giovanni.popup;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import giovanni.popup.component.ComponentLayer;
import giovanni.popup.component.GlassPaneChild;

public class GlassPopup extends JComponent {

    private final GlassPanePopup parent;

    private final ComponentLayer componentLayer;

    private final Option option;

    private Animator animator;

    private MigLayout layout;

    private float animate;

    private boolean show;

    private boolean mouseHover;

    public GlassPopup(GlassPanePopup parent, GlassPaneChild component, Option option) {
        this.parent = parent;
        this.option = option;
        this.componentLayer = new ComponentLayer(component);
        init();
        initAnimator();
    }

    private void init() {
        this.layout = new MigLayout();
        initOption();
        setLayout((LayoutManager) this.layout);
        add((Component) this.componentLayer, this.option.getLayout(this.parent.layerPane, 0.0F));
    }

    private void initOption() {
        if (this.option.closeWhenClickOutside()) {
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    GlassPopup.this.mouseHover = true;
                }

                public void mouseExited(MouseEvent e) {
                    GlassPopup.this.mouseHover = false;
                }

                public void mouseReleased(MouseEvent e) {
                    if (GlassPopup.this.mouseHover && SwingUtilities.isLeftMouseButton(e)) {
                        GlassPopup.this.setShowPopup(false);
                    }
                }
            });
        } else if (this.option.blockBackground()) {
            addMouseListener(new MouseAdapter() {

            });
        }
        if (this.option.closeWhenPressedEsc()) {
            registerKeyboardAction(e -> setShowPopup(false), KeyStroke.getKeyStroke(27, 0), 2);
        }
    }

    private void initAnimator() {
        this.animator = new Animator(this.option.duration(), new Animator.TimingTarget() {
            public void timingEvent(float fraction) {
                if (GlassPopup.this.show) {
                    GlassPopup.this.animate = fraction;
                } else {
                    GlassPopup.this.animate = 1.0F - fraction;
                }
                float f = GlassPopup.this.format(GlassPopup.this.animate);
                GlassPopup.this.option.setAnimate(f);
                String lc = GlassPopup.this.option.getLayout(GlassPopup.this.parent.layerPane, f);
                if (lc != null) {
                    GlassPopup.this.layout.setComponentConstraints((Component) GlassPopup.this.componentLayer, lc);
                }
                GlassPopup.this.repaint();
                GlassPopup.this.revalidate();
            }

            public void begin() {
                GlassPopup.this.componentLayer.showSnapshot();
                if (GlassPopup.this.option.useSnapshot()) {
                    GlassPopup.this.parent.windowSnapshots.createSnapshot();
                    GlassPopup.this.parent.contentPane.setVisible(false);
                }
            }

            public void end() {
                GlassPopup.this.componentLayer.hideSnapshot();
                if (GlassPopup.this.show) {
                    GlassPopup.this.componentLayer.getComponent().popupShow();
                } else {
                    GlassPopup.this.parent.removePopup(GlassPopup.this);
                }
                if (GlassPopup.this.option.useSnapshot()) {
                    GlassPopup.this.parent.contentPane.setVisible(true);
                    GlassPopup.this.parent.windowSnapshots.removeSnapshot();
                }
            }
        });
        this.animator.setInterpolator((Animator.Interpolator) CubicBezierEasing.EASE_IN_OUT);
    }

    public void setShowPopup(boolean show) {
        if (this.show != show
                && !this.animator.isRunning()) {
            this.show = show;
            this.animator.start();
            if (show) {
                setFocusCycleRoot(true);
            } else {
                uninstallOption();
            }
        }
    }

    private void uninstallOption() {
        if ((getMouseListeners()).length != 0) {
            removeMouseListener(getMouseListeners()[0]);
        }
        if (this.option.closeWhenPressedEsc()) {
            unregisterKeyboardAction(KeyStroke.getKeyStroke(27, 0));
        }
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.SrcOver.derive(this.animate * this.option.opacity()));
        g2.setColor(this.option.background());
        g2.fill(new Rectangle2D.Double(0.0D, 0.0D, getWidth(), getHeight()));
        g2.setComposite(AlphaComposite.SrcOver.derive(this.animate));
        super.paintComponent(g);
    }

    private float format(float v) {
        int a = Math.round(v * 100.0F);
        return a / 100.0F;
    }

    protected Component getComponent() {
        return (Component) this.componentLayer.getComponent();
    }

    protected ComponentLayer getComponentLayer() {
        return this.componentLayer;
    }
}
