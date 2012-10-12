package org.sagebionetworks.web.client.presenter;

import java.util.List;

import org.sagebionetworks.web.client.UserDataStoreAsync;
import org.sagebionetworks.web.client.cookie.SessionManager;
import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.view.UserHomeView;
import org.sagebionetworks.web.shared.EntityData;

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
	
	private static final String[] tableHeaders = new String[]{
		"SynapseId",
		"Name",
		"Description",
		"",
	};
	
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
		view.setTableHeaders(tableHeaders);
	}

	@Override
	public void setPlace(UserHome place, PlaceController controller) {
		this.place = place;
		this.controller = controller;
		// Set the user's name on the header
		// Set the username
		view.setUserDisplayName(sessionManager.getUserDisplayName());
		// Get the user's data list.
		dataStore.getUsersWatchList(sessionManager.getSessionToken(), sessionManager.getUserPrincipalId(), new AsyncCallback<List<EntityData>>() {
			@Override
			public void onSuccess(List<EntityData> result) {
				view.clearList();
				for(EntityData data: result){
					String[] row = new String[]{
							createLink(data.getId(), data.getUrl()),
							data.getName(),
							data.getDescription(),
							buildRemoveForm(data.getId()),
					};
					view.addRowToList(row);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				view.showErrorMessage(caught.getMessage());
			}
		});
		
	}
	
	private static String createLink(String display, String url){
		StringBuilder builder = new StringBuilder();
		builder.append("<a href=\"");
		builder.append(url);
		builder.append("\">");
		builder.append(display);
		builder.append("</a>");
		return builder.toString();
	}
	
	/**
	 * Setup a remove button for each entity.
	 * @param entityId
	 * @return
	 */
	private static String buildRemoveForm(String entityId){
		StringBuilder builder = new StringBuilder();
		builder.append("<form action=\"/monitor/service/edit/remove\" method=\"post\">");
		builder.append("<input type=\"hidden\" name=\"synapseId\" value=\"");
		builder.append(entityId);
		builder.append("\">");
		builder.append("<input type=\"image\" src=\"images/remove-ask.png\" alt=\"remove\"\">");
		builder.append("</form>");
		return builder.toString();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Install the view
		panel.setWidget(view);
	}

}
