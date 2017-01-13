package IOT.IOTApplication.alarmclock;

import java.io.Serializable;
import java.util.TimerTask;

public class Alarm implements Serializable {
	private static final long serialVersionUID = -4202909320493134616L;
	private Long id;
	private Long time;
	private transient TimerTask task;
	
	public Alarm(Long newTime, TimerTask newTask) {
		time = newTime;
		task = newTask;
	}

	public Long getTime() {
		return time;
	}

	public TimerTask getTask() {
		return task;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public void setTask(TimerTask task) {
		this.task = task;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
