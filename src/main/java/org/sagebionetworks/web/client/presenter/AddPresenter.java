package org.sagebionetworks.web.client.presenter;


import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.mvp.ActivityPresenter;
import org.sagebionetworks.web.client.place.Add;
import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.view.WaitView;
import org.sagebionetworks.web.shared.EntityData;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class AddPresenter extends AbstractActivity implements ActivityPresenter<Add> {
	
	
	private Add place;
	private PlaceController controller;
	private WaitView view;
	SynapseClientAsync client;
	SessionManager session;
	
	@Inject
	public AddPresenter(WaitView view, SynapseClientAsync client, SessionManager session){
		this.view = view;
	}

	@Override
	public void setPlace(Add place, final PlaceController controller) {
		this.place = place;
		this.controller = controller;
		this.view.setMessage("Adding Entity: "+place.getToken()+"...");
		// Fetch the entity
		this.client.getEntityData(session.getSessionToken(), place.getToken(), new AsyncCallback<EntityData>() {
			
			@Override
			public void onSuccess(EntityData result) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.showErrorMessage(caught.getMessage());
				controller.goTo(new UserHome(session.getUserPrincipalId()));
			}
		});
		
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
	}

}
