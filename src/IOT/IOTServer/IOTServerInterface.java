package IOT.IOTServer;


import IOT.IOTApplication.IOTMessage;

import java.net.InetAddress;

/**
 * Created by melaniebalaz on 24/10/2016.
 * @author Melanie, Mai
 * @version Milestone1
 */
public interface IOTServerInterface {


    /**
     * This method receives a UDP Service Offering package and analyzes its content.
     * If it is interested in the Service it tells the Client to sent a subscription request.
     * @param sourceAddress The IP-address of where the offering came from.
     * @param data The data contained within the service offering.
     */
    public void receiveServiceOffering(InetAddress sourceAddress, String data);

    /**
     * This method handles incoming subscription requests.
     * @param destinationIP The IP of the node instered in the service.
     * @param destinationPort The port of the node interested in the service.
     */
    public void subscribeRequestHandler(String destinationIP, int destinationPort);

    /**
     * This method passes incoming notifications on to the application.
     * @param message The message received from an outside source.
     */
    public void incomingNotificationHandler(IOTMessage message);

}
