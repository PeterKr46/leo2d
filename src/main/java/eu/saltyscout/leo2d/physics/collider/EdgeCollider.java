package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.math.Segment;
import eu.saltyscout.leo2d.physics.Physics;
import eu.saltyscout.leo2d.util.data.Triplet;
import eu.saltyscout.math.Vector;

import java.util.Arrays;

public class EdgeCollider extends Collider {

    public Edge[] edges;

    public EdgeCollider(Transform transform) {
        super(transform);
        this.edges = new Edge[0];
    }

    public Edge[] getWorldEdges() {
        /*Edge[] segs = new Edge[edges.length];
        for (int i = 0; i < edges.length; i++) {
            Segment edge = edges[i];
            Vector origin = edge.getOrigin().clone().rotate(getTransform().getRotation());
            Vector direction = edge.getDirection().rotate(getTransform().getRotation()).mul(edge.getLength());
            segs[i] = new Edge(origin.add(getTransform().getPosition()), direction, direction.magnitude());
        }
        return segs;*/
        return edges;
    }

    public Physics.RaycastHit cast(Ray ray) {
        Physics.RaycastHit hit = new Physics.RaycastHit(null, Double.MAX_VALUE, null);
        for (Edge edgeSeg : getWorldEdges()) {
            Triplet<Vector, Float, Float> tHit = ray.intersect(edgeSeg);
            if (tHit.c >= -0.001 && tHit.c <= edgeSeg.getLength() && tHit.b > 0 && tHit.b < hit.hitDistance) {
                hit = new Physics.RaycastHit(tHit.a, tHit.b, edgeSeg.getNormal(), this);
            }
        }
        return hit;
    }

    @Override
    public Rect getBounds() {
        Vector pos = getTransform().getPosition();
        Vector min = getTransform().getPosition(), max = min.clone();
        Arrays.stream(edges).flatMap(edge -> Arrays.stream(new Vector[]{edge.getOrigin(), edge.getOrigin().clone().add(edge.getDirection().mul(edge.getLength()))})).map(v -> v.add(pos))
                .forEach(
                        v -> {
                            min.set(Math.min(min.getX(), v.getX()), Math.min(min.getY(), v.getY()));
                            max.set(Math.max(max.getX(), v.getX()), Math.max(max.getY(), v.getY()));
                        });
        Rect r = new Rect(min, max);
        return r;
    }

    @Override
    protected void visualize() {
        for (Edge edge : getWorldEdges()) {
            edge.visualize();
        }
        getBounds().visualize();
    }

    public static class Edge extends Segment {

        private Vector normal;

        public Edge(Vector start, Vector end) {
            super(start, end);
        }

        @Override
        public void setDirection(Vector direction) {
            super.setDirection(direction);
            normal = direction.getOrth();
        }

        public Edge(Vector start, Vector direction, float length) {
            super(start, direction, length);
        }

        public Vector getNormal() {
            return normal.clone();
        }

        public boolean setNormal(Vector normal) {
            if(Vector.dotProd(normal, getDirection()) == 0) {
                this.normal = normal.clone().normalize();
                return true;
            } else {
                return false;
            }
        }

        public void flipNormal() {
            normal.mul(-1);
        }

        @Override
        public void visualize() {
            float[] soft = new float[]{debugColor[0], debugColor[1], debugColor[2], 0.2f};
            VoltImg volty = Scene.getMainCamera().getVolty();

            Vector delta = getDirection().clone().mul(getLength());
            Vector orth = getNormal().mul(Scene.getMainCamera().getVerticalSize()/40);

            volty.line(getOrigin(), getOrigin().add(delta), debugColor);
            Vector half = getOrigin().add(delta.clone().mul(0.5f));
            volty.arrow(half, half.clone().add(orth), soft);
        }
    }
}
