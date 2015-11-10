package game.packet;


import net.client.UClient;
import net.packet.UPacket;
import net.util.ByteConverter;

/**
 * Created by Peter on 24.05.2015.
 */
public class HeartbeatPacket extends UPacket {
    private static final byte[] content = "Heartbeat".getBytes();

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void handle(byte[] data, UClient origin) {
        String content = ByteConverter.bytes2String(data);
    }

    @Override
    public byte[] getData() {
        return content;
    }
}
