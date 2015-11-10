package net.client;

import net.packet.Registry;
import net.packet.UPacket;
import net.server.UServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Created by Peter on 25.05.2015.
 */
public class UClient {

    private boolean connected = true;
    private String alias;
    private UUID uuid;
    private Socket socket;
    private InputThread inputThread;

    public UClient(String uuid, Socket socket) {
        this.uuid = UUID.fromString(uuid);
        this.socket = socket;
        this.inputThread = new InputThread();
        this.inputThread.start();
    }

    public UClient(String uuid, Socket socket, String alias) {
        this(uuid, socket);
        this.alias = alias;
    }

    public void close() {
        connected = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UServer.getServer().removeClient(this);
        UServer.log(socket.getInetAddress().toString() + " disconnected.");
    }

    public boolean isConnected() {
        return connected;
    }

    public String getAlias() {
        return alias;
    }

    public UUID getUuid() {
        return uuid;
    }

    private void write(byte[] data) throws IOException {
        OutputStream os = socket.getOutputStream();
        int toSendLen = data.length;
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte)(toSendLen & 0xff);
        toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
        os.write(toSendLenBytes);
        os.write(data);
    }

    private byte[] read() throws IOException {
        InputStream is = socket.getInputStream();
        byte[] lenBytes = new byte[4];
        is.read(lenBytes, 0, 4);
        int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
        byte[] receivedBytes = new byte[len];
        is.read(receivedBytes, 0, len);
        return receivedBytes;
    }

    public void sendPacket(UPacket packet) {
        /*if(!(packet instanceof HeartbeatPacket)) {
            UServer.log(packet.getClass().toString() + " -> " + getUuid());
        }*/
        try {
            write(packet.build());
        } catch (Exception e) {
            if(e.getLocalizedMessage().contains("Connection reset") || e.getLocalizedMessage().contains("Socket is closed")) {
                close();
            } else {
                e.printStackTrace();
            }
        }
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private class InputThread extends Thread {
        @Override
        public void run() {
            while(UClient.this.isConnected() && UServer.getServer() != null && UServer.getServer().isRunning()) {
                try {
                    byte[] raw = read();
                    int id = ByteBuffer.wrap(raw, 0, 4).
                            order(ByteOrder.LITTLE_ENDIAN).
                            getInt();
                    byte[] data = new byte[raw.length - 4];
                    System.arraycopy(raw, 4, data, 0, data.length);
                    UPacket packet = (UPacket) Registry.packets[id].getConstructor().newInstance();
                    packet.handle(data, UClient.this);
                } catch (SocketException e) {
                    UClient.this.close();
                } catch (IOException | InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
