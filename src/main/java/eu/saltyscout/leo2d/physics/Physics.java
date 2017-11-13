package eu.saltyscout.leo2d.physics;

import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.Ray;

public class Physics {

    public static RaycastResult cast(Ray ray) {
        return null; //Scene.getDyn4J().raycast(ray);
    }

    @Deprecated
    public static RaycastResult castCamBox(Ray ray) {
        /*RaycastHit hit = new RaycastHit(null, Double.MAX_VALUE, null);
        for (Segment edgeSeg : Scene.getMainCamera().getBoundingSegments()) {
            Triplet<Vector, Float, Float> tHit = ray.intersect(edgeSeg);
            if (tHit.c >= -0.0001 && tHit.c <= edgeSeg.getLength() && tHit.b > 0 && tHit.b < hit.hitDistance) {
                hit = new Physics.RaycastHit(tHit.a, tHit.b, null);
            }
        }*/
        return null;
    }

}
