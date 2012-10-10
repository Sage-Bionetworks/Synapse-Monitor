package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.place.Login;
import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.transform.JSONEntityFactory;
import org.sagebionetworks.web.client.view.LoginView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
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
	SessionManager sessionManager;
	JSONEntityFactory entityFactory;
	PlaceController controller;
	Login place;
	
	@Inject
	public LoginPresenter(SessionManager sessionManager, SynapseClientAsync synapseAsynch, LoginView view, JSONEntityFactory entityFactory){
		this.sessionManager = sessionManager;
		this.synapseAsynch = synapseAsynch;
		this.view = view;
		this.entityFactory = entityFactory;
		// Set the presenter
		this.view.setPresenter(this);
	}
	
	@Override
	public void setPlace(Login place, PlaceController controller) {
		this.place = place;
		this.controller = controller;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// First clear any session data
		sessionManager.clearSession();
		// Install the view
		panel.setWidget(view);
		// Did the user fail to authenticate?
		if(Login.TOKEN_AUTH_FAILED.equals(place.getToken())){
			view.showErrorMessage("Authentication failed");
		}else if(Login.TOKEN_ERROR.equals(place.getToken())){
			view.showErrorMessage("Failed to connect to Synapse");
		}else if(!Login.TOKEN_NEW.equals(place.getToken())){
			// Try to login with the token that the user provider
			synapseAsynch.getUserData(place.getToken(), new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String json) {
					// Convert json to the object.
					UserSessionData usd = entityFactory.createEntity(json, UserSessionData.class);
					// Save the session
					sessionManager.saveSession(usd);
					// Send the user to their home page
					controller.goTo(new UserHome(sessionManager.getUserPrincipalId()));
				}
				
				@Override
				public void onFailure(Throwable caught) {
					view.showErrorMessage(caught.getMessage());
				}
			});
		}
	}
	


	@Override
	public void login() {

	}



}
