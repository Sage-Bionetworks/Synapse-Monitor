package org.sagebionetworks.web.client.view;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Simple view that shows a message and a wait 
 * @author jmhill
 *
 */
public class WaitViewImpl extends Composite implements WaitView {
	
	public interface WaitViewImplUiBinder extends UiBinder<Widget,WaitViewImpl> {}
	
	@UiField
	HeadingElement addingHeader;
	
	@Inject
	public WaitViewImpl(WaitViewImplUiBinder binder){
		initWidget(binder.createAndBindUi(this));
	}

	
	@Override
	public void showInfo(String title, String message) {
		Window.alert(message);
	}

	@Override
	public void showErrorMessage(String message) {
		Window.alert(message);
	}

	@Override
	public void setMessage(String message) {
		addingHeader.setInnerText(message);
	}

}
