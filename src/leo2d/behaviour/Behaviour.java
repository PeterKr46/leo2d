package leo2d.behaviour;

import leo2d.Transform;

public abstract class Behaviour {
	public abstract void update();
	
	protected Transform transform;

	public Behaviour(Transform transform) {
		this.transform = transform;
	}
}
