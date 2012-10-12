package org.sagebionetworks.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * The login place.
 * @author John
 *
 */
public class Login extends Place {
	
	/**
	 * Token strings used to indicate state other an a user's session token.
	 */
	public static String TOKEN_ERROR = "ERROR";
	public static String TOKEN_AUTH_FAILED = "AUTH_FAILED";
	public static String TOKEN_NEW = "0";
	
    private String token;

    public Login(String token) {
        this.token = token;
    }

    public String getToken(){
    	return this.token;
    }

    public static class Tokenizer implements PlaceTokenizer<Login> {
        @Override
        public String getToken(Login place) {
            return place.getToken();
        }

        @Override
        public Login getPlace(String token) {
            return new Login(token);
        }
    }
    
    public String getURL(){
    	return "#Login:"+token;
    }
    
}
