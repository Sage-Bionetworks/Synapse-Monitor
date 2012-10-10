package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.view.UserHomeView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
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
	
	@Inject
	public UserHomePresenter(UserHomeView view){
		this.view = view;
		// Wire the view to this presenter
		view.setPresenter(this);
	}

	@Override
	public void setPlace(UserHome place, PlaceController controller) {
		this.place = place;
		this.controller = controller;		
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Install the view
		panel.setWidget(view);
	}

}
