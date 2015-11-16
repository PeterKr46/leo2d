package leo2d.editor;

import leo2d.core.Camera;
import leo2d.core.Debug;
import leo2d.core.Transform;
import leo2d.input.Input;
import leo2d.math.Rect;
import leo2d.math.Vector;

/**
 * Created by Peter on 16.11.2015.
 */
public class PositioningController extends EditorController {

    private Transform moving;
    public double range = 0.025;

    @Override
    public void update() {
        if(!Camera.main().debug())
            return;
        Debug.log(Input.getRawState('w'));
        Rect cameraBounds = Camera.main().getAABB();
        Vector mousePos = Camera.main().localizePixelPos(Input.getMousePosition());
        if(Input.getMouseButtonDown(1)) {
            for(Transform t : Transform.getAllTransforms()) {
                if(cameraBounds.contains(t.position)) {
                    Vector camPos = Camera.main().localize(t.position);
                    if(Vector.difference(mousePos,camPos).sqrMagnitude() <= Math.pow(range,2)) {
                        Debug.log(t.name + " clicked.");
                    }
                }
            }
        }
    }
}
