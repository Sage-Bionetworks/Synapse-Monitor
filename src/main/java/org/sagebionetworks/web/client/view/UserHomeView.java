package org.sagebionetworks.web.client.view;

import org.sagebionetworks.web.client.mvp.ActivityPresenter;
import org.sagebionetworks.web.client.place.UserHome;

import com.google.gwt.user.client.ui.IsWidget;

public interface UserHomeView extends IsWidget, View  {
	
	/**
	 * Set this view's presenter
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);
	
	/**
	 * 
	 * @param username
	 * @param email
	 */
	public void setUserInfo(String username, String email);
	
	
	/**
	 * The presenter contract.
	 *
	 */
	public interface Presenter extends ActivityPresenter<UserHome> {
		

	}

}
