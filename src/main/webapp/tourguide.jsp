<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- Link The Foundation CSS -->
<link rel="stylesheet" href="css/foundation.css" type="text/css" />

<!-- Load Script -->
<script src="jQuery/jquery-1.11.2.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="jQuery/jquery.validate.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style type="text/css">
.fillterCSS {
  display: inline-block;
  -webkit-box-sizing: content-box;
  -moz-box-sizing: content-box;
  box-sizing: content-box;
  padding: 10px 20px;
  border: 1px solid #b7b7b7;
  -webkit-border-radius: 3px;
  border-radius: 3px;
  font: normal 31px/normal Tahoma, Geneva, sans-serif;
  color: rgba(0,142,198,1); 
  -o-text-overflow: clip;
  text-overflow: clip;
  background: rgba(252,252,252,1);
  -webkit-box-shadow: 2px 2px 2px 0 rgba(0,0,0,0.2) inset;
  box-shadow: 2px 2px 2px 0 rgba(0,0,0,0.2) inset;
  text-shadow: 1px 1px 0 rgba(255,255,255,0.66) ;
  -webkit-transition: all 200ms cubic-bezier(0.42, 0, 0.58, 1);
  -moz-transition: all 200ms cubic-bezier(0.42, 0, 0.58, 1);
  -o-transition: all 200ms cubic-bezier(0.42, 0, 0.58, 1);
  transition: all 200ms cubic-bezier(0.42, 0, 0.58, 1);
}
</style>

<!-- Search Buses -->
<script>
$(document).ready(function() {
	$("#busButton").click(function(e) {
		e.preventDefault();
		$.ajax({
			url : "busServlet",
			type : "GET",
			data : {date: "today"},
			datatype : "text", 
			success : success,
			error : error
		});
		function success(data, textStatus, jqXHR) {
			$("#resultAreaForBus").html(data);
			document.getElementById("GOButtonDiv").style.display = "block";
		}
		function error(jqXHR, textStatus, errorThrown) {
			$("#resultAreaForBus").html("No Good! Please Contact Benjamin T_T");
			alert("error!");
		}
	});
});
</script>

<!-- Use Bus Search Pax -->
<script>
$(document).ready(function() {
	
	$("#filter").hide();
	$("#filterClearButton").hide();
	
	$("#GOButton").click(function(e) {
		e.preventDefault();
		var busName = document.getElementById("busSelect").value;
		$.ajax({
			url : "paxServlet",
			type : "GET",
			data : {busName: busName},
			datatype : "text", 
			success : success,
			error : error
		});
		function success(data, textStatus, jqXHR) {
			$("#filter").show();
			$("#filterClearButton").show();
			$("#resultArea").html(data);
			document.getElementById("GOButton").innerHTML = "Refresh";
		}
		function error(jqXHR, textStatus, errorThrown) {
			$("#resultAreaForBus").html("No Good! Please Contact Benjamin T_T");
			alert("error!");
		}
	});
	
	$("#filter").keyup(function(){
	    _this = this;
	    // Show only matching TR, hide rest of them
	    $.each($("#paxList tbody").find("tr"), function() {
	        if($(this).text().toLowerCase().indexOf($(_this).val().toLowerCase()) == -1)
	           $(this).hide();
	        else
	             $(this).show();                
	    });
	}); 
});
</script>

<script type="text/javascript">
function updatePax(id,busNum,oldStatus){
	var busNum = busNum;
	var idNum = id;
	var oldStatus = oldStatus;
	var newStatus = 1;
	
	//Disable Updatepax Button
	$("#"+idNum+"Button").prop("disabled",true);
	
	var tempLinePaxNum = parseInt(document.getElementById(idNum+"paxnum").innerHTML);
	var tempArrivedPaxNum = parseInt(document.getElementById(busNum+"arrived").innerHTML);
	var tempNotPaxNum = parseInt(document.getElementById(busNum+"not").innerHTML);
	
	var arrF = document.getElementById(id+"Button");
	if(arrF.value == "true"){
		newStatus = 0;
	}
	
	$.ajax({
		url : "checkpaxfinalServlet",
		type : "GET",
		data : {id: idNum, statusf: newStatus},
		datatype : "text", 
		success : success,
		error : error
	});
	function success(data, textStatus, jqXHR){
		var row = document.getElementById(idNum);
		console.log("This is color:"+row.style.background);
		
		//If Red Or Yellow
		if(row.style.background == "rgb(255, 178, 178)" || row.style.background == "rgb(255, 255, 102)"){
			row.style.background = "#B2FFCE";
			arrF.value = "true";
			arrF.innerHTML = "Arrived";
			//Change Pax Number
			document.getElementById(busNum+"arrived").innerHTML = tempArrivedPaxNum + tempLinePaxNum;
			document.getElementById(busNum+"not").innerHTML = tempNotPaxNum - tempLinePaxNum;
		}
		//If Green
		else{
			console.log("IN GREEN");
			if(oldStatus == true){ //Back To Yellow
				row.style.background = "rgb(255, 255, 102)";
				arrF.innerHTML = "Helper Checked";
			}
			else{ //Back To Red
				row.style.background = "#FFB2B2";
				arrF.innerHTML = "Not Arrived";
			}
			arrF.value = "false";
			//Change Pax Number
			document.getElementById(busNum+"arrived").innerHTML = tempArrivedPaxNum - tempLinePaxNum;
			document.getElementById(busNum+"not").innerHTML = tempNotPaxNum + tempLinePaxNum;
		}
		//Make Button Avaliable Again
		$("#"+idNum+"Button").prop("disabled",false);
	}
	function error(jqXHR, textStatus, errorThrown){
		$("#resultArea").html("No Good! Please contact Benjamin");
		alert("Error!");
	}
}
</script>

<script>
//Clear Filter Input Beta
function clearFilter(){
	$("#filter").val("");
	$.each($("#paxList tbody").find("tr"), function() {
		$(this).show();
	});
}

</script>



<title>For Tour Leader</title>

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



<!-- Body Starts Here -->
<body>

<div class="row">
	<div class="row">
		<a href="http://gci4-lltravel.rhcloud.com/checkin.jsp">CHECK-IN</a>
		<a href="http://gci4-lltravel.rhcloud.com/logout.jsp" style="margin: 0 0 0 600px;">Quit</a>
	</div>
	<div class="large-9 columns">
		<h3>
			Easy Check In <small>Beta Edition :D</small>
		</h3>

		<form name="pickupForm" id="pickupForm">
			<table>		
				<tr>
					<td><button name="busButton" id="busButton">Find Buses</button></td>
				</tr>
			</table>
		</form>
		
		
		<br>
		<!-- Servlet Response Here -->
		<div id="resultAreaForBus"></div>
		
		
		<br>
		
		
		<div id="GOButtonDiv" style="display: none;">
			<button name="GOButton" id="GOButton">GO</button>
		</div>
		
		<div>
			<input class="fillterCSS" name="filter" id="filter" placeholder="filter...">
			<button id="filterClearButton" onclick="clearFilter()">Clear Filter</button>
		</div>
		<!-- Serlvet Response Here -->
		<div id="resultArea"></div>
		
	</div>
</div>

</body>
</html>