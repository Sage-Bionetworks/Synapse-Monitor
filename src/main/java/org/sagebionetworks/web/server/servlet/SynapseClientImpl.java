package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.EntityFactory;
import org.sagebionetworks.web.client.SynapseClient;
import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class SynapseClientImpl extends RemoteServiceServlet implements	SynapseClient {
	
	public static final String ENTITY_BASE_URL = "https://synapse.sagebase.org/#Synapse:";
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


	@Override
	public EntityData getEntityData(String token, String entityId)
			throws SynapseException {
		Synapse synapse = synapseProvide.createNewSynapse();
		synapse.setSessionToken(token);
		
		// Download the entity
		Entity e = synapse.getEntityById(entityId);
		// Copy over the fields we need
		EntityData data = translateToEntityData(e);
		// Return the data
		return data;
	}


	/**
	 * @param e
	 * @return
	 */
	public static EntityData translateToEntityData(Entity e) {
		EntityData data = new EntityData();
		data.setName(e.getName());
		data.setEtag(e.getEtag());
		data.setDescription(e.getDescription());
		data.setUrl(ENTITY_BASE_URL+e.getId());
		data.setId(e.getId());
		return data;
	}


}
