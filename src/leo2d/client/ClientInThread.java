package leo2d.client;

import leo2d.component.ThreadedComponent;
import leo2d.core.Transform;
import net.packet.Registry;
import net.packet.UPacket;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Peter on 10.11.2015.
 */
public class ClientInThread extends ThreadedComponent {
    public Client client;
    public ClientInThread(Transform transform) {
        super(transform);
    }

    @Override
    public void threadedUpdate() {
        if(client != null && client.isConnected()) {
            byte[] raw = client.receive();
            int id = ByteBuffer.wrap(raw, 0, 4).
                    order(ByteOrder.LITTLE_ENDIAN).
                    getInt();
            byte[] data = new byte[raw.length - 4];
            System.arraycopy(raw, 4, data, 0, data.length);
            try {
                UPacket packet = (UPacket) Registry.packets[id].getConstructor().newInstance();
                packet.clientHandle(data);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
