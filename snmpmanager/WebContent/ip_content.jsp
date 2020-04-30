
<%@ page import="snmp.*"%>
<%@page import="java.util.*" %>
<%@page import="java.time.*" %>
<%@page import="java.text.*" %>
<!DOCTYPE html>
<html lang="en">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
 <jsp:include page="/ip_servlet" />

 
<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                    <tr>
                      <th>IP Address</th>
                      <th>Action</th>

                    </tr>
                  </thead>

                  <tbody>
                   
					<%
					ArrayList ipdata=(ArrayList)request.getAttribute("ipdata");	
					for ( int i=0;i<ipdata.size();i++)	{%>
					<tr>
					
						<% m_bean c  = (m_bean)ipdata.get(i);%>
						<td>
						<%=c.getip()%>
						
						</td>
                      <td><form action="./delete" method="POST"><button type="submit" class="btn btn-danger" name="ipaddr" value=<%=c.getip() %>>Delete</button></form></td>
						<%} %>
                    </tr>
                  </tbody>
                </table>
                
                
