package eu.saltyscout.leo2d.render.particle;

import eu.saltyscout.leo2d.util.MathUtil;

/**
 * Created by Peter on 01.11.2017.
 */
public class FloatArrayCurve extends Curve<float[]> {

    public FloatArrayCurve(float[] start) {
        super(start);
    }

    @Override
    float[] lerp(float pc, float[] a, float[] b) {
        float[] r = new float[a.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = MathUtil.lerp(pc, a[i], b[i]);
        }
        return r;
    }
}
