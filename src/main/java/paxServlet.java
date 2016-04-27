//This servlet is for guide chec-in page (Uses Buses)
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
 * Servlet implementation class paxServlet
 */
@WebServlet("/paxServlet")
public class paxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public paxServlet() {
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

		//Global Variables
		String cDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //Get Current Date
		String busName = request.getParameter("busName");
		String command = String.format("SELECT * FROM gci4.paxinfo WHERE date='%s' AND bus='%s'", cDate, busName);
		String tourGuide = "";
		String tourPhone = "";
		String tour = "";
		String direct = "";
		boolean stat = false;
		boolean statf = false;
		String topRes = "";
		String botRes = "";
		int tempCount = 0;
		int tempArr = 0;
		int tempNot = 0;
		int idNum = 0;


		topRes += "<table id=\"paxList\">";
		//Query For the pax information.
		try {
			rs = sm.executeQuery(command);
			while(rs.next()){
				idNum = rs.getInt("id");
				tourGuide = rs.getString("tourguide"); //Get TourGuide
				tourPhone = rs.getString("tourphone"); //Get TourPhone
				tour = rs.getString("groupcode");
				stat = rs.getBoolean("status");
				statf = rs.getBoolean("statusf");
				if(rs.getBoolean("direct")){direct = "Direct";} //Check if it's direct
				if(statf == false){ //Not Final
					if(stat == false){ // Not Check in
						botRes += "<tr id=\""+idNum+"\" style=\"background: rgb(255, 178, 178);\">"; //RED Not Arrived
						botRes += "<td><button onclick=\"updatePax('"+rs.getInt("id")+"','"+rs.getString("bus")+"','"+rs.getBoolean("status")+"')\" id=\""+rs.getInt("id")+"Button\" value=\""+rs.getBoolean("statusf")+"\">Not Arrived</button></td>";
						tempNot += Integer.parseInt(rs.getString("paxnum"));
					}
					else{ //Pending Status
						botRes += "<tr id=\""+idNum+"\" style=\"background: rgb(255, 255, 102);\">"; //Yellow Pending
						botRes += "<td><button onclick=\"updatePax('"+rs.getInt("id")+"','"+rs.getString("bus")+"',"+rs.getBoolean("status")+")\" id=\""+rs.getInt("id")+"Button\" value=\""+rs.getBoolean("statusf")+"\">Helper Checked</button></td>";
						tempNot += Integer.parseInt(rs.getString("paxnum"));
					}
				}
				else{ //Final Status
					botRes += "<tr id=\""+idNum+"\" style=\"background: rgb(102, 255, 102);\">"; //GREEN Arrived
					botRes += "<td><button onclick=\"updatePax('"+rs.getInt("id")+"','"+rs.getString("bus")+"',"+rs.getBoolean("status")+")\" id=\""+rs.getInt("id")+"Button\" value=\""+rs.getBoolean("statusf")+"\">Arrived</button></td>";
					tempArr += Integer.parseInt(rs.getString("paxnum"));
				}
				botRes += "<td>"+rs.getString("groupnum")+"</td>";
				botRes += "<td>"+rs.getString("conf")+"</td>";
				botRes += "<td id=\""+rs.getInt("id")+"paxnum\">"+rs.getString("paxnum")+"</td>";
				botRes += "<td>"+rs.getString("paxroom")+"</td>";
				botRes += "<td>"+rs.getString("pickloc")+"</td>";
				botRes += "<td>"+rs.getString("phone")+"</td>";
				botRes += "<td>"+rs.getString("groupcode")+"</td>";
				botRes += "<td>"+rs.getString("remark")+"</td>";
				botRes += "</tr>";

				//Add Pax Counts
				tempCount += Integer.parseInt(rs.getString("paxnum"));

				if(rs.isLast() && tempCount != 0){
					botRes += "<tr>";
					botRes += "<td>Arrived:</td><td id=\""+rs.getString("bus")+"arrived\">"+tempArr+"</td><td>Not:</td><td id=\""+rs.getString("bus")+"not\">"+tempNot+"</td><td>Total:</td><td id=\""+rs.getString("bus")+"total\">"+tempCount+"</td><td></td><td></td>";
					botRes += "</tr>";
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//Top 3 Lines
		topRes += "<tr>";
		topRes += "<th colspan=\"2\">"+cDate+"</th>";
		topRes += "<th colspan=\"6\">"+tourGuide+"  "+tourPhone+"</th>";
		topRes += "</tr>";
		topRes += "<tr>";
		topRes += "<th colspan=\"8\">Tour: "+tour+" "+direct+"</th>";
		topRes += "</tr>";
		topRes += "<tr>";
		topRes += "<th></th>";
		topRes += "<th>Group</th>";
		topRes += "<th>Confirmation</th>";
		topRes += "<th>Pax</th>";
		topRes += "<th>Room</th>";
		topRes += "<th>Pick Up</th>";
		topRes += "<th>Phone</th>";
		topRes += "<th>Tour</th>";
		topRes += "<th>Remark</th>";
		topRes += "</tr>";
		topRes += "<tbody>";

		//Add Body
		topRes += botRes;


		//End Table
		topRes += "</tbody>";
		topRes += "</table>";

		//Close Connection
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//Response.
		response.setContentType("text/plain; charset=GB18030");
	    response.setCharacterEncoding("GB18030");
	  	response.getOutputStream().write(topRes.getBytes());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
