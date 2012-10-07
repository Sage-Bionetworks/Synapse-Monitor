package org.sagebionetworks.web.client;

import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.cookie.GWTCookieImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Guice binding for this project.
 * 
 * @author John
 *
 */
public class MonitorClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		// Bind the cookie provider
		bind(GWTCookieImpl.class).in(Singleton.class);
		bind(CookieProvider.class).to(GWTCookieImpl.class);

	}

}
