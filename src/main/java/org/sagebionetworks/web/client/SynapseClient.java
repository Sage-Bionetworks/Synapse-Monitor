package org.sagebionetworks.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Defines the RCP calls for communicating with Synapse.
 * @author John
 *
 */
@RemoteServiceRelativePath("synapse")
public interface SynapseClient extends RemoteService {
	
	public String login(String username, String password);

}
