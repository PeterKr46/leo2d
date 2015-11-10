package leo2d.animation;


import leo2d.sprite.Sprite;
import leo2d.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 10.11.2015.
 */
public class Animation {

    public double timePerFrame = 0.1f;
    private Frame[] frames;

    public Animation(int frames) {
        this.frames = new Frame[frames];
        for(int i = 0; i < frames; i++) {
            this.frames[i] = new Frame();
        }
    }

    public Frame getFrame(int i) {
        return (i < frames.length && i >= 0) ? frames[i] : null;
    }

    public int length() {
        return frames.length;
    }

    public class Frame {
        public Sprite sprite = null;
        public List<Callback> calls = new ArrayList<Callback>();
    }
}
