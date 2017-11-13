package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Scene;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Ray;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Peter on 07.11.2017.
 */
public class CircleCollider extends Collider<Circle> {

    public CircleCollider(GameObject gameObject) {
        super(gameObject);
        setShape(new Circle(0.5f));
        setOffset(0.5, 0.5);
    }

    @Override
    protected void visualize() {
        Transform transform = getGameObject().getTransform();
        Vector2 center = transform.getTransformed(getShape().getCenter());
        Scene.getMainCamera().getVolty().circle(center, getRadius(), getRadius() * 90, new double[]{1,0,0});
    }

    public double getRadius() {
        return getShape().getRadius();
    }

    public void setRadius(double radius) {
        setShape(new Circle(radius));
    }

    @Override
    public RaycastResult cast(Ray ray) {
        throw new UnsupportedOperationException("Lazy fuck");
    }

    @Override
    public void onDestroy() {

    }
}
