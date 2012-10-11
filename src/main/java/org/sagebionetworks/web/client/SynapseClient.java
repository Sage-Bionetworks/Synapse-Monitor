package org.sagebionetworks.web.client;

import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Defines the RCP calls for communicating with Synapse.
 * @author John
 *
 */
@RemoteServiceRelativePath("synapse")
public interface SynapseClient extends RemoteService {
	
	/**
	 * Login to synapse
	 * @param username
	 * @param password
	 * @return
	 * @throws SynapseException
	 */
	public String getUserData(String token) throws SynapseException;
	
	/**
	 * Get data about an entity.
	 * 
	 * @param token
	 * @param entityId
	 * @return
	 * @throws SynapseException
	 */
	public EntityData getEntityData(String token, String entityId) throws SynapseException;

}
