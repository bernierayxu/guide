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
 * Servlet implementation class helperBusServlet
 */
@WebServlet("/helperBusServlet")
public class helperBusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public helperBusServlet() {
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

		//Global Variable
		String pickLoc = request.getParameter("area");
		String[] pickLocs = {"FLU","BRK","JCC","EDI"};
		String cDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String bDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date()).toString();
		String finalRes = "";

		//Return Table Start here
		finalRes += "<table>";

		//If Pick All
		if(pickLoc.equals("ALL")){
			for(int i=0; i<pickLocs.length; i++){
				String command = String.format("SELECT * FROM gci4.businfo WHERE date='%s' AND pickloc='%s'", cDate, pickLocs[i]);
				finalRes += findBus(command,pickLocs[i],pickLoc,bDate,rs,sm);
			}
		}
		else{ //Else For One Location
			String command = String.format("SELECT * FROM gci4.businfo WHERE date='%s' AND pickloc='%s'", cDate, pickLoc);
			finalRes += findBus(command,pickLoc,pickLoc,bDate,rs,sm);
		}
		//Return Table End here
		finalRes += "</table>";

		//Connection Close
		try{
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		//Response
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

	protected String findBus(String command, String pickLoc, String ori, String bDate, ResultSet rs, Statement sm){
		String res = "<tr><td colspan=\"5\"><b>"+pickLoc+" "+bDate+"</b></td></tr>";
		try{
			rs = sm.executeQuery(command);
			int count = 0;
			while(rs.next()){
				if(count == 0){ res += "<tr>";}
				if(count == 5) { res += "</tr><tr>";}
				if(!rs.getBoolean("statush")){ //Not Arrived At CTT
					if(ori.equals("ALL")){
						res += "<td><button id=\"bus"+rs.getInt("id")+"\" value=\"false\" onclick=\"updateBus('"+rs.getInt("id")+"')\" style=\"background: rgb(255, 178, 178);\" disabled>"+rs.getString("bus")+"<br>Not Arrived</button></td>";
					}
					else{
						res += "<td><button id=\"bus"+rs.getInt("id")+"\" value=\"false\" onclick=\"updateBus('"+rs.getInt("id")+"')\" style=\"background: rgb(255, 178, 178);\">"+rs.getString("bus")+"<br>Not Arrived</button></td>";
					}
					count++;
				}
				else{ //Arrived At CTT
					if(ori.equals("ALL")){
						res += "<td><button id=\"bus"+rs.getInt("id")+"\" value=\"true\" onclick=\"updateBus('"+rs.getInt("id")+"')\" style=\"background: rgb(102, 255, 102);\" disabled>"+rs.getString("bus")+"<br>Arrived</button></td>";
					}
					else{
						res += "<td><button id=\"bus"+rs.getInt("id")+"\" value=\"true\" onclick=\"updateBus('"+rs.getInt("id")+"')\" style=\"background: rgb(102, 255, 102);\">"+rs.getString("bus")+"<br>Arrived</button></td>";
					}
					count++;
				}
				if(count == 10){ res += "</tr>";}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		return res;
	}

}
