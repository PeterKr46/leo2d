/*package eu.saltyscout.leo2d.physics;

import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Scene;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.math.Ray;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Peter on 15.10.2015.

public class CharacterController implements Component {
    private final GameObject gameObject;
    public float radius = 0.25f;
    public float height = 1f;
    public Vector2 gravity = new Vector2(0, -10);
    public Vector2 velocity = new Vector2(0, -1);
    private boolean enabled = true;

    public CharacterController(GameObject gameObject) {
        this.gameObject = gameObject;
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
        velocity.add(gravity.clone().multiply(Leo2D.deltaTime()));
        Vector2 move = velocity.clone().multiply(Leo2D.deltaTime());
        Vector2 horizontal = new Vector2(move.x, 0).normalize();
        Vector2 vertical = new Vector2(0, move.y).normalize();

        Physics.RaycastHit hitVertical = null;
        for (float i = -radius; i <= radius; i += radius) {
            Ray ray = new Ray(gameObject.getPosition().add(0, i), vertical);
            Physics.RaycastHit hit = Physics.cast(ray);
            if (hitVertical == null || hit.hitDistance < hitVertical.hitDistance) {
                hitVertical = hit;
            }
        }
        Physics.RaycastHit hitHorizontal = null;
        for (float i = -height / 2; i <= height / 2; i += height / 2) {
            Ray ray = new Ray(gameObject.getPosition().add(i, 0), horizontal);
            Physics.RaycastHit hit = Physics.cast(ray);
            if (hitHorizontal == null || hit.hitDistance < hitHorizontal.hitDistance) {
                hitHorizontal = hit;
            }
        }

        if (hitVertical != null) {
            if (move.y < 0 && hitVertical.hitDistance - (height / 2) < Math.abs(move.y)) {
                Scene.getMainCamera().getVolty().cross(hitVertical.point);
                move.setY((float) -(hitVertical.hitDistance - (height / 2)));
            } else if (move.y > 0 && hitVertical.hitDistance - (height / 2) < move.y) {
                move.setY((float) (hitVertical.hitDistance - (height / 2)));
            }
        }
        if (hitHorizontal != null) {
            if (move.x < 0 && hitHorizontal.hitDistance - (radius / 2) < Math.abs(move.x)) {
                Scene.getMainCamera().getVolty().cross(hitHorizontal.point);
                move.setX((float) -(hitHorizontal.hitDistance - (radius / 2)));
            } else if (move.x > 0 && hitHorizontal.hitDistance - (height / 2) < move.x) {
                move.setX((float) (hitHorizontal.hitDistance - (radius / 2)));
            }
        }
        if (move.sqrMagnitude() == 0) {
            velocity = move;
        }
        gameObject.setPosition(gameObject.getPosition().add(move));
    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }

    @Override
    public void onDestroy() {

    }
}
*/