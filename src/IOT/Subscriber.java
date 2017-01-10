package IOT;

import java.io.Serializable;

/**
 * A generic class mainly serving as container for subscriber-information.
 * @author Melanie
 * @version Milestone1
 */
public class Subscriber implements Serializable {

    /**
     * The IP-Address of the node the interested application lies at.
     */
    final private String ipAddress;
    /**
     * The port of the node the the interested application lies at.
     */
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
