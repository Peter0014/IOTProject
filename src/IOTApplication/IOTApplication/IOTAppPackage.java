package IOTApplication.IOTApplication;

/**
 * Class used to package messages between applications ready to be sent.
 * Contains owner type of this application and a message for all the
 * subscribers.
 *
 */
public class IOTAppPackage {
	/** Owner of this message */
	final AppType type;
	/** Message for the receivers. */
	final String message;

	public IOTAppPackage(AppType newType, String newMessage) {
		type = newType;
		message = newMessage;
	}

	public AppType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
