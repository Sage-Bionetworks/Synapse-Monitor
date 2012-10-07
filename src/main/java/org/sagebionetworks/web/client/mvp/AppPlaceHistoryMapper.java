package org.sagebionetworks.web.client.mvp;

import org.sagebionetworks.web.client.place.LoginPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * The mapper
 * @author John
 *
 */
@WithTokenizers({LoginPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper{

}
