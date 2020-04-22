package lol;

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



//********************To Get dCPUload********************
//
//Parameters: String address
//
//Work: Calculate CPU Utilization for the IP passed based on hrProcessorLoad
//
//Return:void

public class calcCPULoad extends SNMPManager implements Runnable {

	ping ping ;
	Thread cpu_thread;
	SNMPManager snmp;
	static String ip;
	SNMPManager client_1;
	int i;
	
	public calcCPULoad() {}
	
//	public Connection connect() throws SQLException {
//      return DriverManager.getConnection("jdbc:postgresql://localhost/mydatabase", "postgres", "root");
//  }
	
//	public void NewThread() {
//		cpu_thread = new Thread(this, "Ping Thread");
//		System.out.println("cpu_thread:" + cpu_thread);
//		cpu_thread.start();
//	}
	
	public calcCPULoad(SNMPManager snmp, SNMPManager client, int i) {
		//(snmp,client,i);
		this.snmp=snmp;
		client_1=client;
		this.i=i;
	}
	
	
	@Override
	public void run() {
		
		Double dCPUUtil=0.0;
		int processorLoadIndex=0;
		String  cpu_perf="";
		for(int i=0;i<=10;i++) 
		{
			try {
				cpu_perf = client_1.getAsString(new OID(".1.3.6.1.2.1.25.3.3.1.2."+i));
				}
			catch(Exception|Error e) {
				System.out.println(" cpu oid error 470: i="+i+" "+e+"\n");				
				}
				if(cpu_perf!="Null") 
				{
					//System.out.println(cpu_perf+"-cpu_perf");
					dCPUUtil+=Double.parseDouble(cpu_perf);	
					processorLoadIndex++;
				}
		}
		//System.out.println(dCPUUtil+" -Double dcpu_perf\n"+processorLoadIndex+" -ProcessorLoadIndex");
		if(dCPUUtil/processorLoadIndex<100)
		{	double CpuUtil=dCPUUtil/processorLoadIndex;
			String string_CpuUtil = String.format("%.4f", CpuUtil);
			System.out.println(string_CpuUtil+"% -****CPU_Utilization****of ip:"+snmp.ipAddress.get(i));
			snmp.ipCPU.set(i, string_CpuUtil);//(dCPUUtil/processorLoadIndex+"%");
			
		}
		else
		{
			System.out.println("100% -****CPU_Utilization****of ip:"+snmp.ipAddress.get(i));	
			snmp.ipCPU.set(i,"100%");
			
		}
		
	
	}
}
