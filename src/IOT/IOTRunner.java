package IOT;

import IOT.IOTApplication.IOTApplicationInterface;
import IOT.IOTApplication.alarmclock.ACRestService;
import IOT.IOTApplication.alarmclock.AlarmClockService;
import IOT.IOTApplication.coffeemachine.CoffeeMachineService;
import IOT.IOTClient.IOTClient;
import IOT.IOTClient.UDPBroadcastService.UDPBroadcastService;
import IOT.IOTServer.IOTServer;
import IOT.IOTServer.IOTServerInterface;
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
 * This class serves as dependency manager. It instantiates all local instances
 * of client, server, application and UDP listener/broadcaster.
 * 
 * @author Mai
 * @version Milestone2
 */
@WebListener
public class IOTRunner implements ServletContextListener {


	/**
	 * A reference to the local instance of the specific application.
	 */
	private IOTApplicationInterface application = null;

	/**
	 * A reference to the local instance of UDPListener.
	 */
	private UDPListenerInterface udpListener = null;
	/**
	 * A reference to the local instance of UDPBroadcastService.
	 */
	private UDPBroadcastService udpBroadcastService = null;

	private Thread listenerThread = null;
	private Thread broadcasterThread = null;

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
	public IOTRunner() {
		System.out.println("IOTRunner: constructor");
    }

	/**
	 * Sets the context attributes for client, server and application.
	 * 
	 * @param servletContextEvent
	 *            Servlet-intern context.
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("IOTRunner: context initialized");

		SubscriberList subscriberList = new SubscriberList();
		IOTClient client = new IOTClient(subscriberList, null);

		// what application are we running? only tomcat can tell...
		switch (servletContextEvent.getServletContext().getInitParameter("application").toLowerCase()) {
			case "alarmclock": application = new AlarmClockService(client); break;
			case "coffeemachine": application = new CoffeeMachineService(client); break;
			default: throw new IllegalStateException("UNKNOWN APPLICATION");
		}

		client.setServiceDescription(application.getServiceDescription());
		IOTServerInterface server = new IOTServer(subscriberList,client,application);

		try {
			// start udp listener
			udpListener = new UDPListener(UDP_SERVICE_PORT, server);
			this.listenerThread = new Thread(udpListener);
			listenerThread.start();
			// start udp broadcast
			udpBroadcastService = new UDPBroadcastService(UDP_SERVICE_PORT, application.getServiceDescription());
			this.broadcasterThread = new Thread(udpBroadcastService);
			broadcasterThread.start();
		} catch(Exception e) {
			System.err.println("OOPS! UDP Service failed to start.");
			e.printStackTrace();
			System.exit(-1);
		}

		// dirty check if we need rest and soap - only alarmclock does that
		if (application instanceof AlarmClockService) {
			/* Start Alarm Clock REST service at localhost:9000 */
			JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
			ACRestService acRest = new ACRestService((AlarmClockService)application);
			sf.setServiceBean(acRest);
			sf.setAddress("http://0.0.0.0:" + REST_SERVICE_PORT + "/");
			sf.create();

        /* Start SOAP Alarm service */
			acSoapService = new ACSoapService((AlarmClockService)application);
			JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
			svrFactory.setAddress("http://localhost:" + SOAP_SERVICE_PORT + "/ACSoapService");
			svrFactory.setServiceBean(acSoapService);
			svrFactory.create();
		}

		servletContextEvent.getServletContext().setAttribute("application", application);
	}

	/**
	 * Terminates all UDP services.
	 * 
	 * @param servletContextEvent
	 *            Servlet-intern context.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			udpBroadcastService.terminate();
			udpListener.terminate();

			broadcasterThread.join();
			listenerThread.join();

		} catch (Exception e) {
			System.err.println("OOPS! UDP Service failed to stop.");
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println("IOTRunner: context destroyed");
		System.exit(0);
	}
}
