package eu.saltyscout.leo2d.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 16.11.2015.
 */
public abstract class EditorController {

    private static List<EditorController> controllers = new ArrayList<>();

    public EditorController() {
        controllers.add(this);
    }

    public static EditorController[] getControllers() {
        return controllers.toArray(new EditorController[controllers.size()]);
    }

    public abstract void update();

}
