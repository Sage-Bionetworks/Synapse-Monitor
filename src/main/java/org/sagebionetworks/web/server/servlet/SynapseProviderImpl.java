package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.client.SynapseClientImpl;

/**
 * A very simple synapse Provider.
 * @author jmhill
 *
 */
public class SynapseProviderImpl implements SynapseProvider {

	@Override
	public SynapseClientImpl createNewSynapse() {
		return new SynapseClientImpl();
	}

}
