package leo2d.core;

import com.jogamp.opengl.GLAutoDrawable;
import leo2d.component.Component;
import leo2d.gl.VoltImg;
import leo2d.math.Vector;
import leo2d.sprite.SpriteRenderer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 7/19/15.
 */
public class Transform {

	private static List<Transform> transforms = new ArrayList<Transform>();

	public static List<Transform> getAllTransforms() {
		return new ArrayList<Transform>(transforms);
	}

	public static Transform createEmpty(String name) {
		return new Transform(name);
	}

	// Public for easy access.
	public String name;
	public String tag = "";
	public Vector position = new Vector(0,0);
	public Vector scale = new Vector(1,1);
	public float rotation = 0;

	public Transform parent;
	public Vector localPosition = Vector.zero();
	public float localRotation = 0;

	public double[] color = new double[] {1,1,1};

	private List<Component> components = new ArrayList<Component>();
	private SpriteRenderer renderer;

	private Transform(String name) {
		transforms.add(this);
		this.name = name;
		Debug.log("Transform(" + name + ") created.");
	}

	public Vector getPosition() {
		return position.clone();
	}

	public Component addComponent(Class<? extends Component> cls) {
		try {
			Component b = cls.getConstructor(Transform.class).newInstance(this);
			components.add(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public SpriteRenderer getRenderer() {
		return renderer;
	}

	public SpriteRenderer addRenderer() {
		if(renderer == null) {
			renderer = new SpriteRenderer(this);
		}
		return renderer;
	}

	//TODO
	public Component getComponent(Class<? extends Component> cls) {
		for(Component b : components) {
			if(b.getClass() == cls) {
				return b;
			}
		}
		return null;
	}

	/**
	 * Calls a method on all Components attached to this Transform.
	 * @param methodName Name of the method to invoke on Components
	 * @return true if at least one Component received it.
	 */

	public boolean sendMessage(String methodName) {
		boolean ret = false;
		for(Component component : components) {
			try {
				Method method = component.getClass().getMethod(methodName);
				ret = true;
				method.invoke(component);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * Calls a method on all Components attached to this Transform.
	 * @param methodName The name of the method to invoke on Components
	 * @param argTypes An Array of types the method requires as arguments.
	 * @param args The values to pass as arguments.
	 * @return true if at least one Component received it.
	 */

	public boolean sendMessage(String methodName, Class<?>[] argTypes, Object[] args) {
		boolean ret = false;
		for(Component component : components) {
			try {
				Method method = component.getClass().getMethod(methodName, argTypes);
				ret = true;
				method.invoke(component, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public void update(GLAutoDrawable drawable) {
		if(renderer != null && renderer.isEnabled()) {
			renderer.draw();
		}
	}

	public void updateComponents(GLAutoDrawable drawable) {
		if(parent != null) {
			position = parent.getPosition().add(localPosition);
			rotation = parent.rotation + localRotation;
		}
		drawArrows(drawable);
		for(Component component : components.toArray(new Component[components.size()])) {
			component.update();
		}
	}

	private void drawArrows(GLAutoDrawable drawable) {
		if(Camera.main().debug()) {
			Camera cam = Camera.main();
			VoltImg volty = cam.getVolty();

			double[] red = new double[] {1,0,0};
			double[] green = new double[] {0,1,0};
			double[] yellow = new double[]{1,1,0};

			Vector center = position.toFixed();
			double len = cam.getVerticalSize() / 5;

			Vector right = new Vector(len,0).rotate(rotation).toFixed();
			Vector up = new Vector(0,len).rotate(rotation).toFixed();
			Vector rightEnd = center.add(right).toFixed();
			Vector topEnd = center.add(up).toFixed();

			Vector tipA = new Vector(0,-len/3).rotate(rotation).rotate(25).add(topEnd);
			Vector tipB = new Vector(0,-len/3).rotate(rotation).rotate(-25).add(topEnd);
			Vector tipC = new Vector(-len/3,0).rotate(rotation).rotate(25).add(rightEnd);
			Vector tipD = new Vector(-len/3,0).rotate(rotation).rotate(-25).add(rightEnd);


			volty.line(center, rightEnd, green);
			volty.line(center, topEnd, red);

			volty.line(rightEnd, tipC, green);
			volty.line(rightEnd, tipD, green);

			volty.line(topEnd, tipA, red);
			volty.line(topEnd, tipB, red);

			Vector scale = Camera.main().getHalfsize();
			scale.x = 1 / scale.x;
			scale.y = 1 / scale.y;

			volty.filledCircle(center, len/8, 0.1f, color);
		}
	}

	public String toString() {
		return "Transform(" + name + ", " + position + ")";
	}

	public void removeComponent(Component component) {
		components.remove(component);
	}

	public boolean hasRenderer() {
		return getRenderer() != null;
	}

	public static Transform findOne(String name) {
		for(Transform transform : transforms) {
			if(transform.name.equalsIgnoreCase(name)) {
				return transform;
			}
		}
		return null;
	}
}
