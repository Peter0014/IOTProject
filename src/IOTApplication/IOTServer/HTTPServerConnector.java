package IOTApplication.IOTServer;

import IOTApplication.IOTApplication.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.BufferedReader;

import IOTApplication.IOTServer.UDPListener.UDPListener;
import IOTApplication.IOTServer.UDPListener.UDPListenerInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 * This class translates HTTP queries into logical IOT requests.
 *
 * @see IOTServer
 */
public class HTTPServerConnector extends HttpServlet {

    private IOTServerInterface server;
    private UDPListenerInterface udpListener = null;

    public HTTPServerConnector(IOTServerInterface pServer) {
        server = pServer;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        //TODO
        // start UDP listener
        // if we had a database, initialize that here too

        udpListener = new UDPListener(1000, server); // or whatever port?
        Thread listenerThread = new Thread(udpListener);
        listenerThread.start();
    }

    @Override
    public void destroy() {
        super.destroy();
        //TODO
        // kill UDP listener - I don't know if this is the proper way to do it
        udpListener.terminate();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //TODO

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
            //
        }

        //get post request body
        //decode a json object -> hashmap
        //turn it into a message object

        //pass this object to the application
    }

    /**
     * This method handles all the Get Requests sent to the server. Get requests are sent in case of a new subscription.
     * The Ip Address and Port of the request is passed over to the server.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ipAddress;
        int port;
        ipAddress = request.getLocalAddr(); //get IP of request
        port = request.getLocalPort(); //get port of request

        server.subscribeRequestHandler(ipAddress,port);

        response.setStatus(HttpServletResponse.SC_OK);

    }
}
