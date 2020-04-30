package snmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Java Discover
 */
public class LoginValidate {
 
 public boolean validateUserLogin(String uname, String pwd){
  boolean flag = false;
  Connection con = null;
  try{
   con = createConnection();
   if(con != null){
    Statement stat = con.createStatement();
    String qry = "SELECT * FROM tbl_login_master WHERE username = '"+uname+"' AND password = '"+pwd+"' ";
    ResultSet rs = stat.executeQuery(qry);
    if(rs.next()){
     flag = true;
    }
   }
  }catch (Exception e) {
   e.printStackTrace();
  }finally{
   if(con != null){
    try {
     con.close();
    } catch (SQLException e) {
     e.printStackTrace();
    }
   }
  }
  return flag;
 }
 
 public Connection createConnection() {
  System.out.println("Createung postgres DataBase Connection");
  Connection connection = null;

  try {
   
   // Provide database Driver according to your database
  
	  Class.forName("org.postgresql.Driver");
   // Provide URL, database and credentials according to your database 
   connection = DriverManager.getConnection("jdbc:postgresql://localhost/mydatabase", "postgres","root");

  } catch (Exception e) {
   e.printStackTrace();
   return null;
  }
  if(connection != null){
   System.out.println("Connection created successfully....");
  }
  return connection;
 }
}