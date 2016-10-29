package IOT.IOTApplication.alarmclock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AlarmClockConnector
 */
@WebServlet("/AlarmClock")
public class AlarmClockGUI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlarmClockGUI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {				
		
		BufferedWriter o = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
		o.write("<html>");o.newLine();
		o.write("<head>");o.newLine();
		o.write("<title>AlarmClock@IoT-home</title>");o.newLine();
		o.write("</head>");o.newLine();

		o.write("<body>");o.newLine();
		o.write("<div> " +
				"<p> " +
				"I am AlarmClock@IoT-home. <br/>" +
				"  Welcome! " +
				"</p> ");
		o.newLine();
		
		o.write("<form method=\"get\" action=\"AlarmClock\"> " +
				"<table> <tr> <td>date</td> <td>time</td> <td/> </tr> <tr> " +
				"<td> <input type=\"date\"  name=\"date\" id=\"date\"/> </td> " +  
				"<td> <input type=\"time\"  name=\"time\" id=\"time\"/> </td> " +
				"<td> <input type=\"submit\"  name=\"action\" value=\"addAlarm\" /> </td> " +
				"</tr> </table> </form> <br/>");
		o.newLine();
		
		
		AlarmClockService acs = (AlarmClockService)getServletContext().getAttribute("alarmclock");
		if (request.getParameter("action") != null) {
			// get request came from form submit:
			String date = request.getParameter("date");
			String time = request.getParameter("time");
			SimpleDateFormat ffmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			try {
				Calendar cal = Calendar.getInstance();				
				cal.setTime(ffmt.parse(date + " " + time));
				acs.setAlarm(cal);
				acs.startAlarm(cal);
			} catch (ParseException e) {
				o.write("<p>Error: " + e.getLocalizedMessage() + " </p>");				
			}
			
		}
		
		o.write("<h2>Alarms:</h2> <br/>");o.newLine();
				
		SimpleDateFormat fmt = new SimpleDateFormat("dd. MM. yyyy. -  HH : mm : ss");
		o.write("<ul>");o.newLine();
		for (Long atime : acs.getAlarms()) {
			o.write("<li> " + fmt.format(new Date(atime)) + " </li>");
			o.newLine();
		} 
		o.write("</ul>");o.newLine();
		
		o.write("</div> </body> </html>");
		
		o.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedWriter o = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
		o.write(request.getParameter("date"));o.newLine();
		o.write(request.getParameter("time"));o.newLine();
		o.flush();
	}

}
