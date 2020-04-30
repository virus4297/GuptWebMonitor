package snmp;
import java.awt.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import snmp.*;
public class m_bean
{
    public String ipadd;
    public String status;
    public String upt;
    public String downt;
    public String sac;
    public String ip;
    public String ram_usage;
    public String disk_usage;
    
    public String cpu_usage;
    public String bw_usage;
    public String sr_ip;
    public String dest_ip;
    public String lut;
    


   public void setipadd(String a) {
	   ipadd=a;
   }
   public String getipadd() {
	   return ipadd;
   }
   void setstatus(String a) {
	   status=a;
   }
   public String getstatus() {
	   return status;
   }
   void setupt(String a) {
	   upt=a;
   }
   public String getupt() {
	   return upt;
   }
   void setdownt(String a) {
	   downt=a;
   }
   public String getdownt() {
	   return downt;
   }
   void setsac(String a) {
	   sac=a;
   }
   public String getsac() {
	   return sac;
   }
   public void setip(String a) {
	   ip=a;
   }
   public String getip() {
	   return ip;
   }
   void setram_usage(String a) {
	   ram_usage=a;
   }
   public String getram_usage() {
	   return ram_usage;
   }
   void setdisk_usage(String a) {
	   disk_usage=a;
   }
   public String getdisk_usage() {
	   return disk_usage;
   }
   void setcpu_usage(String a) {
	   cpu_usage=a;
   }
   public String getcpu_usage() {
	   return cpu_usage;
   }
   void setbw_usage(String a) {
	   bw_usage=a;
   }
   public String getbw_usage() {
	   return bw_usage;
   }
   void setsr_ip(String a) {
	   sr_ip=a;
   }
   public String getsr_ip() {
	   return sr_ip;
   }
   void setdest_ip(String a) {
	   dest_ip=a;
   }
   public String getdest_ip() {
	   return dest_ip;
   }
   void setlut(String a) {
	   lut=a;
   }
   public String getlut() {
	   return lut;
   }
}
