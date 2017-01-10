package org.beatific.microservice.container.instance;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beatific.microservice.container.repository.ObjectRepository;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class InstancePool extends ObjectRepository<Map<URI, Instance>> {

	private Map<URI, Instance> uriPool = new HashMap<>();
	private Map<String, List<Instance>> namePool = new HashMap<>();

	private List<Instance> deadPool = new ArrayList<>();
	private Iterator<Instance> iterator = uriPool.values().iterator();

	// public void load(List<ServiceInstance> serviceInstances) throws
	// RepositoryException {
	//
	// Map<URI, Instance> savedInstances = loadObject();
	// if(savedInstances == null) savedInstances = new HashMap<>();
	//
	// for(ServiceInstance serviceInstance : serviceInstances) {
	//
	// if(!savedInstances.containsKey(serviceInstance.getUri())) {
	//
	// Instance instance = new Instance(serviceInstance);
	//
	// Service service = new Service();
	// service.setServiceName(serviceInstance.getServiceId());
	//
	// instance.setService(service);
	//
	// innerRegister(instance);
	// }
	// }
	//
	// for(Entry<URI, Instance> entry : savedInstances.entrySet()) {
	// if(compare(entry.getKey(), serviceInstances)) {
	// innerRegister(entry.getValue());
	// } else {
	// deadPool.add(entry.getValue());
	// }
	// }
	// }
	
	public Collection<Instance> get() {
		return uriPool.values();
	}

	public void load(List<Instance> instances) throws RepositoryException {

		Map<URI, Instance> savedInstances = loadObject();
		if (savedInstances == null)
			savedInstances = new HashMap<>();

		for (Instance instance : instances) {

			if (!savedInstances.containsKey(instance.getUri())) {

				Service service = new Service();
				service.setServiceName(instance.getServiceName());
				instance.setService(service);

				innerRegister(instance);
			}
		}

		for (Entry<URI, Instance> entry : savedInstances.entrySet()) {
			if (compare(entry.getKey(), instances)) {
				innerRegister(entry.getValue());
			} else {
				deadPool.add(entry.getValue());
			}
		}

	}

	private boolean compare(URI uri, List<Instance> instances) {
		for (Instance instance : instances) {
			if (uri == instance.getUri()) {
				return true;
			}
		}

		return false;
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

	public synchronized List<Instance> get(String name) {
		return namePool.get(name);
	}

	public void remove(Instance instance) throws RepositoryException {
		remove(instance.getUri());
	}

	public synchronized void remove(URI uri) throws RepositoryException {
		Instance instance = uriPool.remove(uri);
		if (instance != null) {
			String name = instance.getService().getServiceName();
			List<Instance> instances = namePool.get(instance.getService().getServiceName());
			instances.remove(instance);
			namePool.put(name, instances);
		}

		try {
			removeObject(uriPool, uri);
		} catch (RepositoryException ex) {
			register(instance);
			throw ex;
		}
	}

}
