//This servlet is for helper check-in page (Uses Locations)
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class pickupServlet
 */
@WebServlet("/pickupServlet")
public class pickupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public pickupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//Connection Variables.
		Connection con = null;
		Statement sm = null;
		ResultSet rs = null;

		//Connect To Server And Create Statement
		String url = "jdbc:mysql://127.4.100.130:3306/gci4";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,"admin5pLIyrz","Emf4kZS4Mtdi");
			sm=con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		//Global Variables.
		String command = null;
		String pickLoc = request.getParameter("area"); //Get Pickup Location
		String cDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());  //Get Current Date
		String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date()); //Get Current Date In Different Format

		//Start Table
		String finalRes = "";
		finalRes += "<table id=\"paxList\" name=\"paxList\">";
		finalRes += "<thead>";
		finalRes += "<tr>";
		finalRes += "<th>"+date+"</th><th>BUS</th><th>GUIDE</th><th>GROUP#</th><th>CONF</th><th>PAX</th><th>PICK</th><th>PHONE</th>";
		finalRes += "</tr>";
		finalRes += "</thead>";
		finalRes += "<tbody>";

		if(pickLoc.equals("ALL")){
			String[] locs = {"CTT","FLU","BRK","JCC","EDI","PAR"};
			for(int i=0; i<locs.length; i++){
				command = String.format("SELECT * FROM gci4.paxinfo WHERE date='%s' AND pickloc='%s' ORDER BY bus", cDate, locs[i]);
				finalRes += QueryToServer(command,sm,rs);
			}
		}
		else{
			command = String.format("SELECT * FROM gci4.paxinfo WHERE date='%s' AND pickloc='%s' ORDER BY bus", cDate, pickLoc);
			finalRes += QueryToServer(command,sm,rs);
		}

		//End Table
		finalRes += "</tbody>";
		finalRes += "</table>";


		//Connection Close
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//Response.
	  	response.setContentType("text/plain; charset=GB18030");
	    response.setCharacterEncoding("GB18030");
	  	response.getOutputStream().write(finalRes.getBytes());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

	protected String QueryToServer(String command, Statement sm, ResultSet rs){
		//Local Variables
		String tempBus = "";
		int tempCount = 0;
		int tempArr = 0;
		int tempNot = 0;
		String finalRes = "";
		String direct = "";

		//Query To Database.
		try {
			rs = sm.executeQuery(command);
			while(rs.next()){
				//Get Direct Information
				if(rs.getBoolean("direct")){
					direct = "直发";
				}
				else{
					direct = "";
				}

				//if different buses..
				if(!rs.getString("bus").equals(tempBus)){
					if(tempCount != 0){
						finalRes += "<tr>";
						finalRes += "<td>Arrived:</td><td id=\""+tempBus+"arrived\">"+tempArr+"</td><td>Not:</td><td id=\""+tempBus+"not\">"+tempNot+"</td><td>Total:</td><td id=\""+tempBus+"total\">"+tempCount+"</td><td></td><td></td>";
						finalRes += "</tr>";
					}
					tempBus = rs.getString("bus");
					tempCount = 0; //Clear Count
					tempNot = 0;
					tempArr = 0;
				}
				if(rs.getBoolean("statusf") == false){ //If Not Final Checked
					if(rs.getBoolean("status") == false){ //Not Arrived
						finalRes += "<tr name=\""+rs.getInt("id")+"\" id=\""+rs.getInt("id")+"\" style=\"background: rgb(255, 178, 178);\">"; //RED
						finalRes += "<td><button onclick=\"updatePax('"+rs.getInt("id")+"','"+rs.getString("bus")+"')\" id=\""+rs.getInt("id")+"Button\" value=\""+rs.getBoolean("status")+"\">Not Arrived</button></td>";
						tempNot += Integer.parseInt(rs.getString("paxnum"));
					}
					else{ //Arrived
						finalRes += "<tr name=\""+rs.getInt("id")+"\" id=\""+rs.getInt("id")+"\" style=\"background: rgb(102, 255, 102);\">"; //GREEN
						finalRes += "<td><button onclick=\"updatePax('"+rs.getInt("id")+"','"+rs.getString("bus")+"')\" id=\""+rs.getInt("id")+"Button\" value=\""+rs.getBoolean("status")+"\">Arrived</button></td>";
						tempArr += Integer.parseInt(rs.getString("paxnum"));
					}
				}
				else{ //Final Checked
					finalRes += "<tr name=\""+rs.getInt("id")+"\" id=\""+rs.getInt("id")+"\" style=\"background: rgb(102, 255, 102);\">"; //GREEN
					finalRes += "<td><button onclick=\"updatePax('"+rs.getInt("id")+"','"+rs.getString("bus")+"')\" id=\""+rs.getInt("id")+"Button\" value=\""+rs.getBoolean("status")+"\" disabled>Guide Checked</button></td>";
					tempArr += Integer.parseInt(rs.getString("paxnum"));
				}

				//Add Count (Without Type Check Risk);
				tempCount += Integer.parseInt(rs.getString("paxnum"));

				finalRes += "<td>"+rs.getString("bus")+" "+direct+"</td>";
				finalRes += "<td>"+rs.getString("tourguide")+" "+rs.getString("tourphone")+"</td>";
				finalRes += "<td>"+rs.getString("groupnum")+"</td>";
				finalRes += "<td>"+rs.getString("conf")+"</td>";
				finalRes += "<td id=\""+rs.getInt("id")+"paxnum\">"+rs.getString("paxnum")+"</td>";
				finalRes += "<td>"+rs.getString("pickloc")+"</td>";
				finalRes += "<td>"+rs.getString("phone")+"</td>";
				finalRes += "</tr>";
				//if last line
				if(rs.isLast() && tempCount != 0){
					finalRes += "<tr>";
					finalRes += "<td>Arrived:</td><td id=\""+tempBus+"arrived\">"+tempArr+"</td><td>Not:</td><td id=\""+tempBus+"not\">"+tempNot+"</td><td>Total:</td><td id=\""+tempBus+"total\">"+tempCount+"</td><td></td><td></td>";
					finalRes += "</tr>";
					tempCount = 0; //Clear Count
					tempNot = 0;
					tempArr = 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return finalRes;
	}
}
