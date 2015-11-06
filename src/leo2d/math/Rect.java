package leo2d.math;

import leo2d.core.Camera;
import leo2d.gl.VoltImg;

/**
 * Created by peter on 7/18/15.
 */
public class Rect {
	private Vector min, max;
	public Rect(double xMin, double yMin, double width, double height) {
		min = new Vector(xMin, yMin);
		max = new Vector(xMin + width, yMin + height);
	}
	
	public double getMinX() {
		return min.x;
	}
	
	public double getMinY() {
		return min.y;
	}
	
	public double getMaxX() {
		return max.x;
	}
	
	public double getMaxY() {
		return max.y;
	}

	public Vector getMin() {
		return min.clone();
	}

	public Vector getMax() {
		return max.clone();
	}

	public void setWidth(double width) {
		max.setX(min.x + width);
	}

	public void setHeight(double height) {
		max.setY(min.y + height);
	}

	public void setMinX(double x) {
		min.x = x;
	}

	public void setMinY(double y) {
		min.y = y;
	}

	public void setMaxX(double x) {
		max.x = x;
	}

	public void setMaxY(double y) {
		max.y = y;
	}


	@Override
	public Rect clone() {
		return new Rect(min.x, min.y, getWidth(), getHeight());
	}

	public double getWidth() {
		return max.x - min.x;
	}

	public double getHeight() {
		return max.y - min.y;
	}

	public void setMin(Vector min) {
		this.min = min.clone();
	}

	public void setMax(Vector max) {
		this.max = max.clone();
	}

	public void visualize() {
		double[] color = new double[] {0, 0.3, 0.9, 0.3};
		VoltImg volty = Camera.main().getVolty();
		volty.line(min, new Vector(max.x, min.y), color);
		volty.line(min, new Vector(min.x, max.y), color);
		volty.line(max, new Vector(max.x, min.y), color);
		volty.line(max, new Vector(min.x, max.y), color);
	}
}
