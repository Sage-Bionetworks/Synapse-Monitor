package org.sagebionetworks.web.client;

import org.sagebionetworks.client.exceptions.SynapseException;

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
	public String login(String username, String password) throws SynapseException;

}
