package IOTApplication;

/**
 * Created by melaniebalaz on 24/10/2016.
 */
public class Subscriber {

    String ipAddress;
    int port;

    public Subscriber(String pIpAddress, int pPort){
        ipAddress = pIpAddress;
        port = pPort;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
