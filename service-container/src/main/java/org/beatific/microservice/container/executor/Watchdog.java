package org.beatific.microservice.container.executor;

import java.util.Map;

import org.beatific.microservice.container.instance.InstanceFinder;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.service.Service;

public interface Watchdog {

	public void watch(Map<String, Service> servicePool, InstancePool instancePool, InstanceFinder finder, Executor executor);
}
