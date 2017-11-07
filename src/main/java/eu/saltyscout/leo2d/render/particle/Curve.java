package eu.saltyscout.leo2d.render.particle;

/**
 * Created by Peter on 01.11.2017.
 */
public abstract class Curve<T> implements Evaluable<T> {

    private CurveChain chain;

    public Curve(T start) {
        chain = new CurveChain(0, start);
    }

    @Override
    public final T get(float pc) {
        CurveChain prev = chain, next = null;
        while (prev.hasNext() && next == null) {
            if (prev.next.pc < pc) {
                prev = prev.next;
            } else {
                next = prev.next;
            }
        }
        if (next == null) {
            next = prev;
        }
        return lerp((pc - prev.pc) / (next.pc - prev.pc), prev.value, next.value);
    }

    abstract T lerp(float pc, T a, T b);

    @Override
    public final void set(float pc, T value) {
        chain.add(new CurveChain(pc, value));
    }

    class CurveChain {
        final float pc;
        T value = null;
        CurveChain next = null;

        CurveChain(float pc, T value) {
            this.pc = pc;
            this.value = value;
        }

        CurveChain add(CurveChain c) {
            if (c.pc >= pc) {
                // c is after this
                if (next == null) {
                    // c is next
                    next = c;
                    return this;
                } else {
                    next = next.add(c);
                    return this;
                }
            } else {
                // c takes this place
                c.add(this);
                return c;
            }
        }

        boolean hasNext() {
            return next != null;
        }
    }
}
