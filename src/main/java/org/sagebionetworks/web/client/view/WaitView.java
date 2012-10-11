package org.sagebionetworks.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface WaitView extends IsWidget, View  {
	

	/**
	 * Set a wait message.
	 * @param message
	 */
	public void setMessage(String message);
	


}
