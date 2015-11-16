package leo2d.component;

import leo2d.core.Transform;

public abstract class Component {
	public abstract void update();
	
	protected Transform transform;

	public Component(Transform transform) {
		this.transform = transform;
	}
}
