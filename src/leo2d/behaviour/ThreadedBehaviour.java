package leo2d.behaviour;

import leo2d.Transform;

/**
 * Created by Peter on 10.11.2015.
 */
public abstract class ThreadedBehaviour extends Behaviour {
    private Thread sideThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!stop) {
                long time = System.currentTimeMillis();
                threadedUpdate();
                time = System.currentTimeMillis() - time;
                try {
                    Thread.sleep(Math.max(0, 40 - time));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private boolean stop = false;
    public ThreadedBehaviour(Transform transform) {
        super(transform);
        sideThread.start();
    }

    public void stop() {
        stop = true;
    }

    @Override
    public final void update() {
        if(stop) {
            transform.removeBehaviour(this);
        }
        // Nothing, this Behaviour does not touch the main thread.
    }

    public abstract void threadedUpdate();
}
