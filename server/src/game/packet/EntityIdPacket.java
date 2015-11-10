package game.packet;


import net.client.UClient;
import net.packet.UPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Peter on 24.05.2015.
 */
public class EntityIdPacket extends UPacket {

    public EntityIdPacket() {

    }

    private int entityId;

    public EntityIdPacket(int id) {
        entityId = id;
    }

    @Override
    public int getId() {
        return 3;
    }

    /**
     * Receiving an EntityIdPacket means the client requests an ID.
     *
     * This should not be a thing.
     */
    @Deprecated
    @Override
    public void handle(byte[] data, UClient origin) {
        /* int id = EntityManager.getInstance().generateEntityId();
        EntityManager.getInstance().setEntityId(origin.getUuid(), id);
        origin.sendPacket(new EntityIdPacket(id)); */
    }

    @Override
    public byte[] getData() {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(entityId).array();
    }

    @Override
    public void clientHandle(byte[] data) {
        int id = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();

    }
}
