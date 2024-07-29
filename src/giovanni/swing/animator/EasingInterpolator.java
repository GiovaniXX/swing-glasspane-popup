package giovanni.swing.animator;

import com.formdev.flatlaf.util.Animator;

public abstract class EasingInterpolator implements Animator.Interpolator {

    public static final EasingInterpolator EASE_OUT_BOUNCE = new EaseOutBounce();

    private static class EaseOutBounce extends EasingInterpolator {

        private EaseOutBounce() {
        }

        public float interpolate(float f) {
            double v;
            float n1 = 7.5625F;
            float d1 = 2.75F;
            if (f < 1.0F / d1) {
                v = (n1 * f * f);
            } else if (f < 2.0F / d1) {
                v = (n1 * (f = (float) (f - 1.5D / d1)) * f) + 0.75D;
            } else if (f < 2.5D / d1) {
                v = (n1 * (f = (float) (f - 2.25D / d1)) * f) + 0.9375D;
            } else {
                v = (n1 * (f = (float) (f - 2.625D / d1)) * f) + 0.984375D;
            }
            return (float) v;
        }
    }
}
