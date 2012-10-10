package org.sagebionetworks.web.client.mvp;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.Place;

public interface ActivityPresenter<T extends Place> {
	
	/**
	 * Binds to the presenter to a place and controller.
	 * 
	 * @param place
	 * @param controller
	 */
	public void setPlace(T place, PlaceController controller);

}
