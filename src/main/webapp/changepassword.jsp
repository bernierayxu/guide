<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- Link The Foundation CSS -->
<link rel="stylesheet" href="css/foundation.css" type="text/css" />

<!-- Load Script -->
<script src="jQuery/jquery-1.11.2.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="jQuery/jquery.validate.js"></script>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change User Password</title>
</head>

<style type="text/css">
body {
	background: #fafafa url(http://jackrugile.com/images/misc/noise-diagonal.png);
	color: #444;
	font: 100%/30px 'Helvetica Neue', helvetica, arial, sans-serif;
	text-shadow: 0 1px 0 #fff;
}

strong {
	font-weight: bold; 
}

em {
	font-style: italic; 
}

table {
	background: #f5f5f5;
	border-collapse: separate;
	box-shadow: inset 0 1px 0 #fff;
	font-size: 12px;
	line-height: 24px;
	margin: 30px auto;
	text-align: left;
	width: 800px;
}	

th {
	background: url(http://jackrugile.com/images/misc/noise-diagonal.png), linear-gradient(#777, #444);
	border-left: 1px solid #555;
	border-right: 1px solid #777;
	border-top: 1px solid #555;
	border-bottom: 1px solid #333;
	box-shadow: inset 0 1px 0 #999;
	color: #fff;
  font-weight: bold;
	padding: 10px 15px;
	position: relative;
	text-shadow: 0 1px 0 #000;	
}

th:after {
	background: linear-gradient(rgba(255,255,255,0), rgba(255,255,255,.08));
	content: '';
	display: block;
	height: 25%;
	left: 0;
	margin: 1px 0 0 0;
	position: absolute;
	top: 25%;
	width: 100%;
}

th:first-child {
	border-left: 1px solid #777;	
	box-shadow: inset 1px 1px 0 #999;
}

th:last-child {
	box-shadow: inset -1px 1px 0 #999;
}

td {
	border-right: 1px solid #fff;
	border-left: 1px solid #e8e8e8;
	border-top: 1px solid #fff;
	border-bottom: 1px solid #e8e8e8;
	padding: 10px 15px;
	position: relative;
	transition: all 300ms;
}

td:first-child {
	box-shadow: inset 1px 0 0 #fff;
}	

td:last-child {
	border-right: 1px solid #e8e8e8;
	box-shadow: inset -1px 0 0 #fff;
}	

tr {
	background: url(http://jackrugile.com/images/misc/noise-diagonal.png);	
}

tr:nth-child(odd) td {
	background: #f1f1f1 url(http://jackrugile.com/images/misc/noise-diagonal.png);	
}

tr:last-of-type td {
	box-shadow: inset 0 -1px 0 #fff; 
}

tr:last-of-type td:first-child {
	box-shadow: inset 1px -1px 0 #fff;
}	

tr:last-of-type td:last-child {
	box-shadow: inset -1px -1px 0 #fff;
}	

tbody:hover td {
	color: transparent;
	text-shadow: 0 0 3px #aaa;
}

tbody:hover tr:hover td {
	color: #444;
	text-shadow: 0 1px 0 #fff;
}
</style>

<script>
function submit(){
			var cpassword = document.getElementById("cpassword").value;
			var npassword = document.getElementById("npassword2").value;
			var badColor = "#ff6666";
			var cMsg = document.getElementById("cpasswordmsg");
			$.ajax({
				url : "changeServlet",
				type : "POST",
				data : {cpassword: cpassword, npassword: npassword},
				datatype : "text", 
				success : success,
				error : error
			});
			function success(data, textStatus, jqXHR) {
				if(data == "1"){
					alert("User password has been changed successfully!");
					window.location.replace("http://gci4-lltravel.rhcloud.com/login.jsp");
				}
				else if(data == "0"){
					cMsg.style.color = badColor;
					cMsg.innerHTML = "The admin password is not correct.";
					alert("The admin password is not correct.");
				}
				else{
					error();
				}
			}
			function error(jqXHR, textStatus, errorThrown) {
				alert("Something went wrong...");
			}
}

function checkSame(){
    var pass1 = document.getElementById('npassword1');
    var pass2 = document.getElementById('npassword2');
    var message = document.getElementById("npassword2msg");
    var subButton = document.getElementById("submitButton");
    var goodColor = "#66cc66";
    var badColor = "#ff6666";
    if(pass1.value == pass2.value){
        pass2.style.backgroundColor = goodColor;
        message.style.color = goodColor;
        message.innerHTML = "Good!"
        subButton.disabled = false;
    }
    else{
        pass2.style.backgroundColor = badColor;
        message.style.color = badColor;
        message.innerHTML = "Do Not Match!"
        subButton.disabled = true;
    }
} 
</script>

<body>

<table>
  <thead>
    <tr>
      <th colspan="3">Change User Password</th>
    </tr>
  </thead>
  <tbody>
  	<tr>
  		<td><strong>Current Admin Password:</strong></td>
  		<td><input type="password" name="cpassword" id="cpassword"></td>
  		<td><span id="cpasswordmsg"></span></td>
  	</tr>
  	<tr>
  		<td><strong>New User Password:</strong></td>
  		<td><input type="password" name="npassword1" id="npassword1"></td>
  		<td><span id="npassword1msg"></span></td>
  	</tr>
  	<tr>
  		<td><strong>Repeat New User Password:</strong></td>
  		<td><input type="password" name="npassword2" id="npassword2" onkeyup="checkSame();"></td>
  		<td><span id="npassword2msg"></span></td>
  	</tr>
  	<tr>
  		<td colspan="3"><button id="submitButton" onclick="submit()" disabled>Submit</button></td>
  	</tr>
  </tbody>
</table>

</body>
</html>