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
.topButtonCSS {
  display: inline-block;
  -webkit-box-sizing: content-box;
  -moz-box-sizing: content-box;
  box-sizing: content-box;
  width: 71px;
  height: 51px;
  position: fixed;
  cursor: crosshair;
  top: 83%;
  left: 85%;
  padding: auto;
  border: 1px solid #018dc4;
  -webkit-border-radius: 3px;
  border-radius: 3px;
  font: normal 12px/normal "Times New Roman", Times, serif;
  color: rgba(255,255,255,0.9);
  text-align: center;
  -o-text-overflow: clip;
  text-overflow: clip;
  background: #0199d9;
  -webkit-box-shadow: 2px 2px 2px 0 rgba(0,0,0,0.2) ;
  box-shadow: 2px 2px 2px 0 rgba(0,0,0,0.2) ;
  text-shadow: -1px -1px 0 rgba(15,73,168,0.66) ;
  -webkit-transition: all 300ms cubic-bezier(0.42, 0, 0.58, 1);
  -moz-transition: all 300ms cubic-bezier(0.42, 0, 0.58, 1);
  -o-transition: all 300ms cubic-bezier(0.42, 0, 0.58, 1);
  transition: all 300ms cubic-bezier(0.42, 0, 0.58, 1);
  z-index: 1;
}
</style>

<script>
$(document).ready(function() {
	
	$("#filter").hide();
	$("#filterClearButton").hide();
	
	//Search Button
	$("#searchButton").click(function(e) {
		e.preventDefault();
		$("#searchButton").prop("disabled",true);
		
		//Query Get Pax Info
		$.ajax({
			url : "pickupServlet",
			type : "GET",
			data : $("#pickupForm").serialize(),
			datatype : "text", 
			success : success,
			error : error
		});
		function success(data, textStatus, jqXHR) {
			$("#searchButton").prop("disabled", false);
			document.getElementById("searchButton").innerHTML = "Refresh";
			$("#filter").show();
			$("#filterClearButton").show();
			$("#resultArea").html(data);
		}
		function error(jqXHR, textStatus, errorThrown) {
			$("#searchButton").prop("disabled", false);
			$("#resultArea").html(data+errorThrown);
			alert("error!");
		}
	});
	
	//Bus Button
	$("#busButton").click(function(e){
		e.preventDefault();
		$("#busButton").prop("disabled",true);
		
		//Query Get Bus Info
		$.ajax({
			url : "helperBusServlet",
			type : "GET",
			data : $("#pickupForm").serialize(),
			datatype : "text", 
			success : success,
			error : error
		});
		function success(data, textStatus, jqXHR) {
			$("#busButton").prop("disabled", false);
			$("#busInfoArea").html(data);
		}
		function error(jqXHR, textStatus, errorThrown) {
			$("#busButton").prop("disabled", false);
			$("#busInfoArea").html(data+errorThrown);
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

function changeColor(rawThis){
	var rowNum = rawThis.rowIndex;
	var table = document.getElementById("paxList");
	var row = table.rows[rowNum];
	if(row.style.background == "rgb(255, 178, 178)"){
		row.style.background = "#B2FFCE";	
	}
	else{
		row.style.background = "#FFB2B2";
	}
	
}
</script>

<script type="text/javascript">
//Upadte Pax Status
function updatePax(id,busNum){ 
	var busNum = busNum;
	var idNum = id;
	var sNum = 1;	
	
	//Disable PaxUpdate Button
	$("#"+idNum+"Button").prop("disabled",true);
	
	//Get Pax Number Information
	var tempLinePaxNum = parseInt(document.getElementById(idNum+"paxnum").innerHTML);
	var tempArrivedPaxNum = parseInt(document.getElementById(busNum+"arrived").innerHTML);
	var tempNotPaxNum = parseInt(document.getElementById(busNum+"not").innerHTML);
	
	
	var arr = document.getElementById(id+"Button");
	if(arr.value == "true"){ //Change To Not Arrived....
		sNum = 0;
	}
	$.ajax({
		url : "checkpaxServlet",
		type : "GET",
		data : {id: idNum, status: sNum},
		datatype : "text", 
		success : success,
		error : error
	});
	function success(data, textStatus, jqXHR) {
		var row = document.getElementById(idNum);
		if(row.style.background == "rgb(255, 178, 178)"){ //RED Change To Arrirved.....
			row.style.background = "#B2FFCE";
			arr.value = "true";
			arr.innerHTML = "Arrived";
			//Change PaxLine Number
			document.getElementById(busNum+"arrived").innerHTML = tempArrivedPaxNum + tempLinePaxNum;
			document.getElementById(busNum+"not").innerHTML = tempNotPaxNum - tempLinePaxNum;
		}
		else{ //GREEN Change To Not Arrived...
			row.style.background = "#FFB2B2";
			arr.value = "false";
			arr.innerHTML = "Not Arrived";
			document.getElementById(busNum+"arrived").innerHTML = tempArrivedPaxNum - tempLinePaxNum;
			document.getElementById(busNum+"not").innerHTML = tempNotPaxNum + tempLinePaxNum;
		}
		//Make Button Avaliable Again
		$("#"+idNum+"Button").prop("disabled",false);
	}
	function error(jqXHR, textStatus, errorThrown) {
		$("#resultArea").html("No Good! Please Contact Benjamin T_T");
		alert("error!");
	}
}

//Update Shuttle Bus Infomation
function updateBus(id){
	var idNum = id;
	var sNum = 1;
	
	//Disable busButton
	$("#bus"+idNum).prop("disable",true);
	
	//Check Bus Button Value
	var busButton = document.getElementById("bus"+id);
	if(busButton.value == "true"){
		sNum = 0;
	}
	
	$.ajax({
		url : "checkbusServlet",
		type : "GET",
		data : {id: idNum, status: sNum},
		datatype : "text", 
		success : success,
		error : error
	});
	function success(data, textStatus, jqXHR) {
		if(busButton.style.background == "rgb(255, 178, 178)"){ //RED Change To Arrived (Green)
			busButton.style.background = "#B2FFCE"; //Change Color
			busButton.value = "true"; //Change Value
			busButton.innerHTML = busButton.innerHTML.split("<br>")[0] + "<br>Arrived"; //Change innerHTML		
		}
		else{ //GREEN Change To Not Arrived
			busButton.style.background = "#FFB2B2";
			busButton.value = "false";
			busButton.innerHTML = busButton.innerHTML.split("<br>")[0] + "<br>Not Arrived"; //Change innerHTML
		}
		//Make Button Avaliable Again
		$("#bus"+idNum).prop("disabled",false);
	}
	function error(jqXHR, textStatus, errorThrown) {
		$("#resultArea").html("No Good! Please Contact Benjamin T_T");
		alert("error!");
	}
	
}

</script>

<script>
//Clear Filter Beta
function clearFilter(){
	$("#filter").val("");
	$.each($("#paxList tbody").find("tr"), function() {
		$(this).show();
	});
}

</script>

<title>For Check In</title>
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
	<button onclick="location.href='#top';" class="topButtonCSS">TOP</button>
	<div class="row">
		<a href="http://gci4-lltravel.rhcloud.com/tourguide.jsp">TOUR GUIDE</a>
		<a href="http://gci4-lltravel.rhcloud.com/logout.jsp" style="margin: 0 0 0 600px;">Quit</a>
	</div>
	<div class="row">
		<div class="large-9 columns">
			<h3>
				Easy Check In <small>Beta Edition :D</small>
			</h3>
			<table>
				<tr>
					<td colspan="2">
						<form name="pickupForm" id="pickupForm">
							<select id="area" name="area">
								<option value="ALL">All Locations</option>
								<option value="CTT">China Town</option>
								<option value="FLU">Flushing</option>
								<option value="BRK">Brooklyn</option>
								<option value="JCC">Jersey City</option>
								<option value="EDI">East Brunswick</option> 
								<option value="PAR">Parsippany</option>
							</select>
						</form>
					</td>
					<td rowspan="2">
						<div>
							<table>
								<tr>
									<td id="busInfoArea">
										<!-- Place to hold shuttle bus information -->
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td><button name="searchButton" id="searchButton">Search</button></td>
					<td><button name="busButton" id="busButton">Search Shuttle</button></td>
				</tr>
			</table>
			
			<br>
			
			<div>
				<input class="fillterCSS" name="filter" id="filter" placeholder="filter...">
				<button id="filterClearButton" onclick="clearFilter()">Clear Filter</button>
			</div>
			<!-- Servlet Response Here -->
			<div id="resultArea"></div>

		</div>
	</div>
</body>
</html>