<%@page contentType="text/html; charset=gbk" language="java" %>
<%@page import="java.io.*" %>
<html>
  <head>
<title>云南移动需求管理系统</title>
  </head>
  
<style type="text/css">
<!--
body,td,td {
	font-size: 12px;
}

.box {
  width:150px;
  border: 1px solid #A9C1EB;
}

.error {
  color: red;
}

body {
	background-color: #f3f6f9;
}
-->
</style>

<script language="JavaScript" type="text/javascript">
  function myFocus(){
	if(document.login_form.userId.value == "")
	    document.login_form.userId.focus();
	else
		document.login_form.password.focus();
  }
  function submitFocus(){
  	if(checkForm())
  		document.login_form.submit();
  }
  function checkForm(){
  	if(document.login_form.userId.value == null || document.login_form.userId.value == "")
  		{alert("请输入用户名！");
  		document.login_form.userId.focus();
  		return false;
  		}
  	return true;	
  }
function enter()
{
	if(event.keyCode==13)
	{
		submitFocus();
	}
}
</script>


<body bgcolor="#FFFFFF">
<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" scope="row"><table width="638" height="313" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="321" background="images/indexbg.jpg" scope="row"><br />
            <br />
              <br />
              <br />
              <br />
              <br />
            <br>          
          <table width="276" height="109" border="0" align="right" cellpadding="0" cellspacing="0">
          <tr>
            <td scope="row"><form name="login_form" action="<%= request.getContextPath() %>/login" method="POST">
              <table width="235" height="108" border="0" cellpadding="0" cellspacing="0">
                <tr align="center">
                  <td colspan="2" scope="row">用户名：
                    <input type="text" name="userId" maxlength="32" autocomplete="off" class="box" value=<%=request.getAttribute("userId")==null?"":request.getAttribute("userId")%>>
                </tr>
                <tr align="center">
                  <td colspan="2" scope="row">口　令：
                    <input type="password" name="password" autocomplete="off" onkeypress="enter();" class="box"></td>
                </tr>
                <tr>
                <td>
          		<%=request.getAttribute("wrong")==null?"":"<b><font color=red>"+request.getAttribute("wrong")+"</font></b>"%>
          		</td>
        		</tr>   
                <tr>
                  <td width="77" scope="row">                  	
                  	<a href="javascript:submitFocus();"><img src="images/login.gif" width="52" height="22" border="0"></a></td>
                </tr>
              
              </table>
              
              </form></td>
          </tr>
          
        </table>
                 
        </td>
      </tr>
    </table></td>
  </tr>
</table>

<script language="JavaScript" type="text/javascript">
  myFocus();
</script>

  </body>
</html>
