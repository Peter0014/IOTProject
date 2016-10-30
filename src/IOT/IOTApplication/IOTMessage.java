package IOT.IOTApplication;

/**
 * Class used to package messages between applications ready to be sent.
 * Contains type of this application and a message for all the subscribers.
 *
 * @author Peter Klosowski (a1403029)
 * @version Milestone1
 */
public class IOTMessage {
	/** Service Description of the application, typically the ID */
	private final String servDesc;
	/** Message type set by application. */
	private final String messageType;
	/** Message for the receivers. */
	private final String message;

	public IOTMessage(String newServDesc, String newMessageType, String newMessage) {
		servDesc = newServDesc;
		messageType = newMessageType;
		message = newMessage;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getMessage() {
		return message;
	}

	public String getServDesc() {
		return servDesc;
	}
}
