package giovanni.alerts;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.Animator;
import java.awt.Color;
import javax.swing.Icon;
import giovanni.swing.AnimateIcon;
import giovanni.swing.animator.EasingInterpolator;
import giovanni.swing.animator.KeyFrames;

public class AlertsOption {

    protected Icon icon;

    protected Color baseColor;

    protected boolean loopAnimation;

    protected EffectOption effectOption;

    public AlertsOption(Icon icon, Color baseColor) {
        this.icon = icon;
        this.baseColor = baseColor;
    }

    public AlertsOption setEffectOption(EffectOption effectOption) {
        this.effectOption = effectOption;
        return this;
    }

    public AlertsOption setLoopAnimation(boolean loopAnimation) {
        this.loopAnimation = loopAnimation;
        return this;
    }

    public static class EffectOption {

        protected float effectAlpha = 1.0F;

        protected boolean effectFadeOut = false;

        protected Icon[] randomEffect;

        public EffectOption setEffectAlpha(float effectAlpha) {
            this.effectAlpha = effectAlpha;
            return this;
        }

        public EffectOption setEffectFadeOut(boolean effectFadeOut) {
            this.effectFadeOut = effectFadeOut;
            return this;
        }

        public EffectOption setRandomEffect(Icon[] randomEffect) {
            this.randomEffect = randomEffect;
            return this;
        }
    }

    protected static AlertsOption getAlertsOption(MessageAlerts.MessageType messageType) {
        if (messageType == MessageAlerts.MessageType.SUCCESS) {
            Icon[] effects = {(Icon) new FlatSVGIcon("raven/alerts/effect/check.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/star.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/firework.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/balloon.svg")};
            return getDefaultOption("giovanni/alerts/icon/success.svg", Color.decode("#10b981"), effects);
        }
        if (messageType == MessageAlerts.MessageType.WARNING) {
            Icon[] effects = {(Icon) new FlatSVGIcon("giovanni/alerts/effect/disclaimer.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/warning.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/query.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/mark.svg")};
            return getDefaultOption("giovanni/alerts/icon/warning.svg", Color.decode("#f59e0b"), effects);
        }
        if (messageType == MessageAlerts.MessageType.ERROR) {
            Icon[] effects = {(Icon) new FlatSVGIcon("giovanni/alerts/effect/error.svg"), (Icon) new FlatSVGIcon("raven/alerts/effect/sad.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/shield.svg"), (Icon) new FlatSVGIcon("giovanni/alerts/effect/nothing.svg")};
            return getDefaultOption("giovanni/alerts/icon/error.svg", Color.decode("#ef4444"), effects);
        }
        return getDefaultOption("giovanni/alerts/icon/information.svg", null);
    }

    private static AlertsOption getDefaultOption(String icon, Color color, Icon[] effects) {
        AnimateIcon.AnimateOption option = (new AnimateIcon.AnimateOption()).setInterpolator((Animator.Interpolator) EasingInterpolator.EASE_OUT_BOUNCE).setScaleInterpolator((Animator.Interpolator) new KeyFrames(new float[]{1.0F, 1.5F, 1.0F})).setRotateInterpolator((Animator.Interpolator) new KeyFrames(new float[]{0.0F, (float) Math.toRadians(-30.0D), 0.0F}));
        return (new AlertsOption((Icon) new AnimateIcon(icon, 4.0F, option), color))
                .setEffectOption((new EffectOption())
                        .setEffectAlpha(0.9F)
                        .setEffectFadeOut(true)
                        .setRandomEffect(effects))
                .setLoopAnimation(true);
    }

    public static AlertsOption getDefaultOption(String icon, Color color) {
        AnimateIcon.AnimateOption option = (new AnimateIcon.AnimateOption()).setScaleInterpolator((Animator.Interpolator) new KeyFrames(new float[]{1.0F, 1.2F, 1.0F})).setRotateInterpolator((Animator.Interpolator) new KeyFrames(new float[]{0.0F, (float) Math.toRadians(-30.0D), (float) Math.toRadians(30.0D), 0.0F}));
        return (new AlertsOption((Icon) new AnimateIcon(icon, 4.0F, option), color))
                .setLoopAnimation(true);
    }
}
