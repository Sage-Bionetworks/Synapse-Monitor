package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.web.client.SynapseClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SynapseClientImpl extends RemoteServiceServlet implements	SynapseClient {

	@Override
	public String login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
