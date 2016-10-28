package IOTApplication.IOTClient;

import IOTApplication.IOTApplication.IOTMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        //Send HTTP GET Request to the passed IP and Port

        HttpURLConnection newConnection;
        try {
            URL url = new URL("http://" + destinationIP + ":" + destinationPort + "/"); //Set the custom URL
            newConnection = (HttpURLConnection)url.openConnection(); //Open the connection
            newConnection.setRequestMethod("GET"); //Set the request type

        }
        catch (MalformedURLException exception){

        }
        catch (IOException exception){

        }
    }
}
