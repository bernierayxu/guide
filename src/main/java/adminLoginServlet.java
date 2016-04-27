

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
 * Servlet implementation class adminLoginServlet
 */
@WebServlet("/adminLoginServlet")
public class adminLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public adminLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Connection Variables
		Connection con = null;
		Statement sm = null;
		ResultSet rs = null;

		//Connect To Server
		String url = "jdbc:mysql://127.4.100.130:3306/gci4";
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,"admin5pLIyrz","Emf4kZS4Mtdi");
			sm = con.createStatement();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		//Global Variables
		String pw = request.getParameter("password");
		String command = String.format("SELECT password FROM gci4.admin WHERE admin='%s' AND password='%s'", "upload", pw);

		//Initialize Session
		HttpSession sn = request.getSession();

		try{
			rs = sm.executeQuery(command);
			if(rs.next()){
				//Set Redirect To Upload Function Page
				sn.setAttribute("adminLogStatus", 1);
				response.sendRedirect("http://gci4-lltravel.rhcloud.com/upload.jsp");
				con.close();
			}
			else{
				//Set Wrong Username and Password Mark
				sn.setAttribute("adminLogStatus", 0);
				response.sendRedirect("http://gci4-lltravel.rhcloud.com/adminlogin.jsp");
				con.close();
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

}
