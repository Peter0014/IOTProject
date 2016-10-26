package IOTApplication.IOTServer.UDPListener;

/**
 * Created by Mai on 10/26/2016.
 */
public interface UDPListenerInterface extends Runnable {

    /**
     * Starts a new socket on the specified port.
     */
    @Override
    public void run();

    /**
     * Terminates the UDP listener.
     */
    public void terminate();
}
