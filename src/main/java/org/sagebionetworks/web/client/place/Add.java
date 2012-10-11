package org.sagebionetworks.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Add an entity.
 * 
 * @author jmhill
 *
 */
public class Add extends Place{
	
	String token = null;
	
	public Add(String token){
		this.token = token;
	}
	
	public String getToken(){
		return token;
	}
	
	
    public static class Tokenizer implements PlaceTokenizer<Add> {
        @Override
        public String getToken(Add place) {
            return place.getToken();
        }

        @Override
        public Add getPlace(String token) {
            return new Add(token);
        }
    }

    /**
     * The URL of this add
     * @return
     */
    public String getURL() {
		return "#Add:"+token;
	}

}
