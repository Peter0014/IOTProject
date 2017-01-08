package IOT.IOTApplication.coffeemachine;

import IOT.DeviceDetection;
import IOT.IOTApplication.IOTApplicationInterface;
import IOT.IOTApplication.IOTFilePersistenceManager;
import IOT.IOTApplication.IOTMessage;
import IOT.IOTClient.IOTClientInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The CoffeeMachine class is mainly responsible for managing the
 * executions times on which to switch the relay for the coffee machine on.
 * @author Mai
 * @version Milestone3
 */
public class CoffeeMachineService implements IOTApplicationInterface {

    /** Scheduler controlling the coffee times. */
    private Timer timer = new Timer();
    /** List of all times at which to turn the coffee machine on and make coffee. */
    private ArrayList<Long> coffeeTimes;

    /** Connection to the client to send out notifications to subscriber */
    private IOTClientInterface client;

    /** Service description string for the coffee machine. **/
    final private String servDesc = "CMS";

    // ToDo remove this if we don't need persistence
    /** Filename for persistent file storage */
    final private String persFilename = servDesc + ".txt";
    /** Persistence File Manager to store times at which to make coffee. */
    private IOTFilePersistenceManager<Long> fileManager;

    /**
     * A list of service descriptions the coffee machine is interested in.
     */
    private final String[] compatDevice = {"ACS", "CMS"};

    /**
     * In case the coffee machine is not available.
     */
    public static final int ERROR_CMS_DEVICE_UNAVAILABLE = -1;
    /**
     * In case of an illegal time having been passed (eg. a duplicate time, or a time in the past).
     */
    public static final int ERROR_CMS_INVALID_TIME = -2;

    /**
     * Sets up the coffee machine service.
     * @param client Connection to any clients interested in our services.
     */
    public CoffeeMachineService(IOTClientInterface client) {
        this.client = client;
        // if we need persistence, it'd go here!
        // ToDo find out if we need persistence
        coffeeTimes = new ArrayList<>(); // or read from file
    }

    // ToDo Coffee Machine business logic method -- makeCoffee? storeAlarm? whatever we need.

    /**
     * Adds a new time (in long == epoch) on which to make coffe to the internal list and schedules it for execution.
     * @return ERROR_CMS_INVALID_TIME if the alarm is illegal (in the past, double, etc), 0 else.
     */
    public int addCoffeeTime(long time) {
        // first check if alarm is valid
        if (coffeeTimes.contains(time)) return ERROR_CMS_INVALID_TIME; // already exists
        if ((time - System.currentTimeMillis()) < 0) return ERROR_CMS_INVALID_TIME; // in past

        coffeeTimes.add(time);

        TimerTask coffeeTask = new TimerTask() {
            @Override
            public void run() {
                makeCoffee();
                coffeeTimes.remove(time);
            }
        };

        timer.schedule(coffeeTask, new Date(time));

        return 0;
    }

    /**
     * Triggers the coffee machine to make coffee and notifies subscribers of this event.
     * @return ERROR_CMS_DEVICE_UNAVAILABLE if there is no coffee machine, 0 else
     */
    public synchronized int makeCoffee() {
        // if client exists, send notification to subscribers
        if (client != null) {
            client.notifySubscribers(
                    new IOTMessage(servDesc, "MakingCoffee", servDesc + " - Started making coffee."));
        }

        if (new DeviceDetection().isRasp()) {
            new CoffeeMaker().turnOn();
            return 0;
        } else {
            System.out.println("Error " + ERROR_CMS_DEVICE_UNAVAILABLE + ": No coffee machine available to make coffee :(");
            return ERROR_CMS_DEVICE_UNAVAILABLE;
        }

    }

    /**
     * Handles a received package and starts an event if triggered.
     *
     * @param message Message type and ID of the application with optional text
     */
    @Override
    public void handleIncomingNotification(IOTMessage message) {
        // react to alarmclock
        if (message.getServDesc().startsWith("ACS") && message.getMessageType().equals("AlarmPlaying")) {
            makeCoffee();
        }
        else {
            System.out.println("Got a message of type " + message.getMessageType() + ":");
            System.out.println(message.getMessage());
        }
    }

    /**
     * Returns the application's service description which will be broadcast
     * over the network via UDP.
     *
     * @return String containing a service description.
     */
    @Override
    public String getServiceDescription() {
        return this.servDesc;
    }

    /**
     * Returns if application is interested to subscribe.
     *
     * @param broadcast ID of broadcast from client
     * @return true if subscription is desirable
     */
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
