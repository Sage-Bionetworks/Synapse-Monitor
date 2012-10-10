package org.sagebionetworks.web.client;

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

}
