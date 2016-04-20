
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class busServlet
 */
@WebServlet("/busServlet")
public class busServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public busServlet() {
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
		String cDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());  //Get Current Date
		ArrayList<String> busList = new ArrayList<String>();
		String finalRes = "";
		String command = String.format("SELECT id,bus FROM gci4.paxinfo WHERE date='%s'", cDate);

		//Query To Database.
		try {
			rs = sm.executeQuery(command);
			while(rs.next()){
				String sBus = findBus(rs.getString("bus"),busList);
				if(!sBus.equals("already")){
					busList.add(sBus);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		finalRes += "<select name=\"busSelect\" id=\"busSelect\">";
		for(String b : busList){
			finalRes += "<option value=\"" + b + "\">" + b + "</option>";
		}
		finalRes += "</select>";

		//Close Connection
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
	}

	public static String findBus(String bus, ArrayList<String> busList){
		for(String b : busList){
			if(b.equals(bus)){
				return "already";
			}
		}
		return bus;
	}

}
