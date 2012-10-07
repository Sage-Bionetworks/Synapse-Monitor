package org.sagebionetworks.web.client.view;

import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl implements LoginView {
	
	Presenter presenter;

	@Override
	public Widget asWidget() {
		
		return null;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

}
