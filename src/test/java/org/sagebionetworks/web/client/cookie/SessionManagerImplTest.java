package org.sagebionetworks.web.client.cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.sagebionetworks.repo.model.UserProfile;
import org.sagebionetworks.repo.model.UserSessionData;

/**
 * Unit test for the SessionManagerImpl
 * @author John
 *
 */
public class SessionManagerImplTest {
	
	CookieProvider mockProvider;
	SessionManagerImpl manager;
	UserSessionData data;
	
	@Before
	public void before(){
		mockProvider = new StubCookieProvider();
		manager = new SessionManagerImpl(mockProvider);
		data = new UserSessionData();
		data.setProfile(new UserProfile());
		data.getProfile().setDisplayName("display");
		data.getProfile().setEmail("email");
		data.setSessionToken("token");
		data.getProfile().setOwnerId("123");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSaveSessionNull(){
		manager.saveSession(null);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testSaveSessionNullProfile(){
		UserSessionData data = new UserSessionData();
		manager.saveSession(data);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSaveSessionNullProfileDisplay(){
		data.getProfile().setDisplayName(null);
		manager.saveSession(data);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSaveSessionNullProfileEmail(){
		data.getProfile().setEmail(null);
		manager.saveSession(data);
	}
	@Test (expected=IllegalArgumentException.class)
	public void testSaveSessionNullProfileSessionToken(){
		data.setSessionToken(null);
		manager.saveSession(data);
	}
	
	@Test
	public void testSaveSession(){
		// Pass the complete session
		manager.saveSession(data);
		assertTrue(manager.hasSession());
		assertEquals(data.getProfile().getDisplayName(), manager.getUserDisplayName());
		assertEquals(data.getProfile().getEmail(), manager.getUserEmail());
		assertEquals(data.getProfile().getOwnerId(), manager.getUserPrincipalId());
		assertEquals(data.getSessionToken(), manager.getSessionToken());
		// clear the data
		manager.clearSession();
		assertFalse(manager.hasSession());
		assertEquals(null, manager.getUserDisplayName());
		assertEquals(null, manager.getUserEmail());
		assertEquals(null, manager.getUserPrincipalId());
		assertEquals(null, manager.getSessionToken());
	}
}
