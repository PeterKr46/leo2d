package game.packet;


import game.GameServer;
import leo2d.controllers.ChatController;
import net.client.UClient;
import net.packet.UPacket;
import net.util.ByteConverter;

/**
 * Created by Peter on 24.05.2015.
 */
public class ChatPacket extends UPacket {

    public String message = "";

    public ChatPacket() {

    }

    public ChatPacket(String message) {
        this.message = message;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void handle(byte[] data, UClient origin) {
        message = ByteConverter.bytes2String(data);
        GameServer.log("<" + origin.getAlias() + "> " + message);
        message = "<" + origin.getAlias() + "> " + message;
        ((GameServer)GameServer.getServer()).broadcast(this);
    }

    @Override
    public byte[] getData() {
        return message.getBytes();
    }

    @Override
    public void clientHandle(byte[] data) {
        message = ByteConverter.bytes2String(data);
        ChatController.addLine(message);
    }
}
