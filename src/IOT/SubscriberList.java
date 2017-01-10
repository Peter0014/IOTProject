package IOT;


import IOT.IOTApplication.dao.IOTFilePersistenceManager;
import IOT.IOTApplication.dao.IOTPersistenceManagerInterface;

import java.util.List;

/**
 * This class manages the subscribers, checks for duplicate subscribing requests and keeps the subscribers list up to date.
 * @author Melanie
 * @version Milestone1
 */
public class SubscriberList {

    private IOTPersistenceManagerInterface<Subscriber> persistenceManager = null;

    public SubscriberList() {
        this.persistenceManager = new IOTFilePersistenceManager<>("subscribers");
    }

    /**
     * Adds a new subscriber to the stored list of subscribers and checks for duplicates, which are overwritten in case the port number changed.
     * @param newSubscriber A subscriber object representing a node interested in the local service.
     */
    public void addSubscriber(Subscriber newSubscriber){
        this.persistenceManager.add(newSubscriber);
    }

    public List<Subscriber> getSubscribers(){
        return this.persistenceManager.findAll();
    }

}
