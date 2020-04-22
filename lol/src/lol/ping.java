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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//********************Ping********************
//
//Parameters: 
//
//Work: Update device Status by Pinging each from Arraylist.
//
//Return:void


public class ping extends SNMPManager implements Runnable {

		Thread ping_thread;
		SNMPManager snmp;
		public Connection connect() throws SQLException {
	        return DriverManager.getConnection("jdbc:postgresql://localhost/mydatabase","postgres","root");
	    }
		
		public ping() {}
		
		public ping(SNMPManager snmp) { 
			// TODO Auto-generated constructor stub
			this.snmp=snmp;
		}

		public void NewThread() {
			ping_thread = new Thread(this, "Ping Thread");
			System.out.println("ping_thread:" + ping_thread);
			ping_thread.start();
		}

		@Override
		public void run() {
			
			// TODO Auto-generated method stub
	/*		Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, user, password);
				// System.out.println("Connected to the PostgreSQL server successfully.");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	*/		
				
				System.out.println("\nipAddress="+snmp.ipAddress+"\nipIfIndex="+snmp.ipIfIndex+"\nipStatus="+snmp.ipStatus+"\nSNMPstatus="+snmp.ipSnmpStatus+"\n");
				
				for (int ii = 0; ii < snmp.ipAddress.size(); ii++) {
					//final Integer i=new Integer(ii);
					doAll d = new doAll(snmp,ii);
					Thread t = new Thread(d);
					t.start();
					}
			
		}

	}

