package leo2d.physics.collider;

import leo2d.core.Transform;
import leo2d.math.Segment;
import leo2d.math.Vector;

public class BoxCollider extends EdgeCollider {
	public BoxCollider(Transform transform) {
		super(transform);
		Vector r = new Vector(1,0);
		Vector u = new Vector(0,1);
		Vector l = new Vector(-1,0);
		Vector d = new Vector(0,-1);
		edges = new Segment[] {
			new Segment(new Vector(-0.5, -0.5), u),
			new Segment(new Vector(-0.5, 0.5), r),
			new Segment(new Vector(0.5, 0.5), d),
			new Segment(new Vector(0.5, -0.5), l)
		};
	}

	public void setSize(double width, double height) {
		Vector r = new Vector(width,0);
		Vector u = new Vector(0,height);
		Vector l = new Vector(-width,0);
		Vector d = new Vector(0,-height);
		edges = new Segment[] {
				new Segment(new Vector(-width/2, -height/2), u),
				new Segment(new Vector(-width/2, height/2), r),
				new Segment(new Vector(width/2, height/2), d),
				new Segment(new Vector(width/2, -height/2), l)
		};
	}
}
