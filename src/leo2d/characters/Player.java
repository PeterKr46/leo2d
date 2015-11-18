package leo2d.characters;

import game.packet.EntityMovePacket;
import leo2d.animation.Animation;
import leo2d.animation.Animator;
import leo2d.client.ClientOutThread;
import leo2d.component.Component;
import leo2d.controllers.ChatController;
import leo2d.core.Camera;
import leo2d.core.Transform;
import leo2d.input.Input;
import leo2d.math.Vector;
import leo2d.util.MathUtil;

import java.awt.event.KeyEvent;

/**
 * Created by Peter on 09.11.2015.
 */
public class Player extends Component {

    private static Player instance;

    public static Player getInstance() {
        return instance;
    }

    public void teleport(Vector pos) {
        target = pos.clone();
        origin = pos.clone();
    }

    public void teleport(float x, float y) {
        target = new Vector(x,y);
        origin = target.clone();
    }

    public int getFaceDirection() {
        Vector dir = getDirection();
        return dir.y > 0 ? 1 : dir.x > 0 ? 2 : dir.y < 0 ? 3 : dir.x < 0 ? 4 : 0;
    }

    private Vector target = Vector.zero();
    private Vector origin = Vector.zero();
    private long start = 0;
    private float movementSpeed = 3f;
    public Animator animator;
    public Animation horizontal, up, down, idle;

    private Vector lastSent = target.clone();

    public Vector nextTarget = new Vector(0,0);

    public Vector getTarget() {
        return target.clone();
    }
    public Vector getDirection() {
        return getTarget().clone().subtract(origin);
    }

    public int entityId = 0;

    public Player(Transform transform) {
        super(transform);
        animator = (Animator) transform.getComponent(Animator.class);
        instance = this;
    }


    @Override
    public void update() {
        if(transform.position.x == target.x && transform.position.y == target.y) {
            animator.animation = idle;
            start = System.currentTimeMillis();
            origin.x = target.x;
            origin.y = target.y;
            target = nextTarget.clone();
            if(Vector.difference(origin,target).sqrMagnitude() > 1) {
                target = origin;
            }
        }
        double timeSince = (System.currentTimeMillis() - start) * 0.001;
        double pc = movementSpeed*timeSince;
        transform.position = new Vector(MathUtil.lerp(pc, origin.x, target.x), MathUtil.lerp(pc, origin.y, target.y));

        if(!Vector.isEqual(target, lastSent)) {
            ((ClientOutThread) transform.getComponent(ClientOutThread.class)).outQueue.add(new EntityMovePacket(entityId, new float[]{(float) target.x, (float) target.y}, getFaceDirection()));
            lastSent = target.clone();
        }


        Vector delta = Vector.zero();
        if(!ChatController.chatActive) {
            if (Input.getKey(KeyEvent.VK_W)) {
                delta = new Vector(0, 1);
            } else if (Input.getKey(KeyEvent.VK_S)) {
                delta = new Vector(0, -1);
            } else if (Input.getKey(KeyEvent.VK_D)) {
                delta = new Vector(1, 0);
            } else if (Input.getKey(KeyEvent.VK_A)) {
                delta = new Vector(-1, 0);
            }
        }
        if(delta.x != 0 || delta.y != 0) {
            if(Vector.isOpposite(delta, getDirection()) && pc > 0.3) {
                Vector tmpTarget = target;
                target = origin;
                origin = tmpTarget;
                nextTarget = target;
                start = (long) (System.currentTimeMillis() - 1000*(1 / movementSpeed - timeSince));
            } else if(pc > 0.5 || (transform.position.x == target.x && transform.position.y == target.y) ) {
                nextTarget = getTarget().add(delta);
            }
        }
        transform.getRenderer().setIndexInLayer((int) (transform.position.y * 100));
        Vector dir = getDirection();
        dir.normalize();
        transform.scale = new Vector(dir.x != 0 ? dir.x : 1, 1);
        if(animator != null) {
            if(dir.y == 0) {
                animator.animation = dir.x != 0 ? horizontal : idle;
            } else {
                animator.animation = dir.y > 0 ? up : dir.y < 0 ? down : idle;
            }
        }

        if(!Camera.main().debug()) {
            Camera.main().setPosition(transform.position);
        }
    }
}
