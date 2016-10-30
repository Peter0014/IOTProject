package IOT.IOTApplication;

/**
 * Interface for all application implementations to include handling of incoming
 * Notifications as well as sending them to the Subscribers.
 *
 */
public interface IOTApplicationInterface {
	/**
	 * Handles a received package and starts an event if triggered.
	 * 
	 * @param message
	 *            Message type and ID of the application with optional text
	 */
	public void handleIncomingNotification(IOTMessage message);

	/**
	 * Returns the application's service description which will be broadcast
	 * over the network via UDP.
	 * 
	 * @return String containing a service description.
	 */
	public String getServiceDescription();

	/**
	 * Returns if application is interested to subscribe.
	 * 
	 * @param broadcast
	 *            ID of broadcast from client
	 * @return true if subscription is desirable
	 */
	public boolean isInterested(String broadcast);
}
