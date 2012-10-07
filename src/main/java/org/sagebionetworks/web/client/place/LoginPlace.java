package org.sagebionetworks.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * The login place.
 * @author John
 *
 */
public class LoginPlace extends Place {

    private String loginName;

    public LoginPlace(String token) {
        this.loginName = token;
    }

    public String getHelloName() {
        return loginName;
    }

    public static class Tokenizer implements PlaceTokenizer<LoginPlace> {
        @Override
        public String getToken(LoginPlace place) {
            return place.getHelloName();
        }

        @Override
        public LoginPlace getPlace(String token) {
            return new LoginPlace(token);
        }
    }

}
