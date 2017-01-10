package org.beatific.microservice.container.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beatific.microservice.container.executor.Judge;
import org.beatific.microservice.container.repository.ObjectRepository;
import org.beatific.microservice.container.repository.RepositoryException;

import lombok.Setter;

public abstract class ServiceContainer extends ObjectRepository<Map<String, Service>> {

	protected Map<String, Service> container;
	
	@Setter
	protected Judge judge; 

	public synchronized void registerService(String serviceName, Service service) {
		try {
			container.put(serviceName, service);
			saveObject(container);
			judge.entrust(service);
		} catch (RepositoryException e) {
			container.remove(serviceName);
			throw new RuntimeException(e);
		}
	}
	
	public List<Service> getAllService() {
		Collection<Service> services = container.values();
		if(services instanceof List)
			return (List<Service>)services;
		else 
			return new ArrayList<Service>(services);
		
	}

	public synchronized Service getService(String serviceName) {
		return container.get(serviceName);
	}

	protected void clearServices() {

		try {
			synchronized (this) {
				clearObject();
				container.clear();
				judge.clear();
			}

		} catch (RepositoryException e) {
			loadServices();
			throw new RuntimeException(e);
		}
	}

	protected synchronized void loadServices() {
		
		try {
			container = loadObject();
			
			if(container == null) container = new HashMap<>();
			
			for(Service service: container.values()) {
				judge.entrust(service);
			}
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	protected synchronized void removeService(String serviceName) {
		
		Service old = null;
		
		try {
			old = container.remove(serviceName);
			removeObject(container, serviceName);
			judge.take(serviceName);
		} catch (RepositoryException e) {
			if(old != null) container.put(serviceName, old);
			throw new RuntimeException(e);
		}
	}

}
