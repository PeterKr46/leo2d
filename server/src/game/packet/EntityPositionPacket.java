package game.packet;

import game.EntityManager;
import game.GameServer;
import net.client.UClient;
import net.packet.UPacket;
import net.util.ArrayMerger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Peter on 25.05.2015.
 */
public class EntityPositionPacket extends UPacket {

    private int entityId;
    private float[] position;
    private int direction;
    private boolean moving;

    public EntityPositionPacket() {

    }

    public EntityPositionPacket(int id, float[] position, int direction, boolean moving) {
        this.entityId = id;
        this.position = position;
        this.direction = direction;
        this.moving = moving;
    }

    /**
     * Format:
     * 0-3:     Entity ID       (int)
     * 4-7:     Position X      (float)
     * 8-11:    Position Y      (float)
     * 12-15    Rotation        (int, 1 = up, 2 = right, 3 = down, 4 = left, 0 = idle)
     * 16:      Moving          (boolean)
     */
    @Override
    public byte[] getData() {
        byte[] id = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(entityId).array();
        byte[] x = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(position[0]).array();
        byte[] y = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(position[1]).array();
        byte[] dir = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(direction).array();
        byte[] total = new ArrayMerger(id).append(x).append(y).append(dir).append((byte) (moving ? 1 : 0)).append((byte) 1).array();
        return total;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void handle(byte[] data, UClient origin) {
        // Formality
        int id = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        float x = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        float y = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        int dir = ByteBuffer.wrap(data, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        boolean moving = data[16] > 0 ? true : false;
        float[] pos = new float[] {x,y};
        GameServer.log("Pos " + x + "|" + y);
        EntityManager.Entity entity = EntityManager.getInstance().getEntity(origin.getUuid());
        if(!entity.isAt(pos) || moving != entity.isMoving()) {
            EntityManager.getInstance().getEntity(origin.getUuid()).setPosition(pos);
            EntityManager.getInstance().getEntity(origin.getUuid()).setMoveDirection(direction);
            EntityManager.getInstance().getEntity(origin.getUuid()).setMoving(moving);
        }
    }
}
