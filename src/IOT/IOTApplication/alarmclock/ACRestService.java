package IOT.IOTApplication.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ACRestService {
	
	AlarmClockService acs = null;
	
	public ACRestService(AlarmClockService newAcs) {
		acs = newAcs;
	}
	
	@GET
    @Path("/getalarms")
    @Produces({"application/json"})
    @Consumes({"application/json","application/x-www-form-urlencoded"})
	public Response getAlarms() {
		System.out.println("Received REST enquiry for all alarms");
		
		ArrayList<Long> alarms = acs.getAlarms();

        Gson gson = new GsonBuilder().create();
        String alarmsJson = gson.toJson(alarms);
        
		if(alarmsJson == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }else{
            return Response.ok(alarmsJson).build();
        }
	}
	
}
