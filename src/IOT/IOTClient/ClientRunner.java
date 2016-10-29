package IOT.IOTClient;

import IOT.SubscriberList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Emily on 10/29/2016.
 */
public class ClientRunner implements ServletContextListener {

    private IOTClient client = null;

    public ClientRunner () {
        this.client = new IOTClient(new SubscriberList(), null);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("client", client);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
