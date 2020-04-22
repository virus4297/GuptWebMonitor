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


//********************Bandwidth Utilization********************
//
//Parameters: Integer ifInd, String ip2,long interval
//
//Work: Calculate and print Bandwidth Utilization based on Inoctet and Outoctets
//
//Return:void

public class bandwidthUtil_calc extends SNMPManager implements Runnable {


	public ping ping;
	public String ip;
	public SNMPManager client_1;
	public SNMPManager snmp;
	static Integer polling_interval;
	static long ifSpeed=0;
	static double ifUtilRate=0;
	static long old_inoctet=0, old_outoctet=0, del_outoctet=0;
	static long new_inoctet=0, new_outoctet=0, del_inoctet=0;
	static long oldOctet=0,newOctet=0,del_oldOctet=0,del_newOctet=0;
	public Integer ifIndex,flag=0;
	public Integer ipIfIndex;
	public int i,flag2=0;
	
	public bandwidthUtil_calc() {}

	public bandwidthUtil_calc(SNMPManager snmp, int i, SNMPManager client) {
		this.snmp=snmp;
		ifIndex=i;
		client_1=client;
		flag=0;
		this.i=i;
	}


	public void run() {
		try {
			polling_interval=snmp.polling_interval;
			
			ipIfIndex=snmp.ipIfIndex.get(ifIndex);
			Double d = 0.0;
//			System.out.println(ifIndex + "IfIndex\n");
//			System.out.println(snmp.ipIfIndex.get(ifIndex) + "IPIfIndex\n");
			String IfSpeed = client_1.getAsString(new OID(".1.3.6.1.2.1.2.2.1.5." + ipIfIndex));
			try{ifSpeed = Long.parseLong(IfSpeed);
			}catch(Error|Exception e) {System.out.println("line 94\n");}
			
			System.out.println(ifSpeed + "Mb :IfSpeed\n");
 
			// loop infinitely every polling_interval for 15 seconds
			//while (true) {

				String IfInOctets = client_1.getAsString(new OID(".1.3.6.1.2.1.2.2.1.10." + ipIfIndex));
				try{new_inoctet = Long.parseLong(IfInOctets);
				}catch(Error|Exception e) {System.out.println("line 102\n");}
//				System.out.println(new_inoctet + " -IfInOctets & ="+ifIndex);
				
				String IfOutOctets = client_1.getAsString(new OID(".1.3.6.1.2.1.2.2.1.16." + ipIfIndex));
				try{new_outoctet = Long.parseLong(IfOutOctets);
				}catch(Error|Exception e) {System.out.println("line 106\n");}
//				System.out.println(new_outoctet + " -IfOutOctets & ="+ifIndex);

				
				try {
					del_inoctet = new_inoctet - old_inoctet;
					del_inoctet =Math.abs(del_inoctet);
					del_outoctet = new_outoctet - old_outoctet;
					del_outoctet =Math.abs(del_outoctet);
				}
				catch(Error|Exception e) {System.out.println("line 123\n");}

//				System.out.println(del_inoctet + " -del_inoctet after & ="+ifIndex);
//				System.out.println(del_outoctet + " -del_outoctet after & ="+ifIndex);
//
//				System.out.println(old_inoctet + " -old_inoctet");
//				System.out.println(old_outoctet + " -old_outoctet");

				old_inoctet = new_inoctet;
				old_outoctet = new_outoctet;

//				System.out.println(old_inoctet + " -old_inoctet");
//				System.out.println(old_outoctet + " -old_outoctet");
				try {
				if(Long.compare(ifSpeed,0)==0)
					ifSpeed=10000000;

				//System.out.println(ifSpeed + "Mb :IfSpeed again:"+ifIndex+"\n");
				
				
				long del_sum=Long.sum(del_inoctet ,del_outoctet);
				
				ifUtilRate = ((del_sum) * 8 * 100) / ((polling_interval/1000) * ifSpeed);

		//		System.out.println(del_sum + "del_sum\n");
				ifUtilRate = Math.abs(ifUtilRate);
				}catch(Exception|Error e) {
					System.out.println(snmp.ipAddress.get(i)+"=Ip 139BW Conversion from long to double:"+e);
				}
			

				String string_ifUtilRate=Double.toString(ifUtilRate);
		//		System.out.println(string_ifUtilRate + "Mb :string_ifUtilRate\n");
				try {
				string_ifUtilRate = String.format("%.4f", ifUtilRate);
		//		System.out.println(string_ifUtilRate + "Mb :string_ifUtilRate\n");
				}catch(Error|Exception e) {System.out.println("line 94\n");}
				
				System.out.println(string_ifUtilRate + "Kbps -******Bandwidth Utilization*****of ip:"+snmp.ipAddress.get(i));
				snmp.ipBandwidth.set(i,string_ifUtilRate);
			
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" bw error 672: "+e+"\n");				
			
		}
	}
}
