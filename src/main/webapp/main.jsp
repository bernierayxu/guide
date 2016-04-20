<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Welcome to OpenShift</title>
  
<style type="text/css">
.cButton {
	display: inline-block;
	-webkit-box-sizing: content-box;
	-moz-box-sizing: content-box;
	box-sizing: content-box;
	width: 120px;
	height: 60px;
	cursor: pointer;
	padding: 10px 20px;
	border: 1px solid #018dc4;
	-webkit-border-radius: 3px;
	border-radius: 3px;
	font: normal 16px/normal "Lucida Sans Unicode", "Lucida Grande",
		sans-serif;
	color: rgba(255, 255, 255, 0.9);
	text-align: center;
	-o-text-overflow: clip;
	text-overflow: clip;
	background: #0199d9;
	-webkit-box-shadow: 2px 2px 2px 0 rgba(0, 0, 0, 0.2);
	box-shadow: 2px 2px 2px 0 rgba(0, 0, 0, 0.2);
	text-shadow: -1px -1px 0 rgba(15, 73, 168, 0.66);
	-webkit-transition: all 200ms cubic-bezier(0.035, 0.79, 0.58, 1) 10ms;
	-moz-transition: all 200ms cubic-bezier(0.035, 0.79, 0.58, 1) 10ms;
	-o-transition: all 200ms cubic-bezier(0.035, 0.79, 0.58, 1) 10ms;
	transition: all 200ms cubic-bezier(0.035, 0.79, 0.58, 1) 10ms;
}

.cButton:hover {
	background: #202dbc;
}

.cButton:active {
	background: #0199d9;
}
</style>

</head>

<%
	//Detect Login Status And Setup Login Header And Setup userName
	Object logStatus = session.getAttribute("logStatus");
	if (logStatus != null) {
		if (!logStatus.toString().equals("1")) {
			session.setAttribute("logStatus", 2);
			response.sendRedirect("http://gci4-lltravel.rhcloud.com");
		}
	}
	else{
		response.sendRedirect("http://gci4-lltravel.rhcloud.com");
	}
%>

<body>
	<table>
		<tr>
			<td><a href="http://gci4-lltravel.rhcloud.com/checkin.jsp"><button class="cButton" value="Check-in">Check In Helper</button></a></td>
		</tr>
		<tr>
			<td><a href="http://gci4-lltravel.rhcloud.com/tourguide.jsp"><button class="cButton" value="Tour Leader">Tour Leader</button></a></td>
		</tr>
	</table>
</body>
</html>
