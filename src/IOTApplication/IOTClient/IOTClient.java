package IOTApplication.IOTClient;

import IOTApplication.IOTApplication.IOTMessage;
import IOTApplication.Subscriber;
import IOTApplication.SubscriberList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by melaniebalaz on 28/10/2016.
 */
public class IOTClient implements IOTClientInterface {

    SubscriberList subscribers;

    public IOTClient (SubscriberList pSubscribers){
        subscribers = pSubscribers;
    }

    public void broadcastServiceOffering(){
        //make a UDP broadcast
    }
    public void notifySubscribers(IOTMessage message){
        //Create a gson object and wrap it into json


        //Send HTTP Post Request to each subscriber in the list
        ArrayList<Subscriber> list;
        list = subscribers.getSubscribers();

        for (Subscriber subcriber: list) {
            String url = createUrl(subcriber.getIpAddress(),subcriber.getPort());
            createNewPostRequest(url);
        }

    }

    private String createUrl(String ipAddress,  int port){
        return("http://" + ipAddress + ":" + port + "/");
    }

    public void createNewPostRequest(String urlString){
        HttpURLConnection newConnection;
        try {
            URL url = new URL(urlString); //Set the custom URL
            newConnection = (HttpURLConnection) url.openConnection(); //Open the connection
            newConnection.setRequestMethod("POST"); //Set the request type

        } catch (MalformedURLException exception) {

        } catch (IOException exception) {

        }
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
