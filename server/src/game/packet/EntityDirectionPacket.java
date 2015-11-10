package game.packet;

import game.EntityManager;
import net.client.UClient;
import net.packet.UPacket;
import net.util.ArrayMerger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Peter on 25.05.2015.
 */
public class EntityDirectionPacket extends UPacket {

    private int entityId;
    private float faceDirection, moveDirection;

    public EntityDirectionPacket() {

    }

    public EntityDirectionPacket(int id,  float faceDirection, float moveDirection) {
        this.entityId = id;
        this.faceDirection = faceDirection;
        this.moveDirection = moveDirection;
    }

    /**
     * Format:
     * 0-3:     Entity ID       (int)
     * 4-7:     Position X      (float)
     * 8-11:    Position Y      (float)
     */
    @Override
    public byte[] getData() {
        byte[] id = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(entityId).array();
        byte[] moveDir = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(moveDirection).array();
        byte[] faceDir = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(faceDirection).array();
        return new ArrayMerger(id).append(faceDir).append(moveDir).array();
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void handle(byte[] data, UClient origin) {
        // Formality
        int id = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        float faceDir = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        float walkDir = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        EntityManager.Entity entity = EntityManager.getInstance().getEntity(origin.getUuid());
        if(entity.getFaceDirection() != faceDir || entity.getMoveDirection() != walkDir) {
            entity.setFaceDirection(faceDir);
            entity.setMoveDirection(walkDir);
        }
    }
}
