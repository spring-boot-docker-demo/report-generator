package com.example.demo.mrunali.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.demo.mrunali.app.model.ResourceMaster;

public class ResourceItemProcessor implements ItemProcessor<ResourceMaster, ResourceMaster> {

	private static final Logger logger = LoggerFactory.getLogger(ResourceItemProcessor.class);
	
	@Override
	public ResourceMaster process(ResourceMaster item) throws Exception {
		
		logger.debug("Processing entry - " + item);
		final ResourceMaster master = new ResourceMaster();
		master.setResId(item.getResId());
		master.setFirstName(item.getFirstName());
		master.setLastName(item.getLastName());
		master.setDesignation(item.getDesignation());
		return master;
	}

}
