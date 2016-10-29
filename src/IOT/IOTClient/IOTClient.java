package IOT.IOTClient;

import IOT.IOTApplication.IOTMessage;
import IOT.Subscriber;
import IOT.SubscriberList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by melaniebalaz on 28/10/2016.
 */
public class IOTClient implements IOTClientInterface {

    /**
     * A list containing all Subscriber-objects the Client has subscribed to.
     */
    SubscriberList subscribers;

    /**
     * Describes the service offered by the node the client sits on.
     */
    private String serviceDescription;

    public IOTClient (SubscriberList pSubscribers, String serviceDescription) {
        this.subscribers = pSubscribers;
        this.serviceDescription = serviceDescription;
    }

    public void broadcastServiceOffering(){
        //make a UDP broadcast
    }

    public void notifySubscribers(IOTMessage message){
        //Create a a new json string to send in the Post body
        Gson gson = new GsonBuilder().create();
        String jsonMessage = gson.toJson(message);


        ArrayList<Subscriber> list;
        list = subscribers.getSubscribers();

        //Send HTTP Post Request with message body to each subscriber in the list
        for (Subscriber subscriber: list) {
            String url = createUrl(subscriber.getIpAddress(),subscriber.getPort());
            createNewPostRequest(url, jsonMessage);
        }

    }

    /**
     * This method  creates a finished url out of the two cpomponents
     * @param ipAddress
     * @param port
     * @return
     */
    private String createUrl(String ipAddress,  int port){
        return("http://" + ipAddress + ":" + port + "/");
    }

    private void createNewPostRequest(String urlString, String jsonMessage){
        HttpURLConnection newConnection;
        try {
            URL url = new URL(urlString); //Set the custom URL
            newConnection = (HttpURLConnection) url.openConnection(); //Open the connection
            newConnection.setRequestMethod("POST"); //Set the request type
            newConnection.setDoOutput(true); //informs the other side that they are gonna get some output from the package
            DataOutputStream writer = new DataOutputStream(newConnection.getOutputStream()); //create a writer for the connection
            writer.writeBytes(jsonMessage); //write the json string
            writer.flush();
            writer.close();

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

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
