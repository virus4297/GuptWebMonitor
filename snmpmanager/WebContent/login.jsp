<%@page import="snmp.LoginValidate"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Please Login....</title>

<script type="text/javascript">
    function validate_required(field, alerttxt) {
        with (field) {
            if (value == null || value == "") {
                alert(alerttxt);
                return false;
            }
            else {
                return true;
            }
        }
    }

    function validate_Loginform(thisform) {
        with (thisform) {
            if (validate_required(username, "Please enter the username") == false)
            {
                username.focus();
                return false;
            }

            if (validate_required(password, "Please enter the password") == false)
            {
                password.focus();
                return false;
            }
            return true;
        }
    }
</script>
</head>
<body>


<%
 String msg = "";
 String uname = request.getParameter("username");
 String password = request.getParameter("password");
 if(uname != null && password != null && uname.length() > 0 && password.length() > 0){
  LoginValidate obj = new LoginValidate();
  boolean flag = obj.validateUserLogin(uname, password);
  if(flag){
	  response.sendRedirect("monitor.jsp");
  }else{
   msg = "Invalid username or password";
  }
 }
%>

 <!-- <form action="login.jsp" method="post" onsubmit="return validate_Loginform(this)">
  <table width="40%" border="1" align="center">
   <tr>
    <th colspan="2" align="center" valign="top">Please enter login details</th>
   </tr>
   <tr height="50">
    <td valign="middle" align="right">User Name</td>
    <td align="left"><input name="username" size="20" value=""  type="text">
    </td>
   </tr>
   <tr>
    <td valign="middle" align="right">Password</td>
    <td align="left"><input name="password" size="20" value=""  type="password">
    </td>
   </tr>
   <tr height="40">
    <td colspan="2" align="center"><input value="Login" name="B1" type="submit"></td>
   </tr>
  </table>
  
  <br>
  <br>
  <br>
  <br>
  
  <p align="center"> <b><font color="darkred"><%=msg %></font></b></p>

 </form>
</body>
</html>-->




<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"> 

<!------ Include the above in your HEAD tag ---------->
<style>
body {
  margin: 0;
  padding: 0;
  background-color: #17a2b8;
  height: 100vh;
}
body {
  margin: 0;
  padding: 0;
  background-color: #17a2b8;
  height: 100vh;
}
#login .container #login-row #login-column #login-box {
  margin-top: 120px;
  max-width: 600px;
  height: 320px;
  border: 1px solid #9C9C9C;
  background-color: #EAEAEA;
}
#login .container #login-row #login-column #login-box #login-form {
  padding: 20px;
}
#login .container #login-row #login-column #login-box #login-form #register-link {
  margin-top: -85px;
}
</style>
  <body>
    <div id="login">
        
        <div class="container">
            <div id="login-row" class="row justify-content-center align-items-center">
                <div id="login-column" class="col-md-6">
                    <div id="login-box" class="col-md-12">
                        <form id="login-form" class="form" action="login.jsp" method="post" onsubmit="return validate_Loginform(this)">
                            <h3 class="text-center text-info">Login</h3>
                            <div class="form-group">
                                <label for="username" class="text-info">Username:</label><br>
                                <input type="text" name="username" id="username" class="form-control">
                            </div>
                            <div class="form-group">
                                <label for="password" class="text-info">Password:</label><br>
                                <input type="text" name="password" id="password" class="form-control">
                            </div>
                            <div class="form-group">
                                <label for="remember-me" class="text-info"><span>Remember me</span> <span><input id="remember-me" name="remember-me" type="checkbox"></span></label><br>
                                <input type="submit" name="submit" class="btn btn-info btn-md" value="Login">
                            </div>
                            <p align="center"> <b><font color="darkred"><%=msg %></font></b></p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>