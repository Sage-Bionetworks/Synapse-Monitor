package org.sagebionetworks.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Represents a user's home page.
 * 
 * @author John
 *
 */
public class UserHome extends Place {
	
    private String userId;

    public UserHome(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
    
    public String getUrl(){
    	return "#UserHome:"+userId;
    }

    public static class Tokenizer implements PlaceTokenizer<UserHome> {
        @Override
        public String getToken(UserHome place) {
            return place.getUserId();
        }

        @Override
        public UserHome getPlace(String token) {
            return new UserHome(token);
        }
    }
    
}
