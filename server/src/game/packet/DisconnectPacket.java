package game.packet;

import net.client.UClient;
import net.packet.UPacket;

/**
 * Created by Peter on 25.05.2015.
 */
public class DisconnectPacket extends UPacket {
    @Override
    public byte[] getData() {
        return "Server is full".getBytes();
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void handle(byte[] data, UClient origin) {
        origin.close();
    }
}
