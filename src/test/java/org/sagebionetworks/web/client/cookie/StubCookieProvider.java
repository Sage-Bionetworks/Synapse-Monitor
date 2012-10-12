package org.sagebionetworks.web.client.cookie;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Stub implementation of the CookieProvider for tests.
 * 
 * @author John
 *
 */
public class StubCookieProvider implements CookieProvider {
	
	private Map<String, String> map = new HashMap<String, String>();

	@Override
	public String getCookie(String key) {
		return map.get(key);
	}

	@Override
	public Collection<String> getCookieNames() {
		return map.keySet();
	}

	@Override
	public void removeCookie(String key) {
		map.remove(key);
	}

	@Override
	public void setCookie(String key, String value) {
		map.put(key, value);
	}

	@Override
	public void setCookie(String key, String value, Date expires) {
		map.put(key, value);
	}

	@Override
	public void setCookie(String key, String value, Date expires,
			String domain, String path, boolean secure) {
		map.put(key, value);
	}

}
