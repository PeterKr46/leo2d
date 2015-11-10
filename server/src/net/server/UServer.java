package net.server;

import game.GameServer;
import game.packet.DisconnectPacket;
import net.client.UClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Peter on 25.05.2015.
 */
public abstract class UServer {

    public boolean cameraMovement = true;

    public static void main(String[] args) throws IOException {
        UServer server = new GameServer(2000, 4);
    }

    public static void log(Object msg) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "][Server] " + (msg != null ? msg.toString() : "null"));
    }

    private static UServer instance;
    public static UServer getServer() {
        return instance;
    }

    public UServer(int port, int maxClients) throws IOException {
        this.maxClients = maxClients;
        instance = this;
        socket = new ServerSocket(port);
        clients = new CopyOnWriteArrayList<UClient>();
        mainThread = new MainThread();
        connectThread = new ConnectThread();
        mainThread.start();
        connectThread.start();
        log("Started on Port " + port + " with " + maxClients + " max. clients.");
    }

    protected long ticksLived = 0;
    protected int maxClients = 4;
    protected ServerSocket socket;
    protected CopyOnWriteArrayList<UClient> clients;
    protected ConnectThread connectThread;
    protected MainThread mainThread;

    public boolean isRunning() {
        return true;
    }

    public void removeClient(UClient uClient) {
        clients.remove(uClient);
    }

    public abstract void tick();

    public void onConnect(UClient client) {

    }

    private class ConnectThread extends Thread {

        @Override
        public void run() {
            while(UServer.this.isRunning()) {
                try {
                    Socket newSocket = socket.accept();
                    UClient newClient = new UClient(UUID.randomUUID().toString(), newSocket, (Math.random() * 10) + "");
                    if(clients.size() < maxClients) {
                        clients.add(newClient);
                        onConnect(newClient);
                        log("Accepted connection from " + newSocket.getInetAddress().toString());
                    } else {
                        newClient.sendPacket(new DisconnectPacket());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MainThread extends Thread {
        @Override
        public void run() {
            while(UServer.this.isRunning()) {
                ticksLived++;
                long time = System.currentTimeMillis();
                tick();
                time = System.currentTimeMillis() - time;
                try {
                    sleep(Math.max(0,40 - time));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
