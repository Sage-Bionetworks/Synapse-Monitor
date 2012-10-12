package org.sagebionetworks.web.client.transform;

import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.web.shared.EntityData;

/**
 * Data conversion 
 * @author John
 *
 */
public class DataConversionUtils {

	public static final String ENTITY_BASE_URL = "https://synapse.sagebase.org/#Synapse:";
	/**
	 * @param e
	 * @return
	 */
	public static EntityData translateToEntityData(Entity e) {
		EntityData data = new EntityData();
		data.setName(e.getName());
		data.setEtag(e.getEtag());
		data.setDescription(e.getDescription());
		data.setUrl(ENTITY_BASE_URL+e.getId());
		data.setId(e.getId());
		return data;
	}
	
}
