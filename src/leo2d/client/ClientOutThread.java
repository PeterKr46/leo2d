package leo2d.client;

import game.packet.EntityPositionPacket;
import leo2d.Transform;
import leo2d.behaviour.ThreadedBehaviour;

/**
 * Created by Peter on 10.11.2015.
 */
public class ClientOutThread extends ThreadedBehaviour {
    public Client client;
    public ClientOutThread(Transform transform) {
        super(transform);
    }

    @Override
    public void threadedUpdate() {
        if(client != null && client.isConnected()) {
            client.send(new EntityPositionPacket(0, new float[] {(float) transform.position.x, (float) transform.position.y},false).build());
        }
    }
}
