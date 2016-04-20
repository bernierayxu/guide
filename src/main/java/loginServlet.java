

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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
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
		String command = String.format("SELECT password FROM gci4.admin WHERE admin='%s'", "ken");
		String pw = request.getParameter("password");
		HttpSession sn = request.getSession();
		
		try {
			rs = sm.executeQuery(command);
			if(rs.next()){
				if(pw.equals(rs.getString("password"))){
					sn.setAttribute("logStatus", 1);
					response.sendRedirect("http://gci4-lltravel.rhcloud.com/main.jsp");
				}
				else{
					sn.setAttribute("logStatus", 0);
					response.sendRedirect("http://gci4-lltravel.rhcloud.com/login.jsp");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
