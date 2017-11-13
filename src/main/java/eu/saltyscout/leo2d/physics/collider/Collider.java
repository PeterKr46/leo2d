package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.component.Component;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Ray;
import org.dyn4j.geometry.Vector2;


public abstract class Collider<T extends Convex> implements Component {
    private final GameObject gameObject;
    private boolean enabled = true;
    private T shape;
    private BodyFixture bodyFixture;
    private Vector2 offset = new Vector2();

    public Collider(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public final void update() {
        if(Leo2D.isDebugEnabled()) {
            visualize();
        }
    }

    protected abstract void visualize();

    public abstract RaycastResult cast(Ray ray);

    public AABB getAABB() {
        return shape.createAABB(getGameObject().getTransform());
    }

    @Override
    public final void earlyUpdate() {

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
    public GameObject getGameObject() {
        return gameObject;
    }

    protected final T getShape() {
        return shape;
    }

    protected final void setShape(T shape) {
        // If we were already attached, remove that fixture first.
        if(bodyFixture != null) {
            gameObject.getPhysicsBody().removeFixture(bodyFixture);
        }
        this.shape = shape;
        if(shape != null) {
            bodyFixture = gameObject.getPhysicsBody().addFixture(getShape());
            shape.translate(offset.difference(shape.getCenter()));
        }
    }

    public final Vector2 getOffset() {
        return getShape().getCenter();
    }

    public final void setOffset(Vector2 offset) {
        this.offset = offset.copy();
        getShape().translate(offset.difference(getOffset()));
    }

    public final void setOffset(double x, double y) {
        this.offset.set(x,y);
        getShape().translate(offset.difference(getOffset()));
    }

    @Override
    public void onDestroy() {
        // If we were already attached, remove that fixture.
        if(bodyFixture != null) {
            gameObject.getPhysicsBody().removeFixture(bodyFixture);
        }
    }
}
