<%@ page import="IOT.ServletController.AlarmClock" %>
<%@ page import="IOT.AlarmClockServerInterface" %><%--
  Created by IntelliJ IDEA.
  User: Emily
  Date: 10/24/2016
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AlarmClock@IoT-home</title>
</head>

<body>
<div>
    <p>
        I am AlarmClock@IoT-home. <br>
        Local time: <%= request.getAttribute("localtime")%> <br>
        Welcome!
    </p>

    <form method="post" action="alarmClock">
        <input type="hidden"  name="action" value="addAlarm" /> <!-- identify the action -->
        <input placeholder="It will set the alarm to now. Whatever you type." type="text" id="addAlarmTime">
        <button type="submit">Go!</button>
    </form>

    <ul id="setalarms">
      <li> This would show all set alarms - if i had an idea what the hell i am doing here.</li>
    </ul>

</div>
</body>
</html>
