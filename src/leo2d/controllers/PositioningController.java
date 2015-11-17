package leo2d.controllers;

import leo2d.characters.Player;
import leo2d.core.Camera;
import leo2d.core.Transform;
import leo2d.input.Input;
import leo2d.math.Rect;
import leo2d.math.Vector;
import leo2d.sprite.font.Font;

/**
 * Created by Peter on 16.11.2015.
 */
public class PositioningController extends EditorController {

    public static Transform moving;
    public static double range = 0.025;
    Font font = Font.load("assets/Font.png");

    public PositioningController() {
        super();
    }

    @Override
    public void update() {

        font.write(Camera.main().getMin(), Player.getInstance().getTarget().toString(), 0.5, 0);


        if(!Camera.main().debug())
            return;
        Rect cameraBounds = Camera.main().getAABB();
        Vector mousePos = Camera.main().localizePixelPos(Input.getMousePosition());
        if(Input.getMouseButtonDown(1)) {
            for(Transform t : Transform.getAllTransforms()) {
                if(cameraBounds.contains(t.position)) {
                    Vector camPos = Camera.main().localize(t.position);
                    if(Vector.difference(mousePos,camPos).sqrMagnitude() <= Math.pow(range,2)) {
                        moving = t;
                        break;
                    }
                }
            }
        } else if(Input.getMouseButtonUp(1)) {
            moving = null;
        }
        if(moving != null) {
            moving.position = Camera.main().toWorldPos(mousePos);
            if(Input.getKey('q')) {
                moving.position.x = Math.round(moving.position.x);
                moving.position.y = Math.round(moving.position.y);
            }
        } else {
            Camera c = Camera.main();
            if(c.debug() && Input.getMouseButton(1)) {
                Vector d = Input.getMouseDelta();
                c.setPosition(c.getPosition().subtract(d));
            }
        }
    }
}
