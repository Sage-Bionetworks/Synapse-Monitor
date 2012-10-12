package org.sagebionetworks.web.shared;

/**
 * Constants for the Client and server.
 * @author John
 *
 */
public class Constants {
	// html ids for the login button and label of the header.
	public static final String ELE_ID_LOGOUT_BUTTON = "logoutButton";
	public static final String ELE_ID_USER_NAME_TITLE_BAR = "userNameTitleBar";
	
	// Add and remove path
	public static final String PATH_SUFFIX_REMOVE = "remove";
	public static final String PATH_ADD_SUFFIX = "add";
	// Referer header key
	public static final String HEADER_REFERER = "Referer";
	// Synapse Param.
	public static final String PARAM_SYNAPSE_ID = "synapseId";
	// 	JSON content type.
	public static final String CONTENT_TYPE_JSON = "application/json";
	// what else would we use?
	public static final String UTF_8 = "UTF-8";
	// the bucket where all data for this application is stored.
	public static final String DATA_BUCKET = "synapse.monitor.sagebase.org";
	// The folder where all of the user's information is stored.
	public static final String FOLDER_USERS = "users";
	// The configuration file key
	public static final String KEY_CONFIGURATION_KEY = "configuration/SynapseMonitorConfig.properties";

}
