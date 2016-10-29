package IOTApplication.IOTApplication.alarmclock;

import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class AlarmClockConnector
 *
 */
@WebListener
public class AlarmClockRunner implements ServletContextListener {
	
	private AlarmClockService alarmClock;

    /**
     * Default constructor. 
     */
    public AlarmClockRunner() {
        alarmClock = new AlarmClockService(null);
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.HOUR, 1);
        alarmClock.setAlarm(cal);
        cal = Calendar.getInstance();
        cal.roll(Calendar.MONTH,6);
        alarmClock.setAlarm(cal);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	arg0.getServletContext().setAttribute("alarmclock", alarmClock);
    }
	
}
