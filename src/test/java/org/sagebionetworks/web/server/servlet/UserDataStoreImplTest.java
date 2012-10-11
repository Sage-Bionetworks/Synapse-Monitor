package org.sagebionetworks.web.server.servlet;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Unit test for the datastore
 * @author John
 *
 */
public class UserDataStoreImplTest {
	
	AmazonS3Client mockClient;
	AmazonClientFactory mockFactory;
	UserDataStoreImpl dataStore;
	
	@Before
	public void before(){
		// Mock the factory
		mockFactory = Mockito.mock(AmazonClientFactory.class);
		mockClient = Mockito.mock(AmazonS3Client.class);
		when(mockFactory.createS3Client()).thenReturn(mockClient);
		dataStore = new UserDataStoreImpl(mockFactory);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNullFactory(){
		new UserDataStoreImpl(null);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNullClient(){
		when(mockFactory.createS3Client()).thenReturn(null);
		new UserDataStoreImpl(mockFactory);
	}
	
	@Test
	public void testEntityFileKey(){
		String userId = "user";
		String entityId = "entity";
		String expected = userId+"/"+entityId;
		String result = UserDataStoreImpl.entityFileKey(userId, entityId);
		assertEquals(expected, result);
		
	}

}
