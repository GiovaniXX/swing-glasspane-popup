package giovanni.swing;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class AnimateIcon extends FlatSVGIcon {

    private Component component;

    private Animator animator;

    private AnimateOption animateOption;

    private float animate;

    public AnimateIcon(String name, float scale) {
        this(name, scale, new AnimateOption());
    }

    public AnimateIcon(String name, float scale, AnimateOption animateOption) {
        super(name, scale);
        this.animateOption = animateOption;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (c != this.component) {
            this.component = c;
        }
        float scale = getInterpolator(this.animateOption.scaleInterpolator, this.animate, 1.0F);
        if (scale > 0.0F) {
            Graphics2D g2 = (Graphics2D) g;
            AffineTransform tran = g2.getTransform();
            int centerX = x + getIconWidth() / 2;
            int centerY = y + getIconHeight() / 2;
            g2.translate(centerX, centerY);
            g2.rotate(getInterpolator(this.animateOption.rotateInterpolator, this.animate, 0.0F));
            g2.scale(scale, scale);
            g2.translate(-centerX, -centerY);
            super.paintIcon(c, g, x, y);
            g2.setTransform(tran);
        }
    }

    protected float getInterpolator(Animator.Interpolator interpolator, float fraction, float isNull) {
        if (interpolator != null) {
            return interpolator.interpolate(fraction);
        }
        return isNull;
    }

    public void setAnimate(float f) {
        this.animate = getInterpolator(this.animateOption.interpolator, f, f);
        if (this.component != null) {
            this.component.repaint();
        }
    }

    public boolean animate() {
        boolean act = false;
        if (this.component != null) {
            if (this.animator == null) {
                this.animator = new Animator(2000, f -> setAnimate(f));
                this.animator.setInterpolator((Animator.Interpolator) CubicBezierEasing.EASE);
            }
            if (!this.animator.isRunning()) {
                this.animator.start();
                act = true;
            }
        }
        return act;
    }

    public static class AnimateOption {

        protected Animator.Interpolator interpolator;

        protected Animator.Interpolator scaleInterpolator;

        protected Animator.Interpolator rotateInterpolator;

        public AnimateOption setInterpolator(Animator.Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public AnimateOption setScaleInterpolator(Animator.Interpolator scaleInterpolator) {
            this.scaleInterpolator = scaleInterpolator;
            return this;
        }

        public AnimateOption setRotateInterpolator(Animator.Interpolator rotateInterpolator) {
            this.rotateInterpolator = rotateInterpolator;
            return this;
        }
    }
}
