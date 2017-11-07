package eu.saltyscout.leo2d.render.particle;

import eu.saltyscout.leo2d.util.MathUtil;

/**
 * Created by Peter on 01.11.2017.
 */
public class FloatCurve extends Curve<Float> {

    public FloatCurve(float start) {
        super(start);
    }

    @Override
    Float lerp(float pc, Float a, Float b) {
        return MathUtil.lerp(pc, a, b);
    }
}
