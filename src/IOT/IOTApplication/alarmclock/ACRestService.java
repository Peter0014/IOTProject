package IOT.IOTApplication.alarmclock;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response getAllElements() {
		System.out.println("Received REST enquiry for all alarms");

		ArrayList<Long> alarms = acs.getAlarms();

		Gson gson = new GsonBuilder().create();
		String alarmsJson = gson.toJson(alarms);

		if (alarmsJson == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok(alarmsJson).build();
		}
	}

	@POST
	@Path("/postalarm")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response postElement(@FormParam("date") String date, @FormParam("time") String time) {
		System.out.println("Received REST enquiry POST: " + date + ", " + time);

		String[] sDate = date.split("-");
		String[] sTime = time.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(sDate[0]), Integer.parseInt(sDate[1]), Integer.parseInt(sDate[2]),
				Integer.parseInt(sTime[0]), Integer.parseInt(sTime[1]));

		int errorSet = acs.setAlarm(cal);
		int errorStart = acs.startAlarm(cal);

		if (errorSet < 0) {
			return Response.status(Response.Status.CONFLICT).build();
		} else if (errorStart < 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			try {
				return Response.created(new URI("")).build();
			} catch (URISyntaxException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
	}

	@DELETE
	@Path("/delalarm")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response delElement(@FormParam("date") String date, @FormParam("time") String time) {
		System.out.println("Received REST enquiry DELETE: " + date + ", " + time);

		String[] sDate = date.split("-");
		String[] sTime = time.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(sDate[0]), Integer.parseInt(sDate[1]), Integer.parseInt(sDate[2]),
				Integer.parseInt(sTime[0]), Integer.parseInt(sTime[1]));

		int errorDel = acs.remAlarm(cal);

		if (errorDel < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok().build();
		}
	}

}
