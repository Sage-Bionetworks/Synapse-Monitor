package org.sagebionetworks.web.server.servlet;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sagebionetworks.web.client.UserDataStore;
import org.sagebionetworks.web.shared.EntityData;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
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
	
	private static final String DATA_BUCKET = "synapse.monitor.sagebase.org";
	
	AmazonS3Client client;
	
	@Inject
	public UserDataStoreImpl(AmazonClientFactory factory){
		if(factory == null) throw new IllegalArgumentException("Factory cannot be null");
		// Check the bucket and make sure we have access to it.
		client = factory.createS3Client();
		if(client == null) throw new IllegalArgumentException("AmazonClientFactory.createS3Client() returned null");
	}

	@Override
	public void addEntitToUsersWatchList(String userId, EntityData toAdd) {
		if(userId == null) throw new IllegalArgumentException("UserId cannot be null");
		if(toAdd == null) throw new IllegalArgumentException("EntityData cannot be null");
		// Add the file if it does not exist
		String fileKey = entityFileKey(userId, toAdd.getId());
		try{
			log.info("Storing: "+fileKey);
			ObjectMetadata meta = client.getObjectMetadata(DATA_BUCKET, fileKey);
		}catch(AmazonClientException e){
			log.info(fileKey+" does not exsit so creating it...");
			// Save the file
			StringInputStream stream;
			try {
				stream = new StringInputStream(toAdd.getEtag());
				PutObjectResult put = client.putObject(DATA_BUCKET, fileKey, stream, null);
				log.debug("Created: "+fileKey+" AWS Etag: "+put.getETag());
				
			} catch (UnsupportedEncodingException e1) {
				throw new RuntimeException(e1);
			}
		}
	}

	@Override
	public List<String> getUsersWatchList(String userId) {
		if(userId == null) throw new IllegalArgumentException("UserId cannot be null");
		// List the Entity ids
		List<String> list = new LinkedList<String>();;
		ObjectListing listing = null;
		do{
			listing = client.listObjects(DATA_BUCKET, userId);
			List<S3ObjectSummary> sums = listing.getObjectSummaries();
			for(S3ObjectSummary sum: sums){
				list.add(sum.getKey());
			}
		}while(listing.getMarker() != null);
				
		return list;
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
		return builder.toString();
	}

}
