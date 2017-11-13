package eu.saltyscout.leo2d.component;

import eu.saltyscout.leo2d.GameObject;

/**
 * Created by Peter on 10.11.2015.
 */
public abstract class ThreadedComponent implements Component {
    private final GameObject gameObject;
    private boolean stop = false;
    private Thread sideThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!stop) {
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

    public ThreadedComponent(GameObject gameObject) {
        this.gameObject = gameObject;
        sideThread.start();
    }

    public void stop() {
        stop = true;
    }

    @Override
    public final void update() {
        if (stop) {
            gameObject.removeComponent(this);
        }
        // Nothing, this Component does not touch the main thread.
    }

    public abstract void threadedUpdate();
}
