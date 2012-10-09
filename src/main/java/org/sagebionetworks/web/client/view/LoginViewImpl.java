package org.sagebionetworks.web.client.view;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * The login view implementation.
 * 
 * @author jmhill
 *
 */
public class LoginViewImpl implements LoginView, IsWidget{

	private VerticalPanel vp;
	Presenter presenter;
	TextField<String> userNameField;
	TextField<String> passwordField;

	@Override
	public Widget asWidget() {
		if (vp == null) {
			vp = new VerticalPanel();
			vp.addStyleName("center");
			vp.setSpacing(10);
			createForm();
		}
		return vp;
	}

	
	private void createForm() {
		FormPanel formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setHeading("Synapse Login"); 
		formPanel.setWidth(350);
		formPanel.setLayout(new FlowLayout()); 
		
		Margins margins = new Margins(0, 0, 0, 0);
		FormData formData = new FormData("-20");
		formData.setMargins(margins);
		
	    FieldSet fieldSet = new FieldSet();
	    fieldSet.setBorders(false);
	  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelWidth(75);  
	    fieldSet.setLayout(layout);
		// Basic form data
	    userNameField = new TextField<String>();
	    userNameField.setAllowBlank(false);
	    userNameField.setFieldLabel("username");
	    fieldSet.add(userNameField, formData);
	    // Password field.
	    passwordField = new TextField<String>();
	    passwordField.setAllowBlank(false);
	    passwordField.setFieldLabel("password");
	    passwordField.setPassword(true);
	    fieldSet.add(passwordField, formData); 
	    
	    formPanel.add(fieldSet);
	    formPanel.setButtonAlign(HorizontalAlignment.CENTER);
	    Button loginButton = new Button("Login");
	    loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				// Let the presenter know the user pressed login.
				presenter.login();
			}
	    });
	    formPanel.addButton(loginButton);
	 
	    vp.add(formPanel);
	  }

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public String getUserName() {
		return userNameField.getValue();
	}

	@Override
	public String getPassword() {
		return passwordField.getValue();
	}

	@Override
	public void showInfo(String title, String message) {
		MessageBox.info(title, message, null);
	}


	@Override
	public void showErrorMessage(String message) {
		MessageBox.info("Error", message, null);
	}


}
