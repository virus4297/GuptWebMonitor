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


public class SNMPManager {
	
	
	public SNMPManager() {}
	
	//connection
	
	//date and time
	 public static String date1() {
			Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("d MMM yyyy HH:mm:ss");
		    String d=  ft.format(dNow).toString();
		    
		    return d;
			}


public Snmp snmp = null;
public String address = "",host="",subnet=""; //keep empty to auto detect IP
public ArrayList<Integer> ipIndex=new ArrayList<Integer>();
public ArrayList<String> ipAddress=new ArrayList<String>();
public ArrayList<String> ipSnmpStatus=new ArrayList<String>();
public ArrayList<String> ipDesc=new ArrayList<String>();
public ArrayList<String> ipStatus=new ArrayList<String>();
public ArrayList<String> ipCPU=new ArrayList<String>();
public ArrayList<String> ipRAM=new ArrayList<String>();
public ArrayList<String> ipDisk=new ArrayList<String>();
public ArrayList<String> ipBandwidth=new ArrayList<String>();
public ArrayList<String> ipTimeStamp=new ArrayList<String>();
public ArrayList<Integer> ipIfIndex=new ArrayList<Integer>();
public Integer polling_interval=1000;
public static Connection connection;
public static  Connection connect() throws SQLException {
	connection=DriverManager.getConnection("jdbc:postgresql://localhost/mydatabase", "postgres", "root");
    return connection;
}

/**
* Constructor
* @param add
*/
public SNMPManager(String add)
{
address = add;
}
public static void truncate(String tname, Connection connection2) throws SQLException {
	try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ) {
	 
		 String query = "Truncate table "+tname;
		 stmt.execute(query);
		 conn.close();
		}catch(Exception E) {
			E.printStackTrace();
		}
	}

public static void main(String[] args) throws Exception {
	
/**
* Port 161 is used for Read and Other operations
* Port 162 is used for the trap generation
*/
	//truncate all
//	truncate("ip");
//	truncate("ping_result");      //SHUT TF UP GANESH!!
//	truncate("cpu_usage");
//	truncate("ram_usage");
//	truncate("disk_usage");
//	truncate("link_info");
	
	truncate("ip",connection);
	truncate("ping_result",connection);
	truncate("cpu_usage",connection);
	truncate("ram_usage",connection);
	truncate("disk_usage",connection);//no down in array 
	truncate("link_info",connection);
	
	SNMPManager snmp=new SNMPManager("");
	getCurrentIp(snmp); //obtain myip & host and calls checkHost(host) to populate ipAddress,ipStatus,ipIfIndex ArrayLists 
	String Insert_IP="INSERT INTO ip(ip) VALUES(?)";
	String Insert_PingResult_for_up="INSERT INTO ping_result(ipadd,status,upt,downt) VALUES(?,?,?,?)";
	//add ip able
	
	try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(Insert_IP)) {
		for(int i=0;i<snmp.ipAddress.size();i++) {
        pstmt.setString(1, snmp.ipAddress.get(i));
        pstmt.executeUpdate();
		}

    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
	//ping result add
	for(int i=0;i<snmp.ipAddress.size();i++) {
//	System.out.println("ip at index "+i+" is "+snmp.ipAddress.get(i)+"\n");
	try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(Insert_PingResult_for_up)) {
        pstmt.setString(1, snmp.ipAddress.get(i));
        pstmt.setString(2, snmp.ipStatus.get(i));
        String tem=snmp.date1();
		pstmt.setString(3,tem);
		pstmt.setString(4,tem);
        pstmt.executeUpdate();

    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }

		Insert_in_CRB("link_info","pw_usage",snmp.ipAddress.get(i),"PR");
    	Insert_in_CRB("cpu_usage","cpu_usage",snmp.ipAddress.get(i),"PR");
		Insert_in_CRB("ram_usage","ram_usage",snmp.ipAddress.get(i),"PR");
		Insert_in_CRB("disk_usage","disk_usage",snmp.ipAddress.get(i),"PR");
	
	}
	
	try{

		int minCapacity = snmp.ipAddress.size()+1;
		
			snmp.ipDesc.ensureCapacity(minCapacity);
			snmp.ipCPU.ensureCapacity(minCapacity);
			snmp.ipRAM.ensureCapacity(minCapacity);
			snmp.ipDisk.ensureCapacity(minCapacity);
			snmp.ipBandwidth.ensureCapacity(minCapacity);
			snmp.ipTimeStamp.ensureCapacity(minCapacity);
			
			for (int ii = 0; ii < snmp.ipAddress.size(); ii++) {
				snmp.ipDesc.add("");
				snmp.ipCPU.add("");
				snmp.ipRAM.add("");
				snmp.ipDisk.add("");
				snmp.ipBandwidth.add("");
				snmp.ipTimeStamp.add("");
				
			}
		System.out.println("\nipAddress="+snmp.ipAddress+"\nipIfIndex="+snmp.ipIfIndex+"\nipStatus="+snmp.ipStatus+"\nSNMPstatus="+snmp.ipSnmpStatus+"\n");
		//delete all data in database
		
	
		
	for (int ii = 0; ii < snmp.ipAddress.size(); ii++) {
		//final Integer i=new Integer(ii);
		doAll d = new doAll(snmp,ii,connection);
		Thread t = new Thread(d);
		t.start();
		//DB HERE FOR Index ii
		//System.out.println("\n******************\n************************\nipAddress="+snmp.ipAddress.get(ii)+"\nipIfIndex="+snmp.ipIfIndex.get(ii)+"\nipStatus="+snmp.ipStatus.get(ii)+"\nSNMPstatus="+snmp.ipSnmpStatus.get(ii)+"Bandwidth="+snmp.ipBandwidth.get(ii)+"CPU="+snmp.ipCPU.get(ii)+"Description="+snmp.ipDesc.get(ii)+"Disk="+snmp.ipDisk.get(ii)+"RAM="+snmp.ipRAM.get(ii)+"\n*************************\n*********************\n");
		
		//System.out.println("\n******************\n************************\nipAddress="+snmp.ipAddress+"\nipIfIndex="+snmp.ipIfIndex+"\nipStatus="+snmp.ipStatus+"\nSNMPstatus="+snmp.ipSnmpStatus+"\nBandwidth="+snmp.ipBandwidth+"\nCPU="+snmp.ipCPU+"\nDescription="+snmp.ipDesc+"\nDisk="+snmp.ipDisk+"\nRAM="+snmp.ipRAM+"\n*************************\n*********************\n");
		
		}
	
	

	}
	catch(Error|Exception e)
	{
		System.out.println("line 98:"+e+"\n");
	}	
}

public static void  Insert_in_CRB(String table,String usage,String IP,String USAGE){
	String lut="Ganesh ka birthday";
	String insert_in_CRB="INSERT INTO "+table+" (ip,lut,"+usage+") VALUES(?,?,?)";

	 try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ) {
		 PreparedStatement pstmt = conn.prepareStatement(insert_in_CRB);
				
		 	pstmt.setString(1,IP);
			pstmt.setString(2,"blank");
			pstmt.setString(3,USAGE);
			pstmt.executeUpdate();
			conn.close();
	 }
	 catch(Exception E) {
			System.out.println("Exception at 71 DoAll"+E);
		 E.printStackTrace();
	 }
	 }

/**********Function to get Current IP of System**********
*
*Parameters: String address
*
*Work: Calculate CPU Utilization for the IP passed based on hrProcessorLoad
*
*Return:void
*/
public static void getCurrentIp(SNMPManager snmp) throws Exception
{
  InetAddress IP = InetAddress.getLocalHost();
  if(snmp.address.isEmpty())
	  snmp.address=IP.getHostAddress();
  System.out.println("IP of my system is := "+snmp.address);

      int first_point=(snmp.address.indexOf('.', 0));
      int second_point=(snmp.address.indexOf('.', first_point+1));
      //int third_point=(address.indexOf('.', second_point+1));
      snmp.host=(String) snmp.address.subSequence(0, second_point);
      snmp.subnet=snmp.host;
  //System.out.println(host+" -host");
  checkHosts(snmp);

  //System.out.println(getARPTable(ARP_GET_IP_HW ));
}

/*********************Function to get All IP on network*******************
*
*Parameters: String subnet
*
*Work: Checks for devices on the local network 
*
*Return:void
*/
public static void checkHosts(SNMPManager snmp) throws UnknownHostException, IOException{
	   int timeout=1000;
      	SNMPManager client ;
	   for (int i=1;i<5;i++){  /// CAIR CHANGES change to //i=100;i<105;i++
		   for(int j=1;j<15;j++) //// CAIR CHANGES change to // not required
			   /*
			    * 192.168.x.y 192.168.101.10 & 192.168.103.10
			    * */
		   {
			   String host_1=snmp.subnet + "." + i+"."+j;
			   //System.out.println("i="+i+"j="+j+" Host_1="+host_1+"subnet="+snmp.subnet);
			   
		       if (InetAddress.getByName(host_1).isReachable(timeout)){
		           System.out.println(host_1 + " is reachable");
		           snmp.ipAddress.add(host_1);
		           snmp.ipStatus.add("UP");
		           //DB HERE for all reachable devices irrespective of their snmp status
		           //add host_1 as ip and UP as status 
		          
			       	//Ititialize SNMP client

		   			client = new SNMPManager("udp:"+host_1+"/161");
			       	try{
			       		client.start();
			       	}catch(Exception|Error e) {
			       		System.out.println("917:snmp error for device "+host_1+"\nError is:"+e);
			       		e.printStackTrace();
			       	}
			       	//System.out.println("client");
			       	
			 
			         //********************To Get IfIndex Number********************
			       	String ifNumber=null;
			   		try{//TO CHECK IF SNMP ACTIVE ON DEVICE
			   			ifNumber = client.getAsString(new OID(".1.3.6.1.2.1.2.1.0"));
			   			}
			   		catch(Exception|Error e) {
			   			System.out.println("client Exception :"+e+"\n");
			   			snmp.ipSnmpStatus.add("NO");
			   			snmp.ipIfIndex.add(0);
			   			continue;

			   			}
			   		

		   			snmp.ipSnmpStatus.add("YES");
		   		//get ifIndex from ifNumber
			   		
		   			//if router (ip=x.y.z.1)
		   			if(j==1) {
			   			snmp.ipIfIndex.add(210001);
		   			}
		   			else {
		   			//else, check ifInOctet for each ifNumber
		   			for(int k=0;k<Integer.valueOf(ifNumber);k++) {
		   			//if ifInOctet>0 that index is ifIndex 

				   	   	String  ifInOctet = client.getAsString(new OID(".1.3.6.1.2.1.2.2.1.10."+k));
				   	   	if(ifInOctet != "Null") {
				   	   		Long intInOctet=Long.valueOf(ifInOctet);
				   	   	if(intInOctet > 0) {
				   			snmp.ipIfIndex.add(k);
				   			break;
				   	   		}
		   				}
		   			}
		   			}
			   			
		           
		       }
		       else if(j==1) {
		    	   break;
		       }
		   }
	   }
	}




/**
* Start the Snmp session. If you forget the listen() method you will not
* get any answers because the communication is asynchronous
* and the listen() method listens for answers.
* @throws IOException
*/
void start() throws IOException {
TransportMapping transport = new DefaultUdpTransportMapping();
snmp = new Snmp(transport);
// Do not forget this line!
transport.listen();
}

/**
* Method which takes a single OID and returns the response from the agent as a String.
* @param oid
* @return
* @throws IOException
*/
public String getAsString(OID oid) throws IOException {
ResponseEvent event = get(new OID[] { oid });
return event.getResponse().get(0).getVariable().toString();
}

/**
* This method is capable of handling multiple OIDs
* @param oids
* @return
* @throws IOException
*/
public ResponseEvent get(OID oids[]) throws IOException {
PDU pdu = new PDU();
for (OID oid : oids) {
pdu.add(new VariableBinding(oid));
}
pdu.setType(PDU.GET);
ResponseEvent event = snmp.send(pdu, getTarget(), null);
if(event != null) {
return event;
}
throw new RuntimeException("GET timed out");
}

/**
* This method returns a Target, which contains information about
* where the data should be fetched and how.
* @return
*/
private Target getTarget() {
Address targetAddress = GenericAddress.parse(address);
CommunityTarget target = new CommunityTarget();
target.setCommunity(new OctetString("public"));
target.setAddress(targetAddress);
target.setRetries(2);
target.setTimeout(1500);
target.setVersion(SnmpConstants.version2c);
return target;
}

public void updateArrayList() {
	//DB HERE
	// ipAddress.add("");
	//ipaddress is getting added to ip table
		String SQL = "INSERT INTO ip(ip) "
	            + "VALUES(?)";
	    try (
	            Connection conn = connect();
	            PreparedStatement statement = conn.prepareStatement(SQL);) {
	        int count = 0;

	       for(int i=0;i<ipAddress.size();i++) {
	            statement.setString(1, ipAddress.get(i));
	       }
	           

	           
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	}
}

//SNMPManager class end 
