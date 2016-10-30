package IOT.IOTClient.UDPBroadcastService;

/**
 * This interface provides generic methods to start and terminate an UDP Broadcast Service.
 * @author Mai
 * @version Milestone1
 */
public interface UDPBroadcastServiceInterface extends Runnable {

    /**
     * Starts the UDP broadcast on a port specified beforehand.
     */
    @Override
    public void run();

    /**
     * Terminates the UDP broadcaster.
     */
    public void terminate();
}
