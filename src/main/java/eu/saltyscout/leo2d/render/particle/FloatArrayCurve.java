package eu.saltyscout.leo2d.render.particle;

import eu.saltyscout.leo2d.util.MathUtil;

/**
 * Created by Peter on 01.11.2017.
 */
public class FloatArrayCurve extends Curve<double[]> {

    public FloatArrayCurve(double[] start) {
        super(start);
    }

    @Override
    double[] lerp(float pc, double[] a, double[] b) {
        double[] r = new double[a.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = MathUtil.lerp(pc, a[i], b[i]);
        }
        return r;
    }
}
