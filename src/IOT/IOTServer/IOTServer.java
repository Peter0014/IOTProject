package IOT.IOTServer;

import IOT.IOTApplication.IOTApplicationInterface;
import IOT.IOTApplication.IOTMessage;
import IOT.IOTClient.IOTClientInterface;
import IOT.Subscriber;
import IOT.SubscriberList;

import java.net.InetAddress;

/**
 * This class handles logical IOT requests.
 * @version Milestone1
 */
public class IOTServer implements IOTServerInterface {

    /**
     * A list of all currently known subscribers to whom messages can be forwarded.
     */
    private SubscriberList subscribers;
    /**
     * A reference to the instance of Client running on this node.
     */
    private IOTClientInterface client;
    /**
     * A reference to the instance of Application running on this node.
     */
    private IOTApplicationInterface application;

    public IOTServer() {
        System.out.println("Oh fuck why this server.");
    }

    /**
     * Creates a new instance of server. This constructor is only called once in a servlet's lifetime.
     * @param pSubscribers An (at first empty) list of subscribers for this node.
     * @param pClient A reference to the instance of Client running on this node.
     * @param pApplication A reference to the instance of Application running on this node.
     */
    public IOTServer(SubscriberList pSubscribers, IOTClientInterface pClient, IOTApplicationInterface pApplication){
        subscribers = pSubscribers;
        client = pClient;
        application = pApplication;
    }

    /**
     * {@inheritDoc}
     * @param sourceAddress The IP-address of where the offering came from.
     * @param data The data contained within the service offering.
     */
    public void receiveServiceOffering(InetAddress sourceAddress, String data) {
        //System.out.println("Received data from " + sourceAddress);
        // check if application is interested
        if (application.isInterested(data)) client.createSubscriptionRequest(sourceAddress.getHostAddress().toString(),8080);
    }

    /**
     * This method creates a new subscriber and sets its ip and port attributes, and hands it over to the Subscriber list for storing.
     * @param destinationIP The IP of the node instered in the service.
     * @param destinationPort The port of the node interested in the service.
     */
    public void subscribeRequestHandler(String destinationIP, int destinationPort){

        //Create a new subscriber
        Subscriber newSubscriber = new Subscriber(destinationIP, destinationPort);
        subscribers.addSubscriber(newSubscriber);
    }

    /**
     * {@inheritDoc}
     * @param message The message received from an outside source.
     */
    public void incomingNotificationHandler(IOTMessage message){
        application.handleIncomingNotification(message);
    }
}
