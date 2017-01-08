package IOT;


import java.util.ArrayList;

/**
 * This class manages the subscribers, checks for duplicate subscribing requests and keeps the subscribers list up to date.
 * @author Melanie
 * @version Milestone1
 */
public class SubscriberList {

    /**
     * A list of all subscriber-objects interested in the service this node offers.
     */
    private ArrayList<Subscriber> list = new ArrayList<>();


    /**
     * Adds a new subscriber to the stored list of subscribers and checks for duplicates, which are overwritten in case the port number changed.
     * @param newSubscriber A subscriber object representing a node interested in the local service.
     */
    public void addSubscriber(Subscriber newSubscriber){

        //Check if this Subscriber is already in the storage, if yes, delete the original entry
        for (Subscriber subscriber : list){
            if (subscriber.getIpAddress().equals(newSubscriber.getIpAddress())){
                list.remove(subscriber);
            }
        }

        //Add the new subscriber to the list
        list.add(newSubscriber);
    }

    public ArrayList<Subscriber> getSubscribers(){
        return list;
    }

}
