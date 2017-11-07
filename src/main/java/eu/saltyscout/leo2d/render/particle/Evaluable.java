package eu.saltyscout.leo2d.render.particle;

/**
 * Created by Peter on 01.11.2017.
 */
public interface Evaluable<T> {
    T get(float pc);

    void set(float pc, T value);
}
