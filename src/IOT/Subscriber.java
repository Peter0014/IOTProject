package IOT;

/**
 * Created by melaniebalaz on 24/10/2016.
 */
public class Subscriber {

    final private String ipAddress;
    final private int port;

    public Subscriber(String pIpAddress, int pPort){
        ipAddress = pIpAddress;
        port = pPort;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }


}
