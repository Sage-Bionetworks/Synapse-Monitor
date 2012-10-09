package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.transform.JSONEntityFactory;
import org.sagebionetworks.web.client.view.LoginView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	JSONEntityFactory entityFactory;
	
	@Inject
	public LoginPresenter(CookieProvider cookieProvider, SynapseClientAsync synapseAsynch, LoginView view,JSONEntityFactory entityFactory){
		this.synapseAsynch = synapseAsynch;
		this.view = view;
		this.entityFactory = entityFactory;
		// Set the presenter
		this.view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Install the view
		panel.setWidget(view);
	}

	@Override
	public void login() {
		// Trigger the login
		String username = view.getUserName();
		String password = view.getPassword();
		synapseAsynch.login(username, password, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String json) {
				
				UserSessionData usd = entityFactory.createEntity(json, UserSessionData.class);
				// Set the session token
				view.showInfo("Welcome: ", usd.getProfile().getDisplayName());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.showErrorMessage(caught.getMessage());
			}
		});
		
	}

}
