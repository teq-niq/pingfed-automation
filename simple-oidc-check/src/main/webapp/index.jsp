<%@ page import="oidc.check.others.*" %>
<!DOCTYPE html>
<%
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
Start Authorization code flow and get access token<br/>
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