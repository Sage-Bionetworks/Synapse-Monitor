package org.sagebionetworks.web.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.web.client.place.Login;

import com.google.inject.Inject;
import org.sagebionetworks.repo.model.auth.Session;

/**
 * Login the user to Synapse and save their session information.
 * 
 * @author John
 *
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private Logger log = LogManager.getLogger(LoginServlet.class.getName());
	
	@Inject
	private UserDataStoreImpl userDataStore;
	
	@Override
	protected void doGet(HttpServletRequest rBasic, HttpServletResponse resp)	throws ServletException, IOException {
		
		// Get the username and password
		HttpServletRequest req = (HttpServletRequest)rBasic;
		String referer = req.getHeader("Referer");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		// Did they pass a username and password.
		if(username == null | password == null){
			log.debug("Username or password is null, redirecting as a new login");
			// Send them back to gather username and password
			Login place = new Login(Login.TOKEN_NEW);
			resp.sendRedirect(referer+place.getURL());
			return;
		}
		// Try to authenticate
		try {
			log.debug("Authenticating user: "+username+"...");
			UserSessionData data = userDataStore.login(username, password);
			// Login the user using the token
			Login place = new Login(data.getSession().getSessionToken());
			resp.sendRedirect(referer+place.getURL());
			return;
		} catch (SynapseException e) {
			log.debug("Authentication failed. user: "+username+" message:"+e.getMessage());
			// Authentication failed
			Login place = new Login(Login.TOKEN_AUTH_FAILED);
			resp.sendRedirect(referer+place.getURL());
			return;
		}catch (Throwable e) {
			log("Authentication failed. user: "+username+" message:"+e.getMessage());
			// Authentication failed
			Login place = new Login(Login.TOKEN_ERROR);
			resp.sendRedirect(referer+place.getURL());
			return;
		}
	}

}
