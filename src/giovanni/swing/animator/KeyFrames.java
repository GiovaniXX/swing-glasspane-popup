package giovanni.swing.animator;

import com.formdev.flatlaf.util.Animator;

public class KeyFrames implements Animator.Interpolator {

    private final EvaluatorFloat evaluator = new EvaluatorFloat();

    private final float[] values;

    public KeyFrames(float... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("number of array can't be empty");
        }
        if (values.length == 1) {
            throw new IllegalArgumentException("number of array must be > 1");
        }
        this.values = values;
    }

    public float interpolate(float fraction) {
        float frame = fraction * (this.values.length - 1);
        int index = (int) frame;
        if (index >= this.values.length - 1) {
            return this.values[this.values.length - 1];
        }
        float time = frame - index;
        float from = this.values[index];
        float target = this.values[index + 1];
        return this.evaluator.evaluate(Float.valueOf(from), Float.valueOf(target), time).floatValue();
    }
}
