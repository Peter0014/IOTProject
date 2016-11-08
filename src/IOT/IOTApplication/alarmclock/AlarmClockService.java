package IOT.IOTApplication.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import IOT.DeviceDetection;
import IOT.IOTApplication.IOTApplicationInterface;
import IOT.IOTApplication.IOTMessage;
import IOT.IOTClient.IOTClientInterface;

/**
 * The AlarmClockService creates and saves alarms set by the user. A timer
 * starts to run when the alarm is activated and a loud tune will be played at
 * the corresponding time (through a Piezo element hooked to the Raspberry Pi).
 * An alarm can only be added and started once and is not allowed to be set in
 * the past. An error code will be returned if that happens.
 * 
 * @author Peter Klosowski (a1403029)
 * @version Milestone1
 *
 */
public class AlarmClockService implements IOTApplicationInterface {

	/** This is the timer that runs the alarms */
	private Timer timer = new Timer();
	/** Dates in milliseconds and set TimerTasks that count down to alarm. */
	private Map<Long, TimerTask> alarms;

	/** Connection to the client to send out notifications to subscriber */
	private IOTClientInterface client;

	/* TODO Generate an ID */
	/** Service type of this application */
	final private String servDesc = "ACS101";

	/**
	 * Devices that are able to subscribe to this service, ACS stands for
	 * AlarmClockService, CM for CoffeeMakerService, LBCS for
	 * LightBulbControlService
	 */
	private final String[] compatDevice = { "ACS", "CMS", "LBCS" };

	/** Alarm was already created, i. e. if you want to add the same date. */
	public static final int EC_ALARM_ALREADY_EXISTS = -1;
	/** Alarm wasn't created, i. e. if you want to start it. */
	public static final int EC_ALARM_DOESNT_EXIST = -2;
	/** Alarm was already started, i. e. if you want to start it again. */
	public static final int EC_ALARM_RUNNING = -3;
	/** Passed date is in the past. */
	public static final int EC_ALARM_IN_PAST = -4;
	/** Alarm wasn't cancelled, i. e. because it already happened. */
	public static final int EC_ALARM_NOT_CANCELLED = -5;

	/**
	 * Constructor initializes the alarms Map and connects the client
	 * @param newClient connection to the client
	 */
	public AlarmClockService(IOTClientInterface newClient) {
		client = newClient;
		/* Synchronized because it can be changed by different Threads. */
		alarms = Collections.synchronizedMap(new HashMap<Long, TimerTask>());
	}

	/**
	 * Sets an Alarm if it doesn't exist yet.
	 * 
	 * @param date
	 *            Date that will be added to the alarms.
	 * @return -1 if alarm already exists, else 0.
	 */
	public int setAlarm(Calendar date) {
		if (alarms.containsKey(date)) {
			return EC_ALARM_ALREADY_EXISTS;
		}
		/* Check if alarm is in the past */
		long dateInMs = date.getTimeInMillis();
		long ms = dateInMs - System.currentTimeMillis();
		if (ms < 0) {
			return EC_ALARM_IN_PAST;
		}

		alarms.put(date.getTimeInMillis(), null);
		return 0;
	}

	/**
	 * Getter for the Alarms.
	 * 
	 * @return List of alarms in milliseconds.
	 */
	public ArrayList<Long> getAlarms() {
		return new ArrayList<Long>(alarms.keySet());
	}

	/**
	 * Starts a Timer thread that will be triggered at the time passed in the
	 * call.
	 * 
	 * @param date
	 *            time, at which alarm will sound
	 * @return -2 if the time wasn't added yet, -3 if the alarm was already
	 *         started, -4 if passed date is in the past, else 0
	 */
	public int startAlarm(Calendar date) {
		final long dateInMs = date.getTimeInMillis();
		/* Calc when the alarm should play (in milliseconds) */
		long ms = dateInMs - System.currentTimeMillis();
		if (!alarms.containsKey(dateInMs)) {
			/* setAlarm(date); */
			return EC_ALARM_DOESNT_EXIST;
		}
		if (alarms.get(dateInMs) != null) {
			return EC_ALARM_RUNNING;
		}

		/* Create new TimerTask that runs in a new Thread */
		TimerTask alarmTask = new TimerTask() {
			@Override
			public void run() {

				if (client != null) {
					/* Send info to subscribers */
					client.notifySubscribers(
							new IOTMessage(servDesc, "AlarmPlaying", servDesc + " - Alarm is playing."));
				}

				if (new DeviceDetection().isRasp()) {
					/* Make the Piezo sound */
					PiezoPlayer player = new PiezoPlayer();
					player.playTune(0);
				} else {
					System.out.println("ALARM serving at: " + Calendar.getInstance().getTime());
				}

				/* Remove Task from alarms */
				alarms.remove(dateInMs);
			}
		};

		/* Add Task to alarms */
		alarms.put(dateInMs, alarmTask);
		/* Start task after 'ms' millis */
		timer.schedule(alarmTask, ms);

		return 0;
	}

	/**
	 * Cancel an already started alarm.
	 * 
	 * @param date
	 *            Time that will be canceled.
	 * @return -2 if the time wasn't added yet, -5 if it couldn't be cancelled,
	 *         else 0
	 */
	public int cancelAlarm(Calendar date) {
		long dateInMs = date.getTimeInMillis();

		if (!alarms.containsKey(dateInMs)) {
			return EC_ALARM_DOESNT_EXIST;
		}
		if (alarms.get(dateInMs) != null) {
			if (!alarms.get(dateInMs).cancel()) {
				/* Error occured while cancelling the alarm task */
				return EC_ALARM_NOT_CANCELLED;
			} else {
				/* Remove task from alarms */
				alarms.remove(dateInMs);
			}
		}

		return 0;
	}

	/**
	 * Remove an alarm completely from the application.
	 * 
	 * @param date
	 *            Time that will be removed.
	 * @return -2 if the time wasn't added yet, -5 if it couldn't be cancelled,
	 *         else 0
	 */
	public int remAlarm(Calendar date) {
		long dateInMs = date.getTimeInMillis();

		if (!alarms.containsKey(dateInMs)) {
			return EC_ALARM_DOESNT_EXIST;
		}

		boolean cancelled = false;
		if (alarms.get(dateInMs) != null) {
			cancelled = alarms.get(dateInMs).cancel();
		}
		if (cancelled) {
			alarms.remove(dateInMs);
		} else {
			return EC_ALARM_NOT_CANCELLED;
		}
		return 0;
	}

	/**
	 * Checks if an alarm is active.
	 * 
	 * @param date
	 *            Date, that will be checked
	 * @return true if alarm is turned on
	 */
	public boolean isActive(Calendar date) {
		return alarms.get(date.getTimeInMillis()) != null;
	}

	@Override
	/**
	 * Returns a String representation of all created Alarms.
	 */
	public String toString() {
		return "Set alarms: " + alarms.keySet().toString();

	}

	@Override
	public void handleIncomingNotification(IOTMessage message) {
		// TODO Auto-generated method stub
		System.out.println("Received Message of type " + message.getMessageType() + ".");
		System.out.println("  Content: " + message.getMessage());

	}

	@Override
	public String getServiceDescription() {
		return servDesc;
	}

	@Override
	public boolean isInterested(String broadcast) {
		for (String entry : compatDevice) {
			if (broadcast.startsWith(entry)) {
				return true;
			}
		}
		return false;
	}
}
