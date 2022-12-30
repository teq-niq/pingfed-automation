<%@page import="java.net.URLEncoder"%>
<%@page import="com.example.config.*" %>
<!DOCTYPE html>
<%
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>OIDC selection Page</title>
</head>
<body>
<%if(request.getUserPrincipal()==null){ %>

Start Authorization code flow and get access token<br/>
<%
String orig1=request.getParameter("orig1");
orig1=URLEncoder.encode(orig1);
for(int i=0;i<CurrentSettings.authorizationCodeDefaultSettings.length;i++)
{
	Settings settings=CurrentSettings.authorizationCodeDefaultSettings[i];
	String problem="";
	Exception e=settings.getSetupException();
	if(e!=null)
	{
		problem=e.getMessage();
	}
	
	%>
<a href="start?selection=<%=i%>&orig=<%=orig1%>">Start</a> for <%=settings.getWellKnown() %> <%=(e!=null?"- Has Problem:":" - Looks OK") %> <%=problem%> <br/>
<%} %>


<%} %>


</body>
</html>