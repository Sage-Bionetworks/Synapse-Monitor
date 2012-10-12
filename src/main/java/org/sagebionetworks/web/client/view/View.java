package org.sagebionetworks.web.client.view;

/**
 * All views should implment this interface.
 * @author jmhill
 *
 */
public interface View {


	/**
	 * Shows user info
	 * @param title
	 * @param message
	 */
	public void showInfo(String title, String message);
	
	/**
	 * Show error message
	 * @param string
	 */
	public void showErrorMessage(String message);
}
