package org.sagebionetworks.web.server.servlet;

import java.util.List;

import org.sagebionetworks.web.client.UserDataStore;
import org.sagebionetworks.web.shared.EntityData;

import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

/**
 * The S3 implementation of the datastore.
 * 
 * @author jmhill
 *
 */
public class UserDataStoreImpl extends RemoteServiceServlet implements UserDataStore {

	private static final long serialVersionUID = 1L;
	
	@Inject
	AmazonS3Client s3Client;

	@Override
	public void addEntitToUsersWatchList(String userId, EntityData toAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getUsersWatchList(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
