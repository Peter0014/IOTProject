package IOTApplication.IOTClient.UDPBroadcastService;

/**
 * Created by Emily on 10/28/2016.
 */
public interface UDPBroadcastServiceInterface extends Runnable {

    /**
     * Starts the UDP listener.
     */
    @Override
    public void run();

    /**
     * Terminates the UDP broadcaster.
     */
    public void terminate();
}
