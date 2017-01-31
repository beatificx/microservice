package org.beatific.microservice.monitor.service.instance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.ApplicationInfoManager;

@Component
public class InstanceFactory {

	@Autowired
	private ApplicationInfoManager manager;
	private Instance instance = null;
	
	public Instance getInstance() {
		
		if(instance == null) instance =  new Instance(manager.getInfo());
		return instance;
	}
}
