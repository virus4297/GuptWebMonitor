package snmp;
import java.awt.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import snmp.*;

public class monitor_servlet extends HttpServlet  {
	protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		//response.setIntHeader("Refresh", 3);
			
	      tabledata dd = new tabledata();
	      request.setAttribute("data", dd.getpingresult());
				
	      		System.out.println("entering servlet");
				
	}
}


class tabledata{
public ArrayList getpingresult(){
    ArrayList<m_bean> v = new ArrayList<m_bean>();
    
    try{
    	Connection connection =  DBConnection.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Ping_Result Order by ipadd");
     
     while(rs.next()){
    	 m_bean m_bean= new m_bean();
         m_bean.setipadd(rs.getString("ipadd")); 
         m_bean.setstatus(rs.getString("status")); 
         m_bean.setupt(rs.getString("upt")); 
         m_bean.setdownt(rs.getString("downt")); 
         m_bean.setsac(rs.getString("sac")); 
         v.add(m_bean); 
     }
     try {
        //connection.commit();
         connection.close();
     } catch (SQLException e) {
         e.printStackTrace();
     }
    }catch(Exception asd){
        System.out.println(asd.getMessage());
    }
    
    return v;
}
}