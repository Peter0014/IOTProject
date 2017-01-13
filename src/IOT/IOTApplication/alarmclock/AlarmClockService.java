package IOT.IOTApplication.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.annotation.XmlRootElement;

import IOT.DeviceDetection;
import IOT.IOTApplication.IOTApplicationInterface;
import IOT.IOTApplication.IOTMessage;
import IOT.IOTApplication.dao.IOTFilePersistenceManager;
import IOT.IOTApplication.dao.IOTPersistenceManagerInterface;
import IOT.IOTClient.IOTClientInterface;

/**
 * The AlarmClockService creates and saves alarms set by the user. A timer
 * starts to run when the alarm is activated and a loud tune will be played at
 * the corresponding time (through a Piezo element hooked to the Raspberry Pi).
 * An alarm can only be added and started once and is not allowed to be set in
 * the past. An error code will be returned if that happens.
 * 
 * @author Peter Klosowski (a1403029)
 * @version Milestone2
 *
 */
@XmlRootElement(name = "AlarmService")
public class AlarmClockService implements IOTApplicationInterface {

	/** This is the timer that runs the alarms */
	private Timer timer = new Timer();
	/** Dates in milliseconds and set TimerTasks that count down to alarm. */
	// private Map<Long, TimerTask> alarms;
	private List<Alarm> alarms;

	/** Connection to the client to send out notifications to subscriber */
	private IOTClientInterface client;

	/* TODO Generate an ID */
	/** Service type of this application */
	final private String servDesc = "ACS101";
	/** Filename for persistent file storage */
	final private String persFileName = servDesc;
	/** Persistence File Manager to save Alarms */
	private IOTPersistenceManagerInterface<Alarm> fileManager;

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
	 * 
	 * @param newClient
	 *            connection to the client
	 */
	public AlarmClockService(IOTClientInterface newClient) {
		client = newClient;
		fileManager = new IOTFilePersistenceManager<Alarm>(persFileName);
		/* Synchronized because it can be changed by different Threads. */
		alarms = Collections.synchronizedList(fileManager.findAll());
	}

	/**
	 * Sets an Alarm if it doesn't exist yet.
	 * 
	 * @param date
	 *            Date that will be added to the alarms.
	 * @return -1 if alarm already exists, -4 if the date is in the past, else
	 *         0.
	 */
	public int setAlarm(Calendar date) {
		for (Alarm entry : alarms) {
			if (entry.getTime() == date.getTimeInMillis()) {
				return EC_ALARM_ALREADY_EXISTS;
			}
		}
		/* Check if alarm is in the past */
		long msDate = date.getTimeInMillis();
		long ms = msDate - System.currentTimeMillis();
		if (ms < 0) {
			return EC_ALARM_IN_PAST;
		}

		Alarm alarm = new Alarm(msDate, null);
		alarms.add(alarm);
		fileManager.add(alarm);
		return 0;
	}

	private int containsKey(long msDate) {
		for (int i = 0; i < alarms.size(); i++) {
			if (alarms.get(i).getTime() == msDate) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Getter for the Alarms.
	 * 
	 * @return List of alarms in milliseconds.
	 */
	public ArrayList<Long> getAlarms() {
		ArrayList<Long> allAlarmsInMs = new ArrayList<Long>();
		for (Alarm entry : alarms) {
			allAlarmsInMs.add(entry.getTime());
		}
		return allAlarmsInMs;
	}

	/**
	 * Getter for a specific alarm.
	 * 
	 * @param date
	 *            Calendar date that will be searched if added
	 * @return Map of one date with date in miliseconds and if it has been
	 *         started
	 */
	public HashMap<Long, Boolean> getAlarm(Calendar date) {
		long msDate = date.getTimeInMillis();
		int alarmIndex = containsKey(msDate);
		if (alarmIndex >= 0) {
			HashMap<Long, Boolean> singleAlarm = new HashMap<Long, Boolean>();
			singleAlarm.put(msDate, alarms.get(alarmIndex).getTask() == null);
			return singleAlarm;
		}
		return null;
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
		final long msDate = date.getTimeInMillis();
		/* Calc when the alarm should play (in milliseconds) */
		long ms = msDate - System.currentTimeMillis();
		int alarmIndex = containsKey(msDate);
		if (alarmIndex < 0) {
			setAlarm(date);
			if ((alarmIndex = containsKey(msDate)) < 0)
				;
			return EC_ALARM_DOESNT_EXIST;
		}
		if (alarms.get(alarmIndex).getTask() != null) {
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
				} else
					System.out.println("CLIENT IS NULL!");

				if (new DeviceDetection().isRasp()) {
					/* Make the Piezo sound */
					PiezoPlayer player = new PiezoPlayer();
					player.playTune(0);
				} else {
					System.out.println("ALARM serving at: " + Calendar.getInstance().getTime());
				}

				/* Remove Task from alarms */
				int alarmIndex = containsKey(msDate);
				fileManager.delete(alarms.get(alarmIndex));
				alarms.remove(alarmIndex);
			}
		};

		/* Remove old Alarm and add new one with Task */
		fileManager.delete(alarms.get(alarmIndex));
		alarms.remove(alarmIndex);
		Alarm alarm = new Alarm(date.getTimeInMillis(), alarmTask);
		alarms.add(alarm);
		fileManager.add(alarm);

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
		long msDate = date.getTimeInMillis();
		int alarmIndex = containsKey(msDate);

		if (alarmIndex < 0) {
			return EC_ALARM_DOESNT_EXIST;
		}
		if (alarms.get(alarmIndex).getTask() != null) {
			if (!alarms.get(alarmIndex).getTask().cancel()) {
				/* Error occured while cancelling the alarm task */
				return EC_ALARM_NOT_CANCELLED;
			} else {
				/* Remove entry from alarms */
				fileManager.delete(alarms.get(alarmIndex));
				alarms.remove(alarmIndex);
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
		long msDate = date.getTimeInMillis();
		int alarmIndex = containsKey(msDate);

		if (alarmIndex < 0) {
			return EC_ALARM_DOESNT_EXIST;
		}

		boolean cancelled = false;
		if (alarms.get(alarmIndex).getTask() != null) {
			cancelled = alarms.get(alarmIndex).getTask().cancel();
		}
		if (cancelled || alarms.get(alarmIndex).getTask() == null) {
			/* Remove entry from alarms */
			fileManager.delete(alarms.get(alarmIndex));
			alarms.remove(alarmIndex);
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
		return alarms.get(containsKey(date.getTimeInMillis())).getTask() != null;
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
