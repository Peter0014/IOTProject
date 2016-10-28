package IOTApplication.IOTClient;

import IOTApplication.IOTApplication.*;
import java.util.ArrayList;

/**
 * This interface defines the functionality for the client
 */
public interface IOTClientInterface {

    /**
     * This method broadcasts UDP packages which describe the service
     */
    public void broadcastServiceOffering();
    public void notifySubscribers(IOTMessage message);
    public void createSubscriptionRequest(String destinationIP, int destinationPort);

}
