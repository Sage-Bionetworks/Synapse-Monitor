package org.sagebionetworks.web.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sagebionetworks.web.client.place.Add;

/**
 * Simply redirects back where we want it.
 * @author jmhill
 *
 */
public class AddServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest rBasic, HttpServletResponse resp)
			throws ServletException, IOException {
		// Get the username and password
		HttpServletRequest req = (HttpServletRequest)rBasic;
		String referer = req.getHeader("Referer");
		if(referer == null) throw new IllegalArgumentException("Referer cannot be null");
		String synapseId = req.getParameter("synapseId");
		if(synapseId == null) throw new IllegalArgumentException("synapseId cannot be null");
		// Redirect them back to an add
		Add add = new Add(synapseId);
		resp.sendRedirect(referer+add.getURL());
	}


	
}
