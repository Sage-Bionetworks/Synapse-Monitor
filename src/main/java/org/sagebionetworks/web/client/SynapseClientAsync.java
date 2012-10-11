package org.sagebionetworks.web.client;

import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Auto generated SynapseClientAsync
 * 
 * @author John
 *
 */
public interface SynapseClientAsync {

	/**
	 * Get the User's data.
	 * 
	 * @param token
	 * @param callback
	 */
	void getUserData(String token, AsyncCallback<String> callback);

	/**
	 * Get data about an entity.
	 * @param token
	 * @param entityId
	 * @param callback
	 */
	void getEntityData(String token, String entityId,
			AsyncCallback<EntityData> callback);

}
