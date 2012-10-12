package org.sagebionetworks.web.server.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.repo.model.Project;
import org.sagebionetworks.repo.model.UserProfile;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.EntityFactory;
import org.sagebionetworks.web.shared.Constants;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Unit test for the datastore
 * @author John
 *
 */
public class UserDataStoreImplTest {
	
	AmazonS3Client mockClient;
	AmazonClientFactory mockFactory;
	SynapseProvider mockProvider;
	Synapse mockSynapse;
	UserDataStoreImpl dataStore;
	
	@Before
	public void before(){
		// Mock the factory
		mockFactory = Mockito.mock(AmazonClientFactory.class);
		mockClient = Mockito.mock(AmazonS3Client.class);
		mockProvider = Mockito.mock(SynapseProvider.class);
		mockSynapse = Mockito.mock(Synapse.class);
		when(mockFactory.createS3Client()).thenReturn(mockClient);
		when(mockProvider.createNewSynapse()).thenReturn(mockSynapse);
		dataStore = new UserDataStoreImpl(mockFactory, mockProvider);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNullArgs(){
		new UserDataStoreImpl(null, null);
	}
	
	
	@Test
	public void testEntityFileKey(){
		String userId = "user";
		String entityId = "entity";
		String expected = userId+"/"+entityId+".json";
		String result = UserDataStoreImpl.entityFileKey(userId, entityId);
		assertEquals(expected, result);
		
	}
	
	@Test
	public void testGetEntityBundleFromSynapse() throws SynapseException, JSONObjectAdapterException{
		EntityBundle bundle = createEntityBundle();
		String entityId = bundle.getEntity().getId();
		when(mockSynapse.getEntityBundle(entityId, EntityBundle.ENTITY)).thenReturn(bundle);
		// Make the call
		String json = dataStore.getEntityBundleFromSynapseAsJSON("token", entityId);
		String expected = EntityFactory.createJSONStringForEntity(bundle);
		assertEquals(expected, json);
	}
	
	@Test
	public void testGetUserData() throws SynapseException, JSONObjectAdapterException{
		UserSessionData usd = createUserData();
		String token = "token123";
		usd.setSessionToken(token);
		when(mockSynapse.getUserSessionData()).thenReturn(usd);
		String expected = EntityFactory.createJSONStringForEntity(usd);
		String result = dataStore.getUserData(token);
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	@Test
	public void testGetUsersWatchListAsEntity() throws JSONObjectAdapterException, UnsupportedEncodingException, SynapseException{
		String userId = "userId123";
		String marker = "markerOne";
		String[] entityIds = new String[]{
				"syn1",
				"syn2",
				"syn3",
		};
		String[] keys = new String[entityIds.length];
		EntityBundle[] bundles = new EntityBundle[keys.length];
		for(int i=0; i<entityIds.length; i++){
			// The s3 key
			keys[i] = UserDataStoreImpl.entityFileKey(userId, entityIds[i]);
			// the bundle
			bundles[i] = createEntityBundle(entityIds[i]);
		}
		// This should be the first request
		ListObjectsRequest requestOne = new ListObjectsRequest().withBucketName(Constants.DATA_BUCKET).withPrefix(userId).withMarker(null);
		ObjectListing resultOne = new ObjectListing();
		resultOne.setBucketName(Constants.DATA_BUCKET);
		resultOne.setMarker(marker);
		S3ObjectSummary sum = new S3ObjectSummary();
		sum.setKey(keys[0]);
		resultOne.getObjectSummaries().add(sum);
		sum = new S3ObjectSummary();
		sum.setKey(keys[1]);
		resultOne.getObjectSummaries().add(sum);
		// This should be the second request
		ListObjectsRequest requestTwo = new ListObjectsRequest().withBucketName(Constants.DATA_BUCKET).withPrefix(userId).withMarker(marker);
		ObjectListing resultTwo = new ObjectListing();
		resultTwo.setBucketName(Constants.DATA_BUCKET);
		resultTwo.setMarker(null);
		sum = new S3ObjectSummary();
		sum.setKey(keys[2]);
		resultTwo.getObjectSummaries().add(sum);
		
		// Setup the expected calls

		when(mockClient.listObjects(argThat(new MatchingListObjectsRequest(requestOne)))).thenReturn(resultOne);
		when(mockClient.listObjects(argThat(new MatchingListObjectsRequest(requestTwo)))).thenReturn(resultTwo);
		
		// The next phase is fetching the data
		for(int i=0; i<keys.length; i++){
			GetObjectRequest getRequast = new GetObjectRequest(Constants.DATA_BUCKET, keys[i]);
			S3Object result = new S3Object();
			String json = EntityFactory.createJSONStringForEntity(bundles[i]);
			ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes("UTF-8"));
			result.setObjectContent(in);
			result.setBucketName(Constants.DATA_BUCKET);
			result.setKey(keys[i]);
			when(mockClient.getObject(argThat(new MatchingGetObjectRequest(getRequast)))).thenReturn(result);
		}
		// Now that all of the data is mocked we are ready to run the test.
		List<EntityBundle> results = dataStore.getUsersWatchListAsEntity(userId);
		assertNotNull(results);
		assertEquals(entityIds.length, results.size());
		for(int i=0; i<entityIds.length; i++){
			assertEquals(bundles[i], results.get(i));
		}
	}
	
	/**
	 * Since ListObjectsRequest does not implement hash or equals we are forced to do some crazy matching.
	 * The matches only on the marker.
	 * @author John
	 *
	 */
	class MatchingListObjectsRequest extends ArgumentMatcher<ListObjectsRequest> {
		ListObjectsRequest expected;
		public MatchingListObjectsRequest(ListObjectsRequest expected) {
			this.expected = expected;
		}

		public boolean matches(Object matches) {
			if(expected == null){
				return matches == null;
			}
			if(matches == null) return false;
			ListObjectsRequest other = (ListObjectsRequest)matches;
			if(this.expected.getMarker() == null){
				return other.getMarker() == null;
			}
			return expected.getMarker().equals(other.getMarker());
		}
	}
	
	/**
	 * Since GetObjectRequest does not implement hash or equals we are forced to do some crazy matching.
	 * The matches only on the key.
	 * @author John
	 *
	 */
	class MatchingGetObjectRequest extends ArgumentMatcher<GetObjectRequest> {
		GetObjectRequest expected;
		public MatchingGetObjectRequest(GetObjectRequest expected) {
			this.expected = expected;
		}
		public boolean matches(Object matches) {
			if(expected == null){
				return matches == null;
			}
			if(matches == null) return false;
			GetObjectRequest other = (GetObjectRequest)matches;
			if(this.expected.getKey()== null){
				return other.getKey() == null;
			}
			return expected.getKey().equals(other.getKey());
		}
	}

	/**
	 * Helper
	 */
	public UserSessionData createUserData() {
		UserSessionData usd = new UserSessionData();
		usd.setProfile(new UserProfile());
		usd.getProfile().setDisplayName("display");
		usd.getProfile().setEmail("email");
		usd.getProfile().setOwnerId("123");
		return usd;
	}

	/**
	 * Helper
	 */
	public EntityBundle createEntityBundle() {
		return createEntityBundle("syn123");
	}
	/**
	 * Helper
	 */
	public EntityBundle createEntityBundle(String entityId) {
		EntityBundle bundle = new EntityBundle();
		Project p = new Project();
		p.setId(entityId);
		p.setEtag("etag");
		p.setEntityType(Project.class.getName());
		p.setName("name");
		p.setDescription("Description");
		bundle.setEntity(p);
		return bundle;
	}

}
