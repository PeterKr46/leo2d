package leo2d.math;

import leo2d.core.Camera;
import leo2d.gl.VoltImg;
import leo2d.util.data.Triplet;


/**
 * Created by Peter on 05.08.2015.
 */
public class Ray {
	public Vector origin;
	protected Vector direction;
	public double[] debugColor = new double[] {1, 0.3, 0.3};

	public Ray(Vector origin, Vector direction) {
		this.origin = origin.clone();
		this.direction = direction.clone().normalize();
		if(this.direction.sqrMagnitude() == 0) {
			this.direction = new Vector(0,1);
		}
	}
	
	public Vector getDirection() {
		return direction.clone();
	}
	
	public void setDirection(Vector direction) {
		this.direction = direction.clone().normalize();
		if(this.direction.sqrMagnitude() == 0) {
			this.direction = new Vector(0,1);
		}
	}

	public Vector eval(double scalar) {
		return origin.clone().add(direction.clone().multiply(scalar, scalar));
	}

	public double xOf(double y) {
		double s = (y - origin.y) / direction.y;
		return origin.x + s * direction.x;
	}

	public double yOf(double x) {
		double s = (x - origin.x) / direction.x;
		return origin.y + s * direction.y;
	}

	public double getRotation() {
		return direction.getAngle();
	}

	/**
	 * Calculates the intersection of this Ray and another.
	 * @param other The second Ray.
	 * @return A Triplet containing the intersection, the own scalar value of it and the scalar value of the other ray.
	 */
	public Triplet<Vector, Double, Double> intersect(Ray other) {
		if(Vector.isParallel(other.direction, direction)) {
			return new Triplet(null, 0.0, 0.0);
		}
		if(direction.x == 0 && other.direction.y != 0) {
			Vector hit = new Vector(origin.x, other.yOf(origin.x));
			double T1 = origin.y-hit.y;
			double T2 = hit.clone().subtract(other.origin).magnitude();
			return new Triplet<Vector, Double, Double>(hit, T1, T2);
		}
		double r_dx = direction.x, r_dy = direction.y, r_px = origin.x, r_py = origin.y;
		double s_dx = other.direction.x, s_dy = other.direction.y, s_px = other.origin.x, s_py = other.origin.y;
		// Solve for T2!
		double T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy - s_dy*r_dx);
		// Plug the value of T2 to get T1
		double T1 = (s_px+s_dx*T2-r_px)/r_dx;
		// Debug.log(T1 + " " + T2);
		return new Triplet<Vector, Double, Double>(eval(T1), T1, T2);
	}

	public void visualize() {
		double[] soft = new double[] {debugColor[0], debugColor[1], debugColor[2], 0.2};
		Vector delta = direction.clone();
		Vector endmarker = direction.clone().rotate(90).multiply(Camera.main().getVerticalSize()/20);

		VoltImg volty = Camera.main().getVolty();
		Vector origin = this.origin.toFixed();

		volty.line(origin, origin.add(delta), debugColor);
		volty.line(origin.subtract(endmarker), origin.add(endmarker), soft);
		//volty.line(origin.add(delta).subtract(endmarker), origin.add(delta).add(endmarker));
		/*volty.line(origin.add(delta).subtract(endmarker.clone().rotate(-60)), origin.add(delta), debugColor);
		volty.line(origin.add(delta).add(endmarker.clone().rotate(60)), origin.add(delta), debugColor);*/
	}
}
