package org.sagebionetworks.web.client.cookie;

import org.sagebionetworks.repo.model.UserSessionData;

/**
 * Manage the user's session.
 * 
 * @author John
 *
 */
public interface SessionManager {
	
	/**
	 * Save the current session.
	 * @param data
	 */
	public void saveSession(UserSessionData data);
	
	/**
	 * The user's current session token.
	 * @return
	 */
	public String getSessionToken();
	
	/**
	 * The user's display name.
	 * @return
	 */
	public String getUserDisplayName();
	
	/**
	 * Get the user's email
	 * @return
	 */
	public String getUserEmail();
	
	/**
	 * The principal ID of the user.
	 * @return
	 */
	public String getUserPrincipalId();
	
	/**
	 * Do we have a session?
	 * @return
	 */
	public boolean hasSession();
	
	/**
	 * Clear the session.
	 */
	public void clearSession();

}
