package lol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

//import snmp.commands.HexStrConver;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

import java.time.*;
import java.util.*;
import java.text.*;

public class doAll extends SNMPManager implements Runnable {


public static Connection connection;
	public SNMPManager snmp;
	int i;
	public ArrayList<String> ipAddress;

	
	public doAll(SNMPManager snmp,int i,Connection connection) {
		this.snmp=snmp;
		this.i=i;
		ipAddress=snmp.ipAddress;
		this.connection=connection;
	}
	
	public void Update_in_B(String IP,String USAGE){
		String lut="Ganesh ka birthday";
		String update_query="UPDATE link_info SET lut=?,pw_usage=? Where ip=?";

		 try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ) {
			 PreparedStatement pstmt = conn.prepareStatement(update_query);
				String tem=snmp.date1();
				pstmt.setString(1,tem);
				pstmt.setString(2,USAGE);
				pstmt.setString(3,IP);
				 pstmt.executeUpdate();
				 conn.close();
		 }
		 
		 catch(Exception E) {
				System.out.println(IP+": Exception at 90 DoAll"+E);
			 E.printStackTrace();
		 }
		 }

	public void Update_in_R(String IP,String USAGE){
		String lut="Ganesh ka birthday";
		String update_query="UPDATE ram_usage SET lut=?,ram_usage=? Where ip=?";

		 try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ) {
			 PreparedStatement pstmt = conn.prepareStatement(update_query);
				String tem=snmp.date1();
				pstmt.setString(1,tem);
				pstmt.setString(2,USAGE);
				pstmt.setString(3,IP);
				 pstmt.executeUpdate();
				 conn.close();
				
		 }
		 
		 catch(Exception E) {
				System.out.println(IP+": Exception at 88 DoAll"+E);
			 E.printStackTrace();
		 }
		 }

	public void Update_in_C(String IP,String USAGE){
		String lut="Ganesh ka birthday";
		String update_query="UPDATE cpu_usage SET lut=?,cpu_usage=? Where ip=?";

		 try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ) {
			 PreparedStatement pstmt = conn.prepareStatement(update_query);
				String tem=snmp.date1();
				pstmt.setString(1,tem);
				pstmt.setString(2,USAGE);
				pstmt.setString(3,IP);
				 pstmt.executeUpdate();
				 conn.close();
				
		 }
		 
		 catch(Exception E) {
				System.out.println(IP+": Exception at 88 DoAll"+E);
			 E.printStackTrace();
		 }
		 }
	public void Update_in_D(String IP,String USAGE){
		String lut="Ganesh ka birthday";
		String update_query="UPDATE disk_usage SET lut=?,disk_usage=? Where ip=?";

		 try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ) {
			 PreparedStatement pstmt = conn.prepareStatement(update_query);
				String tem=snmp.date1();
				pstmt.setString(1,tem);
				pstmt.setString(2,USAGE);
				pstmt.setString(3,IP);
				 pstmt.executeUpdate();
				 conn.close();
				
		 }
		 
		 catch(Exception E) {
				System.out.println(IP+": Exception at 88 DoAll"+E);
			 E.printStackTrace();
		 }
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			while(true) {

				//FINAL DB START HERE

				virusdb(i);
				
				//FINAL DB END HERE

				try{
					Thread.sleep(snmp.polling_interval);
				}catch(Exception e) {
					System.out.println("Thread SLEEP Exception"+e);
					e.getStackTrace();
				}
				
			System.out.println("\n\n\n********************\n"+i+"\n**********************\n\n\n");
			InetAddress geek = InetAddress.getByName(snmp.ipAddress.get(i));
			
			System.out.println("Sending Ping Request to " + snmp.ipAddress.get(i));
				
			if (geek.isReachable(5000)) {
				System.out.println("Host is reachable"+snmp.ipAddress.get(i));
				snmp.ipStatus.set(i, "UP");
				//DB HERE
				SNMPManager client = new SNMPManager("udp:"+snmp.ipAddress.get(i)+"/161");
				//System.out.println("\n*****************\nGANESH JI KI JAI:"+snmp.ipAddress.get(i)+"\n*****************\\n");
				if(snmp.ipSnmpStatus.get(i).equals("YES")) {
					try {
						client.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(i+":line 114 snmp client .start exception\n");
					}				

					//**********Bandwidth Utilization**********
					bandwidthUtil_calc c=null;
					try{ c = new bandwidthUtil_calc(snmp,i,client);}catch(Exception|Error e) {System.out.println("120 :"+e);}
					
					System.out.println(i+":bw start\n");
//					Thread ct = new Thread(c);
//					ct.start();
					try{c.run();}catch(Exception|Error e) {System.out.println("125 :"+e);}
					
					System.out.println(i+":bw end\n");
					String tempIP=snmp.ipAddress.get(i);
					try{
						if(tempIP.endsWith(".1")) {
						System.out.println(i+":"+tempIP+"=temp\n");
						
						continue; //x.y.z.1 is Router and doesn't have CPU & MEM utilization hurray!!! Everything is working
						}
					}catch(Exception|Error e) {System.out.println("ends with .1 :"+e);}
					
					//**********To Get dCPUload**********
					calcCPULoad ccl = null;
					try{ccl = new calcCPULoad(snmp,client,i);}catch(Exception|Error e) {System.out.println("138 :"+e);}
					
					//System.out.println(i+":cpu start\n");
//					Thread cclt = new Thread(ccl);
//					cclt.start();
					try{ccl.run();}catch(Exception|Error e) {System.out.println("143 :"+e);}
					
					//System.out.println(i+":cpu end\n");
					
					//**********To Get dMemUtil**********
					ramdiskUtil ru = null;
					try{ru = new ramdiskUtil(snmp,client,i);}catch(Exception|Error e) {System.out.println("149 :"+e);}
					
					//System.out.println(i+":start rd\n");
//					Thread rut = new Thread(ru);
//					rut.start();
					try{ru.run();}catch(Exception|Error e) {System.out.println("154 :"+e);}
					
					//System.out.println(i+":end rd\n");					
					
					//time
					try{Clock clock = Clock.systemDefaultZone();
					Instant instant = clock.instant();
					System.out.println(instant+":TIME********");
					snmp.ipTimeStamp.set(i, instant+"");
					}catch(Exception|Error e) {System.out.println("163 :"+e);}
					
		
				}
			}
			

		else {
				System.out.println("Sorry ! We can't reach to this host"+snmp.ipAddress.get(i));
				snmp.ipStatus.set(i, "DOWN");
				//DB here
				}
			
				
			
			}
		}
		catch (Exception|Error e) {
			System.out.println(i+":186 doAll exception: "+e+"\n IP:"+snmp.ipAddress.get(i));
			}
			
		
		
	}

	private void virusdb(int j) {
		System.out.println("\n******************\n************************\nipAddress="+snmp.ipAddress+"\nipIfIndex="+snmp.ipIfIndex+"\nipStatus="+snmp.ipStatus+"\nSNMPstatus="+snmp.ipSnmpStatus+"\nBandwidth="+snmp.ipBandwidth+"\nCPU="+snmp.ipCPU+"\nDescription="+snmp.ipDesc+"\nDisk="+snmp.ipDisk+"\nRAM="+snmp.ipRAM+"\nTimeStamp="+snmp.ipTimeStamp+"\n*************************\n*********************\n");
		
		
		String Sel_Ping="SELECT ipadd from ping_result";
		String Sel_CPU="SELECT ip from cpu_usage";
		String Sel_RAM="SELECT ip from ram_usage";
		String Sel_DISK="SELECT ip from disk_usage";
		String Sel_BW="SELECT ip from link_info";
		
	
		String Update_PingResult_for_up="UPDATE ping_result SET status=(?),upt=(?),sac=(?) WHERE ipadd=(?)";
		String Update_PingResult_for_down="UPDATE ping_result SET status=(?),downt=(?),sac=(?) WHERE ipadd=(?)";
		  //virus start
		 try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                //ResultSet rs = stmt.executeQuery(Sel_Ping)
	                		) 
		 {
			
			 
	           		//while(rs.next()) {
	           			if(snmp.ipStatus.get(j).equals("UP")) {
	           				System.out.print("if");
	           				PreparedStatement pstmt = conn.prepareStatement(Update_PingResult_for_up);
	           				pstmt.setString(1, snmp.ipStatus.get(i).toString());
	           				String tem=snmp.date1();
	           				pstmt.setString(2,tem);
	           				pstmt.setString(3,snmp.ipSnmpStatus.get(i).toString());
	           				pstmt.setString(4,snmp.ipAddress.get(i).strip().toString());
	           				pstmt.executeUpdate();
	           				
	           			}
	           			else {
	           				PreparedStatement pstmt = conn.prepareStatement(Update_PingResult_for_down);
	           				pstmt.setString(1, snmp.ipStatus.get(j).toString());
	           				
	           				String tem=snmp.date1();
	           				pstmt.setString(2,tem);
	           				pstmt.setString(3,snmp.ipSnmpStatus.get(j).toString());
	           				pstmt.setString(4,snmp.ipAddress.get(i).strip().toString());
	           				pstmt.executeUpdate();
	           			}
	          	        
	           				String ip=snmp.ipAddress.get(j).strip().toString();
	           				Update_in_C(ip,snmp.ipCPU.get(j).strip().toString());
	           				Update_in_R(ip,snmp.ipRAM.get(j).strip().toString());
	           				Update_in_D(ip,snmp.ipDisk.get(j).strip().toString());
	           				Update_in_B(ip,snmp.ipBandwidth.get(j).strip().toString());
System.out.println("Updated All Values for:"+snmp.ipAddress.get(j));	
							connection.close();
	           		//}
	           		}
		 
		 catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
		 //virus end
			
	}

}
	

