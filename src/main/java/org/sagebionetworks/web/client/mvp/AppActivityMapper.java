package org.sagebionetworks.web.client.mvp;

import org.sagebionetworks.web.client.MonitorGinjector;
import org.sagebionetworks.web.client.place.LoginPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * Maps places to activities.
 * 
 * @author John
 *
 */
public class AppActivityMapper implements ActivityMapper {

	MonitorGinjector injector;
	
	public AppActivityMapper(MonitorGinjector injector) {
		this.injector = injector;
	}

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof LoginPlace){
			// Show the login page.
			return injector.createLoginPresenter();
		}else{
			throw new IllegalArgumentException("Unknown place: "+place);
		}
	}

	public Place getDefaultPlace() {
		return new LoginPlace("0");
	}

}
