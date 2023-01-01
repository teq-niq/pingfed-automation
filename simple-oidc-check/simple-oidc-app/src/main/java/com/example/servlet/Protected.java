package com.example.servlet;
import java.io.IOException;
import java.io.PrintWriter;

import com.example.constants.Urls;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet(urlPatterns = Urls.ProtectedPath, name = "Protected")
public class Protected extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
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
		
		
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h1>Reached Protected &nbsp;&nbsp; Hello "+request.getRemoteUser()+"</h1>");
		out.println("<a href=\"logout\">Logout</a><br/>");
		out.println("<a href=\"/\">Home</a><br/>");
		userInRole(request, out, "email");
		userInRole(request, out, "foo");
		userInRole(request, out, "bar");
		out.println("contextPath="+contextPath+"<br/>");
		out.println("requestURI="+requestURI+"<br/>");
		out.println("pathInfo="+pathInfo+"<br/>");
		out.println("queryString="+queryString+"<br/>");
		out.println("contextPath="+contextPath+"<br/>");
		out.println("</body></html>");
	}

	private void userInRole(HttpServletRequest request, PrintWriter out, String roleName) {
		boolean userInRole = request.isUserInRole(roleName);
		out.println("user has role:"+roleName+"="+userInRole+"<br/>");
	}

	

	
	
	


	

	

}
