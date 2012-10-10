package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.EntityFactory;
import org.sagebionetworks.web.client.SynapseClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class SynapseClientImpl extends RemoteServiceServlet implements	SynapseClient {
	
	@Inject
	private SynapseProvider synapseProvide;


	@Override
	public String getUserData(String token) throws SynapseException {
		Synapse synapse = synapseProvide.createNewSynapse();
		synapse.setSessionToken(token);
		UserSessionData data = synapse.getUserSessionData();
		try {
			return EntityFactory.createJSONStringForEntity(data);
		} catch (JSONObjectAdapterException e) {
			throw new SynapseException(e);
		}
	}

}
