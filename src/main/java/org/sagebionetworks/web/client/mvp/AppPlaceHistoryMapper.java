package org.sagebionetworks.web.client.mvp;

import org.sagebionetworks.web.client.place.Add;
import org.sagebionetworks.web.client.place.Login;
import org.sagebionetworks.web.client.place.UserHome;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * The mapper
 * @author John
 *
 */
@WithTokenizers({Login.Tokenizer.class, UserHome.Tokenizer.class, Add.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper{

}
