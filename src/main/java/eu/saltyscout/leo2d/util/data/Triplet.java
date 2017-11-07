package eu.saltyscout.leo2d.util.data;

/**
 * Created by peter on 8/9/15.
 */
public class Triplet<T1, T2, T3> extends Pair<T1, T2> {
    public T3 c;

    public Triplet(T1 a, T2 b, T3 c) {
        super(a, b);
        this.c = c;
    }
}
