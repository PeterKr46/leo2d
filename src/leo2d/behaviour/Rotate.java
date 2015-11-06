package leo2d.behaviour;

import leo2d.Transform;
import leo2d.core.Camera;
import leo2d.math.Vector;

public class Rotate extends Behaviour {
	public Rotate(Transform transform) {
		super(transform);
	}

	double speed = 0.5;
	double lived = 0;

	public void update() {
		lived += Camera.main().deltatime();
		transform.position = new Vector(Math.cos(lived * speed) * 4, Math.sin(lived * speed) * 4);
	}
 }
