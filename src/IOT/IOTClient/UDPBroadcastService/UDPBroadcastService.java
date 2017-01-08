package IOT.IOTClient.UDPBroadcastService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The UDP broadcaster is responsible for flooding the network with service offerings, which are to be
 * received by the {@link IOT.IOTServer.UDPListener.UDPListenerInterface UDP Listener}.
 * @author Mai
 * @version Milestone1
 */
public class UDPBroadcastService implements UDPBroadcastServiceInterface {

    /**
     * The interval between UDP broadcasts (in milliseconds).
     */
    public static final int BROADCAST_MESSAGE_INTERVAL = 1000;

    /**
     * The port on which to broadcast.
     */
    private Integer port = null;
    /**
     * True indicates the broadcaster is actively sending datagrams on the specified port.
     * False indicates the broadcaster has stopped, either by the method {@link #terminate()} terminate) or by an error.
     * Default is false (null if constructor has not been called yet).
     */
    private Boolean running = null;
    /**
     * The socket to which the sending is bound.
     * Defaults to null until the run-method is called.
     */
    private DatagramSocket socket = null;
    /**
     * The message that will be broadcast.
     */
    private String message = null;

    /**
     * Creates a new instance of UDPBroadcastService. It automatically sets "listening" to false; "socket" will be initialized by the "run" method.
     * @param port The port on which to broadcast.
     * @param message The message that will be broadcast.
     */
    public UDPBroadcastService (Integer port, String message) {
        this.port = port;
        this.message = message;
        this.running = false;
    }

    /**
     * This methods starts the UDP Broadcast Service. As destination-IP serves the multicast-IP 255.255.255.255.
     * The service will continually flood the network with messages until it is terminated by external methods or errors.
     * This method can only be called ONCE.
     * @throws IllegalStateException If the run-method has already been called before.
     */
    @Override
    public void run() {
        if (running) throw new IllegalStateException("This instance is already running!");
        System.out.println("Starting UDP broadcast @port_" + port);

        try {
            byte[] mbuf = message.getBytes();
            DatagramPacket broadcastDatagram = new DatagramPacket(mbuf,mbuf.length, InetAddress.getByName("255.255.255.255"),port);
            socket = new DatagramSocket();
            running = true;

            while (running) {
                socket.send(broadcastDatagram);
                Thread.sleep(BROADCAST_MESSAGE_INTERVAL);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Exception caught during UDP broadcast: " + e.getMessage());
        } finally {
            terminate();
        }
    }

    /**
     * Terminates the broadcaster by setting "running" to false. Also does resource cleanup.
     */
    @Override
    public void terminate() {
        System.out.println("closing udp broadcaster");
        this.running = false;
        if (socket != null && !(socket.isClosed())) socket.close();
    }

    /**
     * {@inheritDoc}
     * @return a string representation of the broadcaster's state (if running, and if known, on which address)
     */
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
