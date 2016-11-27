package IOT;

import IOT.IOTApplication.alarmclock.ACRestService;
import IOT.IOTApplication.alarmclock.AlarmClockService;
import IOT.IOTClient.IOTClient;
import IOT.IOTClient.UDPBroadcastService.UDPBroadcastService;
import IOT.IOTServer.IOTServer;
import IOT.IOTServer.UDPListener.UDPListener;
import IOT.IOTServer.UDPListener.UDPListenerInterface;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import IOT.IOT_SOAP.ACSoapService;
import IOT.IOT_SOAP.IACSoapService;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * This class serves as dependency manager. It instantiates all local instances of client, server, application
 * and UDP listener/broadcaster.
 * @author Mai
 * @version Milestone1
 */
@WebListener
public class IOTRunner implements ServletContextListener {

    /**
     * A reference to the local instance of client.
     */
    private IOTClient client = null;
    /**
     * A reference to the local instance of the specific application (alarmclock).
     */
    private AlarmClockService alarmClock = null;
    /**
     * A reference to the local instance of server.
     */
    private IOTServer server = null;
    /**
     * A reference to the local instance of UDPListener.
     */
    private UDPListenerInterface udpListener = null;
    /**
     * A reference to the local instance of UDPBroadcastService.
     */
    private UDPBroadcastService udpBroadcastService = null;
    /**
     * A reference to all subscribers interested in the services of this node.
     */
    private SubscriberList subscriberList = null;

    /**
     * A reference to the SOAP service interface implementation.
     */
    private IACSoapService acSoapService = null;

    /**
     * The port on which UDP broadcasts are sent and received.
     * Trivia: This port was chosen due to its closeness to the Nintendo WiFi port. (The alarmclock theme is Super Mario. ^^ )
     */
    public static final int UDP_SERVICE_PORT = 29902;
    public static final int REST_SERVICE_PORT = 9000;
    public static final int SOAP_SERVICE_PORT = 9090;

    /**
     * Instantiates all references.
     */
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
        
		/* Start Alarm Clock REST service */
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		ACRestService acRest = new ACRestService(alarmClock);
		sf.setServiceBean(acRest);
		sf.setAddress("http://localhost:" + REST_SERVICE_PORT + "/");
		sf.create();

        /* Start SOAP Alarm service */
        acSoapService = new ACSoapService(alarmClock);
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setAddress("http://localhost:" + SOAP_SERVICE_PORT + "/ACSoapService");
        svrFactory.setServiceBean(acSoapService);
        svrFactory.create();
    }

    /**
     * Sets the context attributes for client, server and application.
     * @param servletContextEvent Servlet-intern context.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("IOTRunner: context initialized");
        servletContextEvent.getServletContext().setAttribute("client", client);
        servletContextEvent.getServletContext().setAttribute("server", server);
        servletContextEvent.getServletContext().setAttribute("application", alarmClock);
    }

    /**
     * Terminates all UDP services.
     * @param servletContextEvent Servlet-intern context.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("IOTRunner: context destroyed");
        udpListener.terminate();
        udpBroadcastService.terminate();
    }
}
