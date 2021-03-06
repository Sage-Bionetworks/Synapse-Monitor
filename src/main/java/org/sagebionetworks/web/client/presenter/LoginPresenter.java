package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.web.client.UserDataStoreAsync;
import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.place.Login;
import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.transform.JSONEntityFactory;
import org.sagebionetworks.web.client.view.LoginView;
import org.sagebionetworks.web.client.view.WaitView;

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
	UserDataStoreAsync dataStore;
	SessionManager sessionManager;
	JSONEntityFactory entityFactory;
	PlaceController controller;
	Login place;
	WaitView messageView;
	
	@Inject
	public LoginPresenter(SessionManager sessionManager, UserDataStoreAsync dataStore, LoginView view, JSONEntityFactory entityFactory, WaitView messageView){
		this.sessionManager = sessionManager;
		this.dataStore = dataStore;
		this.view = view;
		this.entityFactory = entityFactory;
		this.messageView = messageView;
		// Set the presenter
		this.view.setPresenter(this);
	}
	
	@Override
	public void setPlace(Login place, PlaceController controller) {
		this.place = place;
		this.controller = controller;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		// First clear any session data
		sessionManager.clearSession();
		// Disable the logout button and clear the text
		view.disableLogOut();
		
		// Install the view
		panel.setWidget(view);
		// Did the user fail to authenticate?
		if(Login.TOKEN_AUTH_FAILED.equals(place.getToken())){
			view.showErrorMessage("Authentication failed");
		}else if(Login.TOKEN_ERROR.equals(place.getToken())){
			view.showErrorMessage("Failed to connect to Synapse");
		}else if(!Login.TOKEN_NEW.equals(place.getToken())){
			// Try to login with the token that the user provider
			messageView.setMessage("Logging in to Synapse...");
			panel.setWidget(messageView);
			dataStore.getUserData(place.getToken(), new AsyncCallback<String>() {
				
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
					// Let them login again
					panel.setWidget(view);
				}
			});
		}
	}

}
