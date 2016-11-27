package IOT.IOT_SOAP;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Server and Client-shared simple Java SEI for AlarmClockService SOAP services.
 * @author Mai
 * @version M2
 */
@WebService(name="SoapAlarmClockService",
        targetNamespace = "http://localhost:8080/SoapAlarmClockService")
@SOAPBinding(style= SOAPBinding.Style.RPC, use= SOAPBinding.Use.LITERAL)
public interface IACSoapService {

    /**
     * Queries the underlying AlarmClockService for all alarms.
     * @return A list containing all currently active alarms (in Milliseconds).
     */
    @WebMethod(operationName = "getAlarms", exclude = false)
    @ResponseWrapper(className= "java.util.ArrayList<Long>")
    @WebResult(name="alarms")
    public ArrayList<Long> getAlarms();

    /**
     * Queries the underlying AlarmClockService for a certain alarm.
     * @param msDate The desired alarm in milliseconds.
     * @return 0 on success, -1 else.
     */
    @WebMethod(operationName = "getAlarm", exclude = false)
    @RequestWrapper(className = "java.lang.String")
    @ResponseWrapper(className = "java.util.HashMap")
    @WebResult(name="alarm")
    @XmlJavaTypeAdapter(LongStringHashmapAdapter.class)
    public HashMap<Long, String> getAlarm(@WebParam(name="msDate", mode= WebParam.Mode.IN) String msDate);

    /**
     * Invokes the underlying AlarmClockService to add an alarm.
     * @param msDate The alarm to be added (in milliseconds).
     * @return 0 on success, -1 else.
     */
    @WebMethod(operationName = "postAlarm", exclude = false)
    @RequestWrapper(className = "java.lang.String")
    @WebResult(name="alarmState")
    public int postAlarm(@WebParam(name="msDate", mode= WebParam.Mode.IN) String msDate);

    /**
     * Invokes the underlying AlarmClockService to remove an alarm.
     * @param msDate The alarm to be deleted (in milliseconds).
     * @return 0 on success, -1 else.
     */
    @WebMethod(operationName = "delAlarm", exclude = false)
    @RequestWrapper(className = "java.lang.String")
    @WebResult(name="alarmState")
    public int delAlarm(@WebParam(name="msDate", mode= WebParam.Mode.IN) String msDate);
}
