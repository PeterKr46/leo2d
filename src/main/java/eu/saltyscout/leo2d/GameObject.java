package eu.saltyscout.leo2d;

import com.jogamp.opengl.GLAutoDrawable;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.physics.collider.Collider;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.render.sprite.SpriteRenderer;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by peter on 7/19/15 - sure has been long.<br/>
 * GameObjects are the backbone of the Scene's Object-/Component Management System.<br/>
 * They also possess a non-unique tag and name.<br/>
 * GameObjects may be assigned a specific {@link #parent} GameObject. It will move and turns with its parent.<br/>
 * Every GameObject can have one of each Component type attached to it. See {@link Component} for details on Component behaviour.
 */
public final class GameObject {

    private final Body body;
    private Vector2 localScale = new Vector2(1,1);

    // Public for easy access.
    public String name;
    public String tag = "";
    public boolean editorLocked = false;
    public GameObject parent;
    public float[] color = new float[]{1, 1, 1};
    private HashMap<Class<? extends Component>, Component> components = new HashMap<>();
    private Collection<Component> unmodifiableComponents = Collections.unmodifiableCollection(components.values());

    GameObject(String name) {
        this.name = name;
        // By default, fuck everything.
        body = new Body();
        body.setMass(MassType.INFINITE);
        body.setAsleep(false);
        body.setAutoSleepingEnabled(true);
        Scene.getDyn4J().addBody(body);
    }

    public Transform getTransform() {
        if(false /*parent != null*/) {
            Transform t = body.getTransform();
            Transform tParent = parent.getTransform();
            t.rotate(tParent.getRotation());
            t.translate(tParent.getTranslation());
            return t;
        } else {
            return body.getTransform();
        }
    }

    public void setTransform(Transform transform) {
        if(transform != null) {
            body.setTransform(transform);
        }
    }

    public Body getPhysicsBody() {
        return body;
    }

    public <T extends Component> T addComponent(Class<T> cls) {
        try {
            T b = cls.getConstructor(GameObject.class).newInstance(this);
            components.put(cls, b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Renderer getRenderer() {
        return getComponent(Renderer.class);
    }

    public SpriteRenderer addSpriteRenderer() {
        Renderer r;
        if ((r = getRenderer()) == null) {
            return addComponent(SpriteRenderer.class);
        }
        return r instanceof SpriteRenderer ? (SpriteRenderer) r : null;
    }

    public <T extends Component> T getComponent(Class<T> cls) {
        return (T) components.get(cls);
    }

    public <T extends Component> List<T> getComponents(Class<T> componentType) {
        List<T> result = new ArrayList<>();
        getComponents().stream().filter(component -> componentType.isAssignableFrom(component.getClass())).forEach(c -> result.add((T) c));
        return result;
    }

    /**
     * Calls a method on all Components attached to this GameObject.
     *
     * @param methodName Name of the method to invoke on Components
     * @return true if at least one Component received it.
     */

    public boolean sendMessage(String methodName) {
        boolean ret = false;
        for (Component component : components.values()) {
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
     * Calls a method on all Components attached to this GameObject.
     *
     * @param methodName The name of the method to invoke on Components
     * @param argTypes   An Array of types the method requires as arguments.
     * @param args       The values to pass as arguments.
     * @return true if at least one Component received it.
     */

    public boolean sendMessage(String methodName, Class<?>[] argTypes, Object[] args) {
        boolean ret = false;
        for (Component component : components.values()) {
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

    void updateComponents(GLAutoDrawable drawable) {
        drawArrows(drawable);
        components.values().forEach(
                component -> {
                    try {
                        component.update();
                    } catch (Exception e) {
                        System.out.println("Error during " + component.getClass().getSimpleName() + ".update():");
                        e.printStackTrace();
                    }
                }
        );
    }

    void earlyUpdateComponents() {
        components.values().forEach(
                component -> {
                    try {
                        component.earlyUpdate();
                    } catch (Exception e) {
                        System.out.println("Error during " + component.getClass().getSimpleName() + ".earlyUpdate():");
                        e.printStackTrace();
                    }
                }
        );
    }

    private void drawArrows(GLAutoDrawable drawable) {
        if (Leo2D.isDebugEnabled() && !editorLocked) {
            Camera cam = Scene.getMainCamera();
            VoltImg volty = cam.getVolty();

            float[] red = new float[]{1, 0, 0};
            float[] green = new float[]{0, 1, 0};
            float[] yellow = new float[]{1, 1, 0};
            Transform transform = getTransform();

            Vector2 center = transform.getTranslation();
            float len = cam.getVerticalSize() / 5;
            double rotation = transform.getRotation();
            Vector2 right = transform.getTransformed(new Vector2(len,0));
            Vector2 up = transform.getTransformed(new Vector2(0,len));

            volty.arrow(center, right, red);
            volty.arrow(center, up, green);
            volty.filledCircle(center, len / 8, 10f, color);
        }
    }

    public String toString() {
        return "GameObject(" + name + ", " + getTransform().getTranslation() + ")";
    }

    public void removeComponent(Component component) {
        component.onDestroy();
        components.remove(component);
    }

    void onDestroy() {
        Scene.getDyn4J().removeBody(body);
    }

    public boolean hasRenderer() {
        return getRenderer() != null;
    }

    public Collection<Component> getComponents() {
        return unmodifiableComponents;
    }

    public Vector2 getLocalScale() {
        return localScale.copy();
    }

    public void setLocalScale(Vector2 localScale) {
        this.localScale = localScale;
    }
}
