package org.beatific.microservice.container.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.instance.InstanceFinder;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.instance.support.FileInstancePool;
import org.beatific.microservice.container.monitor.ServiceMonitor;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JudgeDredd implements Judge {
	
	private Executor executor = new DreddExecutor();
	
	private Map<String, Service> servicePool = new HashMap<>();
	private InstancePool instancePool = new FileInstancePool();
	
//	@Autowired
	private ServiceMonitor monitor;
	
	@Autowired
	private InstanceFinder finder;
	
	@Autowired
	private Watchdog watchdog;
	
	@Autowired
	private SyncWatchdog syncWatchdog;
	
	@PostConstruct
	public void init() throws RepositoryException {
		
		List<Instance> instances = new ArrayList<>();
		for(String serviceName : servicePool.keySet())
			instances.addAll(finder.getAllInstaceInfos(serviceName));
		
		try {
			instancePool.load(instances);
		} catch (RepositoryException e) {
			throw e;
		}
		
		cooldown(instancePool.deadPool());
	}
	
	public void taggingCooldown(String serviceName) {
	}
	
	@Override
	public void cooldown(List<Instance> instances) {
		log.error("instance cooldown [{}]", instances);
	}

	@Override
	public void entrust(Service service) {
		servicePool.put(service.getServiceName(), service);
	}
	
	@Override
	public void take(String serviceName) {
		servicePool.remove(serviceName);
	}
	
	@Override
	public void clear() {
		servicePool.clear();
	}
	
	@Scheduled(fixedDelay = 10000)
	public void watcherDog() {
		watchdog.watch(servicePool, instancePool, finder, executor);
	}
	
	@Scheduled(fixedDelay = 10000)
	public void syncWatcherDog() {
		syncWatchdog.watch(instancePool);
	}

}
