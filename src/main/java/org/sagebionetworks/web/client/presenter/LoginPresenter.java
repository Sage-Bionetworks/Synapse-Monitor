package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.view.LoginView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * The main controller for logging in to Synapse
 * 
 * @author John
 *
 */
public class LoginPresenter extends AbstractActivity implements LoginView.Presenter {
	
	LoginView view;
	SynapseClientAsync synapseAsynch;
	CookieProvider cookieProvider;
	
	@Inject
	public LoginPresenter(CookieProvider cookieProvider, SynapseClientAsync synapseAsynch, LoginView view){
		this.synapseAsynch = synapseAsynch;
		this.view = view;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Install the view
		panel.setWidget(view);
	}

	@Override
	public void login() {
		// Trigger the login
		
		
	}

}
