package org.sagebionetworks.web.server.servlet;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * A very simple class for providing clients using IoC.
 * 
 * @author jmhill
 *
 */
public class AmazonClientFactoryImpl implements AmazonClientFactory {
	
	public static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";
	public static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";
	
	
	AWSCredentials credentials;
	
	@Inject
	public AmazonClientFactoryImpl(
			@Named(AWS_ACCESS_KEY_ID) String accessKeyId,
			@Named(AWS_SECRET_KEY) String secretKey){
		if(accessKeyId == null) throw new IllegalArgumentException("AWS_ACCESS_KEY_ID cannot be null");
		if(secretKey == null) throw new IllegalArgumentException("AWS_SECRET_KEY cannot be null");
		// All we need to do is keep the Credentials.
		credentials = new BasicAWSCredentials(accessKeyId, secretKey);
	}

	@Override
	public AmazonS3Client createS3Client() {
		return new AmazonS3Client(credentials);
	}

	public AmazonSimpleEmailServiceClient createSESClient(){
		return new AmazonSimpleEmailServiceClient(credentials);
	}
}
