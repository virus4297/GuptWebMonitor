package snmp;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class delete extends HttpServlet  {
	protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
			String ipadd = request.getParameter("ipaddr");
			System.out.println(ipadd);
			Connection connection = DBConnection.getConnection();
			try {
			Statement statement = connection.createStatement();
			int customer = statement.executeUpdate("");
		     String query = "delete from ip value where ip=?" ;

		    	      // create the mysql insert preparedstatement
		    	      PreparedStatement preparedStmt = connection.prepareStatement(query);
		    	      preparedStmt.setString (1, ipadd);
		    	      preparedStmt.execute();
			response.sendRedirect("ip.jsp"); 
			}
			catch(Exception e){
				System.out.print(e);
				e.printStackTrace();
				 PrintWriter out = response.getWriter();
					out.println("<html><head></head><body onload=\"alert('hello')\"></body></html>");
				
			}
			finally {
				
				
			}

	}
		

}