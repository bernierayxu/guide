<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>

</head>
<body>
	<form id="loginForm" action="loginServlet" method="POST">
		<table>
			<tr>
				<th>Please Enter Password:</th>
			</tr>
			<tr>
				<td><input type="password" name="password" id="password"></td>
			</tr>
			<tr>
				<td><input type="submit" id="loginButton" value="Login"></input></td>
			</tr>
			<tr>
				  <%Object logStatus = session.getAttribute("logStatus");
  					if(logStatus!=null){
  						if(logStatus.toString().equals("0")){%>
  							<td style="color: #ef2d2d;">Login Failed, Please Check Your Password.</td>
  						<%}
  						if(logStatus.toString().equals("1")){
  							response.sendRedirect("http://gci4-lltravel.rhcloud.com/main.jsp");
  						}
  						if(logStatus.toString().equals("2")){%>
  							<td style="color: #ef2d2d;">Please Re-Login</td>
  						<%}
  					}
  				%>
			</tr>
			<tr>
				<td><a href="http://gci4-lltravel.rhcloud.com/adminlogin.jsp">Administrator Login Page</a></td>
			</tr>
		</table>
	</form>
</body>
</html>