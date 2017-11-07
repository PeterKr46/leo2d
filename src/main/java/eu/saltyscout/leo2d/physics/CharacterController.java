package eu.saltyscout.leo2d.physics;

import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.math.Ray;
import eu.saltyscout.math.Vector;

/**
 * Created by Peter on 15.10.2015.
 */
public class CharacterController implements Component {
    private final Transform transform;
    public float radius = 0.25f;
    public float height = 1f;
    public Vector gravity = Vector.of(0, -10);
    public Vector velocity = Vector.of(0, -1);
    private boolean enabled = true;

    public CharacterController(Transform transform) {
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

    @Override
    public void update() {
        velocity.add(gravity.clone().mul(Leo2D.deltaTime()));
        Vector move = velocity.clone().mul(Leo2D.deltaTime());
        Vector horizontal = Vector.of(move.getX(), 0).normalize();
        Vector vertical = Vector.of(0, move.getY()).normalize();

        Physics.RaycastHit hitVertical = null;
        for (float i = -radius; i <= radius; i += radius) {
            Ray ray = new Ray(transform.getPosition().add(0, i), vertical);
            Physics.RaycastHit hit = Physics.cast(ray);
            if (hitVertical == null || hit.hitDistance < hitVertical.hitDistance) {
                hitVertical = hit;
            }
        }
        Physics.RaycastHit hitHorizontal = null;
        for (float i = -height / 2; i <= height / 2; i += height / 2) {
            Ray ray = new Ray(transform.getPosition().add(i, 0), horizontal);
            Physics.RaycastHit hit = Physics.cast(ray);
            if (hitHorizontal == null || hit.hitDistance < hitHorizontal.hitDistance) {
                hitHorizontal = hit;
            }
        }

        if (hitVertical != null) {
            if (move.getY() < 0 && hitVertical.hitDistance - (height / 2) < Math.abs(move.getY())) {
                Scene.getMainCamera().getVolty().cross(hitVertical.point);
                move.setY((float) -(hitVertical.hitDistance - (height / 2)));
            } else if (move.getY() > 0 && hitVertical.hitDistance - (height / 2) < move.getY()) {
                move.setY((float) (hitVertical.hitDistance - (height / 2)));
            }
        }
        if (hitHorizontal != null) {
            if (move.getX() < 0 && hitHorizontal.hitDistance - (radius / 2) < Math.abs(move.getX())) {
                Scene.getMainCamera().getVolty().cross(hitHorizontal.point);
                move.setX((float) -(hitHorizontal.hitDistance - (radius / 2)));
            } else if (move.getX() > 0 && hitHorizontal.hitDistance - (height / 2) < move.getX()) {
                move.setX((float) (hitHorizontal.hitDistance - (radius / 2)));
            }
        }
        if (move.sqrMagnitude() == 0) {
            velocity = move;
        }
        transform.setPosition(transform.getPosition().add(move));
    }

    @Override
    public Transform getTransform() {
        return transform;
    }
}
