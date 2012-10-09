package org.sagebionetworks.web.server;

import org.sagebionetworks.web.server.servlet.SynapseClientImpl;
import org.sagebionetworks.web.server.servlet.SynapseProvider;
import org.sagebionetworks.web.server.servlet.SynapseProviderImpl;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * Binds the service servlets to their paths and any other 
 * Guice binding required on the server side.
 * 
 * @author John
 *
 */
public class MonitorServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		// Setup the Search service
		bind(SynapseClientImpl.class).in(Singleton.class);
		serve("/monitor/synapse").with(SynapseClientImpl.class);
		
		// Synapse Provider
		bind(SynapseProviderImpl.class).in(Singleton.class);
		bind(SynapseProvider.class).to(SynapseProviderImpl.class);
	}
}
