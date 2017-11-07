package eu.saltyscout.leo2d;

import eu.saltyscout.leo2d.component.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Scene Singleton is a manager class for Transform and such.
 */
public class Scene {
    private static Scene _instance;
    private final List<Transform> transforms = new ArrayList<>();
    /**
     * This is an UnmodifiableCollection for public exposure of the transforms.
     */
    private final List<Transform> unmodifiableTransforms = Collections.unmodifiableList(transforms);
    private Camera mainCamera = null;

    private Scene() {

    }

    private static Scene getInstance() {
        if (_instance == null) {
            _instance = new Scene();
        }
        return _instance;
    }

    /**
     * Gets the main {@link Camera} associated with this Scene.
     *
     * @return a Camera instance, or null.
     */
    public static Camera getMainCamera() {
        return getInstance()._getMainCamera();
    }

    /**
     * Sets the main {@link Camera} associated with this Scene.
     *
     * @param camera a Camera instance, or null.
     */
    public static void setMainCamera(Camera camera) {
        getInstance()._setMainCamera(camera);
    }

    public static List<Transform> getTransforms() {
        return getInstance()._getTransforms();
    }

    public static Transform createTransform(String name) {
        return getInstance()._createTransform(name);
    }

    public static Transform findOne(String name) {
        return getInstance()._findOne(name);
    }

    public static <T extends Component> T findOne(Class<T> componentType) {
        return getInstance()._findOne(componentType);
    }

    public static List<Transform> findAll(String name) {
        return getInstance()._findAll(name);
    }

    public static <T extends Component> List<T> findAll(Class<T> componentType) {
        return getInstance()._findAll(componentType);
    }

    public static void clear() {
        getInstance()._clear();
    }

    public static void destroyTransform(Transform transform) {
        getInstance()._destroyTransform(transform);
    }

    protected Camera _getMainCamera() {
        return mainCamera;
    }

    protected void _setMainCamera(Camera camera) {
        mainCamera = camera;
    }

    protected List<Transform> _getTransforms() {
        return unmodifiableTransforms;
    }

    /**
     * Creates and registers a new Transform instance.
     *
     * @param name the name for the new Transform.
     * @return the new Transform instance.
     */
    protected Transform _createTransform(String name) {
        Transform t = new Transform(name);
        transforms.add(t);
        return t;
    }

    protected Transform _findOne(String name) {
        for (Transform transform : transforms) {
            if (transform.name.equalsIgnoreCase(name)) {
                return transform;
            }
        }
        return null;
    }

    private <T extends Component> T _findOne(Class<T> componentType) {
        return (T) _getTransforms().stream().flatMap(t -> t.getComponents().stream()).filter(component -> component.getClass() == componentType).findFirst().get();
    }

    private List<Transform> _findAll(String name) {
        return _getTransforms().stream().filter(transform -> transform.name.equals(name)).collect(Collectors.toList());
    }

    /**
     * Finds all currently registered Components of the given class (or any subtype!)
     *
     * @param componentType the type of Component to look for.
     * @param <T>
     * @return a List of the found Components.
     */
    private <T extends Component> List<T> _findAll(Class<T> componentType) {
        List<T> result = new ArrayList<>();
        _getTransforms().stream()
                .flatMap(t -> t.getComponents().stream()).filter(component -> componentType.isAssignableFrom(component.getClass())).forEach(c -> {
            result.add((T) c);
        });
        return result;
    }

    protected void _clear() {
        //transforms.forEach(transform -> transform.des);
        //TODO
        transforms.clear();
    }

    protected void _destroyTransform(Transform transform) {
        transforms.remove(transform);
        for (Transform t : transforms) {
            if (t.parent == transform) {
                destroyTransform(t);
            }
        }
    }
}
