package IOT.IOT_SOAP;

import IOT.IOTApplication.alarmclock.AlarmClockService;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Emily on 11/27/2016.
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

    @Override
    public ArrayList<Long> getAlarms() {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");
        return alarmClockService.getAlarms();
    }

    @Override
    public HashMap<Long, String> getAlarm(@WebParam(name="msDate")String msDate) {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(msDate));

        return alarmClockService.getAlarm(cal);
    }

    @Override
    public int postAlarm(@WebParam(name="msDate") String msDate) {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");
        return 0;
    }

    @Override
    public int delAlarm(@WebParam(name="msDate") String msDate) {
        if (alarmClockService == null) throw new IllegalStateException("AlarmClockService not initialized!");
        return 0;
    }
}
