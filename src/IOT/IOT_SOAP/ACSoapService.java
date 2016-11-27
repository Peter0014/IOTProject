package IOT.IOT_SOAP;

import IOT.IOTApplication.alarmclock.AlarmClockService;
import IOT.IOTClient.IOTClient;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Implementation of the Soap Service Java SEI.
 * @author Mai
 * @version M2
 */
@WebService(endpointInterface = "IOT.IOT_SOAP.IACSoapService",
        targetNamespace = "http://localhost:8080/SoapAlarmClockService",
        serviceName = "SoapAlarmClockService")
public class ACSoapService implements IACSoapService {

    private AlarmClockService alarmClockService = null;

    public  ACSoapService () {}

    public ACSoapService (AlarmClockService alarmClockService) {
        this.alarmClockService = alarmClockService;
    }

    public void setAlarmClockService(AlarmClockService alarmClockService) {
        this.alarmClockService = alarmClockService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Long> getAlarms() {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");
        return alarmClockService.getAlarms();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Long, Boolean> getAlarm(@WebParam(name="msDate")String msDate) {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(msDate));

        return alarmClockService.getAlarm(cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int postAlarm(@WebParam(name="msDate") String msDate) {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(msDate));

        int errorSet = alarmClockService.setAlarm(cal);
        int errorStart = alarmClockService.startAlarm(cal);

        if ( (errorSet < 0) || (errorStart < 0)) return -1;
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delAlarm(@WebParam(name="msDate") String msDate) {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(msDate));

        return alarmClockService.cancelAlarm(cal);
    }

    // test runner
    public static void main(String[] args) {
        System.out.println("Starting server...");
        IACSoapService soapService = new ACSoapService(new AlarmClockService(new IOTClient(null,null)));
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setAddress("http://localhost:8080/ACSoapService");
        svrFactory.setServiceBean(soapService);
        svrFactory.create();
    }
}
