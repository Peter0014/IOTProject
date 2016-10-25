package IOTApplication.IOTApplication;

/**
 * Class used to package messages between applications ready to be sent.
 * Contains type of this application and a message for all the subscribers.
 *
 */
public class IOTMessage {
	/** Message type set by application. */
	final String messageType;
	/** Message for the receivers. */
	final String message;

	public IOTMessage(String newMessageType, String newMessage) {
		messageType = newMessageType;
		message = newMessage;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getMessage() {
		return message;
	}
}
