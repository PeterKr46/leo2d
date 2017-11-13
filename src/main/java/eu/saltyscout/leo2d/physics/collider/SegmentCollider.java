package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.GameObject;
import eu.saltyscout.leo2d.Scene;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.*;

/**
 * Created by Peter on 07.11.2017.
 */
public class SegmentCollider extends Collider<Segment> {

    public SegmentCollider(GameObject gameObject) {
        super(gameObject);
        setShape(new Segment(new Vector2(-0.5, 0), new Vector2(0.5, 0)));
    }

    @Override
    protected void visualize() {
        Transform transform = getGameObject().getTransform();
        Vector2[] vertices = new Vector2[getShape().getVertices().length];
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = transform.getTransformed(getShape().getVertices()[i]);
        }
        double[] color = new double[]{1,0,0};
        for(int i = 0; i < vertices.length; i++) {
            if(i < vertices.length-1) {
                Scene.getMainCamera().getVolty().line(vertices[i], vertices[i+1], color);
            } else {
                Scene.getMainCamera().getVolty().line(vertices[i], vertices[0], color);
            }
        }
    }


    @Override
    public RaycastResult cast(Ray ray) {
        throw new UnsupportedOperationException("Lazy fuck");
    }

    @Override
    public void onDestroy() {

    }

    public void set(Vector2 vector1, Vector2 vector2) {
        setShape(new Segment(vector1, vector2));
    }
}
