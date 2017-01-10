package IOT.IOTServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;

import IOT.IOTApplication.IOTApplicationInterface;
import IOT.IOTApplication.IOTMessage;
import IOT.IOTClient.IOTClient;
import IOT.SubscriberList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 * This class translates HTTP queries into logical IOT requests.
 * @version Milestone1
 * @see IOTServer
 */
@WebServlet("/iot")
public class HTTPServerConnector extends HttpServlet {

    /**
     * A reference to the server-instance on this device.
     */
    private IOTServerInterface server;

    private IOTApplicationInterface application;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("Initializing IoT adapter... ");
        this.application = (IOTApplicationInterface)config.getServletContext().getAttribute("application");
    }

    public HTTPServerConnector() {
        SubscriberList subscribers = new SubscriberList();
        IOTClient client = new IOTClient(subscribers,application.getServiceDescription());
        this.server = new IOTServer(subscribers, client, application);
    }

    /**
     * This method handles all the POST-requests sent to the server.
     * Post-requests are interpreted as IOTMessages from this node's subscriptions.
     * @param request Servlet-intern request object.
     * @param response Servlet-intern response object.
     * @throws ServletException In case of internal servlet failure.
     * @throws IOException In case of internal I/O failure.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HTTPServerconnector: new IOTMessage!");

        try {
            // Read from request
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            Gson gson = new GsonBuilder().create();
            IOTMessage message = gson.fromJson(buffer.toString(), IOTMessage.class);
            server.incomingNotificationHandler(message);

            response.setStatus(HttpServletResponse.SC_OK); 

        } catch (Exception e) {
            System.err.println("Caught something while parsing: " + e.getMessage());
        }
    }

    /**
     * This method handles all the Get Requests sent to the server. Get requests are sent in case of a new subscription.
     * The Ip Address and Port of the request is passed over to the server.
     * @param request Servlet-intern request object.
     * @param response Servlet-intern response object.
     * @throws ServletException In case of internal servlet failure.
     * @throws IOException In case of internal I/O failure.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HTTPServerConnector: new subscription request from " + request.getLocalAddr() + "!");
        server.subscribeRequestHandler(request.getLocalAddr(),request.getServerPort()); // send it via servlet /iot (8080 is tomcat port)
        response.setStatus(HttpServletResponse.SC_OK);
        // opt: redirect to landing
    }
}
