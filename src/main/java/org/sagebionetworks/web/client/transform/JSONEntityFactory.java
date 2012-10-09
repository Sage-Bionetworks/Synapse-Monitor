package org.sagebionetworks.web.client.transform;

import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;

/**
 * Abstraction for an entity factory.
 * @author John
 *
 */
public interface JSONEntityFactory {
	
	/**
	 * Create a new entity from a JSON String.
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @throws JSONObjectAdapterException 
	 */
	public <T extends JSONEntity> T createEntity(String jsonString, Class<? extends T> clazz);
	
	/**
	 * Create a new JSONEntity using the passed JSON and classname.
	 * @param json
	 * @param className
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public JSONEntity createEntity(String json, String className);
	
	/**
	 *  Initialize the passed new JSONEntity using the provided JSON string. 
	 * @param <T>
	 * @param json
	 * @param newEntity
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public <T extends JSONEntity> T initializeEntity(String json, T newEntity);
	
	
	/**
	 * Create the JSON string for the given entity.
	 * @param entity
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public String createJsonStringForEntity(JSONEntity entity);
	
	/**
	 * Create an entity from the json string.
	 * @param json
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public Entity createEntity(String json);

	public JSONEntity newInstance(String className);

}
