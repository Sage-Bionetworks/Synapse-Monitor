package org.sagebionetworks.web.client;

import java.util.List;

import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("datastore")
public interface UserDataStore extends RemoteService {
	
	/**
	 * Add an entity to the user's watch list.
	 * @param toAdd
	 */
	public void addEntitToUsersWatchList(String userId, EntityData toAdd);
	
	/**
	 * Get the user's watch list.
	 * @param userId
	 * @return
	 */
	public List<String> getUsersWatchList(String userId);
}
