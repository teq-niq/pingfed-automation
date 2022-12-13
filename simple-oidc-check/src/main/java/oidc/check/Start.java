package oidc.check;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oidc.check.others.CurrentSettings;
import oidc.check.others.Encoder;
import oidc.check.others.Settings;
import oidc.check.others.Urls;

/**
 * Servlet implementation class Start
 */
@WebServlet(urlPatterns = Urls.StartPath, name = "Start")
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATE_KEY = "state";
	private static final String NONCE_KEY = "nonce";
	
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
		req.getSession(true).setAttribute("selected", settings);
		if(settings.getSetupException()!=null)
		{
			throw new ServletException("Did not load settings properly", settings.getSetupException());
		}
		
		String redirectUrl = Urls.getRedirectrUrl(req);
		String state = addCookie(resp, STATE_KEY);
		String nonce = addCookie(resp, NONCE_KEY);
		String loginHint = settings.getLoginHint();
		String atmId = settings.getAtmId();
		String url=settings.getAuthorizationEndpoint()+"?redirect_uri="+
		Encoder.encode(redirectUrl)
		+"&response_type=code&client_id="+settings.getClientId()
		+"&scope="+Encoder.encode("openid email roles")
		+"&state="+Encoder.encode(state)
		+"&nonce="+Encoder.encode(nonce);
		if(loginHint!=null)
		{
			url+="&login_hint="+Encoder.encode(loginHint)	;
		}
		if(atmId!=null)
		{
			url+="&access_token_manager_id="+Encoder.encode(atmId)	;
		}
		
		
		
		System.out.println("url="+url);
		
		//String url="https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=http%3A%2F%2Flocalhost:8080%2Foidc-hello&prompt=consent&response_type=code&client_id=503443466006-65bvcaa25n1ba1lqqd4u72eeg1bspl9p.apps.googleusercontent.com&scope=openid&access_type=offline";
		resp.sendRedirect(url);
	}

	
	
	private String addCookie(HttpServletResponse resp, String cookieKey) {
		String val=UUID.randomUUID().toString();
		Cookie stateCookie = new Cookie(cookieKey, val);
		stateCookie.setMaxAge(60*2);
		stateCookie.setHttpOnly(true);
		resp.addCookie(stateCookie);
		return val;
	}

}
