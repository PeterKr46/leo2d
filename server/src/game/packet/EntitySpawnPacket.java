package game.packet;

import net.client.UClient;
import net.packet.UPacket;
import net.server.UServer;
import net.util.ArrayMerger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Peter on 25.05.2015.
 */
public class EntitySpawnPacket extends UPacket {

    private int entityId;
    private float[] position;

    public EntitySpawnPacket() {

    }

    public EntitySpawnPacket(int id) {
        this.entityId = id;
        this.position = new float[] {0, 0, 0};
    }

    public EntitySpawnPacket(int id, float[] position) {
        this.entityId = id;
        this.position = position;
    }

    @Override
    public byte[] getData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(position.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(position);
        byte[] pos = byteBuffer.array();
        return new ArrayMerger(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(entityId).array()).append(pos).array();
    }

    @Override
    public int getId() {
        return 4;
    }

    /**
     * This should never be executed
     */
    @Override
    public void handle(byte[] data, UClient origin) {
        int id = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        float x = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        float y = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        float z = ByteBuffer.wrap(data, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        UServer.log("[DEPR] Entity #" + id + " spawned at (" + x + " " + y + " " + z + ")");
    }
}
