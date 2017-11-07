package eu.saltyscout.leo2d.render.animation;

import eu.saltyscout.leo2d.Leo2D;
import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.render.sprite.SpriteRenderer;
import eu.saltyscout.leo2d.sprite.Animation;
import eu.saltyscout.leo2d.util.Callback;


/**
 * Created by Peter on 10.11.2015.
 */
public class Animator implements Component {
    private final Transform transform;
    public Animation animation;
    private double sinceFrame = 0;
    private int currentFrame = 0;
    private boolean enabled = true;

    public Animator(Transform transform) {
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
        // All the Animator does is update the Sprite held by the SpriteRenderer.
        // That's why we do an early update, setting the sprite for the next frame.
        if (animation == null)
            return;
        sinceFrame += Leo2D.deltaTime();
        if (sinceFrame >= animation.timePerFrame) {
            sinceFrame = 0; // -= animation.timePerFrame;
            currentFrame++;
            if (currentFrame >= animation.length()) {
                currentFrame = 0;
            }
            Animation.Frame frame = animation.getFrame(currentFrame);
            if (frame.sprite != null) {
                transform.getComponent(SpriteRenderer.class).setSprite(frame.sprite);
                for (Callback callback : frame.calls) {
                    callback.invoke();
                }
            }
        }
    }

    @Override
    public void update() {
        // Nothing.
    }

    @Override
    public Transform getTransform() {
        return transform;
    }
}
