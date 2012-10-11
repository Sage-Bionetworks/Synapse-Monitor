package org.sagebionetworks.web.client.view;

import java.util.List;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
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

	public interface  UserHomeViewImplUiBinder extends UiBinder<Widget,  UserHomeViewImpl> {}
	
	@Inject
	public  UserHomeViewImpl(UserHomeViewImplUiBinder binder){
		initWidget(binder.createAndBindUi(this));
		
		String[] columns = new String[]{
				"SynpaseId",
				"Name",
				"Description",
				"Cows",
		};
		// Set the table headers
		setTableHeaders(columns);
//		// Fill in the table with data.
//		for(int row=0; row<25; row++){
//			TableRowElement tr = TableRowElement.as(DOM.createTR());
//			for(int col=0; col<columns.length; col++){
//				Element td = DOM.createTD();
//				// set the text
//				DOM.setInnerText(td, "row:"+row+"col:"+col);
//				tr.appendChild(td);
//			}
//			tableBody.appendChild(tr);
//		}
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
		infoBox.setInnerText("Welcome '"+username+"' the following is the list of entites you are currently watching.  When any of these entites change you will be notified by email at: "+email);
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
			DOM.setInnerText(td, row[col]);
			tr.appendChild(td);
		}
		tableBody.appendChild(tr);
	}

}
