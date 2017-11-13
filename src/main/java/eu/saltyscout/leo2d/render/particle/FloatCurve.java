package eu.saltyscout.leo2d.render.particle;

import eu.saltyscout.leo2d.util.MathUtil;

/**
 * Created by Peter on 01.11.2017.
 */
public class FloatCurve extends Curve<Double> {

    public FloatCurve(double start) {
        super(start);
    }

    @Override
    Double lerp(float pc, Double a, Double b) {
        return MathUtil.lerp(pc, a, b);
    }
}
