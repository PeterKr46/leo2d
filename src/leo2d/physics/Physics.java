package leo2d.physics;

import leo2d.core.Camera;
import leo2d.math.Ray;
import leo2d.math.Segment;
import leo2d.math.Vector;
import leo2d.physics.collider.Collider;
import leo2d.util.data.Triplet;

import java.util.ArrayList;
import java.util.List;

public class Physics {

	public static RaycastHit cast(Ray ray) {
		RaycastHit hit = new RaycastHit(null, Double.MAX_VALUE);
		for(Collider coll : Collider.getColliders()) {
			RaycastHit tHit = coll.cast(ray);
			if(tHit.point != null) {
				if(tHit.hitDistance < hit.hitDistance) {
					hit = tHit;
				}
			}
		}
		return hit;
	}

	public static Collider[] castThru(Ray ray) {
		List<Collider> hit = new ArrayList<Collider>();
		for(Collider coll : Collider.getColliders()) {
			RaycastHit tHit = coll.cast(ray);
			if(tHit.point != null) {
				hit.add(coll);
			}
		}
		return hit.toArray(new Collider[hit.size()]);
	}
	
	public static RaycastHit castCamBox(Ray ray) {
		RaycastHit hit = new RaycastHit(null, Double.MAX_VALUE);
		for(Segment edgeSeg : Camera.main().getBounds()) {
			Triplet<Vector, Double, Double> tHit = ray.intersect(edgeSeg);
			if(tHit.c >= -0.0001 && tHit.c <= edgeSeg.length && tHit.b > 0 && tHit.b < hit.hitDistance) {
				hit = new Physics.RaycastHit(tHit.a, tHit.b);
			}
		}
		return hit;
	}

	public static class RaycastHit {
		public Vector point;
		public double hitDistance;
		public Collider collider;

		public RaycastHit(Vector hit, double hitDistance) {
			this.point = hit;
			this.hitDistance = hitDistance;
			this.collider = null;
		}

		public RaycastHit(Vector hit, double hitDistance, Collider collider) {
			this.point = hit;
			this.hitDistance = hitDistance;
			this.collider = collider;
		}
	}
}
