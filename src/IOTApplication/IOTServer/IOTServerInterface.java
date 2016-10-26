package IOTApplication.IOTServer;


import IOTApplication.IOTApplication.IOTMessage;

import java.util.ArrayList;

/**
 * Created by melaniebalaz on 24/10/2016.
 */
public interface IOTServerInterface {


    /**
     * This method receives a UDP Service Offering package and analyzes its content.
     * If it is interested in the Service it tells the Client to sent a subscription request.
     */
    public void receiveServiceOffering();

    /**
     * This method handles incoming subscribe requests
     */
    public void subscribeRequestHandler(String destinationIP, int destinationPort);

    /**
     * This method passes incoming Notifications on to the application
     */
    public void incomingNotificationHandler(IOTMessage message);

}
