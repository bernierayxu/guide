<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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


<title>Upload The Excel File</title>

<!-- Query To Server -->
<script>
$(document).ready(function() {
	$("#uploadButton").click(function(e) {
		e.preventDefault(); 
		$("#uploadButton").prop("disabled", true);
		var formData = new FormData(document.getElementById("uploadForm"));
		$.ajax({
			url : "fileUploadServlet",
			type : "POST",
			data : formData,
			cache: false,
			processData: false,
			contentType: false,
			success : success,
			error : error
		});
		function success(data, textStatus, jqXHR) {
			alert("Success! You Can Close This Page Now.");
			$("#resultArea").html(data);
		}
		function error(jqXHR, textStatus, errorThrown) {
			$("#resultArea").html("jqXHR:"+jqXHR+" textStatus:"+textStatus+" errorThrown:"+errorThrown);
			$("#uploadButton").prop("disabled", false);
			alert("error!");
		}
	});
});
</script>

<script type="text/javascript">

function changeColor(rawThis){
	var rowNum = rawThis.rowIndex;
	var table = document.getElementById("paxTable");
	var row = table.rows[rowNum];
	if(row.style.background == "rgb(255, 178, 178)"){
		row.style.background = "#B2FFCE";	
	}
	else{
		row.style.background = "#FFB2B2";
	}
	
}

</script>




</head>


<%
	//Detect Login Status And Setup Login Header And Setup userName
	Object logStatus = session.getAttribute("adminLogStatus");
	if (logStatus != null) {
		if (!logStatus.toString().equals("1")) {
			session.setAttribute("logStatus", 2);
			response.sendRedirect("http://gci4-lltravel.rhcloud.com/adminlogin.jsp");
		}
	}
	else{
		response.sendRedirect("http://gci4-lltravel.rhcloud.com/adminlogin.jsp");
	}
%>



<body>
	<div class="row">
		<a href="http://gci4-lltravel.rhcloud.com/main.jsp">HOME</a>
		<a href="http://gci4-lltravel.rhcloud.com/adminlogout.jsp" style="margin: 0 0 0 600px;">Quit</a>
	</div>
	<div class="row">
		<div class="large-3 columns">
			<h1>
			</h1>
		</div>
	</div>

	<div class="row">


		<div class="large-9 push-3 columns">
			<h3>
				Easy Check In <small>Beta Edition :D</small>
			</h3>
			
			<form name="uploadForm" id="uploadForm">
				<table>
					<tr>
						<td><input type="file" name="file" id="file" accept=".xls,.xlsx"></td>
					</tr>
				</table>
			</form>
			<table>
				<tr>
					<td><button name="uploadButton" id="uploadButton" type="button" >Upload It!</button></td>
				</tr>
			</table>
			
			<br>
			
			<!-- Servlet Response Here -->
			<div id="resultArea">
			</div>
			
		</div>


		<div class="large-3 pull-9 columns">
			<ul class="side-nav">
				<li><a href="upload.jsp">Easy Check In</a></li>
			</ul>
		</div>
	</div>

	<footer class="row">
	<div class="large-12 columns" style="width: 1920px;">
		<hr />
		<div class="row">
			<div class="large-6 columns">
				<p>Copyright no one at all. Go to town.</p>
			</div>
			<div class="large-6 columns">
				<ul class="inline-list right">
					<li><a href="#">Section 1</a></li>
					<li><a href="#">Section 2</a></li>
					<li><a href="#">Section 3</a></li>
					<li><a href="#">Section 4</a></li>
				</ul>
			</div>
		</div>

	</div>
	</footer>
</body>
</html>