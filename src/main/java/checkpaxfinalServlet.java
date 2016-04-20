
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class checkpaxServlet
 */
@WebServlet("/checkpaxfinalServlet")
public class checkpaxfinalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public checkpaxfinalServlet() {
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
		String id = request.getParameter("id");
		String statusf = request.getParameter("statusf");
		String command = String.format("UPDATE gci4.paxinfo SET statusf='%s' WHERE id='%s'", statusf, id);


		//Update Query And Close Connection
		try {
			sm.executeUpdate(command);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
