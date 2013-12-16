package org.sagebionetworks.web.client.cookie;

import java.util.Collection;

import org.sagebionetworks.repo.model.UserSessionData;

import com.google.inject.Inject;

/**
 * Simple implementation of the SessionManager
 * 
 * @author John
 *
 */
public class SessionManagerImpl implements SessionManager {
	
	
	public static final String KEY_USER_ID = "KEY_USER_ID";
	public static final String KEY_USER_SESSION_TOKEN = "KEY_USER_SESSION_TOKEN";
	public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
	public static final String KEY_USER_DISPLAY_NAME = "KEY_USER_DISPLAY_NAME";
	public static final String[] KEYS_TO_CLEAR = new String[]{
		KEY_USER_SESSION_TOKEN,
		KEY_USER_EMAIL,
		KEY_USER_DISPLAY_NAME,
		KEY_USER_ID,
	};
	CookieProvider cookieProvider;
	
	/**
	 * 
	 * @param cookieProvider
	 */
	@Inject
	public SessionManagerImpl(CookieProvider cookieProvider){
		this.cookieProvider = cookieProvider;
	}

	@Override
	public void saveSession(UserSessionData data) {
		if(data == null) throw new IllegalArgumentException("Session data cannot be null");
		if(data.getProfile() == null) throw new IllegalArgumentException("Profile cannot be null");
		if(data.getProfile().getDisplayName() == null) throw new IllegalArgumentException("Dispaly name cannot be null");
		if(data.getProfile().getEmail() == null) throw new IllegalArgumentException("Email canot be null");
		if(data.getProfile().getOwnerId() == null) throw new IllegalArgumentException("OwnerId canot be null");
		if(data.getSession().getSessionToken() == null) throw new IllegalArgumentException("Session token cannot be null");
		// Store the data in the cookies
		cookieProvider.setCookie(KEY_USER_DISPLAY_NAME, data.getProfile().getDisplayName());
		cookieProvider.setCookie(KEY_USER_EMAIL, data.getProfile().getEmail());
		cookieProvider.setCookie(KEY_USER_SESSION_TOKEN, data.getSession().getSessionToken());
		cookieProvider.setCookie(KEY_USER_ID, data.getProfile().getOwnerId());
		
	}

	@Override
	public String getSessionToken() {
		return cookieProvider.getCookie(KEY_USER_SESSION_TOKEN);
	}

	@Override
	public String getUserDisplayName() {
		return cookieProvider.getCookie(KEY_USER_DISPLAY_NAME);
	}

	@Override
	public boolean hasSession() {
		Collection<String> name = cookieProvider.getCookieNames();
		String token = cookieProvider.getCookie(KEY_USER_SESSION_TOKEN);
		return token != null;
	}

	@Override
	public void clearSession() {
		// clear all of the keys
		for(String key: KEYS_TO_CLEAR){
			cookieProvider.removeCookie(key);
		}
	}

	@Override
	public String getUserEmail() {
		return cookieProvider.getCookie(KEY_USER_EMAIL);
	}

	@Override
	public String getUserPrincipalId() {
		return cookieProvider.getCookie(KEY_USER_ID);
	}

}
