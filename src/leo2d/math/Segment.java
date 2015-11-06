package leo2d.math;

import leo2d.core.Camera;
import leo2d.gl.VoltImg;


/**
 * Created by Peter on 05.08.2015.
 */
public class Segment extends Ray {

	public double length = 1.0;

	public Segment(Vector origin, Vector direction) {
		super(origin, direction);
		this.length = direction.magnitude();
	}
	
	@Override 
	public void setDirection(Vector direction) {
		super.setDirection(direction);
		this.length = direction.magnitude();
	}

	@Override
	public void visualize() {
		double[] soft = new double[] {debugColor[0], debugColor[1], debugColor[2], 0.2};
		Vector delta = direction.clone().multiply(length);
		Vector endmarker = direction.clone().rotate(90).multiply(Camera.main().getVerticalSize()/20);

		VoltImg volty = Camera.main().getVolty();
		Vector origin = this.origin.toFixed();

		volty.line(origin, origin.add(delta), debugColor);
		volty.line(origin.subtract(endmarker), origin.add(endmarker), soft);
		//volty.line(origin.add(delta).subtract(endmarker), origin.add(delta).add(endmarker));
	}
}
