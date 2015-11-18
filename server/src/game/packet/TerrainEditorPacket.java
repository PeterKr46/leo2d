package game.packet;


import game.GameServer;
import leo2d.core.Transform;
import leo2d.math.Vector;
import net.client.UClient;
import net.packet.UPacket;
import net.util.ArrayMerger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Peter on 24.05.2015.
 */
public class TerrainEditorPacket extends UPacket {


    public TerrainEditorPacket() {

    }

    private int tex, id;
    private float x, y;

    public TerrainEditorPacket(int tex, int id, float x, float y) {
        this.tex = tex;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void handle(byte[] data, UClient origin) {
        id = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        tex = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        x = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        y = ByteBuffer.wrap(data, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        GameServer.log(x + "." + y + " moved.");
        GameServer.getInstance().getLevel().sync(tex, id, new float[]{x,y});
        GameServer.getInstance().broadcast(this,origin);
    }

    @Override
    public byte[] getData() {
        byte[] id = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this.id).array();
        byte[] tex = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this.tex).array();
        byte[] x = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(this.x).array();
        byte[] y = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(this.y).array();
        return new ArrayMerger(id).append(tex).append(x).append(y).array();
    }

    @Override
    public void clientHandle(byte[] data) {
        id = ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        tex = ByteBuffer.wrap(data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        float x = ByteBuffer.wrap(data, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        float y = ByteBuffer.wrap(data, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        Transform.findOne(id + "T" + tex).position = new Vector(x,y);
    }
}
