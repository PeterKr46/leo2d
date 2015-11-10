package leo2d.client;

import game.packet.EntityPositionPacket;
import leo2d.Transform;
import leo2d.component.ThreadedComponent;
import net.packet.UPacket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Peter on 10.11.2015.
 */
public class ClientOutThread extends ThreadedComponent {

    public ConcurrentLinkedQueue<UPacket> outQueue = new ConcurrentLinkedQueue<>();

    public Client client;
    public ClientOutThread(Transform transform) {
        super(transform);
    }

    @Override
    public void threadedUpdate() {
        if(client != null && client.isConnected()) {
            UPacket send = outQueue.peek();
            if(send != null) {
                outQueue.remove(send);
                client.send(send.build());
            }
        }
    }
}
