package org.sagebionetworks.web.server.servlet;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

public interface AmazonClientFactory {
	
	/**
	 * Create the S3 client.
	 * @return
	 */
	public AmazonS3Client createS3Client();
	
	/**
	 * Create the email client
	 * @return
	 */
	public AmazonSimpleEmailServiceClient createSESClient();

}
