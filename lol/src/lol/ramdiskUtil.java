package lol;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

import org.snmp4j.smi.OID;




/********************To Get DiskRamMemoruy Utilization********************
*
*Parameters: 
*
*Work:
*
*Return:void
*/
public class ramdiskUtil extends SNMPManager implements Runnable {

	ping ping;
	SNMPManager client_1;
	SNMPManager snmp;
	Thread ramdisk_thread;
	static String ip,hrStorageDesc;
	static int ramIndex;
	int i;
//	public Connection connect() throws SQLException {
//        return DriverManager.getConnection("jdbc:postgresql://localhost/mydatabase", "postgres", "root");
//    }
	static String strMemSize,strMemUsed,strStorageAlloc;
//	public void NewThread() {
//		ramdisk_thread = new Thread(this, "Ping Thread");
//		System.out.println("cpu_thread:" + ramdisk_thread);
//		ramdisk_thread.start();
//	}
	
	public ramdiskUtil() {}
		
	
	public ramdiskUtil(SNMPManager snmp, SNMPManager client, int i) {
		this.snmp=snmp;
		client_1=client;
		this.i=i;
	}
	
	
	@Override
	public void run() {

		try {
//			
			//System.out.println(" line 222 IP: "+ip);
			//while(true) {
				Double dDiskMemSize=0.0,dDiskMemUsed=0.0,dRAMMemSize=0.0,dRAMMemUsed=0.0;
				int memUtilIndex=0;
				String  hrStorageIndex="";
				for(int i=0;i<=10;i++) 
				{												
				hrStorageIndex = client_1.getAsString(new OID(".1.3.6.1.2.1.25.2.3.1.1."+i));
					if(hrStorageIndex!="Null") 
					{
						//System.out.println(hrStorageIndex+"-hrStorageIndex");
					
						strStorageAlloc = client_1.getAsString(new OID(".1.3.6.1.2.1.25.2.3.1.4."+i));
						strMemSize = client_1.getAsString(new OID(".1.3.6.1.2.1.25.2.3.1.5."+i));
						strMemUsed = client_1.getAsString(new OID(".1.3.6.1.2.1.25.2.3.1.6."+i));
						hrStorageDesc =  client_1.getAsString(new OID(".1.3.6.1.2.1.25.2.3.1.3."+i));
						if(hrStorageDesc.equals("Physical Memory")) {
							ramIndex=i;
							dRAMMemSize+=Double.valueOf(strMemSize)*Double.valueOf(strStorageAlloc);
							dRAMMemUsed+=Double.valueOf(strMemUsed)*Double.valueOf(strStorageAlloc);
						}
						else 
						{
							dDiskMemSize+=Double.valueOf(strMemSize)*Double.valueOf(strStorageAlloc);
							dDiskMemUsed+=Double.valueOf(strMemUsed)*Double.valueOf(strStorageAlloc);
							memUtilIndex++;
						}
					} 
				}
	
				//System.out.println(dRAMMemSize+" -Double dRAMMemSize\n"+dRAMMemUsed+" -Double dRAMMemUsed\n"+ramIndex+" -ramIndex");
				if((dRAMMemUsed*100)/dRAMMemSize<100)
				{	double RamUtil=(dRAMMemUsed*100)/dRAMMemSize;
						String string_RamUtil = String.format("%.4f", RamUtil);
					System.out.println(string_RamUtil+"% -****RAM_Utilization****of ip:"+snmp.ipAddress.get(i));
					snmp.ipRAM.set(i,string_RamUtil);
				
					
					
				}
				
				//System.out.println(dDiskMemSize+" -Double dDiskMemSize\n"+dDiskMemUsed+" -Double dDiskMemUsed\n"+memUtilIndex+" -ProcessorLoadIndex");
				if((dDiskMemUsed*100)/dDiskMemSize<100)
				{	double DiskUtil=(dRAMMemUsed*100)/dRAMMemSize;
				String string_DiskUtil = String.format("%.4f", DiskUtil);
					System.out.println(string_DiskUtil+"% -****Disk_Utilization****of ip:"+snmp.ipAddress.get(i));
					snmp.ipDisk.set(i,string_DiskUtil);
					
					
				}


			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(" line 240");
			e.printStackTrace();
		}
	}
}
