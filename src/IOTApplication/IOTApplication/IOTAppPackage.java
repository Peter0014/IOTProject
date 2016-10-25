package IOTApplication.IOTApplication;

import java.net.InetAddress;

/**
 * Class used to package messages between applications ready to be sent.
 * Contains owner IP-Address and type of this application and a message for all the
 * subscribers.
 *
 */
public class IOTAppPackage {
	/** Owners IP-Address. */
	final InetAddress address;
	/** Message type set by application. */
	final String messageType;
	/** Message for the receivers. */
	final String message;

	public IOTAppPackage(InetAddress newAddress, String newMessageType, String newMessage) {
		address = newAddress;
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
