package giovanni.swing.animator;

public class EvaluatorFloat extends Evaluator<Float> {

    public Float evaluate(Float from, Float target, float fraction) {
        return Float.valueOf(from.floatValue() + (target.floatValue() - from.floatValue()) * fraction);
    }
}
