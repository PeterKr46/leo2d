package game.packet;


import game.GameServer;
import net.client.UClient;
import net.packet.UPacket;
import net.server.UServer;
import net.util.ArrayMerger;
import net.util.ByteConverter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Created by Peter on 24.05.2015.
 */
public class EntityAliasPacket extends UPacket {

    public EntityAliasPacket() {

    }

    private int entityId;
    private String alias;

    public EntityAliasPacket(int entityId, String alias) {
        this.entityId = entityId;
        this.alias = alias;
    }

    @Override
    public int getId() {
        return 5;
    }

    /**
     * Receiving an EntityAliasPacket means the client requests their Alias to be changed.
     */
    @Deprecated
    @Override
    public void handle(byte[] data, UClient origin) {
        String content = ByteConverter.bytes2String(data);
        content = (content.length() > 16 ? content.substring(0, 15) : content);
        origin.setAlias(content);
        UServer.log(content);
        ((GameServer)GameServer.getServer()).broadcastAlias(origin);
    }

    /**
     * Sending an EntityIdPacket means a client's alias was changed or is not yet know to the target client.
     */
    @Override
    public byte[] getData() {
        return new ArrayMerger(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(entityId).array()).append(alias.getBytes(Charset.forName("UTF-8"))).array();
    }
}
