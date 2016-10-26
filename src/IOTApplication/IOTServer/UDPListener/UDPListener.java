package IOTApplication.IOTServer.UDPListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Mai on 10/26/2016.
 */
public class UDPListener implements UDPListenerInterface {

    private Integer port = null;
    private DatagramSocket socket = null;
    private Boolean running = null;

    public UDPListener (Integer port) {
        this.port = port;
        this.running = false;
    }

    @Override
    public void run() {
        if (running) throw new IllegalStateException("This instance is already running!");
        System.out.println("Starting new thread @port_" + port);

        try {
            socket = new DatagramSocket(port);
            byte[] buffer = new byte[10240];
            this.running = true;

            while(running) {
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
                socket.receive(datagram);

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            this.running = false;
        }
    }

    @Override
    public void terminate() {
        this.running = false;
        socket.close();
    }

    public Integer getPort() {
        return this.port;
    }

    public boolean isRunning() {
        return this.running;
    }
}
