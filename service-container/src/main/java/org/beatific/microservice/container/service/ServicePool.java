package org.beatific.microservice.container.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public class ServicePool  {

	Map<String, Service> servicePool = new HashMap<>();
	
	List<Service> notRegisteredPool = new ArrayList<>();
	
	public Collection<Service> services() {
		return servicePool.values();
	}
	
	public Map<String, Service> instancesByServiceName() {
		return servicePool;
	}
	
	public synchronized void register(Service service) {
		servicePool.put(service.getServiceName(), service);
	}
	
	public synchronized Service get(String name) {
		return servicePool.get(name);
	}
	
	public void remove(Service service) {
		remove(service.getServiceName());
	}

	public synchronized void remove(String name) {
		servicePool.remove(name);
	}
	
	public synchronized void clear() {
		servicePool.clear();
	}
	
}
