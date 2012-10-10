package org.sagebionetworks.web.client;

import org.sagebionetworks.gwt.client.schema.adapter.GwtAdapterFactory;
import org.sagebionetworks.gwt.client.schema.adapter.JSONArrayGwt;
import org.sagebionetworks.gwt.client.schema.adapter.JSONObjectGwt;
import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.JSONArrayAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.cookie.GWTCookieImpl;
import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.cookie.SessionManagerImpl;
import org.sagebionetworks.web.client.transform.JSONEntityFactory;
import org.sagebionetworks.web.client.transform.JSONEntityFactoryImpl;
import org.sagebionetworks.web.client.view.LoginView;
import org.sagebionetworks.web.client.view.LoginViewImpl;
import org.sagebionetworks.web.client.view.UserHomeView;
import org.sagebionetworks.web.client.view.UserHomeViewImpl;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
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
		
		// Event Bus
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		// Bind the cookie provider
		bind(GWTCookieImpl.class).in(Singleton.class);
		bind(CookieProvider.class).to(GWTCookieImpl.class);
		
		// bind the session manager
		bind(SessionManagerImpl.class).in(Singleton.class);
		bind(SessionManager.class).to(SessionManagerImpl.class);
		
		// Bind the views
		// LoginView
		bind(LoginViewImpl.class).in(Singleton.class);
		bind(LoginView.class).to(LoginViewImpl.class);
		// UserHome
		bind(UserHomeViewImpl.class).in(Singleton.class);
		bind(UserHomeView.class).to(UserHomeViewImpl.class);
		
		// JSONAdapters
		bind(JSONObjectAdapter.class).to(JSONObjectGwt.class);
		bind(JSONArrayAdapter.class).to(JSONArrayGwt.class);
		
		// JSONEntityFactory
		bind(JSONEntityFactoryImpl.class).in(Singleton.class);
		bind(JSONEntityFactory.class).to(JSONEntityFactoryImpl.class);
		// Adapter factory
		bind(AdapterFactory.class).to(GwtAdapterFactory.class);

	}

}
