package IOT.IOTApplication.alarmclock;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/acrestservice/")
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

	@GET
	@Path("/getalarm/{msdate}")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response getElement(@PathParam("msdate") String msDate) {
		System.out.println("Received REST enquiry for alarm " + msDate);

		long lDate;

		try {
			lDate = Long.parseLong(msDate);
		} catch (NumberFormatException nfEx) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDate);

		HashMap<Long, String> alarm = acs.getAlarm(cal);
		if (alarm == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		Gson gson = new GsonBuilder().create();
		String alarmsJson = gson.toJson(alarm);
		return Response.ok(alarmsJson).build();
	}

	@POST
	@Path("/postalarm")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response postElement(@FormParam("msdate") String msDate) {
		System.out.println("Received REST enquiry POST: " + msDate);

		long lDate;

		try {
			lDate = Long.parseLong(msDate);
		} catch (NumberFormatException nfEx) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDate);

		int errorSet = acs.setAlarm(cal);
		int errorStart = acs.startAlarm(cal);

		System.out.println(errorSet);
		System.out.println(errorStart);

		if (errorSet < 0) {
			return Response.status(Response.Status.CONFLICT).build();
		} else if (errorStart < 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			try {
				return Response.created(new URI("acrestservice/getalarm/" + lDate)).build();
			} catch (URISyntaxException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
	}

	@DELETE
	@Path("/delalarm")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response delElement(@FormParam("msdate") String msDate) {
		System.out.println("Received REST enquiry DELETE: " + msDate);

		long lDate;

		try {
			lDate = Long.parseLong(msDate);
		} catch (NumberFormatException nfEx) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDate);

		int errorDel = acs.remAlarm(cal);

		if (errorDel < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok().build();
		}
	}

}
