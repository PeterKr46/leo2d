package game.packet;


import game.CommandExecutor;
import game.EntityManager;
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
        if(message.startsWith(".")) {
            EntityManager.Entity entity = EntityManager.getInstance().getEntity(origin.getUuid());
            message = message.substring(1).toLowerCase();
            String[] parts = message.split(" ");
            String command = parts[0];
            if(parts.length > 1) {
                String[] args = new String[parts.length-1];
                System.arraycopy(parts, 1, args, 0, parts.length-1);
                CommandExecutor.onCommand(command, args, entity);
            } else {
                CommandExecutor.onCommand(command, new String[0], entity);
            }
            return;
        }
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
