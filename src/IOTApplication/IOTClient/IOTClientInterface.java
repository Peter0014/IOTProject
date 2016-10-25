package IOTApplication.IOTClient;

import java.util.ArrayList;

/**
 * Created by melaniebalaz on 24/10/2016.
 */
public interface IOTClientInterface {

    public void notifySubscribers(String messageType, ArrayList notification);
    public void receiveServiceOffering();
    public void createSubscriptionRequest(String destinationIP, int destinationPort);

}
