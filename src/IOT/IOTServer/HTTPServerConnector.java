package IOT.IOTServer;

import IOT.IOTApplication.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;

import IOT.IOTServer.UDPListener.UDPListener;
import IOT.IOTServer.UDPListener.UDPListenerInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 * This class translates HTTP queries into logical IOT requests.
 *
 * @see IOTServer
 */
public class HTTPServerConnector extends HttpServlet {

    private IOTServerInterface server;

    public HTTPServerConnector(IOTServerInterface pServer) {
        server = pServer;
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
