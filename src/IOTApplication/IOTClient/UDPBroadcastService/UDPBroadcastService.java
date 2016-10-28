package IOTApplication.IOTClient.UDPBroadcastService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Emily on 10/28/2016.
 */
public class UDPBroadcastService implements UDPBroadcastServiceInterface {

    /**
     * The interval between UDP broadcasts (in milliseconds).
     */
    public static final int BROADCAST_MESSAGE_INTERVAL = 1000;

    private Integer port = null;
    private Boolean running = null;
    private DatagramSocket socket = null;
    private String message = null;

    public UDPBroadcastService (Integer port, String message) {
        this.port = port;
        this.message = message;
        this.running = false;
    }

    @Override
    public void run() {
        if (running) throw new IllegalStateException("This instance is already running!");
        System.out.println("Starting new thread @port_" + port);

        try {
            byte[] mbuf = message.getBytes();
            InetAddress host = InetAddress.getLocalHost();
            DatagramPacket broadcastDatagram = new DatagramPacket(mbuf,mbuf.length, InetAddress.getByName("255.255.255.255"),port);
            socket = new DatagramSocket();
            running = true;

            while (running) {
                socket.send(broadcastDatagram);
                Thread.sleep(BROADCAST_MESSAGE_INTERVAL);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (socket != null) socket.close();
            this.running = false;
        }
    }


    @Override
    public void terminate() {
        if (socket != null) socket.close();
        this.running = false;
    }

    @Override
    public String toString() {
        try {
            return "Broadcast status: " + running + "\n" +
                    "Location: " + InetAddress.getLocalHost() + ":" + port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "Broadcast status: " + running + " at port " + port;
    }

    /**
     * Debug method.
     * @param args No arguments.
     */
    public static void main(String[] args) {
        System.out.println("Debugging broadcast service");
        UDPBroadcastServiceInterface iCast = new UDPBroadcastService(1000,"i'm a teapot");
        Thread broadcastThread = new Thread(iCast);
        broadcastThread.start();
        System.out.println(iCast.toString());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        iCast.terminate();
        System.exit(0);
    }
}
