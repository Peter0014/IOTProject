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

/**
 * AlarmClockRESTService - This class offers an API to the user to get all
 * available alarms or one specific alarm, create a new one and delete a
 * previously created one through a REST Client. The service is available at
 * '/acrestservice/' and the corresponding path following.
 * 
 * @author Peter Klosowski a1403029
 * @version Milestone2
 *
 */
@Path("/acrestservice/")
public class ACRestService {

	/** AlarmClockService instance */
	private AlarmClockService acs = null;

	/**
	 * Constructor connecting the Alarm Clock Service to the REST Service to
	 * get, remove and update the latest alarms.
	 * 
	 * @param newAcs
	 *            AlarmClockService instance
	 */
	public ACRestService(AlarmClockService newAcs) {
		acs = newAcs;
	}

	/**
	 * For GET there are two APIs supported - /getalarms returns an HTTP
	 * response with a JSON string that includes an array filled with created
	 * alarms as dates in milliseconds through a GET request.
	 * 
	 * @return HTTP Response - 404 if ACS wasn't started yet, else JSON
	 *         rendition of available alarms.
	 */
	@GET
	@Path("/getalarms")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response getAllElements() {
		System.out.println("Received REST enquiry for all alarms");

		/* Get all alarms */
		ArrayList<Long> alarms = acs.getAlarms();

		/* Return 404 if ACS wasn't initialized */
		if (alarms == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		/* Build JSON and return 200 */
		Gson gson = new GsonBuilder().create();
		String alarmsJson = gson.toJson(alarms);
		return Response.ok(alarmsJson).build();
	}

	/**
	 * For GET there are two APIs supported - /getalarm/{msdate} returns an HTTP
	 * response with a JSON string that includes an array filled with this alarm
	 * as date in milliseconds and if it has been started through a GET request.
	 * 
	 * @param msDate
	 *            Date in milliseconds
	 * @return HTTP Response - 400 if passed date isn't a number, 404 if alarm
	 *         wasn't found, else 200 with JSON rendition of available alarms.
	 */
	@GET
	@Path("/getalarm/{msdate}")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response getElement(@PathParam("msdate") String msDate) {
		System.out.println("Received REST enquiry for alarm " + msDate);

		/* Date in milliseconds as long */
		long lDate;

		/* Return 400 if it's NaN */
		try {
			lDate = Long.parseLong(msDate);
		} catch (NumberFormatException nfEx) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDate);

		/* Get alarm and see if it exists, return 404 if not */
		HashMap<Long, Boolean> alarm = acs.getAlarm(cal);
		if (alarm == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		/* Build JSON and return 200 */
		Gson gson = new GsonBuilder().create();
		String alarmsJson = gson.toJson(alarm);
		return Response.ok(alarmsJson).build();
	}

	/**
	 * POST method available at /postalarm to create and start a new alarm. The
	 * parameter 'msdate' needs to be passed in a form and represents a
	 * millisecond rendition of the date that will be added.
	 * 
	 * @param msDate
	 *            Date in milliseconds
	 * @return 400 if alarm was already added, in the past or couldnt be
	 *         started, else 201 with alarm location in header.
	 */
	@POST
	@Path("/postalarm")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response postElement(@FormParam("msdate") String msDate) {
		System.out.println("Received REST enquiry POST: " + msDate);

		/* Date in milliseconds as long */
		long lDate;

		/* Return 400 if it's NaN */
		try {
			lDate = Long.parseLong(msDate);
		} catch (NumberFormatException nfEx) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDate);

		/* Add and start alarm */
		int errorSet = acs.setAlarm(cal);
		int errorStart = acs.startAlarm(cal);

		/* Return 400 if error happened, else 201 with location of alarm */
		if (errorStart < 0 || errorSet < 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			try {
				return Response.created(new URI("acrestservice/getalarm/" + lDate)).build();
			} catch (URISyntaxException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
	}

	/**
	 * DELETE method to remove a previously added alarm. The parameter 'msdate'
	 * needs to be passed in a form and represents a millisecond rendition of the
	 * date that will be removed.
	 * 
	 * @param msDate
	 *            Date in milliseconds
	 * @return HTTP Response - 400 if passed date isn't a number, 404 if alarm
	 *         wasn't found, else 200.
	 */
	@DELETE
	@Path("/delalarm")
	@Produces({ "application/json" })
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public Response delElement(@FormParam("msdate") String msDate) {
		System.out.println("Received REST enquiry DELETE: " + msDate);

		/* Date in milliseconds as long */
		long lDate;

		/* Return 400 if it's NaN */
		try {
			lDate = Long.parseLong(msDate);
		} catch (NumberFormatException nfEx) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lDate);

		/* Remove alarm */
		int errorDel = acs.remAlarm(cal);

		/* Return 404 if alarm wasn't found or couldn't be cancelled */
		if (errorDel < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok().build(); /* Return 200 if successful */
		}
	}

}
