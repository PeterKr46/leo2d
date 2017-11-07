package eu.saltyscout.leo2d.physics;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.leo2d.math.Segment;
import eu.saltyscout.leo2d.physics.collider.Collider;
import eu.saltyscout.leo2d.util.data.Triplet;
import eu.saltyscout.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class Physics {

    public static RaycastHit cast(Ray ray) {
        RaycastHit hit = new RaycastHit(null, Double.MAX_VALUE, null);
        for (Collider coll : Scene.findAll(Collider.class)) {
            RaycastHit tHit = coll.cast(ray);
            if (tHit.point != null) {
                if (tHit.hitDistance < hit.hitDistance) {
                    hit = tHit;
                }
            }
        }
        return hit;
    }

    public static RaycastHit cast(Ray ray, Collider... ignore) {
        RaycastHit hit = new RaycastHit(null, Double.MAX_VALUE, null);
        boolean cast;
        for (Collider coll : Scene.findAll(Collider.class)) {
            cast = true;
            for(Component i : ignore) {
                if(coll == i) {
                    cast = false;
                    break;
                }
            }
            if(cast) {
                RaycastHit tHit = coll.cast(ray);
                if (tHit.point != null) {
                    if (tHit.hitDistance < hit.hitDistance) {
                        hit = tHit;
                    }
                }
            }
        }
        return hit;
    }

    public static Collider[] castThru(Ray ray) {
        List<Collider> hit = new ArrayList<Collider>();
        for (Collider coll : Scene.findAll(Collider.class)) {
            RaycastHit tHit = coll.cast(ray);
            if (tHit.point != null) {
                hit.add(coll);
            }
        }
        return hit.toArray(new Collider[hit.size()]);
    }

    @Deprecated
    public static RaycastHit castCamBox(Ray ray) {
        RaycastHit hit = new RaycastHit(null, Double.MAX_VALUE, null);
        for (Segment edgeSeg : Scene.getMainCamera().getBoundingSegments()) {
            Triplet<Vector, Float, Float> tHit = ray.intersect(edgeSeg);
            if (tHit.c >= -0.0001 && tHit.c <= edgeSeg.getLength() && tHit.b > 0 && tHit.b < hit.hitDistance) {
                hit = new Physics.RaycastHit(tHit.a, tHit.b, null);
            }
        }
        return hit;
    }

    public static class RaycastHit {
        public Vector point;
        public double hitDistance;
        public Collider collider;
        public Vector normal;

        public RaycastHit(Vector hit, double hitDistance, Vector normal) {
            this.point = hit;
            this.hitDistance = hitDistance;
            this.normal = normal;
            this.collider = null;
        }

        public RaycastHit(Vector hit, double hitDistance, Vector normal, Collider collider) {
            this.point = hit;
            this.hitDistance = hitDistance;
            this.normal = normal;
            this.collider = collider;
        }
    }
}
