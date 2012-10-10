package org.sagebionetworks.web.server.servlet;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.sagebionetworks.web.client.cookie.CookieProvider;
/**
 * Wraps the servlet side of the cookie provider.
 * @author John
 *
 */
public class ServeletCookieProvider implements CookieProvider{
	
	HttpServletResponse resp;
	public ServeletCookieProvider(HttpServletResponse resp){
		this.resp = resp;
	}

	@Override
	public String getCookie(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> getCookieNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeCookie(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCookie(String key, String value) {
		// Add a cookie
		resp.addCookie(new Cookie(key, value));
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
