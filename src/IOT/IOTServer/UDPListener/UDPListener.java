package IOT.IOTServer.UDPListener;

import IOT.IOTServer.IOTServerInterface;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.*;

/**
 * Created by Mai on 10/26/2016.
 */
public class UDPListener implements UDPListenerInterface {

    private Integer port = null;
    private DatagramSocket socket = null;
    private Boolean running = null;
    private IOTServerInterface receiverServer;

    public UDPListener (@NotNull Integer port, @NotNull IOTServerInterface receiverServer) {
        this.port = port;
        this.receiverServer = receiverServer;

        this.running = false;
    }

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

                if (datagram.getAddress().equals(InetAddress.getLocalHost())) continue;

                receiverServer.receiveServiceOffering(datagram.getAddress(), data);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (socket != null && !(socket.isClosed())) socket.close();
            this.running = false;
        }
    }

    @Override
    public void terminate() {
        this.running = false;
        if (socket != null && !(socket.isClosed())) socket.close();
    }

    public Integer getPort() {
        return this.port;
    }

    public boolean isRunning() {
        return this.running;
    }

    @Override
    public String toString() {
        try {
            return "Listener status: " + running + "\n" +
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
