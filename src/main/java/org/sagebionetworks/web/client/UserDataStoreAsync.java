package org.sagebionetworks.web.client;

import java.util.List;

import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynch UserDataStore
 * 
 * @author jmhill
 *
 */
public interface UserDataStoreAsync {
	
	/**
	 * Get an entity from a user's watch list.
	 * @param userId
	 * @param callback
	 */
	void getUsersWatchList(String sessionToken, String userId, AsyncCallback<List<EntityData>> callback);

	/**
	 * Add an entity to the watch list.
	 * 
	 * @param sessionToken
	 * @param userId
	 * @param entityId
	 * @param callback
	 */
	void addEntitToUsersWatchList(String sessionToken, String userId, String entityId, AsyncCallback<Void> callback);

	/**
	 * Get the datastore.
	 * @param token
	 * @param callback
	 */
	void getUserData(String token, AsyncCallback<String> callback);

	/**
	 * Remove an entity from the watch list.
	 * @param sessionToken
	 * @param userId
	 * @param entityId
	 * @param callback
	 */
	void removeEntityFromWatchList(String sessionToken, String userId,	String entityId, AsyncCallback<Void> callback);


}
