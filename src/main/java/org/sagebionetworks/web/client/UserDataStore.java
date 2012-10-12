package org.sagebionetworks.web.client;

import java.util.List;

import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("datastore")
public interface UserDataStore extends RemoteService {
	
	/**
	 * Add an entity to the user's watch list.
	 * @param toAdd
	 * @throws SynapseException 
	 */
	public void addEntitToUsersWatchList(String sessionToken, String userId, String entityId) throws SynapseException;
	
	/**
	 * Get the user's watch list.
	 * @param userId
	 * @return
	 * @throws SynapseException 
	 */
	public List<EntityData> getUsersWatchList(String sessionToken, String userId) throws SynapseException;
	
	/**
	 * Login to synapse
	 * @param username
	 * @param password
	 * @return
	 * @throws SynapseException
	 */
	public String getUserData(String token) throws SynapseException;
	
	
	/**
	 * Remove an entity from the watch list.
	 * @param sessionToken
	 * @param userId
	 * @param entityId
	 * @throws SynapseException
	 */
	public void removeEntityFromWatchList(String sessionToken, String userId, String entityId) throws SynapseException;
	
}
