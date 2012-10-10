package org.sagebionetworks.web.client.mvp;

import org.sagebionetworks.web.client.MonitorGinjector;
import org.sagebionetworks.web.client.place.Login;
import org.sagebionetworks.web.client.place.UserHome;
import org.sagebionetworks.web.client.presenter.LoginPresenter;
import org.sagebionetworks.web.client.presenter.UserHomePresenter;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

/**
 * Maps places to activities.
 * 
 * @author John
 *
 */
public class AppActivityMapper implements ActivityMapper {

	MonitorGinjector injector;
	PlaceController placeController;
	
	public AppActivityMapper(MonitorGinjector injector, PlaceController placeController) {
		this.injector = injector;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		// If this is a long request then process it.
		if(place instanceof Login){
			// Show the login page.
			return createLoginPlace((Login)place);
		}
		
		// If we do not have a user's session token then the only place we can go is the login place
		if(!injector.getSessionManager().hasSession()){
			return createLoginPlace(new Login(Login.TOKEN_NEW));
		}
		
		// The main page switch.
		if(place instanceof UserHome){
			// the user home
			return createUserHomePlace((UserHome) place);
		}
		else{
			throw new IllegalArgumentException("Unknown place: "+place);
		}
	}

	/**
	 * Fully initialized login presenter.
	 * @param place
	 * @return
	 */
	public Activity createLoginPlace(Login place) {
		LoginPresenter presenter = injector.createLoginPresenter();
		// Wire the presenter with navigation.
		presenter.setPlace(place, placeController);
		return presenter;
	}
	
	/**
	 * Fully initialized user home presenter;
	 * @param place
	 * @return
	 */
	public Activity createUserHomePlace(UserHome place){
		UserHomePresenter presenter = injector.createUserHomePresenter();
		// Wire the presenter with navigation.
		presenter.setPlace(place, placeController);
		return presenter;
	}

	public Place getDefaultPlace() {
		return new Login(Login.TOKEN_NEW);
	}

}
