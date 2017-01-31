package org.beatific.microservice.container.instance;

import org.springframework.beans.factory.annotation.Autowired;

public class InstanceManager {

	@Autowired
	private DiscoveryRegistry registry;
	
	private InstancePhysicalManager invoker;

	public void unregister(String serviceName) {
		registry.tagServices(serviceName);
		
		invoker.invoke(serviceName);
		
		registry.unregister(serviceName);
	}
	
}
