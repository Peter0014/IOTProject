package IOT.IOTApplication.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import IOT.IOTApplication.alarmclock.Alarm;

public interface AlarmClockDaoInterface {
	public ArrayList<Alarm> getAllAlarms();
	public Alarm getAlarm(Calendar date);
	public Alarm getAlarm(int id);
	
	public int updateAlarm(Calendar date, TimerTask task);
	public int updateAlarm(int id, TimerTask task);
	
	public int remAlarm(Calendar date);
	public int remAlarm(int id);
	
	public int addAlarm(Calendar date);
}
