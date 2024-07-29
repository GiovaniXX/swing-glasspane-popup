package giovanni.drawer.component.menu;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;

public class MenuAnimation {

    private final SimpleMenu.SubMenuItem subMenuItem;

    public MenuAnimation(SimpleMenu.SubMenuItem subMenuItem) {
        this.subMenuItem = subMenuItem;
    }

    public void run(final boolean start) {
        Animator animator = new Animator(250, new Animator.TimingTarget() {
            public void timingEvent(float v) {
                float f = start ? v : (1.0F - v);
                MenuAnimation.this.subMenuItem.setAnimate(f);
            }
        });
        animator.setInterpolator((Animator.Interpolator) CubicBezierEasing.EASE_IN_OUT);
        animator.start();
    }
}
