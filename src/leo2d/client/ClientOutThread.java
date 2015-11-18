package leo2d.client;

import leo2d.component.ThreadedComponent;
import leo2d.core.Transform;
import net.packet.UPacket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Peter on 10.11.2015.
 */
public class ClientOutThread extends ThreadedComponent {

    public static ConcurrentLinkedQueue<UPacket> outQueue = new ConcurrentLinkedQueue<>();

    public Client client;
    public ClientOutThread(Transform transform) {
        super(transform);
    }

    @Override
    public void threadedUpdate() {
        if(client != null && client.isConnected()) {
            int num = 10;
            while(num >= 0 && outQueue.size() > 0) {
                UPacket send = outQueue.peek();
                if (send != null) {
                    outQueue.remove(send);
                    client.send(send.build());
                }
                num--;
            }
        }
    }
}
