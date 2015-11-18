package leo2d.controllers;

import game.packet.TerrainEditorPacket;
import leo2d.client.ClientOutThread;
import leo2d.core.Camera;
import leo2d.core.Debug;
import leo2d.core.Transform;
import leo2d.input.Input;
import leo2d.math.Rect;
import leo2d.math.Vector;

import java.awt.event.KeyEvent;

/**
 * Created by Peter on 16.11.2015.
 */
public class PositioningController extends EditorController {

    public static Transform moving;
    public static double range = 0.025;

    public PositioningController() {
        super();
    }

    @Override
    public void update() {



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
            if(moving != null && moving.tag.equals("Terrain")) {
                Debug.log("Moved " + moving.name);
                String[] raw = moving.name.split("T");
                int id = Integer.parseInt(raw[0]);
                int tex = Integer.parseInt(raw[1]);
                ClientOutThread.outQueue.add(new TerrainEditorPacket(tex, id, (float) moving.position.x, (float) moving.position.y));
            }
            moving = null;
        }
        if(moving != null) {
            moving.position = Camera.main().toWorldPos(mousePos);
            if(Input.getKey(KeyEvent.VK_Q)) {
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
