package org.sagebionetworks.web.client;

import org.sagebionetworks.web.client.presenter.LoginPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MonitorClientModule.class)
public interface MonitorGinjector extends Ginjector {

	EventBus getEventBus();
	
	/**
	 * The login presenter controls logging in.
	 * @return
	 */
	LoginPresenter createLoginPresenter();

}
