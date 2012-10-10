package org.sagebionetworks.web.client.view;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class UserHomeViewImpl extends Composite implements UserHomeView {
	
	Presenter presenter;
	String username;
	String email;

	public interface  UserHomeViewImplUiBinder extends UiBinder<Widget,  UserHomeViewImpl> {}
	
	@Inject
	public  UserHomeViewImpl(UserHomeViewImplUiBinder binder){
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setUserInfo(String username, String email) {
		this.username = username;
		this.email = email;
	}

}
