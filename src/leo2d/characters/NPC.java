package leo2d.characters;

import leo2d.Transform;
import leo2d.animation.Animation;
import leo2d.animation.Animator;
import leo2d.component.Component;

/**
 * Created by Peter on 10.11.2015.
 */
public class NPC extends Component {

    public Animator animator;
    public Animation horizontal, up, down, idle;

    public NPC(Transform transform) {
        super(transform);
        animator = (Animator) transform.getComponent(Animator.class);
    }

    @Override
    public void update() {

    }
}
