package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.physics.Physics;
import eu.saltyscout.leo2d.util.data.Triplet;
import eu.saltyscout.math.Vector;

/**
 * Created by Peter on 11.10.2015.
 */
public class CircleCollider extends Collider {
    private float radius = 0.5f;
    private Vector offset = Vector.of(0, 0);

    public CircleCollider(Transform transform) {
        super(transform);
    }

    @Override
    public Physics.RaycastHit cast(Ray ray) {
        // Construct an orthogonal Ray through the Collider's Center to the one cast.
        Ray orth = new Ray(getTransform().getPosition().add(offset.clone()), ray.getDirection().getOrth());
        // Intersect the two rays.
        Triplet<Vector, Float, Float> hit = ray.intersect(orth);
        // If the distance from orth.origin is less than the radius, it hits.
        if (hit.c >= -radius && hit.c <= radius && hit.b >= -0.001) {
            Vector hitPosition = hit.a.clone();
            // The hit is (unless it's a tangent) actually not on the orthogonal itself. Use Pythagoras to determine real position.
            float moveBackBy = (float) Math.sqrt(Math.pow(hit.c, 2) + Math.pow(getRadius(),2));
            hitPosition.add(ray.getDirection().mul(-moveBackBy));
            return new Physics.RaycastHit(hitPosition, hit.b - moveBackBy, Vector.difference(getCenter(), hitPosition), this);
        }
        return new Physics.RaycastHit(null, Double.MAX_VALUE, null);
    }

    @Override
    public Rect getBounds() {
        Vector min = getTransform().getPosition().add(offset.clone()).sub(radius, radius);
        return new Rect(min.getX(), min.getY(), radius * 2, radius * 2);
    }

    @Override
    protected void visualize() {
        VoltImg volty = Scene.getMainCamera().getVolty();
        volty.circle(getTransform().getPosition().add(offset.clone()), radius, 0.5f, new float[]{0.2f, 1, 0.2f, 0.6f});
        getBounds().visualize();
    }

    public Vector getCenter() {
        return getTransform().getPosition().add(offset.clone());
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setOffset(Vector offset) {
        this.offset = offset.clone();
    }

    public Vector getOffset() {
        return offset.clone();
    }
}
