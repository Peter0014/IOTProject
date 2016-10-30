package IOT.IOTClient;

import IOT.IOTApplication.*;

/**
 * This interface defines the functionality for the client
 */
public interface IOTClientInterface {

    public void notifySubscribers(IOTMessage message);
    public void createSubscriptionRequest(String destinationIP, int destinationPort);

}
