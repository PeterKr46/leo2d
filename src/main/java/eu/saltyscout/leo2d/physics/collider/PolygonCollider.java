package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.GameObject;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Peter on 08.11.2017.
 */
public class PolygonCollider extends PolyCollider<Polygon> {

    public PolygonCollider(GameObject gameObject) {
        super(gameObject);
        Vector2 r = new Vector2(0,0.5);
        Vector2[] vertices = new Vector2[]{r, r.copy().rotate(2d/3*Math.PI), r.copy().rotate(4d/3*Math.PI)};
        setShape(new Polygon(vertices));
        setOffset(0.5, 0.5);
    }

    public void setVertices(Vector2... vertices) {
        setShape(new Polygon(vertices));
    }
}
