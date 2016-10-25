package IOTApplication.IOTApplication;

import java.net.InetAddress;

/**
 * Class used to package messages between applications ready to be sent.
 * Contains type of this application and a message for all the subscribers.
 *
 */
public class IOTAppPackage {
	/** Message type set by application. */
	final String messageType;
	/** Message for the receivers. */
	final String message;

	public IOTAppPackage(String newMessageType, String newMessage) {
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
