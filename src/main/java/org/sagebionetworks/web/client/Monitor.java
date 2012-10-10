package org.sagebionetworks.web.client;

import org.sagebionetworks.web.client.mvp.AppActivityMapper;
import org.sagebionetworks.web.client.mvp.AppPlaceHistoryMapper;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Monitor implements EntryPoint {

	// The Gin entry point.
	private final MonitorGinjector injector = GWT.create(MonitorGinjector.class);
	
	private SimplePanel appWidget = new SimplePanel();


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    EventBus eventBus = injector.getEventBus();
	    PlaceController placeController = new PlaceController(eventBus);

		// Start ActivityManager for the main widget with our ActivityMapper
		AppActivityMapper activityMapper = new AppActivityMapper(injector, placeController);
		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
		activityManager.setDisplay(appWidget);
		
		// All pages get added to the root panel
		appWidget.addStyleName("rootPanel");

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);		
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);		
		historyHandler.register(placeController, eventBus, activityMapper.getDefaultPlace());

		RootPanel.get("rootPanel").add(appWidget);
		
		// Goes to place represented on URL or default place
		historyHandler.handleCurrentHistory();
	}
}
