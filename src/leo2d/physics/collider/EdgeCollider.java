package leo2d.physics.collider;

import leo2d.core.Camera;
import leo2d.core.Transform;
import leo2d.gl.VoltImg;
import leo2d.math.Ray;
import leo2d.math.Rect;
import leo2d.math.Segment;
import leo2d.math.Vector;
import leo2d.physics.Physics;
import leo2d.util.data.Triplet;

public class EdgeCollider extends Collider {

	public Segment[] edges;

	public EdgeCollider(Transform transform) {
		super(transform);
		this.edges = new Segment[0];
	}

	public Segment[] getWorldEdges() {
		Segment[] segs = new Segment[edges.length];
		for(int i = 0; i < edges.length; i++) {
			Segment edge = edges[i];
			Vector origin = edge.origin.clone().rotate(transform.rotation);
			Vector direction = edge.getDirection().rotate(transform.rotation).multiply(edge.length);
			segs[i] = new Segment(origin.add(transform.position), direction);
		}
		return segs;
	}

	public Physics.RaycastHit cast(Ray ray) {
		Physics.RaycastHit hit = new Physics.RaycastHit(null, Double.MAX_VALUE);
		for(Segment edgeSeg : getWorldEdges()) {
			Triplet<Vector, Double, Double> tHit = ray.intersect(edgeSeg);
			if(tHit.c >= -0.001 && tHit.c <= edgeSeg.length && tHit.b > 0 && tHit.b < hit.hitDistance) {
				hit = new Physics.RaycastHit(tHit.a, tHit.b, this);
			}
		}
		return hit;
	}

	@Override
	public Rect getBounds() {
		Vector min = null, max = null;
		for(Segment edge : getWorldEdges()) {
			Vector a = edge.origin.clone();
			Vector b = a.clone().add(edge.getDirection().multiply(edge.length));
			if(min == null) {
				min = a;
			}
			if(max == null) {
				max = b;
			}
			min.x = Math.min(min.x, Math.min(a.x, b.x));
			min.y = Math.min(min.y, Math.min(a.y, b.y));

			max.x = Math.max(max.x, Math.max(a.x, b.x));
			max.y = Math.max(max.y, Math.max(a.y, b.y));
		}
		return min == null ? new Rect(transform.position.x, transform.position.y, 0, 0) : new Rect(min.x, min.y, max.x - min.x, max.y - min.y);
	}

	public void update() {
		if(Camera.main().debug()) {
			VoltImg volty = Camera.main().getVolty();
			for (Segment edge : getWorldEdges()) {
				volty.line(
						edge.origin,
						edge.origin.clone().add(edge.getDirection().multiply(edge.length)),
						new double[]{0.2, 1, 0.2, 0.6}
				);
			}
			getBounds().visualize();
		}
	}

}
