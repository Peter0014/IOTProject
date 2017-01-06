package IOT.IOTApplication.testcases;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import IOT.IOTApplication.alarmclock.Alarm;
import IOT.IOTApplication.dao.AlarmClockDaoImpl;
import IOT.IOTApplication.dao.AlarmClockDaoInterface;

public class DaoTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	/** Alarm clock service to create and play alarms. */
	static private AlarmClockDaoInterface daoImpl;
	/** Delay after which time the alarm should sound (from current time). */
	static private long alarmDelay = 5000;
	/** Time at which the alarm should sound. */
	static private long timeInMs;

	private void setUp() {
		helper.setUp();

		daoImpl = new AlarmClockDaoImpl();

		timeInMs = Calendar.getInstance().getTimeInMillis() + alarmDelay;

		System.out.println("Test started at: " + Calendar.getInstance().getTime());
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testAddAlarms() {
		setUp();

		/* Create Calender Object */
		Calendar c = Calendar.getInstance();

		final int numOfAlarms = 10;

		for (int i = 0; i < numOfAlarms; i++) {
			/* And set it to the same time as before */
			c.setTimeInMillis(timeInMs + 5000 * i);

			int errorCode = daoImpl.addAlarm(c);

			/* Returns -2 if c is different than before (even 1 ms) */
			/* -3 if it's called twice for the same c */
			/* -4 if the date already happened */
			assertTrue("Alarm wasn't created succesfully. ErrorCode: " + errorCode, errorCode == 0);
		}

		ArrayList<Alarm> allAlarms = daoImpl.getAllAlarms();

		assertTrue("ArrayList size is not correct: " + allAlarms.size() + " =/= " + numOfAlarms,
				allAlarms.size() == numOfAlarms);

		/* Print empty Line */
		System.out.println();

	}

	@Test
	public void testRemAlarm() {
		setUp();
		testAddAlarms();
		/* Create Calender Object */
		Calendar c = Calendar.getInstance();
		/* And set it to the same time as before */
		c.setTimeInMillis(timeInMs);
		final int numOfAlarms = 5;

		for (int i = 0; i < numOfAlarms; i++) {
			/* And set it to the same time as before */
			c.setTimeInMillis(timeInMs + 5000 * i);

			/* Start the Alarm */
			int errorCode = daoImpl.remAlarm(c);

			/* Returns -2 if c is different than before (even 1 ms) */
			/* -3 if it's called twice for the same c */
			/* -4 if the date already happened */
			assertTrue("Alarm wasn't removed succesfully. ErrorCode: " + errorCode, errorCode == 0);
		}

		ArrayList<Alarm> allAlarms = daoImpl.getAllAlarms();

		assertTrue("ArrayList size is not correct: " + allAlarms.size() + " =/= " + numOfAlarms,
				allAlarms.size() == numOfAlarms);

		/* Print empty Line */
		System.out.println();
	}

	@Test
	public void testUpdAlarm() {
		setUp();

		/* Create Calender Object */
		Calendar c = Calendar.getInstance();
		/* And set it to the same time as before */
		c.setTimeInMillis(timeInMs);

		final int numOfAlarms = 10;

		for (int i = 0; i < numOfAlarms; i++) {
			/* And set it to the same time as before */
			c.setTimeInMillis(timeInMs + 5000 * i);

			int errorCode = daoImpl.addAlarm(c);
			/* Returns -2 if c is different than before (even 1 ms) */
			/* -3 if it's called twice for the same c */
			/* -4 if the date already happened */
			assertTrue("Alarm wasn't created succesfully. ErrorCode: " + errorCode, errorCode == 0);
			
			errorCode = daoImpl.updateAlarm(i + 1, createTimer());
			Alarm alarm = daoImpl.getAlarm(i + 1);
			assertTrue("Alarm wasn't updated succesfully. ErrorCode: " + errorCode,
					errorCode == 0 && alarm.getTask() != null);
		}
	}

	private TimerTask createTimer() {
		return new TimerTask() {
			@Override
			public void run() {
				
			}
		};
	}

}
