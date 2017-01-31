package org.beatific.microservice.container.instance;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beatific.microservice.container.repository.ObjectRepository;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.service.Service;
import org.beatific.microservice.container.service.ServicePool;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class InstancePool extends ObjectRepository<Map<URI, Instance>> {

	private Map<URI, Instance> uriPool = new HashMap<>();
	private Map<String, List<Instance>> namePool = new HashMap<>();
	private List<Instance> deadPool = new ArrayList<>();
	private List<Instance> zombiePool = new ArrayList<>();
	
	@Autowired
	private ServicePool servicePool;
	
	InstanceCaster<Instance> caster = new InstanceCaster<>(Instance.class);

	public Collection<Instance> instances() {
		return uriPool.values();
	}
	
	public Map<String, List<Instance>> instancesByServiceName() {
		return namePool;
	}

	public void load(List<Instance> instances) throws RepositoryException {

		Map<URI, Instance> savedInstances = loadObject();
		
		log.debug("instances {}", instances);
		log.debug("savedInstances {}", savedInstances);
		if (savedInstances == null)
			savedInstances = new HashMap<>();

		for (Instance instance : instances) {

			if (!savedInstances.containsKey(instance.getUri())) {

				try {
					Instance ins = caster.cast(instance.getUri().toString(), "/instance");
					Service service = servicePool.get(instance.getServiceName());
					ins.setService(service);
	
					innerRegister(ins);
				} catch (Exception ex) {
				}
			}
		}

		for (Entry<URI, Instance> entry : savedInstances.entrySet()) {
			
			if (existsInEureka(entry.getKey(), instances)) {
				
				innerRegister(entry.getValue());
				
			} else {
				
				deadPool.add(entry.getValue());
			}
		}
	}

	private synchronized boolean existsInEureka(URI uri, List<Instance> instances) {
		
		return instances.stream().filter(i -> i.getUri().equals(uri)).count() > 0;
	}
	
	public void zombie(Instance instance) {
		synchronized (zombiePool) {
		    zombiePool.add(instance);
		}
	}
	
	public List<Instance> zombies() {
		synchronized (zombiePool) {
		    return zombiePool;
		}
	}

	public synchronized void register(Instance instance) throws RepositoryException {
		log.info("[register instance{}]", instance);
		innerRegister(instance);
	}

	private void innerRegister(Instance instance) throws RepositoryException {
		URI uri = instance.getUri();
		String name = instance.getService().getServiceName();

		uriPool.put(uri, instance);
		List<Instance> instances = namePool.get(name);
		if (instances == null)
			instances = new ArrayList<Instance>();
		instances.add(instance);
		namePool.put(name, instances);

		try {
			saveObject(uriPool);
		} catch (RepositoryException ex) {
			remove(instance);
			throw ex;
		}
	}

	public List<Instance> deadPool() {
		return deadPool;
	}

	public synchronized Instance get(URI uri) {
		return uriPool.get(uri);
	}
	
	public synchronized boolean contains(URI uri) {
		return uriPool.containsKey(uri);
	}
	
	public synchronized boolean contains(String name) {
		return namePool.containsKey(name);
	}

	public synchronized List<Instance> get(String name) {
		return namePool.get(name);
	}

	public synchronized void remove(Instance instance) throws RepositoryException {
		remove(instance.getUri());
	}

	public synchronized void remove(URI uri) throws RepositoryException {
		Instance instance = uriPool.remove(uri);
		if (instance != null) {
			List<Instance> instances = namePool.get(instance.getService().getServiceName());
			instances.remove(instance);
		}

		try {
			removeObject(uriPool, uri);
		} catch (RepositoryException ex) {
			register(instance);
			throw ex;
		}
	}

}
