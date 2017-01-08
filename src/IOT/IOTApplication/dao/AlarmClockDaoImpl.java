package IOT.IOTApplication.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import com.googlecode.objectify.ObjectifyService;

import IOT.IOTApplication.alarmclock.Alarm;

public class AlarmClockDaoImpl implements AlarmClockDaoInterface {

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
	
	public AlarmClockDaoImpl() {
		ObjectifyService.register(Alarm.class);
		ObjectifyService.begin();
	}

	@Override
	public ArrayList<Alarm> getAllAlarms() {
		return new ArrayList<Alarm>(ofy().load().type(Alarm.class).list());
	}

	@Override
	public Alarm getAlarm(Calendar date) {
		return ofy().load().type(Alarm.class).filter("time", date.getTimeInMillis()).first().now();
	}

	@Override
	public Alarm getAlarm(int id) {
		return ofy().load().type(Alarm.class).id(id).now();
	}

	@Override
	public int updateAlarm(Calendar date, TimerTask task) {
		Alarm alarm = ofy().load().type(Alarm.class).filter("time", date.getTimeInMillis()).first().now();
		alarm.setTask(task);
		ofy().save().entity(alarm).now();
		return 0;
	}

	@Override
	public int updateAlarm(int id, TimerTask task) {
		Alarm alarm = ofy().load().type(Alarm.class).id(id).now();
		alarm.setTask(task);
		ofy().save().entity(alarm).now();
		return 0;
	}

	@Override
	public int remAlarm(Calendar date) {
		Alarm alarm = ofy().load().type(Alarm.class).filter("time", date.getTimeInMillis()).first().now();
		ofy().delete().entity(alarm).now();
		return 0;
	}

	@Override
	public int remAlarm(int id) {
		ofy().delete().type(Alarm.class).id(id).now();
		return 0;
	}

	@Override
	public int addAlarm(Calendar date) {
		ArrayList<Alarm> allAlarms = getAllAlarms();
		for (Alarm entry : allAlarms) {
			if (entry.getTime() == date.getTimeInMillis()) {
				return EC_ALARM_ALREADY_EXISTS;
			}
		}
		
		long dateInMs = date.getTimeInMillis();
		long ms = dateInMs - System.currentTimeMillis();
		if (ms < 0) {
			return EC_ALARM_IN_PAST;
		}
		
		Alarm alarm = new Alarm(date.getTimeInMillis(), null);
		ofy().save().entity(alarm).now();
		return 0;
	}

}
