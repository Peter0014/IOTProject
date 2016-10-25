package IOTApplication.IOTServer;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * This class translates HTTP queries into logical IOT requests.
 *
 * @see IOTServer
 */
public class HTTPServerConnector extends HttpServlet {

    IOTServer server;

    HTTPServerConnector(IOTServer pServer){
        server = pServer;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //decode a json object -> hashmap

        //turn it into a message object

        //pass this object to the application

        response.setStatus(200);
    }

    /**
     * This method handles all the Get Requests sent to the server. Get requests are sent in case of a new subscription.
     * The Ip Address and Port of the request is passed over to the server.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ipAddress;
        int port;
        ipAddress = request.getLocalAddr(); //get IP of request
        port = request.getLocalPort(); //get port of request

        server.subscribeRequestHandler(ipAddress,port);

        response.setStatus(200);

    }
}
