package eu.saltyscout.leo2d.physics.collider;

import eu.saltyscout.leo2d.Transform;
import eu.saltyscout.leo2d.math.Segment;
import eu.saltyscout.math.Vector;

public class BoxCollider extends EdgeCollider {
    public BoxCollider(Transform transform) {
        super(transform);
        Vector r = Vector.of(1, 0);
        Vector u = Vector.of(0, 1);
        Vector l = Vector.of(-1, 0);
        Vector d = Vector.of(0, -1);
        edges = new Edge[]{
                new Edge(Vector.of(-0.5f, -0.5), u, 1),
                new Edge(Vector.of(-0.5, 0.5), r, 1),
                new Edge(Vector.of(0.5, 0.5), d, 1),
                new Edge(Vector.of(0.5, -0.5), l, 1)
        };
    }

    public void setSize(float width, float height) {
        Vector r = Vector.of(1, 0);
        Vector u = Vector.of(0, 1);
        Vector l = Vector.of(-1, 0);
        Vector d = Vector.of(0, -1);
        edges = new Edge[]{
                new Edge(Vector.of(-width / 2, -height / 2), u, height),
                new Edge(Vector.of(-width / 2, height / 2), r, width),
                new Edge(Vector.of(width / 2, height / 2), d, height),
                new Edge(Vector.of(width / 2, -height / 2), l, width)
        };
    }
}
