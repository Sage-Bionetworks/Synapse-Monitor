package org.sagebionetworks.web.client;

import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.presenter.LoginPresenter;
import org.sagebionetworks.web.client.presenter.UserHomePresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MonitorClientModule.class)
public interface MonitorGinjector extends Ginjector {

	/**
	 * The main event buss
	 * @return
	 */
	EventBus getEventBus();
	
	/**
	 * The login presenter controls logging in.
	 * @return
	 */
	LoginPresenter createLoginPresenter();
	
	/**
	 * The user home presenter.
	 * @return
	 */
	UserHomePresenter createUserHomePresenter();
	
	
	/**
	 * The session manager keeps track of the user's session.
	 * @return
	 */
	SessionManager getSessionManager();


}
