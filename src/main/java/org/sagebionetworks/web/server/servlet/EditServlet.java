package org.sagebionetworks.web.server.servlet;

import java.io.IOException;

import static org.sagebionetworks.web.shared.Constants.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.web.client.cookie.SessionManagerImpl;

import com.google.inject.Inject;

/**
 * Handles add and remove form submits.
 * 
 * @author jmhill
 *
 */
public class EditServlet extends HttpServlet {

	public static final long serialVersionUID = 1l;
	
	static private Log log = LogFactory.getLog(EditServlet.class);
	@Inject
	UserDataStoreImpl userDataStore;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			// Get the username and password
			SessionManagerImpl session = new SessionManagerImpl(new ServeletCookieProvider(request));
			if(!session.hasSession()) throw new IllegalArgumentException("Can only be called from within an active session");
			String referer = request.getHeader(HEADER_REFERER);
			String path = request.getRequestURI().toLowerCase();
			log.debug("path: "+path);
			if(path.endsWith(PATH_ADD_SUFFIX)){
				// This is an add
				doAdd(session, request);
			}else if(path.endsWith(PATH_SUFFIX_REMOVE)){
				// This is a remove
				doRemove(session, request);
				
			}else{
				throw new IllegalArgumentException("Unknown request: "+path);
			}
			response.sendRedirect(referer);
		}catch(Throwable e){
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * This was a remove so remove the entity from the list.
	 * @param session
	 * @param request
	 * @throws SynapseException
	 */
	private void doRemove(SessionManagerImpl session, HttpServletRequest request) throws SynapseException {
		String synapseId = request.getParameter(PARAM_SYNAPSE_ID);
		if(synapseId == null) throw new IllegalArgumentException("Parmeter "+PARAM_SYNAPSE_ID+" is required");
		userDataStore.removeEntityFromWatchList(session.getSessionToken(), session.getUserPrincipalId(), synapseId);
	}

	/**
	 * This was an add, so add the entity to the user's list
	 * @param request
	 * @throws SynapseException 
	 */
	private void doAdd(SessionManagerImpl session, HttpServletRequest request) throws SynapseException {
		String synapseId = request.getParameter(PARAM_SYNAPSE_ID);
		if(synapseId == null) throw new IllegalArgumentException("Parmeter "+PARAM_SYNAPSE_ID+" is required");
		userDataStore.addEntitToUsersWatchList(session.getSessionToken(), session.getUserPrincipalId(), synapseId);
		
	}
	
}
