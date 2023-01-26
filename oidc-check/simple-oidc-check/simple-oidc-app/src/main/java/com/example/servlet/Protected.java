package com.example.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.constants.Urls;
import com.example.oidc.principal.OidcPrincipal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet(urlPatterns = Urls.ProtectedPath, name = "Protected")
public class Protected extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Protected() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Principal userPrincipal = request.getUserPrincipal();
		String userId=null;
		String[] allScopes= {};
		
		if(userPrincipal!=null)
		{
			
			if(userPrincipal instanceof OidcPrincipal)
			{
				OidcPrincipal oidcPrincipal=(OidcPrincipal)request.getUserPrincipal();
				userId=oidcPrincipal.getUserId();
				Settings settings = settingsArr[oidcPrincipal.getSettingsIndex()];
				allScopes=settings.getScopes();
			}
		}
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h1>Reached Protected &nbsp;&nbsp; Hello "+request.getRemoteUser()+"[userId:"+userId+"]</h1>");
		out.println("<a href=\"logout\">Logout</a><br/>");
		out.println("<a href=\"/\">Home</a><br/>");
		for (String string : allScopes) {
			userInRole(request, out, string);
		}
		
		out.println("contextPath="+contextPath+"<br/>");
		out.println("requestURI="+requestURI+"<br/>");
		out.println("pathInfo="+pathInfo+"<br/>");
		out.println("queryString="+queryString+"<br/>");
		
		out.println("</body></html>");
	}

	private void userInRole(HttpServletRequest request, PrintWriter out, String roleName) {
		boolean userInRole = request.isUserInRole(roleName);
		out.println("user has role:"+roleName+"="+userInRole+"<br/>");
	}

	

	
	
	


	

	

}
