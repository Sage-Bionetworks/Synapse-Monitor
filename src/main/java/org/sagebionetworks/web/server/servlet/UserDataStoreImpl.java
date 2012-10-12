package org.sagebionetworks.web.server.servlet;

import static org.sagebionetworks.web.shared.Constants.CONTENT_TYPE_JSON;
import static org.sagebionetworks.web.shared.Constants.DATA_BUCKET;
import static org.sagebionetworks.web.shared.Constants.FOLDER_USERS;
import static org.sagebionetworks.web.shared.Constants.KEY_CONFIGURATION_KEY;
import static org.sagebionetworks.web.shared.Constants.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.EntityFactory;
import org.sagebionetworks.web.client.UserDataStore;
import org.sagebionetworks.web.client.transform.DataConversionUtils;
import org.sagebionetworks.web.shared.EntityData;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

/**
 * The S3 implementation of the datastore.
 * 
 * @author jmhill
 *
 */
public class UserDataStoreImpl extends RemoteServiceServlet implements UserDataStore {
	
	static private Log log = LogFactory.getLog(UserDataStoreImpl.class);

	private static final long serialVersionUID = 1L;
	
	AmazonS3Client client;
	SynapseProvider synapseProvider;
	
	@Inject
	public UserDataStoreImpl(AmazonClientFactory factory, SynapseProvider synapseProvider){
		if(factory == null) throw new IllegalArgumentException("Factory cannot be null");
		if(synapseProvider == null) throw new IllegalArgumentException("SynapseProvider cannot be null");
		// Check the bucket and make sure we have access to it.
		this.client = factory.createS3Client();
		if(client == null) throw new IllegalArgumentException("AmazonClientFactory.createS3Client() returned null");
		this.synapseProvider = synapseProvider;
	}

	@Override
	public void addEntitToUsersWatchList(String sessionToken, String userId, String entityId) throws SynapseException {
		if(userId == null) throw new IllegalArgumentException("UserId cannot be null");
		if(entityId == null) throw new IllegalArgumentException("EntityData cannot be null");
		if(sessionToken == null) throw new IllegalArgumentException("Session token cannot be null");
		// Add the file if it does not exist
		String fileKey = entityFileKey(userId, entityId);
		try{
			log.info("Storing: "+fileKey);
			ObjectMetadata meta = client.getObjectMetadata(DATA_BUCKET, fileKey);
		}catch(AmazonClientException e){
			log.info(fileKey+" does not exsit so creating it...");
			EntityBundle bundle = getEntityBundleFromSynapse(sessionToken, entityId);
			// Save the file
			writeEntityToS3(bundle, fileKey);
		}
	}

	/**
	 * Write a JSONEntity to S3.
	 * @param bundleJSON
	 * @param fileKey
	 */
	public void writeEntityToS3(JSONEntity object, String fileKey) {
		ByteArrayInputStream stream;
		try {
			String json = EntityFactory.createJSONStringForEntity(object);
			byte[] bytes = json.getBytes(UTF_8);
			stream = new ByteArrayInputStream(bytes);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(bytes.length);
			meta.setContentType(CONTENT_TYPE_JSON);
			PutObjectResult put = client.putObject(DATA_BUCKET, fileKey, stream, meta);
			log.debug("Created: "+fileKey+" AWS Etag: "+put.getETag());
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);
		} catch (JSONObjectAdapterException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read an entity from S3
	 * @param key
	 * @param clazz
	 * @return
	 * @throws SynapseException
	 */
	public <T extends JSONEntity> T readEtntiyFromS3(String key, Class<? extends T> clazz) throws SynapseException {
		// Get the file
		S3Object object = client.getObject(new GetObjectRequest(DATA_BUCKET, key));
		// Read the data
		try {
			String json = DataUtils.readStringFromStream(object.getObjectContent());
			return EntityFactory.createEntityFromJSONString(json, clazz);
		} catch (IOException e) {
			throw new SynapseException(e);
		} catch (JSONObjectAdapterException e) {
			throw new SynapseException(e);
		}
	}
	
	/**
	 * Get the full list of entites for this user.
	 * @param userId
	 * @return
	 * @throws SynapseException
	 */
	public <T extends JSONEntity> List<T> readEntityListFromS3(String prefix, Class<? extends T> clazz) throws SynapseException{
		if(prefix == null) throw new IllegalArgumentException("Prefix cannot be null");
		// List the Entity ids
		List<T> list = new LinkedList<T>();;
		String marker = null;
		// loop as long as there is data
		do{
			ObjectListing listing = client.listObjects(new ListObjectsRequest().withBucketName(DATA_BUCKET).withPrefix(prefix).withMarker(marker));
			marker = listing.getMarker();
			List<S3ObjectSummary> sums = listing.getObjectSummaries();
			for(S3ObjectSummary sum: sums){
				// Load each file
				T object = readEtntiyFromS3(sum.getKey(), clazz);
				list.add(object);
				// Be nice to multiple threads
				Thread.yield();
			}
		}while(marker != null);
		return list;
	}

	/**
	 * Get the entity bundle from Synaspe.
	 * @param sessionToken
	 * @param entityId
	 * @return
	 * @throws SynapseException
	 */
	public String getEntityBundleFromSynapseAsJSON(String sessionToken,
			String entityId) throws SynapseException {
		EntityBundle bundle = getEntityBundleFromSynapse(sessionToken, entityId);
		String bundleJSON;
		try {
			bundleJSON = EntityFactory.createJSONStringForEntity(bundle);
		} catch (JSONObjectAdapterException e2) {
			throw new SynapseException(e2);
		}
		return bundleJSON;
	}

	/**
	 * @param sessionToken
	 * @param entityId
	 * @return
	 * @throws SynapseException
	 */
	public EntityBundle getEntityBundleFromSynapse(String sessionToken,
			String entityId) throws SynapseException {
		Synapse synapse = synapseProvider.createNewSynapse();
		synapse.setSessionToken(sessionToken);
		// Get the bundle
		EntityBundle bundle = synapse.getEntityBundle(entityId, EntityBundle.ENTITY);
		return bundle;
	}

	@Override
	public List<EntityData> getUsersWatchList(String sessionToken, String userId) throws SynapseException{
		if(userId == null) throw new IllegalArgumentException("UserId cannot be null");
		Synapse synapse = synapseProvider.createNewSynapse();
		synapse.setSessionToken(sessionToken);
		synapse.revalidateSession();
		// List the Entity ids
		List<EntityBundle> list = getUsersWatchListAsEntity(userId);
		// Convert each bundle
		List<EntityData> result = new LinkedList<EntityData>();
		for(EntityBundle bundle: list){
			EntityData data = DataConversionUtils.translateToEntityData(bundle.getEntity());
			result.add(data);
			// Be nice to multiple threads
			Thread.yield();
		}
		return result;
	}
	

	/**
	 * Get the full list of entity bundles for this user.
	 * @param userId
	 * @return
	 * @throws SynapseException
	 */
	public List<EntityBundle> getUsersWatchListAsEntity(String userId) throws SynapseException{
		if(userId == null) throw new IllegalArgumentException("UserId cannot be null");
		// Read all of the data for this user.
		return readEntityListFromS3(userId, EntityBundle.class);
	}
	
	
	@Override
	public void removeEntityFromWatchList(String sessionToken, String userId, String entityId) throws SynapseException {
		if(sessionToken == null) throw new IllegalArgumentException("sessionToken cannot be null");
		if(userId == null) throw new IllegalArgumentException("userId cannot be null");
		if(entityId == null) throw new IllegalArgumentException("entityId cannot be null");
		// Validate the user.
		Synapse synapse = synapseProvider.createNewSynapse();
		synapse.setSessionToken(sessionToken);
		synapse.revalidateSession();
		// Remove the file if it exist
		String key = entityFileKey(userId, entityId);
		client.deleteObject(DATA_BUCKET, key);
	}

	
	/**
	 * The entity file key.
	 * @param userId
	 * @param entityId
	 * @return
	 */
	public static String entityFileKey(String userId, String entityId){
		StringBuilder builder = new StringBuilder();
		builder.append(userId);
		builder.append("/");
		builder.append(entityId);
		builder.append(".json");
		return builder.toString();
	}
	
	/**
	 * Get the user data for a token
	 */
	@Override
	public String getUserData(String token) throws SynapseException {
		Synapse synapse = synapseProvider.createNewSynapse();
		synapse.setSessionToken(token);
		UserSessionData usd = synapse.getUserSessionData();
		try {
			// Convert it to a string.
			return EntityFactory.createJSONStringForEntity(usd);
		} catch (JSONObjectAdapterException e) {
			throw new SynapseException(e);
		}
	}
	
	/**
	 * Login the user.
	 * @param username
	 * @param password
	 * @return
	 * @throws SynapseException
	 */
	public UserSessionData login(String username, String password) throws SynapseException{
		Synapse synapse = synapseProvider.createNewSynapse();
		// Login the user.
		UserSessionData usd = synapse.login(username, password);
		// Save the user's data
		// This data is used by the worker.
		String key = createUserDataKey(usd.getProfile().getOwnerId());
		// Save this to S3
		writeEntityToS3(usd, key);
		// Return the data to the user.
		return usd;
	}
	
	/**
	 * The key for user data files.
	 * @param userId
	 * @return
	 */
	public static String createUserDataKey(String userId){
		StringBuilder builder = new StringBuilder();
		builder.append(FOLDER_USERS);
		builder.append("/");
		builder.append(userId);
		builder.append(".json");
		return builder.toString();
	}
	
	/**
	 * Load the configuration properties from S3;
	 * @return
	 */
	public Properties loadConfigurationProperties(){
		S3Object object = client.getObject(new GetObjectRequest(DATA_BUCKET, KEY_CONFIGURATION_KEY));
		try{
			Properties props = new Properties();
			props.load(object.getObjectContent());
			return props;
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		}finally{
			try {
				object.getObjectContent().close();
			} catch (IOException e) {}
		}
	}


}
