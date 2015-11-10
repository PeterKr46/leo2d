package leo2d.component;

import leo2d.Transform;
import leo2d.core.Camera;
import leo2d.gl.VoltImg;
import leo2d.math.Ray;
import leo2d.math.Segment;
import leo2d.math.Vector;
import leo2d.physics.Physics;
import leo2d.physics.collider.CircleCollider;
import leo2d.physics.collider.Collider;
import leo2d.physics.collider.EdgeCollider;
import leo2d.util.data.PriorityQueue;

public class Light extends Component {
	
	public Light(Transform transform) {
		super(transform);
	}

	public void update() {
		PriorityQueue<Vector> verts = new PriorityQueue<Vector>();
		for(Segment edge : Camera.main().getBounds()) {
			Ray ray = new Ray(transform.position, edge.origin.clone().subtract(transform.position));
			Physics.RaycastHit hit = Physics.cast(ray);
			if(hit.point != null) {
				verts.enqueue(hit.point, ray.getDirection().getAngle() * 100.0);
			} else {
				verts.enqueue(edge.origin, ray.getDirection().getAngle() * 100.0);
			}
		}
		for(Collider collider : Collider.getColliders()) {
			if(collider instanceof EdgeCollider) {
				EdgeCollider coll = (EdgeCollider) collider;
				for(Ray ray : coll.getWorldEdges()) {
					Vector vertex = ray.origin;
					Ray r = new Ray(transform.position, vertex.clone().subtract(transform.position));
					Ray r2 = new Ray(transform.position, r.getDirection().rotate(0.2));
					Ray r3 = new Ray(transform.position, r.getDirection().rotate(-0.2));
					Physics.RaycastHit hit = Physics.cast(r);
					Physics.RaycastHit hit2 = Physics.cast(r2);
					Physics.RaycastHit hit3 = Physics.cast(r3);
					if(hit.point == null) {
						hit = Physics.castCamBox(r);
					}
					if(hit2.point == null) {
						hit2 = Physics.castCamBox(r2);
					}
					if(hit3.point == null) {
						hit3 = Physics.castCamBox(r3);
					}
					if(hit.point != null) verts.enqueue(hit.point, r.getDirection().getAngle() * 100.0);
					if(hit2.point != null) verts.enqueue(hit2.point, r2.getDirection().getAngle() * 100.0);
					if(hit3.point != null) verts.enqueue(hit3.point, r3.getDirection().getAngle() * 100.0);
				}
			} else if(collider instanceof CircleCollider) {
				CircleCollider coll = (CircleCollider) collider;
				Vector center = coll.getCenter();
				Vector dir = center.clone().subtract(transform.position).normalize();
				Vector orth = dir.getOrth();
				Vector a = center.clone().add(orth.multiply(coll.radius));
				Vector b = center.clone().add(orth.multiply(-1));
				for(Vector vertex : new Vector[]{a,b}) {
					Ray r = new Ray(transform.position, vertex.clone().subtract(transform.position));
					Ray r2 = new Ray(transform.position, r.getDirection().rotate(0.2));
					Ray r3 = new Ray(transform.position, r.getDirection().rotate(-0.2));
					Physics.RaycastHit hit = Physics.cast(r);
					Physics.RaycastHit hit2 = Physics.cast(r2);
					Physics.RaycastHit hit3 = Physics.cast(r3);
					if(hit.point == null) {
						hit = Physics.castCamBox(r);
					}
					if(hit2.point == null) {
						hit2 = Physics.castCamBox(r2);
					}
					if(hit3.point == null) {
						hit3 = Physics.castCamBox(r3);
					}
					if(hit.point != null) verts.enqueue(hit.point, r.getDirection().getAngle() * 100.0);
					if(hit2.point != null) verts.enqueue(hit2.point, r2.getDirection().getAngle() * 100.0);
					if(hit3.point != null) verts.enqueue(hit3.point, r3.getDirection().getAngle() * 100.0);
				}
			}
		}
		Object[] raw = verts.toArray();
		Vector[] data = new Vector[raw.length+1];
		for(int i = 0; i < raw.length; i++) {
			data[i] = (Vector)raw[i];
		}
		data[raw.length] = (Vector) raw[0];
		VoltImg volty = Camera.main().getVolty();
		volty.triangleFan(transform.position, data);
		volty.filledCircle(transform.position, Camera.main().getVerticalSize() / 50, 1, new double[]{1, 0, 0});
	}

}
