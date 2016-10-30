package IOT.IOTClient;

import IOT.IOTApplication.IOTMessage;

/**
 * This interface defines the messaging functionality of the client.
 * @version Milestone1
 * @author Arreza, Mai, Melanie
 */
public interface IOTClientInterface {

    /**
     * Sends a message to all currently known subscribers.
     * @param message The message to be sent to all subscribers.
     */
    public void notifySubscribers(IOTMessage message);

    /**
     * Creates and sends a subscription requests to another node.
     * @param destinationIP The IP-Address the service offering originally came from.
     * @param destinationPort The port to which to send the request.
     */
    public void createSubscriptionRequest(String destinationIP, int destinationPort);

}
