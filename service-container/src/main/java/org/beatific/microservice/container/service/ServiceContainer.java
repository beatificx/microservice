package org.beatific.microservice.container.service;

import java.util.List;

public abstract class ServiceContainer {

	public abstract void registerServic(String serviceName, Service service);
	
	public abstract Service getService(String serviceName);
	public abstract List<Instance> getAllInstaces(String serviceName);
	
	protected abstract void clearServices();
	protected abstract void reloadServices();
	protected abstract void removeService(String serviceName);
	
	protected abstract void pause();
	protected abstract void play();
}
