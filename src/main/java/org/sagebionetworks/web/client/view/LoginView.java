package org.sagebionetworks.web.client.view;

import org.sagebionetworks.web.client.mvp.ActivityPresenter;
import org.sagebionetworks.web.client.place.Login;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Defines communication between the login view and presenter.
 * @author John
 *
 */
public interface LoginView extends IsWidget, View  {
	
	/**
	 * Set this view's presenter
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);
	
	/**
	 * Get the username from the view.
	 * @return
	 */
	public String getUserName();
	
	/**
	 * Get the password from the view.
	 * @return
	 */
	public String getPassword();
	
	/**
	 * The presenter contract.
	 *
	 */
	public interface Presenter extends ActivityPresenter<Login> {
		
	}
	
	/**
	 * Disable the logout button
	 */
	public void disableLogOut();



}
