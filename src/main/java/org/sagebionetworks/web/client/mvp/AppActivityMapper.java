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

		// If we to not have a session then the the only place we can go is login
		if(!injector.getSessionManager().hasSession()){
			// Keep the passed place if it is a login place.
			Login newLogin = null;
			if(place instanceof Login){
				newLogin = (Login) place;
			}else{
				// We need a new login place.
				newLogin = new Login(Login.TOKEN_NEW);
			}
			return createLoginPlace(newLogin);
		}
		
		// The main switch
		if(place instanceof Login){
			// Show the login page.
			return createLoginPlace((Login)place);
		}else if(place instanceof UserHome){
			// the user home
			return createUserHomePlace((UserHome) place);
		}else{
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
		return new UserHome("0");
	}

}
