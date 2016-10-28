package IOTApplication.IOTApplication;

/**
 * Interface for all application implementations to include handling of incoming
 * Notifications as well as creating, updating, getting and deleting data sets.
 *
 */
public interface IOTApplicationInterface {
	/** Handles a received package and starts an event if triggered. */
	public void handleIncomingNotification(IOTMessage message);
	
	/** Returns the current state of previously added data entries. */
	public String getDataSet();
	
	/** Adds a new entry to the application object */
	public int createDataEntry(String data);

	/** Updates an existing entry in the application object */
	public int updateDataEntry(String data);

	/** Deletes an existing entry in the application object */
	public int deleteDataEntry(String data);
}
