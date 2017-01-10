package org.beatific.microservice.container.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.beatific.microservice.container.context.ServiceContext;
import org.beatific.microservice.container.instance.DiscoveryRegistry;
import org.beatific.microservice.container.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class ServiceController {
	
	@Autowired
	private DiscoveryRegistry registry;
	
	@Autowired
	private ServiceContext context;
	
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
	
	
	@RequestMapping("/register")
	public String registerService(@RequestBody Service service) {
		
		context.registerService(service);
		return "ok";
	}
}
