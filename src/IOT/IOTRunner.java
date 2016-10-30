package IOT;

import IOT.IOTApplication.alarmclock.AlarmClockService;
import IOT.IOTClient.IOTClient;
import IOT.IOTClient.UDPBroadcastService.UDPBroadcastService;
import IOT.IOTServer.IOTServer;
import IOT.IOTServer.UDPListener.UDPListener;
import IOT.IOTServer.UDPListener.UDPListenerInterface;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Emily on 10/29/2016.
 */
@WebListener
public class IOTRunner implements ServletContextListener {

    private IOTClient client = null;
    private AlarmClockService alarmClock = null;
    private IOTServer server = null;

    private UDPListenerInterface udpListener = null;
    private UDPBroadcastService udpBroadcastService = null;

    private SubscriberList subscriberList = null;

    public static final int UDP_SERVICE_PORT = 29902;

    public IOTRunner () {
    	System.out.println("IOTRunner: constructor");
        subscriberList = new SubscriberList();
        client = new IOTClient(subscriberList, null);
        alarmClock = new AlarmClockService(client);
        client.setServiceDescription(alarmClock.getServiceDescription());
        server = new IOTServer(subscriberList,client,alarmClock);

        // start udp listener
        udpListener = new UDPListener(UDP_SERVICE_PORT, server); // or whatever port?
        Thread listenerThread = new Thread(udpListener);
        listenerThread.start();

        // start up
        udpBroadcastService = new UDPBroadcastService(UDP_SERVICE_PORT,alarmClock.getServiceDescription());
        Thread broadcasterThread = new Thread(udpBroadcastService);
        broadcasterThread.start();
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
        udpListener.terminate();
        udpBroadcastService.terminate();
    }
}
