package org.sagebionetworks.web.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.web.client.place.Login;

import com.google.inject.Inject;

/**
 * Login the user to Synapse and save their session information.
 * 
 * @author John
 *
 */
public class LoginServlet extends HttpServlet {

	@Inject
	private SynapseProvider synapseProvide;
	
	@Override
	protected void doGet(HttpServletRequest rBasic, HttpServletResponse resp)	throws ServletException, IOException {
		
		// Get the username and password
		HttpServletRequest req = (HttpServletRequest)rBasic;
		String username = req.getParameter("username");
		String referer = req.getHeader("Referer");
		String password = req.getParameter("password");
		// Try to authenticate
		try {
			UserSessionData data = synapseProvide.createNewSynapse().login(username, password);
			// Login the user using the token
			Login place = new Login(data.getSessionToken());
			resp.sendRedirect(referer+place.getURL());
			return;
		} catch (SynapseException e) {
			// Authentication failed
			Login place = new Login(Login.TOKEN_AUTH_FAILED);
			resp.sendRedirect(referer+place.getURL());
			return;
		}catch (Throwable e) {
			// Authentication failed
			Login place = new Login(Login.TOKEN_ERROR);
			resp.sendRedirect(referer+place.getURL());
			return;
		}
	}

}
