package leo2d.characters;

import leo2d.animation.Animation;
import leo2d.animation.Animator;
import leo2d.component.Component;
import leo2d.core.Camera;
import leo2d.core.Transform;
import leo2d.math.Vector;

import java.util.HashMap;

/**
 * Created by Peter on 10.11.2015.
 */
public class NPC extends Component {

    private static HashMap<Integer, NPC> npcs = new HashMap<>();
    public static NPC getNPC(int entityId) {
        return npcs.get(entityId);
    }

    public Animator animator;
    public Animation horizontal, up, down, idle;
    public int direction = 0;
    private int entityId = -1;

    private float movementSpeed = 3f;

    private Vector target = Vector.zero();

    public NPC(Transform transform) {
        super(transform);
        animator = (Animator) transform.getComponent(Animator.class);
    }

    public void setEntityId(int id) {
        npcs.remove(entityId);
        this.entityId = id;
        npcs.put(id, this);
    }

    public int getEntityId() {
        return entityId;
    }

    public void setTarget(float x, float y) {
        target = new Vector(x,y);
    }

    @Override
    public void update() {
        if(!Vector.isEqual(transform.position,target)) {
            double mv = movementSpeed * Camera.main().deltatime();
            Vector diff = Vector.difference(transform.position,target);
            if(mv > diff.magnitude()) {
                transform.position = target;
            } else {
                transform.position.add(diff.normalize().multiply(mv));
            }
        }
        switch(direction) {
            case 1:
                animator.animation = up;
                transform.scale = new Vector(1,1);
                break;
            case 2:
                animator.animation = horizontal;
                transform.scale = new Vector(1,1);
                break;
            case 3:
                animator.animation = down;
                transform.scale = new Vector(1,1);
                break;
            case 4:
                animator.animation = horizontal;
                transform.scale = new Vector(-1,1);
                break;
            default:
                animator.animation = idle;
                transform.scale = new Vector(1,1);
        }
    }
}
