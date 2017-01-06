package IOT.IOTApplication.alarmclock;

import java.util.TimerTask;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Alarm {
	@Id private Long id;
	@Index private Long time;
	@Ignore private TimerTask task;
	
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
