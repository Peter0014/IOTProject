package IOTApplication.IOTApplication.testcases;

import java.util.Calendar;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import IOTApplication.IOTApplication.alarmclock.AlarmClockService;

public class AlarmTest {

	/** Alarm clock service to create and play alarms. */
	static private AlarmClockService service = new AlarmClockService();
	/** Delay after which time the alarm should sound (from current time). */
	static private long alarmDelay = 5000;
	/** Time at which the alarm should sound. */
	static private long timeInMs = Calendar.getInstance().getTimeInMillis() + alarmDelay;

	@BeforeClass
	public static void setUpBeforeClass() {
		System.out.println("Test started at: " + Calendar.getInstance().getTime());
		System.out.println("Alarm has delay of " + alarmDelay / 1000 + " sec. \n");
		/* Create Calendar Object */
		Calendar c = Calendar.getInstance();
		/* Set it to current time + delay */
		c.setTimeInMillis(timeInMs);
		/* And set it */
		service.setAlarm(c);
	}

	@Test
	public void testStartAlarm() throws InterruptedException {
		/* Create Calender Object */
		Calendar c = Calendar.getInstance();
		/* And set it to the same time as before */
		c.setTimeInMillis(timeInMs);
		/* Start the Alarm */
		int errorCode = service.startAlarm(c);
		/* Returns -2 if c is different than before (even 1 ms) */
		/* -3 if it's called twice for the same c */
		/* -4 if the date already happened */
		assertTrue("Alarm wasn't created succesfully. ErrorCode: " + errorCode, errorCode == 0);
		/*
		 * Cancel Alarm before it sounds
		 * errorCode = service.cancelAlarm(c);
		 */

		/*
		 * Returns -2 if c is different than before (even 1 ms)
		 * Returns -5 if the alarm was already canceled (played already
		 * or called twice)
		 * assertTrue("Alarm wasn't cancelled. ErrorCode: " + errorCode,
		 * errorCode == 0);
		 */

		/*
		 * errorCode = service.remAlarm(c);
		 * assertTrue("Alarm wasn't deleted. ErrorCode: " + errorCode, errorCode
		 * == 0);
		 */
		Thread.sleep(alarmDelay + 5000);

	}

}
