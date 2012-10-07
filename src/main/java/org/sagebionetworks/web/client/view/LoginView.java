package org.sagebionetworks.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Defines communication between the login view and presenter.
 * @author John
 *
 */
public interface LoginView extends IsWidget {
	
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
	public interface Presenter {
		
		/**
		 * Called when the login button is pushed.
		 */
		public void login();
	}

}
