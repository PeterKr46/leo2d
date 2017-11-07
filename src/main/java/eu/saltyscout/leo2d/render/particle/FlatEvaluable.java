package eu.saltyscout.leo2d.render.particle;

/**
 * Created by Peter on 01.11.2017.
 */
public class FlatEvaluable<T> implements Evaluable<T> {
    private T v;

    public FlatEvaluable(T val) {
        v = val;
    }

    @Override
    public T get(float pc) {
        return v;
    }

    @Override
    public void set(float pc, T value) {
        v = value;
    }
}
