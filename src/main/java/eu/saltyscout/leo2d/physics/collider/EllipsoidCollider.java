package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Scene;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.Ray;
import org.dyn4j.geometry.Transform;

/**
 * Created by Peter on 07.11.2017.
 */
public class EllipsoidCollider extends Collider<Ellipse> {

    public EllipsoidCollider(GameObject gameObject) {
        super(gameObject);
        setShape(new Ellipse(1, 0.5));
        setOffset(0.5, 0.25);
    }

    @Override
    protected void visualize() {
        Transform transform = getGameObject().getTransform().copy();
        transform.setTranslation( transform.getTransformed(getOffset()));
        double quality = Math.max(getShape().getWidth(), getShape().getHeight()) * 90;
        Scene.getMainCamera().getVolty().ellipse(transform, getShape().getHalfWidth(), getShape().getHalfHeight(), quality, new double[]{1,0,0});
    }

    public void setRadius(double width, double height) {
        setShape(new Ellipse(width, height));
    }

    @Override
    public RaycastResult cast(Ray ray) {
        throw new UnsupportedOperationException("Lazy fuck");
    }

    @Override
    public void onDestroy() {

    }
}
