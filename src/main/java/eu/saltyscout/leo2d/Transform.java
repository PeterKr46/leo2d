package eu.saltyscout.leo2d;

import com.jogamp.opengl.GLAutoDrawable;
import eu.saltyscout.leo2d.component.Component;
import eu.saltyscout.leo2d.gl.VoltImg;
import eu.saltyscout.leo2d.render.Renderer;
import eu.saltyscout.leo2d.render.sprite.SpriteRenderer;
import eu.saltyscout.math.Vector;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by peter on 7/19/15 - sure has been long.<br/>
 * Transforms are the backbone of the Scene's Object-/Component Management System.<br/>
 * They possess a specific position, rotation and scale.<br/>
 * They also possess a non-unique tag and name.<br/>
 * Transforms may be assigned a specific {@link #parent} Transform. It will move and turns with its parent.<br/>
 * Every Transform can have one of each Component type attached to it. See {@link Component} for details on Component behaviour.
 */
public final class Transform {

    private final Vector localPosition = Vector.of(0, 0);
    private final Vector localScale = Vector.of(1, 1);
    // These can be given out freely as they will not modify the value.
    private final Vector exposableLocalPosition = Vector.copyOnWrite(localPosition);
    private final Vector exposableLocalScale = Vector.copyOnWrite(localScale);
    // Public for easy access.
    public String name;
    public String tag = "";
    public boolean editorLocked = false;
    public Transform parent;
    public float[] color = new float[]{1, 1, 1};
    private float localRotation = 0;
    private HashMap<Class<? extends Component>, Component> components = new HashMap<>();
    private Collection<Component> unmodifiableComponents = Collections.unmodifiableCollection(components.values());

    Transform(String name) {
        this.name = name;
    }

    public Vector getPosition() {
        if (parent == null) {
            return localPosition.clone();
        } else {
            return parent.getPosition().add(localPosition.clone().rotate(parent.getRotation()).mulComponents(parent.getScale()));
        }
    }

    public void setPosition(Vector pos) {
        if (parent == null) {
            setLocalPosition(pos);
        } else {
            pos = pos.clone().sub(parent.getPosition());
            setLocalPosition(pos);
        }
    }

    public Vector getLocalPosition() {
        return exposableLocalPosition;
    }

    public void setLocalPosition(Vector pos) {
        localPosition.setX(pos.getX());
        localPosition.setY(pos.getY());
    }

    public Vector getScale() {
        if (parent == null) {
            return localScale.clone();
        } else {
            return parent.getScale().mulComponents(localScale);
        }
    }

    public void setScale(Vector scale) {
        if (parent == null) {
            setLocalScale(scale);
        } else {
            scale.divComponents(parent.getScale());
            setLocalScale(scale);
        }
    }

    public Vector getLocalScale() {
        return exposableLocalScale;
    }

    public void setLocalScale(Vector scale) {
        localScale.setX(scale.getX());
        localScale.setY(scale.getY());
    }

    public float getLocalRotation() {
        return localRotation;
    }

    public void setLocalRotation(float rotation) {
        localRotation = rotation;
    }

    public float getRotation() {
        if (parent == null) {
            return localRotation;
        }
        return parent.getRotation() + localRotation;
    }

    public void setRotation(float rotation) {
        setLocalRotation(rotation - parent.getRotation());
    }

    public <T extends Component> T addComponent(Class<T> cls) {
        try {
            T b = cls.getConstructor(Transform.class).newInstance(this);
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

    /**
     * Calls a method on all Components attached to this Transform.
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
     * Calls a method on all Components attached to this Transform.
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

            Vector center = Vector.copyOnWrite(getPosition());
            float len = cam.getVerticalSize() / 5;
            float rotation = getRotation();
            Vector right = Vector.copyOnWrite(Vector.of(len, 0).rotate(rotation));
            Vector up = Vector.copyOnWrite(Vector.of(0, len).rotate(rotation));
            Vector rightEnd = Vector.copyOnWrite(center.add(right));
            Vector topEnd = Vector.copyOnWrite(center.add(up));

            Vector tipA = Vector.of(0, -len / 3).rotate(rotation).rotate(25).add(topEnd);
            Vector tipB = Vector.of(0, -len / 3).rotate(rotation).rotate(-25).add(topEnd);
            Vector tipC = Vector.of(-len / 3, 0).rotate(rotation).rotate(25).add(rightEnd);
            Vector tipD = Vector.of(-len / 3, 0).rotate(rotation).rotate(-25).add(rightEnd);


            volty.line(center, rightEnd, green);
            volty.line(center, topEnd, red);

            volty.line(rightEnd, tipC, green);
            volty.line(rightEnd, tipD, green);

            volty.line(topEnd, tipA, red);
            volty.line(topEnd, tipB, red);

            Vector scale = cam.getHalfSize();
            scale.setX(1 / scale.getX());
            scale.setY(1 / scale.getY());

            volty.filledCircle(center, len / 8, 0.1f, color);
        }
    }

    public String toString() {
        return "Transform(" + name + ", " + getPosition() + ")";
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public boolean hasRenderer() {
        return getRenderer() != null;
    }

    public Collection<Component> getComponents() {
        return unmodifiableComponents;
    }
}
