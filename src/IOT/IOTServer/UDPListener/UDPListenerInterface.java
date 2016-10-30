package IOT.IOTServer.UDPListener;

/**
 * This interface provides generic methods to start and terminate an UDP Listener Service.
 * @author Mai
 * @version Milestone1
 */
public interface UDPListenerInterface extends Runnable {

    /**
     * Starts the UDP listener on a port specified beforehand.
     */
    @Override
    public void run();

    /**
     * Terminates the UDP listener.
     */
    public void terminate();
}
