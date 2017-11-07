package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.leo2d.math.Rect;
import eu.saltyscout.leo2d.physics.Physics;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider implements Component {
    private final Transform transform;
    private boolean enabled = true;

    public Collider(Transform transform) {
        this.transform = transform;
    }

    @Override
    public final void update() {
        if(Leo2D.isDebugEnabled()) {
            visualize();
        }
    }

    protected abstract void visualize();

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

    public abstract Physics.RaycastHit cast(Ray ray);

    public abstract Rect getBounds();

    @Override
    public Transform getTransform() {
        return transform;
    }
}
