

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
 * Servlet implementation class changeServlet
 */
@WebServlet("/changeServlet")
public class changeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public changeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub.

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

		//Get Current Password And New Password
		String cpw = request.getParameter("cpassword");
		String npw = request.getParameter("npassword");
		String command = String.format("SELECT * FROM gci4.admin WHERE password='%s' AND admin='%s'", cpw, "upload");
		String res = "1";

		try{
			rs = sm.executeQuery(command);
			//If the old password is correct.
			if(rs.next()){
				//Change new password
				command = String.format("UPDATE gci4.admin SET password='%s' WHERE admin='%s'", npw, "ken" );
				sm.executeUpdate(command);
				response.setContentType("text/plain; charset=GB18030");
				response.setCharacterEncoding("GB18030");
				response.getOutputStream().write(res.getBytes());

			}
			//Wrong Old Password
			else{
				res = "0";
			  	response.setContentType("text/plain; charset=GB18030");
			    response.setCharacterEncoding("GB18030");
			  	response.getOutputStream().write(res.getBytes());
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

}
