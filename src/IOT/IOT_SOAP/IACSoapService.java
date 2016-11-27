package IOT.IOT_SOAP;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Server and Client-shared simple Java SEI for AlarmClockService SOAP services.
 */
@WebService(name="SoapAlarmClockService",
        targetNamespace = "http://localhost:8080/SoapAlarmClockService")
@SOAPBinding(style= SOAPBinding.Style.RPC, use= SOAPBinding.Use.LITERAL)
public interface IACSoapService {

    /**
     * Returns all currently saved alarms.
     */
    @WebMethod(operationName = "getAlarms", exclude = false)
    @ResponseWrapper(className= "java.util.ArrayList<Long>")
    @WebResult(name="alarms")
    public ArrayList<Long> getAlarms();

    @WebMethod(operationName = "getAlarm", exclude = false)
    @RequestWrapper(className = "java.lang.String")
    @ResponseWrapper(className = "java.util.HashMap")
    @WebResult(name="alarm")
    public HashMap<Long, String> getAlarm(@WebParam(name="msDate", mode= WebParam.Mode.IN) String msDate);

    @WebMethod(operationName = "postAlarm", exclude = false)
    @RequestWrapper(className = "java.lang.String")
    @WebResult(name="alarmState")
    public int postAlarm(@WebParam(name="msDate", mode= WebParam.Mode.IN) String msDate);

    @WebMethod(operationName = "delAlarm", exclude = false)
    @RequestWrapper(className = "java.lang.String")
    @WebResult(name="alarmState")
    public int delAlarm(@WebParam(name="msDate", mode= WebParam.Mode.IN) String msDate);
}
