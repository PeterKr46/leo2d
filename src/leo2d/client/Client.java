package leo2d.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Peter on 10.11.2015.
 */
public class Client {

    private Socket socket;

    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(byte[] data) {
        OutputStream os;
        try {
            os = socket.getOutputStream();
            int toSendLen = data.length;
            byte[] toSendLenBytes = new byte[4];
            toSendLenBytes[0] = (byte)(toSendLen & 0xff);
            toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
            toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
            toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
            os.write(toSendLenBytes);
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] receive() {
        try {
            byte[] lenBytes = new byte[4];
            socket.getInputStream().read(lenBytes, 0, 4);
            int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                    ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
            byte[] receivedBytes = new byte[len];
            socket.getInputStream().read(receivedBytes, 0, len);
            return receivedBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void close() throws IOException {
        socket.close();
    }
}
