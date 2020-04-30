package snmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Java Discover
 */
public class DBConnection {
 
 
 
 public static Connection getConnection() {
  System.out.println(" postgres DataBase Connection");
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