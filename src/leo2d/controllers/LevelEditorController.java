package leo2d.controllers;

import leo2d.core.Transform;
import leo2d.input.Input;

import java.util.ArrayList;

/**
 * Created by Peter on 17.11.2015.
 */
public class LevelEditorController extends EditorController {

    public static ArrayList<Transform> store = new ArrayList<>();
    public static double[] permColor = new double[] {0.7, 0, 0.7};

    @Override
    public void update() {
        if(Input.getKeyDown('e') && PositioningController.moving != null) {
            if(store.contains(PositioningController.moving)) {
                PositioningController.moving.color = new double[]{1,1,1};
                store.remove(PositioningController.moving);
            } else {
                PositioningController.moving.color = permColor;
                store.add(PositioningController.moving);
            }
        }
    }
}
