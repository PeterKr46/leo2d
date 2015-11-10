package net.packet;

import net.client.UClient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Peter on 25.05.2015.
 */
public abstract class UPacket {
    public abstract byte[] getData();
    public abstract int getId();

    public byte[] build() {
        byte[] raw = getData();
        byte[] fullPacket = new byte[raw.length + 4];
        byte[] rawId = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(getId()).array();

        System.arraycopy(rawId, 0, fullPacket, 0, rawId.length);
        System.arraycopy(raw, 0, fullPacket, 4, raw.length);
        return fullPacket;
    }

    /**
     * Handles the raw content of incoming packets.
     */
    public abstract void handle(byte[] data, UClient origin);

    public void clientHandle(byte[] data) {

    }
}
