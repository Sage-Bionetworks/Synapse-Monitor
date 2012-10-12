package org.sagebionetworks.web.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Allows injection into our servlets.
 * 
 * @author John
 *
 */
public class MonitorContextListner extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		// This is where we get to set the injector for our servlets
		return Guice.createInjector(new MonitorServletModule());
	}

}
