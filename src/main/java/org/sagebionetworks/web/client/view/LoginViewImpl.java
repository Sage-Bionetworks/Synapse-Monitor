package org.sagebionetworks.web.client.view;


import org.sagebionetworks.web.shared.Constants;

import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginViewImpl extends Composite implements LoginView {
	
	public interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl> {}
	
	@UiField
	InputElement userNameInput;
	@UiField
	InputElement passwordInput;
	@UiField
	InputElement loginButton;
	@UiField
	FormElement formPanel;
	Element titleBarLable;
	Element logOutButton;
	
	@Inject
	public LoginViewImpl(LoginViewImplUiBinder binder){
		initWidget(binder.createAndBindUi(this));
		// register as a listener to the form submit
		titleBarLable = DOM.getElementById(Constants.ELE_ID_USER_NAME_TITLE_BAR);
		logOutButton = DOM.getElementById(Constants.ELE_ID_LOGOUT_BUTTON);
	}
	
	private Presenter presenter;

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public String getUserName() {
		return userNameInput.getValue();
	}

	@Override
	public String getPassword() {
		return passwordInput.getValue();
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
	public void disableLogOut() {
		// Clear the title bar
		DOM.setInnerText(titleBarLable, "");
		// disable the logout button
		DOM.setElementAttribute(logOutButton, "type", "hidden");
	}


}
