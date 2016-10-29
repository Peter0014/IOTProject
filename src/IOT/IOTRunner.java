package IOT;

import IOT.IOTApplication.alarmclock.AlarmClockService;
import IOT.IOTClient.IOTClient;
import IOT.IOTServer.IOTServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Emily on 10/29/2016.
 */
public class IOTRunner implements ServletContextListener {

    private IOTClient client = null;
    private AlarmClockService alarmClock = null;
    private IOTServer server = null;

    SubscriberList subscriberList = null;

    public IOTRunner () {
        subscriberList = new SubscriberList();
        client = new IOTClient(subscriberList, null);
        alarmClock = new AlarmClockService(client);
        client.setServiceDescription(alarmClock.getServiceDescription());
        server = new IOTServer(subscriberList,client,alarmClock);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("IOTRunner: context initialized");
        servletContextEvent.getServletContext().setAttribute("client", client);
        servletContextEvent.getServletContext().setAttribute("server", server);
        servletContextEvent.getServletContext().setAttribute("application", alarmClock);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("IOTRunner: context destroyed");
    }
}
