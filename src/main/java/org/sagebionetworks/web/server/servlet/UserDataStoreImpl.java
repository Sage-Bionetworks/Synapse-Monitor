package org.sagebionetworks.web.server.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.repo.model.UserSessionData;
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
import com.amazonaws.util.StringInputStream;
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
	
	public static final String DATA_BUCKET = "synapse.monitor.sagebase.org";
	
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
		// First get the entity data
		String bundleJSON = getEntityBundleFromSynapse(sessionToken, entityId);
		// Add the file if it does not exist
		String fileKey = entityFileKey(userId, entityId);
		try{
			log.info("Storing: "+fileKey);
			ObjectMetadata meta = client.getObjectMetadata(DATA_BUCKET, fileKey);
		}catch(AmazonClientException e){
			log.info(fileKey+" does not exsit so creating it...");
			// Save the file
			StringInputStream stream;
			try {
				stream = new StringInputStream(bundleJSON);
				PutObjectResult put = client.putObject(DATA_BUCKET, fileKey, stream, null);
				log.debug("Created: "+fileKey+" AWS Etag: "+put.getETag());
			} catch (UnsupportedEncodingException e1) {
				throw new RuntimeException(e1);
			}
		}
	}

	/**
	 * Get the entity bundle from Synaspe.
	 * @param sessionToken
	 * @param entityId
	 * @return
	 * @throws SynapseException
	 */
	public String getEntityBundleFromSynapse(String sessionToken,
			String entityId) throws SynapseException {
		Synapse synapse = synapseProvider.createNewSynapse();
		synapse.setSessionToken(sessionToken);
		// Get the bundle
		EntityBundle bundle = synapse.getEntityBundle(entityId, EntityBundle.ENTITY);
		String bundleJSON;
		try {
			bundleJSON = EntityFactory.createJSONStringForEntity(bundle);
		} catch (JSONObjectAdapterException e2) {
			throw new SynapseException(e2);
		}
		return bundleJSON;
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
		}
		return result;
	}
	

	/**
	 * Get the full list of entites for this user.
	 * @param userId
	 * @return
	 * @throws SynapseException
	 */
	public List<EntityBundle> getUsersWatchListAsEntity(String userId) throws SynapseException{
		if(userId == null) throw new IllegalArgumentException("UserId cannot be null");
		// List the Entity ids
		List<EntityBundle> list = new LinkedList<EntityBundle>();;
		String marker = null;
		// loop as long as there is data
		do{
			ObjectListing listing = client.listObjects(new ListObjectsRequest().withBucketName(DATA_BUCKET).withPrefix(userId).withMarker(marker));
			marker = listing.getMarker();
			List<S3ObjectSummary> sums = listing.getObjectSummaries();
			for(S3ObjectSummary sum: sums){
				// Load each file
				S3Object object = client.getObject(new GetObjectRequest(DATA_BUCKET, sum.getKey()));
				// Read the data
				try {
					String json = DataUtils.readStringFromStream(object.getObjectContent());
					EntityBundle bundle = EntityFactory.createEntityFromJSONString(json, EntityBundle.class);
					list.add(bundle);
				} catch (IOException e) {
					throw new SynapseException(e);
				} catch (JSONObjectAdapterException e) {
					throw new SynapseException(e);
				}
			}
		}while(marker != null);
		return list;
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


}
