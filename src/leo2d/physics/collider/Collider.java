package leo2d.physics.collider;

import leo2d.Transform;
import leo2d.component.Component;
import leo2d.math.Ray;
import leo2d.math.Rect;
import leo2d.physics.Physics;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider extends Component {
	private static List<Collider> colliders = new ArrayList<Collider>();

	public static Collider[] getColliders() {
		return colliders.toArray(new Collider[colliders.size()]);
	}

	public Collider(Transform transform) {
		super(transform);
		colliders.add(this);
	}

	public abstract Physics.RaycastHit cast(Ray ray);

	public abstract  Rect getBounds();
	
}
