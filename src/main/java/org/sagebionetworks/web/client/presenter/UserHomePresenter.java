package org.sagebionetworks.web.client.presenter;

import java.util.List;

import org.sagebionetworks.web.client.UserDataStoreAsync;
import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.cookie.SessionManagerImpl;
import org.sagebionetworks.web.client.place.Login;
import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.view.UserHomeView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * The User home presenter.
 * 
 * @author John
 *
 */
public class UserHomePresenter extends AbstractActivity implements UserHomeView.Presenter{
	
	private UserHome place;
	private PlaceController controller;
	private UserHomeView view;
	private SessionManager sessionManager;
	private UserDataStoreAsync dataStore;
	
	@Inject
	public UserHomePresenter(UserHomeView view, SessionManager sessionManager, UserDataStoreAsync dataStore){
		this.view = view;
		this.sessionManager = sessionManager;
		this.dataStore = dataStore;
		// Wire the view to this presenter
		view.setPresenter(this);
		view.setUserInfo(sessionManager.getUserDisplayName(), sessionManager.getUserEmail());
	}

	@Override
	public void setPlace(UserHome place, PlaceController controller) {
		this.place = place;
		this.controller = controller;
		// Get the user's data list.
		dataStore.getUsersWatchList(sessionManager.getUserPrincipalId(), new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				view.clearList();
				for(String id: result){
					view.addRowToList(new String[]{id});
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.showErrorMessage(caught.getMessage());
			}
		});
		
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Install the view
		panel.setWidget(view);
	}

}
