package IOTApplication.IOTApplication;

/**
 * Interface for all application implementations to include handling of incoming
 * Notifications as well as sending them to the Subscribers.
 *
 */
public interface IOTApplicationInterface {
	/** Handles a received package and starts an event if triggered. */
	public void handleIncomingNotification(IOTMessage message);
}
