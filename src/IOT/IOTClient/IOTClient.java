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
 * The IOTClient is mainly responisble for creating and sending messages over the network.
 * Its implementation is independent from the underlying application.
 * @author Melanie, Mai
 * @version Milestone1
 */
public class IOTClient implements IOTClientInterface {

    /**
     * A list containing all Subscriber-objects the Client has subscribed to.
     */
    private SubscriberList subscribers;

    /**
     * Describes the service offered by the node the client sits on.
     */
    private String serviceDescription;

    /**
     * Creates a new instance of client. This should only happen once per node.
     * @param pSubscribers An (at first empty) list for all subscribers.
     * @param serviceDescription A string describing the service offered by the underlying application.
     */
    public IOTClient (SubscriberList pSubscribers, String serviceDescription) {
        this.subscribers = pSubscribers;
        this.serviceDescription = serviceDescription;
    }

    /**
     * {@inheritDoc}
     * @param message The message to be sent to all subscribers.
     */
    public void notifySubscribers(IOTMessage message){
        //Create a a new json string to send in the Post body
        Gson gson = new GsonBuilder().create();
        String jsonMessage = gson.toJson(message);

        ArrayList<Subscriber> list;
        list = subscribers.getSubscribers();

        if (list != null) {
        	//Send HTTP Post Request with message body to each subscriber in the list
			for (Subscriber subscriber : list) {
				String url = createUrl(subscriber.getIpAddress(), subscriber.getPort()) + "iot";
                System.out.println("Sending post to URL: " + url) ;
                createNewPostRequest(url, jsonMessage);
			}
		}
    }

	/**
	 * This method creates a finished URL to which messages can be sent.
	 * @param ipAddress The IP address to be used in the URL.
	 * @param port The port to be used in the URL.
	 * @return A String representation of an URL to which messages can be sent.
	 */
    private String createUrl(String ipAddress,  int port){
        return("http://" + ipAddress + ":" + port + "/");
    }

    /**
     * Unused.
     * @param urlString unused
     * @param jsonMessage unused
     */
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
            System.out.println();
            System.out.println("STATUS POST NOTIFY: " + newConnection.getResponseCode());

        } catch (MalformedURLException exception) {

        } catch (IOException exception) {
            // TODO: this is a stub
        }
    }

    /**
     * {@inheritDoc}
     * @param destinationIP The IP-Address the service offering originally came from.
     * @param destinationPort The port to which to send the request.
     */
    public void createSubscriptionRequest(String destinationIP, int destinationPort) {
        //Send HTTP GET Request to the passed IP and Port
        HttpURLConnection newConnection;
        try {
            URL url = new URL("http://" + destinationIP + ":" + destinationPort + "/iot"); //Set the custom URL
            newConnection = (HttpURLConnection)url.openConnection(); //Open the connection
            newConnection.setRequestMethod("GET"); //Set the request type
            int response = newConnection.getResponseCode(); // without this no send apparently
            System.out.println(this.serviceDescription + " is sending a sub request to " + url.toString() + "!");
            if (response == 200) {
                // TODO add to list of known IPs to avoid spam?
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
