package leo2d.animation;

import leo2d.Transform;
import leo2d.behaviour.Behaviour;
import leo2d.core.Camera;
import leo2d.util.Callback;


/**
 * Created by Peter on 10.11.2015.
 */
public class Animator extends Behaviour {
    public Animation animation;
    public double sinceFrame = 0;
    public int currentFrame = 0;

    public Animator(Transform transform) {
        super(transform);
    }

    @Override
    public void update() {
        sinceFrame += Camera.main().deltatime();
        if(sinceFrame >= animation.timePerFrame) {
            sinceFrame -= animation.timePerFrame;
            currentFrame++;
            if(currentFrame == animation.length()) {
                currentFrame = 0;
            }
            Animation.Frame frame = animation.getFrame(currentFrame);
            if(frame.sprite != null) {
                transform.getRenderer().sprite = frame.sprite;
                for(Callback callback : frame.calls) {
                    callback.invoke();
                }
            }
        }
    }
}