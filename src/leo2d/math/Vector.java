package leo2d.math;

/**
 * Created by peter on 7/18/15.
 */
public class Vector {

	public static Vector right() {
		return new Vector(1,0);
	}
	public static Vector up() {
		return new Vector(0,1);
	}
	public static Vector zero() {
		return new Vector(0,0);
	}

	public double x, y;
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}


	public Vector getOrth() {
		if(x == 0) {
			return Vector.right();
		}
		if(y == 0) {
			return Vector.up();
		}
		return new Vector(-y, x);
	}


	@Override
	public Vector clone() {
		return new Vector(x,y);
	}

	public Vector subtract(Vector other) {
		return add(-other.x, -other.y);
	}

	public Vector add(Vector other) {
		return add(other.x, other.y);
	}

	public Vector add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}


	public Vector divide(double factor) {
		x /= factor;
		y /= factor;
		return this;
	}

	public Vector multiply(double factor) {
		return multiply(factor, factor);
	}

	public Vector multiply(Vector other) {
		return multiply(other.x, other.y);
	}

	public Vector multiply(double x, double y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}


	public Vector rotate(double a) {
		a = (a / 360f) * 2 * Math.PI;
		double tmpX = this.x;
		this.x = x * Math.cos(a) - y * Math.sin(a);
		this.y = tmpX * Math.sin(a) + y * Math.cos(a);
		return this;
	}

	public String toString() {
		return "Vector(" + x + ", " + y + ")";
	}

	public FixedVector toFixed() {
		return new FixedVector(x, y);
	}

	public double getAngle() {
		if(x == 0) {
			return (y < 0 ? 180 : 0);
		} else if(y == 0) {
			return (x < 0 ? 270 : 90);
		}
		double baseAngle = Math.atan(Math.abs(y)/Math.abs(x)) * 180.0/Math.PI;
		if(y < 0) {
			if(x > 0) {
				return 90 + baseAngle;
			}
			return 180 + 90-baseAngle;
		} else {
			if(x > 0) {
				return 90-baseAngle;
			}
			return 270 + baseAngle;
		}
	}

	public static Vector difference(Vector from, Vector to) {
		return new Vector(to.x - from.x, to.y - from.y);
	}

	public static boolean isParallel(Vector a, Vector b) {
		a = a.clone().normalize();
		b = b.clone().normalize();
		return Math.abs(a.x) == Math.abs(b.x) && Math.abs(a.y) == Math.abs(b.y);
	}

	public static boolean isOpposite(Vector a, Vector b) {
		if(!isParallel(a,b)) return false;
		a = a.clone().normalize();
		b = b.clone().normalize();
		return a.x == -b.x && a.y == -b.y;
	}


	public static double dotProd(Vector a, Vector b) {
		return a.x * b.x + a.y * b.y;
	}

	public double sqrMagnitude() {
		return Math.pow(x,2) + Math.pow(y,2);
	}

	public double magnitude() {
		return Math.sqrt(sqrMagnitude());
	}

	public Vector normalize() {
		if(x == 0 && y == 0) {
			return this;
		}
		double sc = 1.0/magnitude();
		this.x *= sc;
		this.y *= sc;
		return this;
	}

	public Vector subtract(double x, double y) {
		return add(-x, -y);
	}

	public static boolean isEqual(Vector a, Vector b) {
		return a.x == b.x && a.y == b.y;
	}

	public static class FixedVector extends Vector {

		public FixedVector(double x, double y) {
			super(x, y);
		}

		@Override
		public Vector add(double x, double y) {
			return super.clone().add(x, y);
		}

		@Override
		public Vector divide(double factor) {
			return super.clone().divide(factor);
		}

		@Override
		public Vector multiply(Vector other) {
			return super.clone().multiply(other);
		}

		@Override
		public Vector multiply(double x, double y) {
			return super.clone().multiply(x, y);
		}

		@Override
		public Vector rotate(double a) {
			return super.clone().rotate(a);
		}

		@Override
		public void setX(double x) {
			super.clone().setX(x);
		}

		@Override
		public void setY(double y) {
			super.clone().setY(y);
		}

		@Override
		public Vector normalize() {
			return super.clone().normalize();
		}
	}
}
