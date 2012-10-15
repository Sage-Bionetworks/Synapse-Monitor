package org.sagebionetworks.web.server.servlet;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.sagebionetworks.web.client.cookie.CookieProvider;

/**
 * Wraps the servlet side of the cookie provider.
 * @author John
 *
 */
public class ServeletCookieProvider implements CookieProvider{
	
	Map<String, String> cookies;
	
	/**
	 * Map the cookies to map.
	 * @param request
	 */
	public ServeletCookieProvider(HttpServletRequest request){
		cookies = new HashMap<String, String>();
		Cookie[] array = request.getCookies();
		if(array != null){
			for(Cookie cookie: array){
				cookies.put(cookie.getName(), cookie.getValue());
			}
		}
	}

	@Override
	public String getCookie(String key) {
		return cookies.get(key);
	}

	@Override
	public Collection<String> getCookieNames() {
		return cookies.keySet();
	}

	@Override
	public void removeCookie(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCookie(String key, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCookie(String key, String value, Date expires) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCookie(String name, String value, Date expires,
			String domain, String path, boolean secure) {
		throw new UnsupportedOperationException();		
	}

}
