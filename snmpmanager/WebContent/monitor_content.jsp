<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="snmp.*"%>
<%@page import="java.util.*" %>
<%@page import="java.time.*" %>
<%@page import="java.text.*" %>
<!DOCTYPE html>
<html lang="en">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
 <jsp:include page="/monitor_servlet" />

 
          <div class="row" >

   <%

					ArrayList data1=(ArrayList)request.getAttribute("data");	
                    		int x=0,y=0,z=0,b=0;
					for ( int i=0;i<data1.size();i++)	{%>
					<tr>
					
						<% m_bean d = (m_bean)data1.get(i);%>

						<% 
						if("UP".equals(d.getstatus())){
									x++;
											}
								else{
									y++;
								}
						if("YES".equals(d.getsac())){
							z++;
									}
						else{
							b++;
						}
						
					%>
					
					<% }%>

            <!-- Earnings (Monthly) Card Example -->
            <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-success text-uppercase mb-1">UP</div>
                      <div class="h5 mb-0 font-weight-bold text-gray-800"><%=x %></div>
                    </div>

                  </div>
                </div>
              </div>
            </div>



            <!-- Pending Requests Card Example -->
            <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-danger shadow h-100 py-2">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">Down</div>
                      <div class="h5 mb-0 font-weight-bold text-gray-800"><%=y %></div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
                        <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-success text-uppercase mb-1">Snmp Active</div>
                      <div class="h5 mb-0 font-weight-bold text-gray-800"><%=z %></div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
                        <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-danger shadow h-100 py-2">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">Snmp Not Active</div>
                      <div class="h5 mb-0 font-weight-bold text-gray-800"><%=b %></div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Content Row -->

          <div class="card shadow mb-4" >
            <div class="card-header py-3">
              <h6 class="m-0 font-weight-bold text-primary">Ping Result</h6>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                 <thead>
					<tr>		
						<th>IP Address</th>
						<th>Status</th>
						<th>SNMP Status</th>
						<th>Last up time</th>
						<th>Last Down time</th>
						<th>Description</th>

					</tr>
				</thead>
				<%

					ArrayList data=(ArrayList)request.getAttribute("data");	
					for ( int i=0;i<data.size();i++)	{%>
					<tr>
					
						<% m_bean d = (m_bean)data.get(i);%>
						<td>
						<%=d.getipadd()%>
						</td>
						<td>
						<% 
						if("UP".equals(d.getstatus())){
									%><button class="btn btn-success" data-toggle="modal" data-target="#exampleModalLong">UP</Button></td><%
											}
								else{
									%><button class="btn btn-danger" style="pointer-events: none;">DOWN</Button></td> <%
								}
						
					%>
					<td>
						<% 
						if("YES".equals(d.getsac())){
									%><button class="btn btn-success" data-toggle="modal" data-target="#exampleModalLong">ACTIVE</Button></td><%
											}
								else{
									%><button class="btn btn-danger" style="pointer-events: none;">NOT ACTIVE</Button></td> <%
								}
						
					%>
					
					<%
					String s1= d.getupt();
					String s2= d.getdownt();
					Date d1 = new SimpleDateFormat("d MMM yyyy HH:mm:ss").parse(s1);
					Date d2 = new SimpleDateFormat("d MMM yyyy HH:mm:ss").parse(s2);
					
				%>
				<td><%=s1 %></td>
				<td><%=s2 %></td>
					
						<% 
						if("UP".equals(d.getstatus())){
							long diff = d1.getTime() - d2.getTime();

							long diffSeconds = diff / 1000 % 60;
							long diffMinutes = diff / (60 * 1000) % 60;
							long diffHours = diff / (60 * 60 * 1000) % 24;
							long diffDays = diff / (24 * 60 * 60 * 1000); 
												%><td>
												<p>Up Since:</p>
												<p>Days:<%=diffDays %></p>
												<p>Hours:<%=diffHours %></p>
												<p>Minute:<%=diffMinutes %></p>
												<p>Seconds:<%=diffSeconds %></p>
												
												</td><% 
							
							
									}
						else{
						
							long diff = d2.getTime() - d1.getTime();

							long diffSeconds = diff / 1000 % 60;
							long diffMinutes = diff / (60 * 1000) % 60;
							long diffHours = diff / (60 * 60 * 1000) % 24;
							long diffDays = diff / (24 * 60 * 60 * 1000); 
							%>
										
												<td>
												<p>Down Since:</p>
												<p>Days:<%=diffDays %></p>
												<p>Hours:<%=diffHours %></p>
												<p>Minute:<%=diffMinutes %></p>
												<p>Seconds:<%=diffSeconds %></p>
												</td><% 
						}
						%>



					<% }%>

				</tr>
				

				
			</table>
                </table>
              </div>
            </div>
          </div>


      <!-- End of Main Content -->

      <!-- Footer -->
      <footer class="sticky-footer bg-white">
        <div class="container my-auto">
          <div class="copyright text-center my-auto">
            <span>Copyright &copy; Your Website 2019</span>
          </div>
        </div>
      </footer>
      <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

  </div>
  <!-- End of Page Wrapper -->



  <!-- Logout Modal-->
  