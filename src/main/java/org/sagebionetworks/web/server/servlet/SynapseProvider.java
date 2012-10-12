package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.client.Synapse;

/**
 * Abstraction for creating a Synapse object.
 * @author jmhill
 *
 */
public interface SynapseProvider {
	
	/**
	 * Create a new Syanpse object
	 * @return
	 */
	public Synapse createNewSynapse();

}
