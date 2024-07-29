package giovanni.alerts;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.Graphics2DProxy;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import giovanni.popup.GlassPanePopup;
import giovanni.popup.component.GlassPaneChild;
import giovanni.popup.component.PopupCallbackAction;
import giovanni.popup.component.PopupController;
import giovanni.swing.AnimateIcon;

public class SimpleAlerts extends GlassPaneChild {

    private PanelEffect panelEffect;

    private final Component component;

    private final int option;

    public SimpleAlerts(Component component, AlertsOption alertsOption, int option, PopupCallbackAction callbackAction) {
        this.component = component;
        this.option = option;
        this.callbackAction = callbackAction;
        init(alertsOption);
    }

    private void init(AlertsOption alertsOption) {
        setLayout((LayoutManager) new MigLayout("wrap,fillx,insets 15 0 15 0", "[fill,400]"));
        this.panelEffect = new PanelEffect(this.component, alertsOption);
        add(this.panelEffect);
    }

    @Override
    public void popupShow() {
        this.panelEffect.startAnimation();
    }

    @Override
    public int getRoundBorder() {
        return 20;
    }

    protected final class PanelEffect extends JPanel {

        private final AlertsOption alertsOption;

        private SimpleAlerts.Effect[] effects;

        private float animate;

        private Animator animator;

        private final JLabel labelIcon;

        private final Component component;

        private AnimateIcon animateIcon;

        private final MigLayout layout;

        private final Component closeButton;

        protected void start() {
            if (this.animator == null) {
                this.animator = new Animator(2000, new Animator.TimingTarget() {
                    @Override
                    public void timingEvent(float v) {
                        SimpleAlerts.PanelEffect.this.animate = v;
                        if (SimpleAlerts.PanelEffect.this.animateIcon != null) {
                            SimpleAlerts.PanelEffect.this.animateIcon.setAnimate(v);
                        }
                        SimpleAlerts.PanelEffect.this.repaint();
                    }

                    @Override
                    public void end() {
                        if (SimpleAlerts.PanelEffect.this.alertsOption.loopAnimation && SimpleAlerts.PanelEffect.this.isShowing()) {
                            SwingUtilities.invokeLater(() -> SimpleAlerts.PanelEffect.this.startAnimation());
                        }
                    }
                });
                this.animator.setInterpolator((Animator.Interpolator) CubicBezierEasing.EASE);
            }
            if (this.animator.isRunning()) {
                this.animator.stop();
            }
            this.animator.start();
        }

        private void startAnimation() {
            createEffect();
            start();
        }

        private void createEffect() {
            if (this.alertsOption.effectOption != null) {
                this.effects = new SimpleAlerts.Effect[30];
                for (int i = 0; i < this.effects.length; i++) {
                    this.effects[i] = new SimpleAlerts.Effect(this.alertsOption.effectOption.randomEffect);
                }
            }
        }

        public PanelEffect(Component component, AlertsOption alertsOption) {
            this.component = component;
            this.alertsOption = alertsOption;
            this.layout = new MigLayout("fillx,wrap,insets 0", "[fill,center]", "0[]3[]20[]5");
            setLayout((LayoutManager) this.layout);
            if (alertsOption.icon instanceof AnimateIcon animateIcon1) {
                this.animateIcon = animateIcon1;
            }
            this.labelIcon = new JLabel(alertsOption.icon);
            this.labelIcon.putClientProperty("FlatLaf.style", "border:25,5,10,5");
            boolean ltr = getComponentOrientation().isLeftToRight();
            this.closeButton = createCloseButton();
            add(this.closeButton, "pos " + (ltr ? "100%-pref-25" : "25") + " 2");
            add(this.labelIcon);
            add(component);
            add(createActionButton(SimpleAlerts.this.option, alertsOption.baseColor));
        }

        @Override
        public void applyComponentOrientation(ComponentOrientation o) {
            super.applyComponentOrientation(o);
            boolean ltr = getComponentOrientation().isLeftToRight();
            this.layout.setComponentConstraints(this.closeButton, "pos " + (ltr ? "100%-pref-25" : "25") + " 2");
        }

        protected Component createCloseButton() {
            JButton cmdClose = new JButton((Icon) new FlatSVGIcon("raven/popup/icon/close.svg", 0.8F));
            cmdClose.putClientProperty("FlatLaf.style", "arc:999;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null");
            applyCloseButtonEvent(cmdClose, -1);
            return cmdClose;
        }

        protected void applyCloseButtonEvent(JButton button, int opt) {
            button.addActionListener(e -> {
                if (SimpleAlerts.this.callbackAction == null) {
                    GlassPanePopup.closePopup((Component) SimpleAlerts.this);
                    return;
                }
                PopupController action = SimpleAlerts.this.createController();
                SimpleAlerts.this.callbackAction.action(action, opt);
                if (!action.getConsume()) {
                    GlassPanePopup.closePopup((Component) SimpleAlerts.this);
                }
            });
        }

        @Override
        protected void paintChildren(Graphics g) {
            if (this.alertsOption.effectOption != null && this.effects != null && this.effects.length > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                FlatUIUtils.setRenderingHints(g2);
                int width = getWidth();
                int height = getHeight();
                int x = this.labelIcon.getX() + this.labelIcon.getWidth() / 2;
                int y = this.labelIcon.getY() + this.labelIcon.getHeight() / 2;
                float size = (Math.min(width, height) / 2);
                float l = size * this.animate;
                g2.translate(x, y);
                for (Effect effect : this.effects) {
                    double sp = (effect.speed * 0.05F);
                    double xx = Math.cos(Math.toRadians(effect.direction)) * l * sp;
                    double yy = Math.sin(Math.toRadians(effect.direction)) * l * sp;
                    AffineTransform oldTran = g2.getTransform();
                    Icon icon = this.alertsOption.effectOption.randomEffect[effect.effectIndex];
                    int iw = icon.getIconWidth() / 2;
                    int ih = icon.getIconHeight() / 2;
                    xx -= iw;
                    yy -= ih;
                    g2.translate(xx, yy);
                    g2.rotate(Math.toRadians((this.animate * 360.0F)), iw, ih);
                    if (this.alertsOption.effectOption.effectAlpha < 1.0F) {
                        g2.setComposite(AlphaComposite.getInstance(3, this.alertsOption.effectOption.effectAlpha));
                    }
                    if (this.alertsOption.effectOption.effectFadeOut) {
                        float remove = 0.7F;
                        if (this.animate >= remove) {
                            float f = (this.animate - remove) / (1.0F - remove) * this.alertsOption.effectOption.effectAlpha;
                            g2.setComposite(AlphaComposite.getInstance(3, this.alertsOption.effectOption.effectAlpha - f));
                        }
                    }
                    icon.paintIcon(null, (Graphics) new SimpleAlerts.GraphicsColorFilter(g2, effect.color), 0, 0);
                    g2.setTransform(oldTran);
                }
                g2.dispose();
            }
            super.paintChildren(g);
        }

        private JPanel createActionButton(int option, Color color) {
            JPanel panel = new JPanel((LayoutManager) new MigLayout("insets 3,center,gapx 15", "[90,fill]"));
            switch (option) {
                case 2:
                    panel.add(createButton("Cancel", (Color) null, 2));
                case -1:
                    panel.add(createButton("OK", color, 0), 0);
                    break;
                case 1:
                    panel.add(createButton("Cancel", (Color) null, 2));
                case 0:
                    panel.add(createButton("No", (Color) null, 1), 0);
                    panel.add(createButton("Yes", color, 0), 0);
                    break;
            }
            return panel;
        }

        private JButton createButton(String text, Color color, int option) {
            JButton cmd = new JButton(text);
            applyCloseButtonEvent(cmd, option);
            cmd.putClientProperty("FlatLaf.style", "borderWidth:0;focusWidth:0;innerFocusWidth:0;arc:10;font:+1;margin:5,5,5,5;foreground:" + ((color == null) ? "null" : "#F0F0F0") + ";arc:999");
            if (color != null) {
                cmd.setBackground(color);
            }
            return cmd;
        }
    }

    protected class Effect {

        protected int effectIndex;

        protected Color color;

        protected float direction;

        protected float speed;

        public Effect(Icon[] randomEffect) {
            Random random = new Random();
            this.color = SimpleAlerts.effectColors[random.nextInt(SimpleAlerts.effectColors.length)];
            if (randomEffect != null && randomEffect.length > 0) {
                this.effectIndex = random.nextInt(randomEffect.length);
            }
            this.direction = random.nextInt(360);
            this.speed = (random.nextInt(25) + 5);
        }
    }

    protected static final Color[] effectColors = new Color[]{
        Color.decode("#f43f5e"), Color.decode("#6366f1"),
        Color.decode("#ec4899"), Color.decode("#3b82f6"),
        Color.decode("#d946ef"), Color.decode("#0ea5e9"),
        Color.decode("#a855f7"), Color.decode("#06b6d4"),
        Color.decode("#8b5cf6"), Color.decode("#14b8a6"),
        Color.decode("#10b981"), Color.decode("#22c55e"),
        Color.decode("#84cc16"), Color.decode("#eab308"),
        Color.decode("#f59e0b"), Color.decode("#f97316"),
        Color.decode("#ef4444"), Color.decode("#78716c"),
        Color.decode("#737373"), Color.decode("#71717a"),
        Color.decode("#6b7280"), Color.decode("#64748b")};

    private static class GraphicsColorFilter extends Graphics2DProxy {

        protected Color color;

        public GraphicsColorFilter(Graphics2D delegate, Color color) {
            super(delegate);
            this.color = color;
        }

        @Override
        public Graphics create() {
            return (Graphics) new GraphicsColorFilter((Graphics2D) super.create(), this.color);
        }

        @Override
        public void setPaint(Paint paint) {
            super.setPaint(this.color);
        }
    }
}
