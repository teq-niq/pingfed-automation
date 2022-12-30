<%@ page import="com.example.config.*" %>
<!DOCTYPE html>
<%
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welcome Page</title>
</head>
<body>
<%if(request.getUserPrincipal()!=null){ %>
Hello <%= request.getUserPrincipal().getName()%><br/>
<a href="logout">Logout</a><br/>
<%} %>
<a href="protected">Protected link click to start authorization code flow if not logged on already</a>
<br/>
<%if(request.getUserPrincipal()==null){ %>
You can start Authorization code flow and get access token<br/>
<%

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
<a href="start?selection=<%=i%>">Start</a> for <%=settings.getWellKnown() %> <%=(e!=null?"- Has Problem:":" - Looks OK") %> <%=problem%> <br/>
<%} %>
<%} %>
Note: Client credentials grant flow is not meant for web applications. Just having a convenient demo below.<br/>
Try Client Credentials Grant flow  and get access token<br/>
<%
for(int i=0;i<CurrentSettings.clientCredentialsDefaultSettings.length;i++)
{
	Settings settings=CurrentSettings.clientCredentialsDefaultSettings[i];
	String problem="";
	Exception e=settings.getSetupException();
	if(e!=null)
	{
		problem=e.getMessage();
	}
	%>
<a href="ccgfd?selection=<%=i%>">Try</a> for <%=settings.getWellKnown() %> <%=(e!=null?"- Has Problem:":" - Looks OK") %> <%=problem%> <br/>
<%} %>

</body>
</html>