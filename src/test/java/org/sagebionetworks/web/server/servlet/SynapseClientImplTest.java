package org.sagebionetworks.web.server.servlet;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.Project;
import org.sagebionetworks.web.shared.EntityData;

public class SynapseClientImplTest {
	
	@Test
	public void testTranslateToEntityData(){
		Entity e = new Project();
		e.setId("syn123");
		e.setDescription("description");
		e.setEtag("etag");
		e.setName("name");
		String url = SynapseClientImpl.ENTITY_BASE_URL+e.getId();
		
		EntityData expected = new EntityData();
		expected.setId(e.getId());
		expected.setDescription(e.getDescription());
		expected.setEtag(e.getEtag());
		expected.setName(e.getName());
		expected.setUrl(url);
		
		EntityData result = SynapseClientImpl.translateToEntityData(e);
		assertEquals(expected, result);
	}

}
