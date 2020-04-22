package lol;

import java.io.IOException;
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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class band {

Snmp snmp = null;
String address = null;
int update_freq=12;
/**
* Constructor
* @param add
*/
public band(String add)
{
address = add;
}

public static void main(String[] args) throws IOException {
/**
* Port 161 is used for Read and Other operations
* Port 162 is used for the trap generation
*/
band client = new band("udp:192.168.103.10/161");
client.start();
/**
* OID - .1.3.6.1.2.1.1.1.0 => SysDec
* OID - .1.3.6.1.2.1.1.5.0 => SysName
* => MIB explorer will be usefull here, as discussed in previous article
*/
 
String sysDescr5 = client.getAsString(new OID(".1.3.6.1.2.1.2.2.1.5.2"));
System.out.println(sysDescr5+":IfSpeed\n");

String sysDescr8 = client.getAsString(new OID(".1.3.6.1.2.1.2.2.1.10.1"));
System.out.println(sysDescr8+":IfInOctets\n");
String sysDescr9 = client.getAsString(new OID(".1.3.6.1.2.1.2.2.1.16.1"));
System.out.println(sysDescr9+":IfOutOctets\n");
String sysDescr1 = client.getAsString(new OID(".1.3.6.1.2.1.2.2.1.16.1"));
System.out.println(sysDescr1+":IfInDiscards\n");
String  sysDescr = client.getAsString(new OID(".1.3.6.1.2.1.2.2.1.19.1"));
System.out.println(sysDescr+":IfOutDiscards\n");


}

/**
* Start the Snmp session. If you forget the listen() method you will not
* get any answers because the communication is asynchronous
* and the listen() method listens for answers.
* @throws IOException
*/
private void start() throws IOException {
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


public  void calc_bandwidth(double idata[],double odata[],int p,double ifspeed){
    int i,x,j;
    int loop;

        
        double delta_inoctets=(idata[1]-idata[0]);
        double delta_outoctets=(odata[1]-odata[0]);

//        


//        
       		System.out.println("delta inoctets= "+delta_inoctets+" delta_outoctets "+delta_outoctets);
        	
//        
        	double data_in_mb=((double)delta_inoctets)/(1024*1024); 
        double data_out_mb=((double)delta_outoctets)/(1024*1024);
        double data_datarate_in_mbps=data_in_mb/((double)update_freq)*8;
       double data_datarate_out_mbps=data_out_mb/((double)update_freq)*8;
//        
       System.out.println("data_in_mb "+data_in_mb+"data_out_mb "+data_out_mb);
//        
        double in_num=(double)delta_inoctets*8*100;
       double in_denom=(double)update_freq*ifspeed;
       double inbw=in_num/in_denom;
//        
        double out_num=(double)delta_outoctets*8*100;
        double out_denom=(double)update_freq*ifspeed;
        double outbw=out_num/out_denom;
//        
        inbw= Math.floor(inbw * 100000) / 100000;
        outbw= Math.floor(outbw * 100000) / 100000;
//       
        System.out.println("inbw = "+inbw+" outbw = "+outbw);
        System.out.printf("inbw = %.4f  outbw = %.4f \n",inbw,outbw);
//    }
        
        
//    }

//}

}
}

