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
	 * Add an entity to a user's watch list.
	 * @param userId
	 * @param toAdd
	 * @param callback
	 */
	void addEntitToUsersWatchList(String userId, EntityData toAdd,
			AsyncCallback<Void> callback);

	/**
	 * Get an entity from a user's watch list.
	 * @param userId
	 * @param callback
	 */
	void getUsersWatchList(String userId, AsyncCallback<List<String>> callback);

}
