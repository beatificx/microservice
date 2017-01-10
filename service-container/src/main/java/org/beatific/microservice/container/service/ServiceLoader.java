package org.beatific.microservice.container.service;

import java.util.List;

import org.beatific.microservice.container.executor.Judge;
import org.beatific.microservice.container.service.event.EventCaster;
import org.beatific.microservice.container.service.event.ServiceEvent;
import org.beatific.microservice.container.service.support.FileServiceContainer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceLoader {

	private final ServiceContainer container;
	
	@Autowired
	private EventCaster eventCaster;
	
	@Autowired
	private Judge judge;
	
	public ServiceLoader() {
		this.container = new FileServiceContainer();
	}
	
	public void load() {
		container.setJudge(judge);
		container.loadServices();
	}
	
	public List<Service> getAllServices() {
		return container.getAllService();
	}
	
	public void registerService(Service service) {
		container.registerService(service.getServiceName(), service);
	}
	
	public void removeService(Service service) {
		container.removeService(service.getServiceName());
	}
	
	public void publishEvent(ServiceEvent event) {
		eventCaster.cast(event);
	}
	
}
