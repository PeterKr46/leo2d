package leo2d.physics;

import leo2d.Transform;
import leo2d.behaviour.Behaviour;
import leo2d.input.Input;
import leo2d.math.Vector;
import leo2d.util.MathUtil;

/**
 * Created by Peter on 09.11.2015.
 */
public class IsometricController extends Behaviour {
    private Vector target = Vector.zero();
    private Vector origin = Vector.zero();
    private long start = 0;
    public float movementSpeed = 5f;

    public Vector nextTarget = new Vector(1,0);

    public Vector getTarget() {
        return target.clone();
    }
    public Vector getDirection() {
        return getTarget().clone().subtract(origin);
    }

    public IsometricController(Transform transform) {
        super(transform);
    }


    @Override
    public void update() {
        double timeSince = (System.currentTimeMillis() - start) * 0.001;
        double pc = movementSpeed*timeSince;
        transform.position = new Vector(MathUtil.lerp(pc, origin.x, target.x), MathUtil.lerp(pc, origin.y, target.y));
        if(transform.position.x == target.x && transform.position.y == target.y) {
            start = System.currentTimeMillis();
            origin.x = target.x;
            origin.y = target.y;
            target = nextTarget.clone();
        }

        if(pc > 0.75 || (transform.position.x == target.x && transform.position.y == target.y)) {
            Vector delta = Vector.zero();
            if (Input.isKeyDown('w')) {
                delta = new Vector(0, 1);
            } else if (Input.isKeyDown('s')) {
                delta = new Vector(0, -1);
            } else if (Input.isKeyDown('d')) {
                delta = new Vector(1, 0);
            } else if (Input.isKeyDown('a')) {
                delta = new Vector(-1, 0);
            }
            if(delta.x != 0 || delta.y != 0) {
                nextTarget = getTarget().add(delta);
                if(Vector.isOpposite(delta, getDirection())) {
                    target = origin;
                    origin = transform.position.clone();
                    start = System.currentTimeMillis();
                }
            }
        }
        transform.getRenderer().setIndexInLayer((int) (transform.position.y * 100));
        Vector dir = getDirection();
        dir.normalize();
        transform.scale = new Vector(dir.x != 0 ? dir.x : 1, 1);
    }
}
