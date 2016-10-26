package IOTApplication.IOTServer.UDPListener;

/**
 * Created by Mai on 10/26/2016.
 */
public interface UDPListenerInterface extends Runnable {

    /**
     * Sets up a new DatagramSocket on a port specified in the Constructor.
     */
    @Override
    public void run();

    /**
     * Terminates the UDP listener.
     */
    public void terminate();
}
