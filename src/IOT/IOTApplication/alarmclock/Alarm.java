package IOT.IOTApplication.alarmclock;

import java.io.Serializable;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Alarm implements Serializable{
	
	private static final long serialVersionUID = 4399487403808161276L;
	
	private static AtomicInteger idCounter = new AtomicInteger();
	private int objectId;
	private Long time;
	private TimerTask task;
	
	public Alarm() {
		objectId = nextId();
	}
	
	public static int nextId() {
		return idCounter.incrementAndGet();
	}
	
	public void put(Long newTime, TimerTask newTask) {
		time = newTime;
		task = newTask;
	}
}
