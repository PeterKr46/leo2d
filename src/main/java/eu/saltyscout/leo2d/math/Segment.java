package eu.saltyscout.leo2d.math;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.math.Vector;


/**
 * Created by Peter on 05.08.2015.
 */
public class Segment extends Ray {

    private float length = 1.0f;

    public Segment(Vector start, Vector end) {
        super(start, Vector.difference(start, end));
        length = Vector.difference(start, end).magnitude();
    }

    public Segment(Vector start, Vector direction, float length) {
        super(start, direction);
        this.length = Math.abs(length);
    }

    public void setLength(float length) {
        this.length = Math.abs(length);
    }

    public float getLength() {
        return length;
    }

    @Override
    public void visualize() {
        float[] soft = new float[]{debugColor[0], debugColor[1], debugColor[2], 0.2f};
        Vector delta = getDirection().clone().mul(length);
        Vector endmarker = getDirection().clone().rotate(90).mul(Scene.getMainCamera().getVerticalSize() / 20);

        VoltImg volty = Scene.getMainCamera().getVolty();
        Vector origin = Vector.copyOnWrite(getDirection());

        volty.line(origin, origin.add(delta), debugColor);
        volty.line(origin.sub(endmarker), origin.add(endmarker), soft);
        //volty.line(origin.add(delta).sub(endmarker), origin.add(delta).add(endmarker));
    }

}
