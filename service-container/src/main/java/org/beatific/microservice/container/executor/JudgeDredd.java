package org.beatific.microservice.container.executor;

import java.util.ArrayList;
import java.util.List;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.instance.InstanceFinder;
import org.beatific.microservice.container.instance.InstanceLogicalManager;
import org.beatific.microservice.container.instance.InstancePhysicalManager;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.monitor.ServiceMonitor;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.service.Service;
import org.beatific.microservice.container.service.ServicePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JudgeDredd implements Judge {

	@Autowired
	private ServicePool servicePool;

	@Autowired
	private InstancePool instancePool;

	@Autowired
	private InstanceFinder finder;

	@Autowired
	private Watchdog watchdog;

	@Autowired
	private InstanceLogicalManager logical;

	@Autowired
	private InstancePhysicalManager physical;

	@Autowired
	private SyncWatchdog syncWatchdog;

	@Autowired
	private ServiceWatchdog serviceWatchdog;

	@Autowired
	private ServiceMonitor monitor;

	public void init() {

		List<Instance> instances = new ArrayList<>();
		for (String serviceName : servicePool.instancesByServiceName().keySet())
			instances.addAll(finder.getAllInstaceInfos(serviceName));

		log.debug("instances {}", instances);
		try {
			instancePool.load(instances);
		} catch (RepositoryException e) {
		}

		cooldown(instancePool.deadPool());
	}

	public void taggingCooldown(String serviceName) {
	}

	@Override
	public void cooldown(List<Instance> instances) {

		log.error("instance cooldown [{}]", instances);

		instances.forEach(logical::unregister);

		int used = 10;

		while (used == 0) {
			try {
				Thread.sleep(1000);
				if (monitor.monitor(instances).getRequest() == 0)
					used--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		instances.forEach(physical::shutdown);
	}

	@Override
	public void entrust(Service service) {
		servicePool.register(service);
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
	public void watchDog() {
		watchdog.watch();
	}

	@Scheduled(fixedDelay = 10000)
	public void syncWatchDog() {
		syncWatchdog.watch();
	}

	@Scheduled(fixedDelay = 10000)
	public void serviceWatchDog() {
		serviceWatchdog.watch();
	}

}
