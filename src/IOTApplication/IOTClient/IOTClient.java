package AlarmClock.Server.ServletController;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class IOTClient implements IOTClientInterface{
    DatagramSocket socket;

    public void broadcastServiceOffering() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            byte[] b = new byte[1000];

            DatagramPacket dgram;

            dgram = new DatagramPacket(b, b.length,
                    InetAddress.getByName(""), 8080);

            System.err.println("Sending " + b.length + " bytes to " +
                    dgram.getAddress() + ':' + dgram.getPort());
            while (true) {
                System.err.print(".");
                socket.send(dgram);
                Thread.sleep(1000);
            }

            ByteArrayOutputStream b_out = new ByteArrayOutputStream();
            ObjectOutputStream o_out = new ObjectOutputStream(b_out);

            o_out.writeObject(new Message());

            byte[] m = b_out.toByteArray();

            DatagramPacket dgram = new DatagramPacket(m, m.length,
                    InetAddress.getByName(""), 8080); // multicast
            socket.send(dgram);

            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(2000);
            this.socket.receive(reply);
            System.out.println("Reply: " + new
                    String(reply.getData()));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            socket.close();
        }

    }
    public void notifySubscribers(String messageType, ArrayList notification)
    {
        for(Subscriber s: notification){
            s.update(messageType);
        }
    }
}