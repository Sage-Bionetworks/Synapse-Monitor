package org.sagebionetworks.web.server.servlet;

import static org.sagebionetworks.web.shared.Constants.FOLDER_USERS;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.repo.model.UserSessionData;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.GetIdentityVerificationAttributesRequest;
import com.amazonaws.services.simpleemail.model.GetIdentityVerificationAttributesResult;
import com.amazonaws.services.simpleemail.model.IdentityVerificationAttributes;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityRequest;
import com.google.inject.Inject;

/**
 * Drives the actual monitoring of all of the user's entities.
 * @author John
 *
 */
public class MonitorWorkerServelet extends HttpServlet {
	
	static private Log log = LogFactory.getLog(MonitorWorkerServelet.class);
	
	UserDataStoreImpl dataStore;
	SynapseProvider synapseProvider;
	AmazonSimpleEmailServiceClient awsSESClient;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String adminUsername = null;
    private String adminPassword = null;
    private String sendingEmail = null;
    private long periodMS = 0;
    private long sleepMS = 0;
	
	@Inject
	public MonitorWorkerServelet(UserDataStoreImpl dataStore, SynapseProvider synapseProvider, AmazonClientFactory factory){
		this.dataStore = dataStore;
		this.synapseProvider = synapseProvider;
		awsSESClient = factory.createSESClient();
		if(awsSESClient == null) throw new IllegalArgumentException("AmazonClientFactory.createSESClient() returned null");
		// Load the config from S3
		Properties props = this.dataStore.loadConfigurationProperties();
		adminUsername = props.getProperty("org.sagebionetworks.admin.username");
		if(adminUsername == null) throw new IllegalArgumentException("Admin username is null");
		adminPassword = props.getProperty("org.sagebionetworks.admin.password");
		if(adminPassword == null) throw new IllegalArgumentException("Admin password is null");
		sendingEmail = props.getProperty("org.sagebionetworks.sending.email");
		if(sendingEmail == null) throw new IllegalArgumentException("Sending email cannot be null");
		periodMS = Long.parseLong(props.getProperty("org.sagebionetworks.period.ms"));
		sleepMS = Long.parseLong(props.getProperty("org.sagebionetworks.worker.sleep.ms"));
		
		// We need to verify the sending email
		GetIdentityVerificationAttributesResult givar = awsSESClient.getIdentityVerificationAttributes(new GetIdentityVerificationAttributesRequest().withIdentities(sendingEmail));
		IdentityVerificationAttributes iiva = givar.getVerificationAttributes().get(sendingEmail);
		if(iiva == null || "Failed".equals(iiva.getVerificationStatus())){
			// We need to send a request
			awsSESClient.verifyEmailIdentity(new VerifyEmailIdentityRequest().withEmailAddress(sendingEmail));
			log.debug("Verifying email = "+sendingEmail);
		}else{
			log.debug("Status email = "+iiva.getVerificationStatus());
		}
		// Setup the timer
		// run every two minutes
		ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(new Worker(), 0, periodMS, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * This is the worker that scans and reports errors
	 * @author John
	 *
	 */
	public class Worker implements Runnable{
		@Override
		public void run() {
			log.debug("Worker started...");
			// First get all of the users
			try {
				// Make sure we have a fresh synapse connection
				Synapse client = synapseProvider.createNewSynapse();
				client.login(adminUsername, adminPassword, true);
				
				List<UserSessionData> userList = dataStore.readEntityListFromS3(FOLDER_USERS, UserSessionData.class);
				// for each user look up the entities they are watching
				for(UserSessionData user: userList){
					// List this user's entities
					List<EntityBundle> bundleList = dataStore.readEntityListFromS3(user.getProfile().getOwnerId(), EntityBundle.class);
					for(EntityBundle bundle: bundleList){
						// Look up the current object
						Entity saved = bundle.getEntity();
						try{
							Entity currentEntity = client.getEntityById(saved.getId());
							// Has the entity changed?
							if(!saved.getEtag().equals(currentEntity.getEtag())){
								// If we failed to get the entity then notify the user.
								String message = "The entity has changed.";
								String subject = createMessageSubject(saved.getId());
								String body = createMessageBody(user.getProfile().getDisplayName(), saved.getId(), message);
								sendMessage(user.getProfile().getEmail(), subject, body);
								// Save back the current entity to prevent sending this message again.
								bundle.setEntity(currentEntity);
								String key = UserDataStoreImpl.entityFileKey(user.getProfile().getOwnerId(), saved.getId());
								dataStore.writeEntityToS3(bundle, key);
								Thread.sleep(sleepMS);
							}
						}catch(SynapseException e){
							// If we failed to get the entity then notify the user.
							String subject = createMessageSubject(saved.getId());
							String body = createMessageBody(user.getProfile().getDisplayName(), saved.getId(), e.getMessage());
							sendMessage(user.getProfile().getEmail(), subject, body);
						}
					}
				}
			} catch (Throwable e) {
				// log any errors
				log.error("Worker failed:", e);
			}
		}
		
	}
	
	/**
	 * Build the message subject
	 * @param entityID
	 * @return
	 */
	public static String createMessageSubject(String entityID){
		StringBuilder builder = new StringBuilder();
		builder.append("Synapse Monitor Notification - ");
		builder.append(entityID);
		return builder.toString();
	}
	
	/**
	 * Create a message body
	 * @param userName
	 * @param entityId
	 * @param message
	 * @return
	 */
	public static String createMessageBody(String userName, String entityId, String message){
		StringBuilder builder = new StringBuilder();
		builder.append("Dear ");
		builder.append(userName);
		builder.append(",<br><br>");
		builder.append("The <a href=\"https://synapse-monitor.sagebase.org\">Synapse Monitor Service</a> is sending this message because the following was detected: <br> ");
		builder.append(message);
		builder.append("<br>");
		builder.append("Click the following link to see the Entity in Synapse: <a href=\"https://synapse.sagebase.org/#Synapse:");
		builder.append(entityId);
		builder.append("\">");
		builder.append(entityId);
		builder.append("</a>.");
		builder.append("<br><br>");
		builder.append("Thanks,<br><br>Synapse Team");
		return builder.toString();
		
	}
	
	/**
	 * Send a message
	 * @param destEmail
	 * @param subject
	 * @param body
	 */
	public void sendMessage(String destEmail, String subject, String body){
		// send a message
		awsSESClient.sendEmail(new SendEmailRequest(sendingEmail, new Destination().withToAddresses(destEmail), new Message().withSubject(new Content(subject)).withBody(new Body().withHtml((new Content(body))))));
	}

}
