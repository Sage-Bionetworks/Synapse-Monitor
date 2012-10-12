package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.client.Synapse;

/**
 * A very simple synapse Provider.
 * @author jmhill
 *
 */
public class SynapseProviderImpl implements SynapseProvider {

	@Override
	public Synapse createNewSynapse() {
		return new Synapse();
	}

}
