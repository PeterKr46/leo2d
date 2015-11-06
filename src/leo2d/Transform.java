package leo2d;

import com.jogamp.opengl.GLAutoDrawable;
import leo2d.behaviour.Behaviour;
import leo2d.core.Camera;
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
	public Vector position = new Vector(0,0);
	public Vector scale = new Vector(1,1);
	public float rotation = 0;

	private List<Behaviour> behaviours = new ArrayList<Behaviour>();
	private SpriteRenderer renderer;

	private Transform(String name) {
		transforms.add(this);
		this.name = name;
		Debug.log("Transform(" + name + ") created.");
	}

	public Vector getPosition() {
		return position.clone();
	}

	public Behaviour addBehaviour(Class<? extends Behaviour> cls) {
		try {
			Behaviour b = cls.getConstructor(Transform.class).newInstance(this);
			behaviours.add(b);
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
	public Behaviour getBehaviour(Class<? extends Behaviour> cls) {
		for(Behaviour b : behaviours) {
			if(b.getClass() == cls) {
				return b;
			}
		}
		return null;
	}

	/**
	 * Calls a method on all Behaviours attached to this Transform.
	 * @param methodName Name of the method to invoke on Behaviours
	 * @return true if at least one Behaviour received it.
	 */

	public boolean sendMessage(String methodName) {
		boolean ret = false;
		for(Behaviour behaviour : behaviours) {
			try {
				Method method = behaviour.getClass().getMethod(methodName);
				ret = true;
				method.invoke(behaviour);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * Calls a method on all Behaviours attached to this Transform.
	 * @param methodName The name of the method to invoke on Behaviours
	 * @param argTypes An Array of types the method requires as arguments.
	 * @param args The values to pass as arguments.
	 * @return true if at least one Behaviour received it.
	 */

	public boolean sendMessage(String methodName, Class<?>[] argTypes, Object[] args) {
		boolean ret = false;
		for(Behaviour behaviour : behaviours) {
			try {
				Method method = behaviour.getClass().getMethod(methodName, argTypes);
				ret = true;
				method.invoke(behaviour, args);
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

	public void updateBehaviours(GLAutoDrawable drawable) {
		drawArrows(drawable);
		for(Behaviour behaviour : behaviours.toArray(new Behaviour[behaviours.size()])) {
			behaviour.update();
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

			volty.filledCircle(center, len/8, 0.1f);
			/*Vector pp = cam.worldToPixelPos(position);
			Vector mp = Input.getMousePosition();
			Vector diff = Vector.difference(pp, mp);
			float rad = 0.12f * len / (cam.getVerticalSize() * 2);
			rad *= cam.getScreenHeight();
			if (diff.magnitude() < rad /*&& Debug.dragging == null) || Debug.dragging == this) {
				volty.filledCircle(center, 0.075f * len, 0.1f, red);
				if(Input.isLMouseDown() && (Debug.dragging == null || Debug.dragging == this)) {
					//Debug.dragging = this;
				}
			} else {
				volty.filledCircle(center, 0.075f * len, 0.1f, yellow);
			}
			if(Debug.dragging == this) {
				position = cam.screenToWorldPos(Input.getPercentiveMousePosition());
				if(Input.isKeyDown('q')) {
					position = new Vector(Math.round(position.x), (int)position.y);
				}
			}*/
		}
	}

	public String toString() {
		return "Transform(" + name + ", " + position + ")";
	}
}
