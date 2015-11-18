package game;

/**
 * Created by Peter on 18.11.2015.
 */
public class Level {
    private float[][][] data = new float[256][][];
    public Level() {
        for(int i = 0; i < 256; i++) {
            data[i] = new float[256][];
        }
    }

    public void sync(int tex, int id, float[] position) {
        data[tex][id] = position.clone();
    }

    public float[] getPosition(int tex, int id) {
        return data[tex][id].clone();
    }
}
