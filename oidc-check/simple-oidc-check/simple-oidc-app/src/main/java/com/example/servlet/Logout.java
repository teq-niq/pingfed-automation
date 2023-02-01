package com.example.servlet;
import java.io.IOException;

import com.example.constants.Urls;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@WebServlet(urlPatterns = Urls.Logout, name = "Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	

	private ObjectMapper objectMapper=ObjectMapperHolder.mapper;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.logout();
		HttpSession existingSessionIfAny = req.getSession(false);
		if(existingSessionIfAny!=null)
		{
			existingSessionIfAny.invalidate();
		}
		resp.sendRedirect((req.getContextPath() + Urls.WelcomePath));
		
	}

	

	
	
	


	

	

}
