package eu.saltyscout.leo2d.component;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.leo2d.math.Segment;
import eu.saltyscout.leo2d.physics.Physics;
import eu.saltyscout.leo2d.physics.collider.CircleCollider;
import eu.saltyscout.leo2d.physics.collider.Collider;
import eu.saltyscout.leo2d.physics.collider.EdgeCollider;
import eu.saltyscout.leo2d.util.data.PriorityQueue;
import eu.saltyscout.math.Vector;

public class Light implements Component {

    private final Transform transform;
    private boolean enabled = true;

    public Light(Transform transform) {
        this.transform = transform;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void earlyUpdate() {

    }

    public void update() {
        PriorityQueue<Vector> verts = new PriorityQueue<>();
        for (Segment edge : Scene.getMainCamera().getBoundingSegments()) {
            Ray ray = new Ray(transform.getPosition(), edge.getOrigin().clone().sub(transform.getPosition()));
            Physics.RaycastHit hit = Physics.cast(ray);
            if (hit.point != null) {
                verts.enqueue(hit.point, ray.getDirection().getAngle() * 100.0);
            } else {
                verts.enqueue(edge.getOrigin(), ray.getDirection().getAngle() * 100.0);
            }
        }
        for (Collider collider : Scene.findAll(Collider.class)) {
            // TODO
            if (collider.getBounds().intersects(Scene.getMainCamera().getAABB())) {
                if (collider instanceof EdgeCollider) {
                    EdgeCollider coll = (EdgeCollider) collider;
                    for (Ray ray : coll.getWorldEdges()) {
                        Vector vertex = ray.getOrigin();
                        Ray r = new Ray(transform.getPosition(), vertex.clone().sub(transform.getPosition()));
                        Ray r2 = new Ray(transform.getPosition(), r.getDirection().rotate(0.2f));
                        Ray r3 = new Ray(transform.getPosition(), r.getDirection().rotate(-0.2f));
                        Physics.RaycastHit hit = Physics.cast(r);
                        Physics.RaycastHit hit2 = Physics.cast(r2);
                        Physics.RaycastHit hit3 = Physics.cast(r3);
                        if (hit.point == null) {
                            hit = Physics.castCamBox(r);
                        }
                        if (hit2.point == null) {
                            hit2 = Physics.castCamBox(r2);
                        }
                        if (hit3.point == null) {
                            hit3 = Physics.castCamBox(r3);
                        }
                        if (hit.point != null) verts.enqueue(hit.point, r.getDirection().getAngle() * 100.0);
                        if (hit2.point != null) verts.enqueue(hit2.point, r2.getDirection().getAngle() * 100.0);
                        if (hit3.point != null) verts.enqueue(hit3.point, r3.getDirection().getAngle() * 100.0);
                    }
                } else if (collider instanceof CircleCollider) {
                    CircleCollider coll = (CircleCollider) collider;
                    Vector center = coll.getCenter();
                    Vector dir = center.clone().sub(transform.getPosition()).normalize();
                    Vector orth = dir.getOrth();
                    Vector a = center.clone().add(orth.mul(coll.getRadius()));
                    Vector b = center.clone().add(orth.mul(-1));
                    for (Vector vertex : new Vector[]{a, b}) {
                        Ray r = new Ray(transform.getPosition(), vertex.clone().sub(transform.getPosition()));
                        Ray r2 = new Ray(transform.getPosition(), r.getDirection().rotate(0.2f));
                        Ray r3 = new Ray(transform.getPosition(), r.getDirection().rotate(-0.2f));
                        Physics.RaycastHit hit = Physics.cast(r);
                        Physics.RaycastHit hit2 = Physics.cast(r2);
                        Physics.RaycastHit hit3 = Physics.cast(r3);
                        if (hit.point == null) {
                            hit = Physics.castCamBox(r);
                        }
                        if (hit2.point == null) {
                            hit2 = Physics.castCamBox(r2);
                        }
                        if (hit3.point == null) {
                            hit3 = Physics.castCamBox(r3);
                        }
                        if (hit.point != null) verts.enqueue(hit.point, r.getDirection().getAngle() * 100.0);
                        if (hit2.point != null) verts.enqueue(hit2.point, r2.getDirection().getAngle() * 100.0);
                        if (hit3.point != null) verts.enqueue(hit3.point, r3.getDirection().getAngle() * 100.0);
                    }
                }
            }
        }
        Object[] raw = verts.toArray();
        Vector[] data = new Vector[raw.length + 1];
        for (int i = 0; i < raw.length; i++) {
            data[i] = (Vector) raw[i];
        }
        data[raw.length] = (Vector) raw[0];
        VoltImg volty = Scene.getMainCamera().getVolty();
        volty.gl().glColor4f(1, 1, 1, 0.1f);
        volty.triangleFan(transform.getPosition(), data);
        volty.filledCircle(transform.getPosition(), Scene.getMainCamera().getVerticalSize() / 50, 1, new float[]{1, 0, 0});
        volty.gl().glColor3f(1, 1, 1);
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

}