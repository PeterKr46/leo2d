package eu.saltyscout.leo2d;

import eu.saltyscout.leo2d.component.Component;
import org.dyn4j.dynamics.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * The Scene Singleton is a manager class for GameObject and such.
 */
public class Scene {
    private static Scene _instance;
    private final CopyOnWriteArrayList<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    /**
     * This is an UnmodifiableCollection for public exposure of the gameObjects.
     */
    private final List<GameObject> unmodifiableGameObjects = Collections.unmodifiableList(gameObjects);
    private Camera mainCamera = null;
    private World dyn4j;

    private Scene() {
        dyn4j = new World();
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

    public static List<GameObject> getTransforms() {
        return getInstance()._getTransforms();
    }

    public static GameObject createTransform(String name) {
        return getInstance()._createTransform(name);
    }

    public static GameObject findOne(String name) {
        return getInstance()._findOne(name);
    }

    public static <T extends Component> T findOne(Class<T> componentType) {
        return getInstance()._findOne(componentType);
    }

    public static List<GameObject> findAll(String name) {
        return getInstance()._findAll(name);
    }

    public static <T extends Component> List<T> findAll(Class<T> componentType) {
        return getInstance()._findAll(componentType);
    }

    public static void clear() {
        getInstance()._clear();
    }

    public static void destroyTransform(GameObject gameObject) {
        getInstance()._destroyTransform(gameObject);
    }

    protected Camera _getMainCamera() {
        return mainCamera;
    }

    protected void _setMainCamera(Camera camera) {
        mainCamera = camera;
    }

    protected List<GameObject> _getTransforms() {
        return unmodifiableGameObjects;
    }

    /**
     * Creates and registers a new GameObject instance.
     *
     * @param name the name for the new GameObject.
     * @return the new GameObject instance.
     */
    protected GameObject _createTransform(String name) {
        GameObject t = new GameObject(name);
        gameObjects.add(t);
        return t;
    }

    protected GameObject _findOne(String name) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.name.equalsIgnoreCase(name)) {
                return gameObject;
            }
        }
        return null;
    }

    private <T extends Component> T _findOne(Class<T> componentType) {
        return (T) _getTransforms().stream().flatMap(t -> t.getComponents().stream()).filter(component -> component.getClass() == componentType).findFirst().get();
    }

    private List<GameObject> _findAll(String name) {
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
        gameObjects.forEach(this::_destroyTransform);
        dyn4j.removeAllBodies();
    }

    protected void _destroyTransform(GameObject gameObject) {
        gameObject.onDestroy();
        for (GameObject t : gameObjects) {
            if (t.parent == gameObject) {
                destroyTransform(t);
            }
        }
        gameObjects.remove(gameObject);
    }

    protected World _getDyn4J() {
        return dyn4j;
    }

    public static World getDyn4J() {
        return getInstance()._getDyn4J();
    }
}
