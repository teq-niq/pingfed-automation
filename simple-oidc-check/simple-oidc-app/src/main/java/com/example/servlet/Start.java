package com.example.servlet;

import java.io.IOException;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.constants.Urls;
import com.example.util.RedirectToOidc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Start
 */
@WebServlet(urlPatterns = Urls.StartPath, name = "Start")
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Start() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String selection = req.getParameter("selection");
		int selected=0;
		if(selection!=null)
		{
			selected=Integer.parseInt(selection);
		}
		Settings settings=settingsArr[selected];
		
		if(settings.getSetupException()!=null)
		{
			throw new ServletException("Did not load settings properly", settings.getSetupException());
		}
		
		RedirectToOidc.redirectToOidc(req, resp, settings);
	}

	

}
