package IOTApplication.IOTClient;

import IOTApplication.IOTApplication.IOTMessage;

/**
 * Created by melaniebalaz on 28/10/2016.
 */
public class IOTClient implements IOTClientInterface {

    public void broadcastServiceOffering(){
        //make a UDP broadcast
    }
    public void notifySubscribers(IOTMessage message){
        //Send HTTP Post Request

    }
    public void createSubscriptionRequest(String destinationIP, int destinationPort){
        //Send HTTP GET Request
    }
}
