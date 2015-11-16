package leo2d.physics;

import leo2d.component.Component;
import leo2d.core.Camera;
import leo2d.core.Debug;
import leo2d.core.Transform;
import leo2d.math.Ray;
import leo2d.math.Vector;

/**
 * Created by Peter on 15.10.2015.
 */
public class CharacterController extends Component {
    public double radius = 0.25f;
    public double height = 1f;

    public Vector gravity = new Vector(0,-10);

    public Vector velocity = new Vector(0,-1);

    public CharacterController(Transform transform) {
        super(transform);
    }

    @Override
    public void update() {
        Debug.log(Camera.main().deltatime());
        velocity.add(gravity.clone().multiply(Camera.main().deltatime()));
        Vector move = velocity.clone().multiply(Camera.main().deltatime());
        Vector horizontal = new Vector(move.x, 0).normalize();
        Vector vertical = new Vector(0, move.y).normalize();

        Physics.RaycastHit hitVertical = null;
        for(double i = -radius; i <= radius; i+=radius) {
            Ray ray = new Ray(transform.getPosition().add(0, i), vertical);
            Physics.RaycastHit hit = Physics.cast(ray);
            if(hitVertical == null || hit.hitDistance < hitVertical.hitDistance) {
                hitVertical = hit;
            }
        }
        Physics.RaycastHit hitHorizontal = null;
        for(double i = -height/2; i <= height/2; i+=height/2) {
            Ray ray = new Ray(transform.getPosition().add(i, 0), horizontal);
            Physics.RaycastHit hit = Physics.cast(ray);
            if(hitHorizontal == null || hit.hitDistance < hitHorizontal.hitDistance) {
                hitHorizontal = hit;
            }
        }

        if(hitVertical != null) {
            if (move.y < 0 && hitVertical.hitDistance - (height / 2) < Math.abs(move.y)) {
                Camera.main().getVolty().cross(hitVertical.point);
                move.y = -(hitVertical.hitDistance - (height / 2));
            } else if(move.y > 0 && hitVertical.hitDistance - (height/2) < move.y) {
                move.y = hitVertical.hitDistance - (height / 2);
            }
        }
        if(hitHorizontal != null) {
            if (move.x < 0 && hitHorizontal.hitDistance - (radius / 2) < Math.abs(move.x)) {
                Camera.main().getVolty().cross(hitHorizontal.point);
                move.x = -(hitHorizontal.hitDistance - (radius / 2));
            } else if(move.x > 0 && hitHorizontal.hitDistance - (height/2) < move.x) {
                move.x = hitHorizontal.hitDistance - (radius / 2);
            }
        }
        if(move.sqrMagnitude() == 0) {
            velocity = move;
        }
        transform.position.add(move);
    }
}
