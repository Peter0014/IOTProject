package IOTApplication.IOTServer;


import java.util.ArrayList;

/**
 * Created by melaniebalaz on 24/10/2016.
 */
public interface IOTServerInterface {

    /**
     * This method broadcasts UDP packages which describe the service
     */
    public void broadcastServiceOffering(String typeOfService);

    /**
     * This method handles incoming subscribe requests
     */
    public void subscribeRequestHandler(String destinationIP, int destinationPort);
    public void incomingNotificationHandler(String messageType, ArrayList notification);

}
