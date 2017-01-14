package IOT.IOTServer.UDPListener;

import IOT.IOTServer.IOTServerInterface;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * The UDP Listener is responsible for receiving service offerings which are regularly sent out by the
 * {@link IOT.IOTClient.UDPBroadcastService.UDPBroadcastServiceInterface UDP Broadcast Service}.
 * @version Milestone1
 * @author Mai
 */
public class UDPListener implements UDPListenerInterface {

    /**
     * The port on which to listen.
     */
    private Integer port = null;
    /**
     * The socket to which the listening is bound.
     * Defaults to null until the run-method is called.
     */
    private DatagramSocket socket = null;
    /**
     * True indicates the listener is actively listening for Datagrams on the specified port.
     * False indicates the listener has stopped, either by the method {@link #terminate()} terminate) or by an error.
     * Default is false (null if constructor has not been called yet).
     */
    private Boolean running = null;
    /**
     * The server to whom the received datagram data are forwarded.
     */
    private IOTServerInterface receiverServer;

    /**
     * Create a new instance of UDPListener. It automatically sets "listening" to false; "socket" will be initialized by the "run" method.
     * @param port The port on which to Listen.
     * @param receiverServer The server to whom the recieved datagram data are forwarded.
     */
    public UDPListener (@NotNull Integer port, @NotNull IOTServerInterface receiverServer) {
        this.port = port;
        this.receiverServer = receiverServer;

        this.running = false;
    }

    /**
     * This methods starts the listener on the port specified in the constructor.
     * The listener will continue running until it is terminated by external methods or errors.
     * This method can only be called ONCE.
     * @throws IllegalStateException If the run-method has already been called before.
     */
    @Override
    public void run() {
        if (running) throw new IllegalStateException("This instance is already running!");
        System.out.println("Starting UDP listener @port_" + port);

        try {
            socket = new DatagramSocket(port);
            byte[] buffer = new byte[10240];
            this.running = true;

            while(running) {
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
                socket.receive(datagram);
                //parse the service offering -- UDP, so it's a string
                String data = new String(datagram.getData());
                //System.out.println("Received offering from " + datagram.getAddress());
                //System.out.println(data);
                if (datagram.getAddress().equals(InetAddress.getLocalHost().getHostAddress())) continue;

                receiverServer.receiveServiceOffering(datagram.getAddress(), data);
            }

        } catch (IOException e) {
            System.err.println("Exception caught while closing UDP listener: " + e.getMessage());
        } finally {
            terminate();
        }
    }

    /**
     * Terminates the listener by setting "running" to false. Also does resource cleanup.
     */
    @Override
    public void terminate() {
        System.out.println("closing udp listener");
        this.running = false;
        if (socket != null && !(socket.isClosed())) socket.close();
    }

    public Integer getPort() {
        return this.port;
    }

    public boolean isRunning() {
        return this.running;
    }

    /**
     * {@inheritDoc}
     * @return a string representation of the listener's state (if running, and if known, on which address)
     */
    @Override
    public String toString() {
        try {
            return "Listener status: " + running + "\n" +
                    "Location: " + InetAddress.getLocalHost() + ":" + port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "Listener status: " + running + " at port " + port;
    }

    /**
     * Debug method.
     * @param args No arguments.
     */
    public static void main(String[] args) {
        System.out.println("Debugging Listener");
        UDPListenerInterface iListen = new UDPListener(1000, null);

        Thread listenerThread = new Thread (iListen);
        listenerThread.start();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        iListen.terminate();
        System.exit(0);
    }
}
