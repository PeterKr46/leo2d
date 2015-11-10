package leo2d.physics.collider;

import leo2d.Transform;
import leo2d.core.Camera;
import leo2d.gl.VoltImg;
import leo2d.math.Ray;
import leo2d.math.Rect;
import leo2d.math.Vector;
import leo2d.physics.Physics;
import leo2d.util.data.Triplet;

/**
 * Created by Peter on 11.10.2015.
 */
public class CircleCollider extends Collider {
    private Vector offset = new Vector(0,0);
    public double radius = 0.5;
    public CircleCollider(Transform transform) {
        super(transform);
    }

    @Override
    public Physics.RaycastHit cast(Ray ray) {
        Ray orth = new Ray(transform.getPosition().add(offset.clone()), ray.getDirection().getOrth());
        Triplet<Vector, Double, Double> hit = ray.intersect(orth);
        if(hit.c >= -radius && hit.c <= radius && hit.b >= -0.001) {
            return new Physics.RaycastHit(hit.a, hit.b, this);
        }
        return new Physics.RaycastHit(null, Double.MAX_VALUE);
    }

    @Override
    public Rect getBounds() {
        Vector min = transform.getPosition().add(offset.clone()).subtract(radius, radius);
        return new Rect(min.x, min.y, radius * 2, radius * 2);
    }

    @Override
    public void update() {
        if(Camera.main().debug()) {
            VoltImg volty = Camera.main().getVolty();
            volty.circle(transform.getPosition().add(offset.clone()), radius, 0.5, new double[]{0.2, 1, 0.2, 0.6});
            getBounds().visualize();
        }
    }

    public Vector getCenter() {
        return transform.getPosition().add(offset.clone());
    }
}
