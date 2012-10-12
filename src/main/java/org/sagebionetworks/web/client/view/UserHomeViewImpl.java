package org.sagebionetworks.web.client.view;

import org.sagebionetworks.web.shared.Constants;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class UserHomeViewImpl extends Composite implements UserHomeView {
		
	Presenter presenter;
	String username;
	String email;
	@UiField
	ParagraphElement infoBox;
	@UiField
	TableElement table;
	@UiField
	TableSectionElement tableHead;
	@UiField
	TableSectionElement tableBody;
	Element titleBarLable;
	Element logOutButton;

	public interface  UserHomeViewImplUiBinder extends UiBinder<Widget,  UserHomeViewImpl> {}
	
	@Inject
	public  UserHomeViewImpl(UserHomeViewImplUiBinder binder){
		initWidget(binder.createAndBindUi(this));
		// register as a listener to the form submit
		titleBarLable = DOM.getElementById(Constants.ELE_ID_USER_NAME_TITLE_BAR);
		logOutButton = DOM.getElementById(Constants.ELE_ID_LOGOUT_BUTTON);
	}

	/**
	 * @param columns
	 */
	public void setTableHeaders(String[] columns) {
		TableRowElement thr = TableRowElement.as(DOM.createTR());
		for(String colHeader: columns){
			Element th = DOM.createTH();
			DOM.setInnerText(th, colHeader);
			thr.appendChild(th);
		}
		tableHead.appendChild(thr);
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
		infoBox.setInnerHTML("The following is the list of entities you are currently watching. When any of these entities change you will be notified by email at: <b><em>"+email+"</em></b>");
	}

	@Override
	public void clearList() {
		// Remove all children
		while(tableBody.getChildCount() > 0){
			Node child = tableBody.getChild(0);
			tableBody.removeChild(child);
		}
	}

	@Override
	public void addRowToList(String[] row) {
		TableRowElement tr = TableRowElement.as(DOM.createTR());
		for(int col=0; col<row.length; col++){
			Element td = DOM.createTD();
			// set the text
			DOM.setInnerHTML(td, row[col]);
			tr.appendChild(td);
		}
		tableBody.appendChild(tr);
	}
	
	@Override
	public void setUserDisplayName(String userDisplayName) {
		// Set the user in the Title bar
		DOM.setInnerText(titleBarLable, "Welcome, "+userDisplayName);
		// enable the logout button
		DOM.setElementAttribute(logOutButton, "type", "submit");
	}

}
