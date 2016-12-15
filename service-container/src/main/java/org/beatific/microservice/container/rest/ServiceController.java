package org.beatific.microservice.container.rest;

import org.beatific.microservice.container.discovery.DiscoveryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {
	
	@Autowired
	private DiscoveryRegistry registry;
	
	@RequestMapping("/bluegreen/{serviceName}")
	public String bluegreen(@PathVariable("serviceName") String serviceName) {
		
		registry.unregister(serviceName);
		return "ok";
	}
	
	@RequestMapping("/tagging/{serviceName}")
	public String taggingName(@PathVariable("serviceName") String serviceName) {
		
		registry.tagServices(serviceName);
		return "ok";
	}
}
